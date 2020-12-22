package online.stringtek.toy.framework.tomcat.http.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseBuilder {
    private String info;
    private Map<String,String> header=new HashMap<>();
    private String content;
    public String build(){
        StringBuilder sb=new StringBuilder(info);
        sb.append("\n");
        header.forEach((k,v)->{
            sb.append(k).append(": ").append(v).append("\n");
        });
        sb.append("\n");
        sb.append(content);
        return sb.toString();
    }
}
