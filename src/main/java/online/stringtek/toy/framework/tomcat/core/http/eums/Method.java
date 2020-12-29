package online.stringtek.toy.framework.tomcat.core.http.eums;

import lombok.Getter;

@Getter
public enum Method {
    GET(0,"GET"),
    POST(1,"POST"),
    UNSUPPORTED(-1,"UNSUPPORTED")
    ;
    private final int code;
    private final String val;
    Method(int code,String val){
        this.code=code;
        this.val=val;
    }
    public static Method parse(String str){
        for (Method method : Method.values()) {
            if(method.val.equals(str)){
                return method;
            }
        }
        return Method.UNSUPPORTED;
    }
}
