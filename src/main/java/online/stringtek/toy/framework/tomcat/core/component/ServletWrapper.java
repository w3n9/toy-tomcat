package online.stringtek.toy.framework.tomcat.core.component;

import lombok.Data;
import online.stringtek.toy.framework.tomcat.core.api.Servlet;

@Data
public class ServletWrapper{
    //从context传过来的classLoader,该ClassLoader的上级是
    private ClassLoader classLoader;
    private Servlet servlet;
    public static ServletWrapper build(ClassLoader classLoader, String className) throws Exception {
        ServletWrapper mappedWrapper=new ServletWrapper();
        mappedWrapper.setClassLoader(classLoader);
        //这里不使用servlet懒加载，直接给他加载就行了
        Class<?> servletClass = Class.forName(className, true, classLoader);
        Servlet servlet = (Servlet)(servletClass.getConstructor().newInstance());
        mappedWrapper.setServlet(servlet);
        return mappedWrapper;
    }
}
