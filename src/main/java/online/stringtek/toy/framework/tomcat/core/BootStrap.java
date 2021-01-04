package online.stringtek.toy.framework.tomcat.core;

import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.core.component.Mapper;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ConnectorElem;
import online.stringtek.toy.framework.tomcat.core.config.element.server.EngineElem;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ServerElem;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ServiceElem;
import online.stringtek.toy.framework.tomcat.core.config.parser.ServerXmlParser;
import online.stringtek.toy.framework.tomcat.core.config.parser.WebXmlParser;
import online.stringtek.toy.framework.tomcat.core.server.Server;
import online.stringtek.toy.framework.tomcat.core.server.NioServer;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@Slf4j
public class BootStrap {


    //webapps类加载器map
    public static Map<String,ClassLoader> classLoaderMap=new HashMap<>();//写入的时候是单线程操作所以不需要加锁


    private final static String defaultWebAppsPath= "webapps";
    public static void start(ConnectorElem connectorElem,Mapper mapper) throws IOException {
        int port = connectorElem.getPort();
        Server server=null;
        switch (connectorElem.getProtocol()){
//            case HTTP1_0:server=new Http10NioServer();break;
//            case HTTP1_1:server=new Http11NioServer();break;
//            case HTTP2_0:server=new Http20NioServer();break;
            default:server=new NioServer(mapper);break;
        }
        server.start(connectorElem.getPort());
    }


    public static void main(String[] args) throws Exception {
        String serverXmlPath=System.getProperty("serverXml");
        String webXmlPath=System.getProperty("webXml");
        if(StringUtils.isEmpty(serverXmlPath)||StringUtils.isEmpty(webXmlPath)){
            log.error("please specify the location of server.xml with -serverXml and web.xml with -webXml");
            return;
        }
        log.info("use server.xml located at {}",serverXmlPath);
        log.info("use web.xml located at {}",webXmlPath);
        ServerXmlParser serverXmlParser=new ServerXmlParser();
        ServerElem serverElem = serverXmlParser.parseByAbsolutePath(serverXmlPath);
        //这里偷懒，初始化的时候就去读取所有的webapps下的web.xml
        List<ServiceElem> serviceList = serverElem.getServiceList();
        for (ServiceElem service : serviceList) {
            //处理每个service
            //获取connector
            List<ConnectorElem> connectorElemList = service.getConnectorElemList();
            //获取engine
            EngineElem engine = service.getEngine();
            //TODO 全局Web.xml文件处理
            //启动connector
            for (ConnectorElem connectorElem : connectorElemList) {
                start(connectorElem,Mapper.build(engine));
            }
        }
    }

}
