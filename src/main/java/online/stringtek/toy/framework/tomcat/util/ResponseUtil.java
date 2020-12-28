package online.stringtek.toy.framework.tomcat.util;

import online.stringtek.toy.framework.tomcat.http.Response;
import online.stringtek.toy.framework.tomcat.http.eums.Header;
import online.stringtek.toy.framework.tomcat.http.eums.Protocol;
import online.stringtek.toy.framework.tomcat.http.eums.Status;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Response header( Protocol protocol, Status status, Map<Header,String> headers,String body){
        Response response=new Response();
        response.setProtocol(protocol);
        response.setStatus(status);
        response.setHeaders(headers);
        response.setBody(body);
        return response;
    }
    public static Response ok(String body){
        Map<Header,String> headers=new HashMap<>();
        headers.put(Header.CONTENT_LENGTH,String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));
        return header(Protocol.HTTP1_1,Status.OK,headers,body);
    }
    public static Response notFound(){
        Map<Header,String> headers=new HashMap<>();
        return header(Protocol.HTTP1_1,Status.NOT_FOUND,headers,null);
    }
}
