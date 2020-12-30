package online.stringtek.toy.framework.tomcat.core;

import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ConnectorElem;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ServerElem;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ServiceElem;
import online.stringtek.toy.framework.tomcat.core.config.parser.ServerXmlParser;
import online.stringtek.toy.framework.tomcat.core.config.parser.WebXmlParser;
import online.stringtek.toy.framework.tomcat.core.server.Server;
import online.stringtek.toy.framework.tomcat.core.server.NioServer;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BootStrap {


    //webapps类加载器map
    public static Map<String,ClassLoader> classLoaderMap=new HashMap<>();//写入的时候是单线程操作所以不需要加锁


    private final static String defaultWebAppsPath="tomcat/webapps";
    public static void start(ConnectorElem connectorElem,Map<String,String> servletMap) throws IOException {
        int port = connectorElem.getPort();
        Server server=null;
        switch (connectorElem.getProtocol()){
//            case HTTP1_0:server=new Http10NioServer();break;
//            case HTTP1_1:server=new Http11NioServer();break;
//            case HTTP2_0:server=new Http20NioServer();break;
            default:server=new NioServer(servletMap);break;
        }
        server.start(connectorElem.getPort());
    }


    public static void main(String[] args) throws DocumentException, IOException {
        ServerXmlParser serverXmlParser=new ServerXmlParser();
        ServerElem serverElem = serverXmlParser.parse();
        //这里偷懒，初始化的时候就去读取所有的webapps下的web.xml
        Map<String, String> servletMap = parseWebXml();
        List<ServiceElem> serviceList = serverElem.getServiceList();
        for (ServiceElem service : serviceList) {
            List<ConnectorElem> connectorElemList = service.getConnectorElemList();
            for (ConnectorElem connectorElem : connectorElemList) {
                start(connectorElem,servletMap);
            }
        }
    }

    private static Map<String, String> parseWebXml() throws DocumentException, IOException {
        WebXmlParser webXmlParser=new WebXmlParser();
        Map<String, String> servletMap = webXmlParser.parse();
        String path = BootStrap.class.getResource("/").getPath();
        String webAppsPath=path+defaultWebAppsPath;
        File webAppsDir=new File(webAppsPath);
        if(webAppsDir.exists()&&webAppsDir.isDirectory()){
            File[] files = webAppsDir.listFiles();
            if(files!=null){
                for (File file : files) {
                    String prefix="/"+file.getName();
                    File classPath=new File(file.getAbsolutePath()+"/WEB-INF/classes");
                    String contextName=file.getName();
                    loadClasses(contextName,classPath,classPath,new StringBuilder());
                    File webXmlPath=new File(file.getAbsolutePath()+"/WEB-INF/web.xml");
                    if(webXmlPath.exists()&&webXmlPath.isFile()){
                        Map<String,String> map = webXmlParser.parseByFile(webXmlPath,prefix);
                        servletMap.putAll(map);
                    }
                }
            }
        }
        return servletMap;
    }

    private static void loadClasses(String contextName,File classPath,File currentPath,StringBuilder prefix){
        if(currentPath.exists()){
            File[] files = currentPath.listFiles();
            if(files!=null){
                for (File file : files) {
                    if(file.isDirectory()){
                        prefix.append(file.getName()).append('.');
                        loadClasses(contextName,classPath,file,prefix);
                    }else if(file.isFile()){
                        String fileName=file.getName();
                        if(fileName.endsWith(".class")){
                            String className=prefix.append(fileName.substring(0,fileName.lastIndexOf('.'))).toString();
                            loadClass(contextName,classPath,className);
                        }
                    }
                }
            }
        }
    }
    private static void loadClass(String contextName,File classPath,String className) {
        ClassLoader systemClassLoader=ClassLoader.getSystemClassLoader();
        URL classes;
        try {
            classes=new URL("file:///"+classPath.getPath()+"/");
            if(!classLoaderMap.containsKey(contextName))
                classLoaderMap.put(contextName,new URLClassLoader(new URL[]{classes},systemClassLoader));
            ClassLoader classLoader = classLoaderMap.get(contextName);
            classLoader.loadClass(className);
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
