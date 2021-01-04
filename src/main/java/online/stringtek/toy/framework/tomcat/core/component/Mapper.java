package online.stringtek.toy.framework.tomcat.core.component;

import lombok.Data;
import online.stringtek.toy.framework.tomcat.core.api.Servlet;
import online.stringtek.toy.framework.tomcat.core.config.element.server.EngineElem;
import online.stringtek.toy.framework.tomcat.core.http.Request;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Data
public class Mapper{
    private Map<String,MappedHost> hostMap;

    public static Mapper build(EngineElem engineElem) throws Exception {
         Mapper mapper=new Mapper();
        mapper.setHostMap(new HashMap<>());
        for (Map.Entry<String, String> entry : engineElem.getHostMap().entrySet()) {
            mapper.getHostMap().put(entry.getKey(),MappedHost.build(entry.getValue()));
        }
        return mapper;
    }

    public static Servlet getServlet(Mapper mapper,Request request){
        String host = request.getHost();
        String path = request.getPath();
        try{
            Map<String, MappedHost> hostMap = mapper.getHostMap();
            MappedHost mappedHost = hostMap.get(host);
            String[] field = path.split("/");
            String contextPath = field.length>1?field[1]:"";
            // 根据contextPath获取MappedContext
            MappedContext mappedContext = mappedHost.getObjectMap().get(contextPath);
            String url;
            if(mappedContext==null||"".equals(contextPath)){
                //不存在context，则尝试以根目录作为context
                mappedContext=mappedHost.getObjectMap().get("");
                url=path;
            }else{
                url=path.substring(path.indexOf("/",1));
            }
            // 根据具体url获取ServletWrapper
            ServletWrapper servletWrapper = mappedContext.getObjectMap().get(url);
            return servletWrapper.getServlet();
        }catch (Exception e){
//            e.printStackTrace();
            return null;
        }
    }
}
