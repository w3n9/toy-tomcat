package online.stringtek.toy.framework.tomcat.core.component;


import lombok.Data;
import online.stringtek.toy.framework.tomcat.core.config.parser.WebXmlParser;
import org.dom4j.DocumentException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

@Data
public class MappedContext extends MappedElement<ServletWrapper> {
    //每个context有自己独立的ClassLoader
    private ClassLoader classLoader;
    private static WebXmlParser webXmlParser=new WebXmlParser();
    public static MappedContext build(String contextBase) throws Exception {
        MappedContext mappedContext=new MappedContext();
        String classesPath=contextBase+"/WEB-INF/classes/";
        String webXmlPath=contextBase+"/WEB-INF/web.xml";
        File file=new File(classesPath);
        if(file.exists()){
            //读取web.xml
            File webXmlFile=new File(webXmlPath);
            if(webXmlFile.exists()&&webXmlFile.isFile()){
                //存在servlet
                URL classes=new URL("file://"+classesPath);
                //设置classLoader
                //TODO 控制classloader只允许他加载api下面的Servlet，其他的tomcat类不允许被加载
                mappedContext.setClassLoader(new URLClassLoader(new URL[]{classes},ClassLoader.getSystemClassLoader()));
                mappedContext.setObjectMap(new HashMap<>());
                Map<String, String> urlServletMap = webXmlParser.parseByAbsolutePath(webXmlPath);
                for (Map.Entry<String, String> entry : urlServletMap.entrySet()) {
                    String urlPattern = entry.getKey();
                    String className = entry.getValue();
                    mappedContext.getObjectMap().put(urlPattern,ServletWrapper.build(mappedContext.getClassLoader(),className));
                }
            }else{
                //不存在web.xml
            }
        }else{
            //只有静态资源
        }
        return mappedContext;
    }

}

