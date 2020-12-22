package online.stringtek.toy.framework.tomcat.config.element;

import lombok.Data;
import online.stringtek.toy.framework.tomcat.config.element.Connector;

import java.util.List;

@Data
public class Service {
    private List<Connector> connectorList;
}
