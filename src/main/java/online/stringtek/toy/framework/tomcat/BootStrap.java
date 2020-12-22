package online.stringtek.toy.framework.tomcat;

import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.config.element.Connector;
import online.stringtek.toy.framework.tomcat.config.element.Server;
import online.stringtek.toy.framework.tomcat.config.element.Service;
import online.stringtek.toy.framework.tomcat.config.parser.ServerXmlParser;
import online.stringtek.toy.framework.tomcat.http.pojo.ResponseBuilder;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class BootStrap {

    public static void start(Connector connector) throws IOException {
        //默认HTTP1.1暂不处理其他，当前使用BIO
        int port = connector.getPort();
        ServerSocket serverSocket=new ServerSocket(port);
        log.info("server listen on "+serverSocket.getInetAddress().getHostAddress()+":"+port);
        while(true){
            Socket socket = serverSocket.accept();
            log.info("client connected");
            OutputStream out = socket.getOutputStream();
            String content="玩具Tomcat1.0响应";
            ResponseBuilder builder=new ResponseBuilder();
            builder.setInfo("HTTP/1.1 200 OK");
            builder.getHeader().put("Content-Type","text/html;charset=utf-8");
            builder.getHeader().put("Content-Length",content.getBytes().length+"");
            builder.setContent(content);
            System.out.println(builder.build());
            out.write(builder.build().getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }


    public static void main(String[] args) throws DocumentException, IOException {
        ServerXmlParser serverXmlParser=new ServerXmlParser();
        Server server = serverXmlParser.parse();
        List<Service> serviceList = server.getServiceList();
        for (Service service : serviceList) {
            List<Connector> connectorList = service.getConnectorList();
            for (Connector connector : connectorList) {
                start(connector);
            }
        }
    }
}
