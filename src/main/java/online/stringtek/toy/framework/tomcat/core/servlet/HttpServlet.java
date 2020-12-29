package online.stringtek.toy.framework.tomcat.core.servlet;

import online.stringtek.toy.framework.tomcat.core.http.NioResponse;
import online.stringtek.toy.framework.tomcat.core.http.Request;
import online.stringtek.toy.framework.tomcat.core.http.Response;
import online.stringtek.toy.framework.tomcat.core.http.eums.Method;
import online.stringtek.toy.framework.tomcat.core.util.NioResponseUtil;

public abstract class HttpServlet implements Servlet {
    @Override
    public void service(Request request, Response response) throws Exception {
        if(Method.GET==request.getMethod()){
            doGet(request,response);
        }else if(Method.POST==request.getMethod()) {
            doPost(request, response);
        }else{
            if(response instanceof NioResponse)
                NioResponseUtil.methodNotAllowed((NioResponse) response);
        }
    }
}
