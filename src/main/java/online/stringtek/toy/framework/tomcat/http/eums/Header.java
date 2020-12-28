package online.stringtek.toy.framework.tomcat.http.eums;

public enum Header {
    CONTENT_LENGTH(0,"Content-Length"),
    DATE(1,"Date"),
    HOST(2,"Host"),
    CONNECTION(3,"Connection"),
    PRAGMA(4,"Pragma"),
    CACHE_CONTROL(5,"Cache-Control"),
    SEC_CH_UA(6,"sec-ch-ua"),
    SEC_CH_UA_MOBILE(7,"sec-ch-ua-mobile"),
    UPGRADE_INSECURE_REQUESTS(8,"Upgrade-Insecure-Requests"),
    USER_AGENT(9,"User-Agent"),
    ACCEPT(10,"Accept"),
    SEC_FETCH_SITE(11,"Sec-Fetch-Site"),
    SEC_FETCH_MODE(12,"Sec-Fetch-Mode"),
    SEC_FETCH_USER(13,"Sec-Fetch-User"),
    SEC_FETCH_DEST(14,"Sec-Fetch-Dest"),
    ACCEPT_ENCODING(15,"Accept-Encoding"),
    ACCEPT_LANGUAGE(16,"Accept-Language"),
    UNSUPPORTED(-1,"UNSUPPORTED")
    ;
    private final int code;
    private final String val;
    Header(int code,String val){
        this.code=code;
        this.val=val;
    }
    public static Header parse(String str){
        for (Header header : Header.values()) {
            if(header.val.equals(str)){
                return header;
            }
        }
        return Header.UNSUPPORTED;
    }



}
