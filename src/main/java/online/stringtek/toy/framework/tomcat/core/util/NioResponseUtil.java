package online.stringtek.toy.framework.tomcat.core.util;

import online.stringtek.toy.framework.tomcat.core.http.NioResponse;
import online.stringtek.toy.framework.tomcat.core.http.eums.Header;
import online.stringtek.toy.framework.tomcat.core.http.eums.Protocol;
import online.stringtek.toy.framework.tomcat.core.http.eums.Status;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class NioResponseUtil {
    public static NioResponse header(SocketChannel sc, Protocol protocol, Status status, Map<Header,String> headers, String body) throws IOException {
        NioResponse response=new NioResponse();
        response.setProtocol(protocol);
        response.setStatus(status);
        response.setHeaders(headers);
        response.setSc(sc);
        if(body!=null)
            response.write(body);
        return response;
    }
    public static NioResponse ok(SocketChannel sc, String body) throws IOException {
        Map<Header,String> headers=new HashMap<>();
        return header(sc,Protocol.HTTP1_1,Status.OK,headers,body);
    }
    public static NioResponse notFound(SocketChannel sc) throws IOException {
        Map<Header,String> headers=new HashMap<>();
        return header(sc,Protocol.HTTP1_1,Status.NOT_FOUND,headers,null);
    }
    public static NioResponse methodNotAllowed(SocketChannel sc) throws IOException{
        Map<Header,String> headers=new HashMap<>();
        return header(sc,Protocol.HTTP1_1,Status.METHOD_NOT_ALLOWED,headers,null);
    }
    public static NioResponse internalServerError(SocketChannel sc) throws IOException {
        Map<Header,String> headers=new HashMap<>();
        return header(sc,Protocol.HTTP1_1,Status.INTERNAL_SERVER_ERROR,headers,null);
    }
    public static void methodNotAllowed(NioResponse response) throws IOException {
        NioResponse resp = methodNotAllowed(response.getSc());
        response.setBody(resp.getBody());
        response.setHeaders(resp.getHeaders());
        response.setProtocol(resp.getProtocol());
        response.setStatus(resp.getStatus());
    }
}
