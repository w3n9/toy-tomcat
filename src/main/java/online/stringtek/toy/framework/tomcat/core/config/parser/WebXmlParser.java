package online.stringtek.toy.framework.tomcat.core.config.parser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebXmlParser {
    //全局web.xml
    private final static String serverXmlPath= "conf/web.xml";
    public Map<String,String> parse() throws DocumentException {
        return parseByRelativePath(serverXmlPath,"");
    }

    public Map<String,String> parseByAbsolutePath(String absolutePath) throws IOException, DocumentException {
        InputStream inputStream = new FileInputStream(new File(absolutePath));
        return parse(inputStream,"");
    }
    public Map<String,String> parseByAbsolutePath(String absolutePath,String prefix) throws IOException, DocumentException {
        InputStream inputStream = new FileInputStream(new File(absolutePath));
        return parse(inputStream,prefix);
    }
    public Map<String,String> parseByFile(File file,String prefix) throws IOException, DocumentException {
        InputStream inputStream = new FileInputStream(file);
        return parse(inputStream,prefix);
    }
    public Map<String,String> parseByRelativePath(String path,String prefix) throws DocumentException {
        InputStream inputStream = WebXmlParser.class.getClassLoader().getResourceAsStream(path);
        return parse(inputStream,prefix);
    }
    public Map<String,String> parse(InputStream inputStream,String prefix) throws DocumentException {
        Map<String,String> ansMap=new HashMap<>();
        SAXReader reader=new SAXReader();
        Document document = reader.read(inputStream);
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
                ansMap.put(prefix+urlPattern,servletClassMap.get(servletName));
            }
        }
        return ansMap;
    }

    public static void main(String[] args) throws DocumentException {
        System.out.println(new WebXmlParser().parse());
    }
}
