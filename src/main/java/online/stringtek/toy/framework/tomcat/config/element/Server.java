package online.stringtek.toy.framework.tomcat.config.element;

import lombok.Data;

import java.util.List;

@Data
public class Server {
    private List<Service> serviceList;
}
