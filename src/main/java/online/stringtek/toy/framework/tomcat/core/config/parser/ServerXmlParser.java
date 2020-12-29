package online.stringtek.toy.framework.tomcat.core.config.parser;

import online.stringtek.toy.framework.tomcat.core.config.element.server.ConnectorElem;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ServerElem;
import online.stringtek.toy.framework.tomcat.core.config.element.server.ServiceElem;
import online.stringtek.toy.framework.tomcat.core.http.eums.Protocol;
import online.stringtek.toy.framework.tomcat.core.exception.XmlParsingException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.List;

public class ServerXmlParser {
    private final static String defaultPort="8080";
    private final static String defaultProtocol="HTTP/1.1";
    private final static String serverXmlPath= "conf/server.xml";
    public ServerElem parse(String xmlPath) throws DocumentException {
        SAXReader reader=new SAXReader();
        Document document = reader.read(getClass().getClassLoader().getResourceAsStream(xmlPath));
        //获取根
        Element root = document.getRootElement();
        ServerElem serverElem =new ServerElem();

        //获取具体service标签集合
        List<?> serviceElemList = root.elements("service");
        if(serviceElemList!=null){
            List<ServiceElem> serviceList=new ArrayList<>();
            serverElem.setServiceList(serviceList);
            for (Object serviceElemObj : serviceElemList) {
                //拿到了具体的service
                Element serviceElem=(Element) serviceElemObj;
                ServiceElem service=new ServiceElem();
                serviceList.add(service);

                //获取具体的connector标签集合
                List<?> connectorElemList = serviceElem.elements("connector");
                if(connectorElemList!=null){
                    List<ConnectorElem> connectorList=new ArrayList<>();
                    service.setConnectorElemList(connectorList);
                    for (Object connectorElemObj : connectorElemList) {
                        //拿到了具体的connector
                        Element connectorElem=(Element) connectorElemObj;
                        String port = connectorElem.attributeValue("port", defaultPort);
                        String protocol = connectorElem.attributeValue("protocol", defaultProtocol);
                        ConnectorElem connector=new ConnectorElem();
                        connectorList.add(connector);
                        connector.setPort(Integer.parseInt(port));
                        Protocol protocolEnum = Protocol.parse(protocol);
                        if(protocolEnum==Protocol.UNSUPPORTED)
                            throw new XmlParsingException(xmlPath,protocol+"协议不存在");
                        connector.setProtocol(protocolEnum);
                    }
                }

            }
        }
        return serverElem;
    }
    public ServerElem parse() throws DocumentException {
        return parse(serverXmlPath);
    }
}
