package online.stringtek.toy.framework.tomcat.core.http;

import lombok.Data;
import online.stringtek.toy.framework.tomcat.core.http.eums.Header;
import online.stringtek.toy.framework.tomcat.core.http.eums.Method;
import online.stringtek.toy.framework.tomcat.core.http.eums.Protocol;

import java.util.HashMap;
import java.util.Map;

@Data
public class NioRequest implements Request {
    private Method method;
    private String path;
    private Protocol protocol;
    private Map<Header,String> headers=new HashMap<>();
    private String body;
}
