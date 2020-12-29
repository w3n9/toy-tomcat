package online.stringtek.toy.framework.tomcat.core.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.stringtek.toy.framework.tomcat.core.http.eums.Header;
import online.stringtek.toy.framework.tomcat.core.http.eums.Protocol;
import online.stringtek.toy.framework.tomcat.core.http.eums.Status;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * NIO Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NioResponse implements Response {
    private Protocol protocol;
    private Status status;
    private Map<Header,String> headers=new HashMap<>();
    private String body;
    private SocketChannel sc;

    public void write(String content){
        if(body==null)
            body="";
        body+=content;
        String contentLength = headers.get(Header.CONTENT_LENGTH);
        headers.put(Header.CONTENT_LENGTH,String.valueOf(
                (contentLength==null?0L:Long.parseLong(contentLength)) +content.getBytes().length));
    }
    public void flush() throws IOException {
        ByteBuffer buffer=ByteBuffer.wrap(toString().getBytes(StandardCharsets.UTF_8));
        while(buffer.hasRemaining())
            sc.write(buffer);
    }
    public String toString(){
        //TODO
        StringBuilder sb=new StringBuilder();
        sb.append(protocol.getVal()).append(' ').append(status.getCode()).append(' ').append(status.getVal()).append("\r\n");
        headers.forEach((k,v)-> sb.append(k).append(": ").append(v).append("\r\n"));
        sb.append("\r\n");
        sb.append(body==null?"":body);
        return sb.toString();
    }
}
