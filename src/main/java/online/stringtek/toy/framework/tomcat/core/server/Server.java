package online.stringtek.toy.framework.tomcat.core.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public abstract class Server {
    public void start(int port) throws IOException{
        log.info("server is listening on "+port);
    }
}
