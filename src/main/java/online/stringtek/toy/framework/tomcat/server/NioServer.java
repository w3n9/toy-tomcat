package online.stringtek.toy.framework.tomcat.server;

import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.server.handler.NioHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.*;

@Slf4j
public class NioServer extends Server {
    private Selector selector;
    @Override
    public void start(int port) throws IOException {
        selector=Selector.open();
        ServerSocketChannel ssc=ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        super.start(port);
        loop();
    }

    //单线程loop
    private void loop() throws IOException {
        while(true){
            log.info("selecting...");
            selector.select();
            log.info("select ok");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while(it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();
                if (selectionKey.isAcceptable()) {
                    log.info("acceptable");
                    ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    log.info("readable");
                    new NioHttpHandler((SocketChannel) selectionKey.channel()).handle();
                }
            }
        }
    }
}
