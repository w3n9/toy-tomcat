package online.stringtek.toy.framework.tomcat.config.element;

import lombok.Data;
import online.stringtek.toy.framework.tomcat.http.eums.Protocol;

@Data
public class Connector {
    private int port;
    private Protocol protocol;
}
