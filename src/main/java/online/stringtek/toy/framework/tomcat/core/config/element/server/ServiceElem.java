package online.stringtek.toy.framework.tomcat.core.config.element.server;

import lombok.Data;

import java.util.List;

@Data
public class ServiceElem {
    private List<ConnectorElem> connectorElemList;
}
