package online.stringtek.toy.framework.tomcat.http;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.stringtek.toy.framework.tomcat.http.eums.Header;
import online.stringtek.toy.framework.tomcat.http.eums.Method;
import online.stringtek.toy.framework.tomcat.http.eums.Protocol;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
public class Request {
    private Method method;
    private String path;
    private Protocol protocol;
    private Map<Header,String> headers=new HashMap<>();
    private String body;
}
