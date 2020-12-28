package online.stringtek.toy.framework.tomcat.util;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class RequestUtilTest {
    @Test
    void protocolTest() throws IOException {
        byte[] bytes = "GET / HTTP/1.1".getBytes(StandardCharsets.UTF_8);
        RequestUtil.build(bytes);
    }
}