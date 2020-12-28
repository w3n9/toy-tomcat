package online.stringtek.toy.framework.tomcat.plover;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


public class NioServerTest {
    @Test
    void testServer() throws IOException {
        SocketChannel sc=SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));
        ByteBuffer buffer=ByteBuffer.allocate(512);
        buffer.put("操你妈".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        sc.write(buffer);
    }
}
