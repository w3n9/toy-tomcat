package online.stringtek.toy.framework.tomcat.core.config.parser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebXmlParser {
    private final static String serverXmlPath= "web/WEB-INF/web.xml";
    public Map<String,String> parse() throws DocumentException {
        return parse(serverXmlPath);
    }
    public Map<String,String> parse(String path) throws DocumentException {
        Map<String,String> ansMap=new HashMap<>();
        SAXReader reader=new SAXReader();
        Document document = reader.read(WebXmlParser.class.getClassLoader().getResourceAsStream(path));
        Element rootElement = document.getRootElement();
        List<?> servletElemList = rootElement.elements("servlet");
        List<?> servletMappingElemList = rootElement.elements("servlet-mapping");
        Map<String,String> servletClassMap=new HashMap<>();
        for (Object servletObj : servletElemList) {
            Element servletElem=(Element) servletObj;
            String servletName=servletElem.elementText("servlet-name");
            String servletClass=servletElem.elementText("servlet-class");
            servletClassMap.put(servletName,servletClass);
        }
        for (Object servletMappingObj : servletMappingElemList) {
            Element servletMappingElem=(Element) servletMappingObj;
            String servletName=servletMappingElem.elementText("servlet-name");
            String urlPattern=servletMappingElem.elementText("url-pattern");
            if(servletClassMap.containsKey(servletName)){
                ansMap.put(urlPattern,servletClassMap.get(servletName));
            }
        }
        return ansMap;
    }

    public static void main(String[] args) throws DocumentException {
        System.out.println(new WebXmlParser().parse());
    }
}
