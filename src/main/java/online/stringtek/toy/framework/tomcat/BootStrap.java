package online.stringtek.toy.framework.tomcat;

import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.config.element.ConnectorElem;
import online.stringtek.toy.framework.tomcat.config.element.ServerElem;
import online.stringtek.toy.framework.tomcat.config.element.ServiceElem;
import online.stringtek.toy.framework.tomcat.config.parser.ServerXmlParser;
import online.stringtek.toy.framework.tomcat.server.Server;
import online.stringtek.toy.framework.tomcat.server.NioServer;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;

@Slf4j
public class BootStrap {
    public static void start(ConnectorElem connectorElem) throws IOException {
        int port = connectorElem.getPort();
        Server server=null;
        switch (connectorElem.getProtocol()){
//            case HTTP1_0:server=new Http10NioServer();break;
//            case HTTP1_1:server=new Http11NioServer();break;
//            case HTTP2_0:server=new Http20NioServer();break;
            default:server=new NioServer();break;
        }
        server.start(connectorElem.getPort());
    }


    public static void main(String[] args) throws DocumentException, IOException {
        ServerXmlParser serverXmlParser=new ServerXmlParser();
        ServerElem serverElem = serverXmlParser.parse();
        List<ServiceElem> serviceList = serverElem.getServiceList();
        for (ServiceElem service : serviceList) {
            List<ConnectorElem> connectorElemList = service.getConnectorElemList();
            for (ConnectorElem connectorElem : connectorElemList) {
                start(connectorElem);
            }
        }
    }
}
