package online.stringtek.toy.framework.tomcat.core.config.element.server;

import lombok.Data;
import online.stringtek.toy.framework.tomcat.core.http.eums.Protocol;

@Data
public class ConnectorElem {
    private int port;
    private Protocol protocol;
}
