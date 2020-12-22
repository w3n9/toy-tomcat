package online.stringtek.toy.framework.tomcat.exception;


public class XmlParsingException extends RuntimeException{
    private String xmlPath;
    private String content;

    public XmlParsingException(String xmlPath, String content) {
        super(xmlPath+":"+content);
    }
}
