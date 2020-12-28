package online.stringtek.toy.framework.tomcat.config.element;

import lombok.Data;

import java.util.List;

@Data
public class ServerElem {
    private List<ServiceElem> serviceList;
}
