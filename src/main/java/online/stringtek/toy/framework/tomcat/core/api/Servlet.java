package online.stringtek.toy.framework.tomcat.core.api;

import online.stringtek.toy.framework.tomcat.core.http.NioRequest;
import online.stringtek.toy.framework.tomcat.core.http.NioResponse;
import online.stringtek.toy.framework.tomcat.core.http.Request;
import online.stringtek.toy.framework.tomcat.core.http.Response;

public interface Servlet {
    void doGet(Request request, Response response) throws Exception;
    void doPost(Request request, Response response) throws Exception;
    void service(Request request, Response response) throws Exception;
}
