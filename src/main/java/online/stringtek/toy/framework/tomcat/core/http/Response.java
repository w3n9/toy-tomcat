package online.stringtek.toy.framework.tomcat.core.http;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface Response {
    void write(String content);
    void flush()throws IOException;
    SocketChannel getSc();
}
