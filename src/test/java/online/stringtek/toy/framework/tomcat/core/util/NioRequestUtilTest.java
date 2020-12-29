package online.stringtek.toy.framework.tomcat.core.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class NioRequestUtilTest {
    @Test
    void protocolTest() throws IOException {
        byte[] bytes = "GET / HTTP/1.1".getBytes(StandardCharsets.UTF_8);
        NioRequestUtil.build(bytes);
    }
}