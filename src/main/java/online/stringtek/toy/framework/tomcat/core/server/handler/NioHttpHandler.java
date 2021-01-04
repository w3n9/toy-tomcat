package online.stringtek.toy.framework.tomcat.core.server.handler;

import cn.hutool.core.io.file.FileReader;
import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.core.BootStrap;
import online.stringtek.toy.framework.tomcat.core.common.SynchronizedQueue;
import online.stringtek.toy.framework.tomcat.core.component.MappedHost;
import online.stringtek.toy.framework.tomcat.core.component.Mapper;
import online.stringtek.toy.framework.tomcat.core.http.NioRequest;
import online.stringtek.toy.framework.tomcat.core.http.NioResponse;
import online.stringtek.toy.framework.tomcat.core.http.eums.Method;
import online.stringtek.toy.framework.tomcat.core.api.Servlet;
import online.stringtek.toy.framework.tomcat.core.util.NioRequestUtil;
import online.stringtek.toy.framework.tomcat.core.util.NioResponseUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class NioHttpHandler implements Runnable{
    private final static Pattern pattern = Pattern.compile("WEB-INF");
    private final static String webPrefix= "/webapps";
    private final static int BUFFER_SIZE=1024*8;
    private ByteBuffer buffer=ByteBuffer.allocate(BUFFER_SIZE);
    private final SocketChannel sc;
    private final Mapper mapper;
    private Map<String, Servlet> servletMap;
    private NioRequest request;
    private NioResponse response;
    private final SynchronizedQueue<SocketChannel> queue;
    public NioHttpHandler(SocketChannel sc,Mapper mapper){
        this(sc,mapper,null);
    }

    public NioHttpHandler(SocketChannel sc, Mapper mapper, SynchronizedQueue<SocketChannel> queue){
        this.sc=sc;
        this.mapper=mapper;
        this.servletMap=new HashMap<>();
        this.queue=queue;
    }

    public void handle(){
        if(!read()){
            try {
                write();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean read() {
        boolean exit=false;
        try{
            int read=0;
            List<Byte> byteList=new ArrayList<>();
            //buffer写模式
            buffer.clear();
            while((read=sc.read(buffer))!=0){
                System.out.println(read);
                if(read==-1){
                    exit=true;
                    break;
                }
                //buffer读模式
                buffer.flip();
                byte[] array = ArrayUtils.subarray(buffer.array(),0,read);
                byteList.addAll(Arrays.asList(ArrayUtils.toObject(array)));
                //buffer写模式
                buffer.clear();
            }
            if(!exit){
                byte[] bytes = ArrayUtils.toPrimitive(byteList.toArray(new Byte[0]));
                request = NioRequestUtil.build(bytes);
                if(request==null)
                    exit = true;
            }
            return exit;
        }catch (Exception e){
            exit=true;
            e.printStackTrace();
            return true;
        }finally {
            if(exit&&sc!=null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void write() throws Exception {
        Servlet servlet=Mapper.getServlet(mapper,request);
        if(servlet!=null)
            handleServlet(servlet);
        else
            handleStatic();
        response.flush();
        sc.close();
    }

    /**
     * 处理静态资源
     * @throws IOException
     */
    void handleStatic() throws IOException {
        if(Method.GET==request.getMethod()){
            MappedHost host = mapper.getHostMap().get(request.getHost());
            String absolutePath=host.getAppBase()+request.getPath();
            File file=new File(absolutePath);
            Matcher matcher = pattern.matcher(request.getPath());
            //排除WEB-INF下的文件
            if(file.exists()&&file.isFile()&&!matcher.find()){
                String content = new FileReader(file, StandardCharsets.UTF_8).readString();
                response= NioResponseUtil.ok(sc,content);
            }else
                response= NioResponseUtil.notFound(sc);
        }else
            response= NioResponseUtil.methodNotAllowed(sc);
    }

    /**
     * 处理Servlet
     */
    void handleServlet(Servlet servlet) throws Exception {
        if(servlet==null){
            response=NioResponseUtil.notFound(sc);
        }else{
            response= NioResponseUtil.ok(sc,"");
            servlet.service(request,response);
        }
    }

    @Override
    public void run() {
        handle();
        queue.poll();
    }
}
