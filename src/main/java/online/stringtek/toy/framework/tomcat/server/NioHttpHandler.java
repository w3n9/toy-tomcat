package online.stringtek.toy.framework.tomcat.server;

import cn.hutool.core.io.file.FileReader;
import online.stringtek.toy.framework.tomcat.http.Request;
import online.stringtek.toy.framework.tomcat.http.Response;
import online.stringtek.toy.framework.tomcat.http.eums.Method;
import online.stringtek.toy.framework.tomcat.util.RequestUtil;
import online.stringtek.toy.framework.tomcat.util.ResponseUtil;
import org.apache.commons.lang3.ArrayUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NioHttpHandler {
    private final static int BUFFER_SIZE=1024*8;
    private ByteBuffer buffer=ByteBuffer.allocate(BUFFER_SIZE);
    private final SocketChannel sc;
    private Request request;
    private Response response;
    public NioHttpHandler(SocketChannel sc){
        this.sc=sc;
    }
    public void handle(){
        if(!read()){
            try {
                write();
            } catch (IOException e) {
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
                request = RequestUtil.build(bytes);
            }
        }catch (Exception e){
            exit=true;
            e.printStackTrace();
        }finally {
            if(exit&sc!=null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return exit;
        }
    }
    public void write() throws IOException {
        if(Method.GET==request.getMethod()){
            String path = NioHttpHandler.class.getResource("/").getPath();
            String absolutePath=path+request.getPath();
            File file=new File(absolutePath);
            if(file.exists()&&file.isFile()){
                String content = new FileReader(file, StandardCharsets.UTF_8).readString();
                response=ResponseUtil.ok(content);
            }else{
                response=ResponseUtil.notFound();
            }
            //写模式
            buffer.clear();

            //读模式
            buffer.flip();
            sc.write(buffer);
        }
    }
}
