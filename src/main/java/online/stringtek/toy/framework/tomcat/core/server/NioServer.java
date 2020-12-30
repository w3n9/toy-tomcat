package online.stringtek.toy.framework.tomcat.core.server;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.core.common.SynchronizedQueue;
import online.stringtek.toy.framework.tomcat.core.factory.NioThreadFactory;
import online.stringtek.toy.framework.tomcat.core.server.handler.NioHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class NioServer extends Server {
    private Selector selector;
    private final Map<String, String> servletMap;
    private final SynchronizedQueue<SocketChannel> queue;

    public NioServer(Map<String, String> servletMap) {
        this.servletMap = servletMap;
        this.queue=new SynchronizedQueue<>();
    }

    @Override
    public void start(int port) throws IOException {
        selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        super.start(port);
        loop();
    }

    //单线程loop
    private void loop() throws IOException {
        //线程池
        ThreadPoolExecutor tp = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() / 2,
                Runtime.getRuntime().availableProcessors(),
                100L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                new NioThreadFactory("NIO"),
                new ThreadPoolExecutor.DiscardPolicy()
        );
        while (true) {
            log.info("selecting...");
            selector.select();
            log.info("select ok");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();
                if (selectionKey.isValid() && selectionKey.isAcceptable()) {
                    log.info("acceptable");
                    ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                    SocketChannel sc = (SocketChannel) selectionKey.channel();
//                    //单线程调用
//                    new NioHttpHandler((SocketChannel) selectionKey.channel(),servletMap).handle();
                    //读写操作多线程化
                    if(!queue.isContainsIfNotThenOffer(sc)){
                        //第二次进入的时候要判断是否被关闭了
                        if(!sc.socket().isClosed())
                            tp.execute(new NioHttpHandler((SocketChannel) selectionKey.channel(), servletMap,queue));
                    }
                }
            }
        }
    }
}
