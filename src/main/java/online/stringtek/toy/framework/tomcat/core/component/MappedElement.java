package online.stringtek.toy.framework.tomcat.core.component;

import lombok.Data;

import java.util.Map;

@Data
public abstract class MappedElement<T> {
    private String name;
    private Map<String,T> objectMap;
}
