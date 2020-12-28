package online.stringtek.toy.framework.tomcat.http;

import lombok.Data;
import online.stringtek.toy.framework.tomcat.http.eums.Header;
import online.stringtek.toy.framework.tomcat.http.eums.Protocol;
import online.stringtek.toy.framework.tomcat.http.eums.Status;

import java.util.HashMap;
import java.util.Map;

@Data
public class Response {
    private Protocol protocol;
    private Status status;
    private Map<Header,String> headers=new HashMap<>();
    private String body;
    public String toString(){
        //TODO
        return "";
    }
}
