package online.stringtek.toy.framework.tomcat.http.eums;


import lombok.Getter;

@Getter
public enum Protocol {
    HTTP1_0(0,"HTTP/1.0"),
    HTTP1_1(1,"HTTP/1.1"),
    HTTP2_0(2,"HTTP/2"),
    UNKNOWN(-1,"UNKNOWN")
    ;
    private final int code;
    private final String val;
    Protocol(int code,String val){
        this.code=code;
        this.val=val;
    }
    public static Protocol parse(String str){
        for (Protocol protocol : Protocol.values()) {
            if(protocol.val.equals(str)){
                return protocol;
            }
        }
        return Protocol.UNKNOWN;
    }
}
