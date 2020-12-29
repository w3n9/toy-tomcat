package online.stringtek.toy.framework.tomcat.app.servlet;

import online.stringtek.toy.framework.tomcat.core.http.NioRequest;
import online.stringtek.toy.framework.tomcat.core.http.NioResponse;
import online.stringtek.toy.framework.tomcat.core.http.Request;
import online.stringtek.toy.framework.tomcat.core.http.Response;
import online.stringtek.toy.framework.tomcat.core.servlet.HttpServlet;

public class TestServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) throws Exception {
        String content="<h1>TestServlet GET</h1>";
        response.write(content);
    }

    @Override
    public void doPost(Request request, Response response) throws Exception {
        String content="<h1>TestServlet POST</h1>";
        response.write(content);
    }
}
