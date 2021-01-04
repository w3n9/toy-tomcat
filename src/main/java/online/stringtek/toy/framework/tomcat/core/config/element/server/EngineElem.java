package online.stringtek.toy.framework.tomcat.core.config.element.server;

import lombok.Data;

import java.util.Map;

@Data
public class EngineElem {
    //key是name,value是appBase
    private Map<String,String> hostMap;
}
