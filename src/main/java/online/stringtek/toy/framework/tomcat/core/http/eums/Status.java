package online.stringtek.toy.framework.tomcat.core.http.eums;

import lombok.Getter;

@Getter
public enum Status {
    OK(200,"OK"),
    INTERNAL_REDIRECT(307,"Internal Redirect"),
    NOT_FOUND(404,"Not Found"),
    METHOD_NOT_ALLOWED(405,"Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500,"Internal Server Error"),
    ;
    private final int code;
    private final String val;
    Status(int code,String val){
        this.code=code;
        this.val=val;
    }
    public static Status parse(String str){
        for (Status protocol : Status.values()) {
            if(protocol.val.equals(str)){
                return protocol;
            }
        }
        return Status.INTERNAL_SERVER_ERROR;
    }

}
