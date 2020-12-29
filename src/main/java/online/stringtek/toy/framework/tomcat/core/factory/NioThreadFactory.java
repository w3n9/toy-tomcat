package online.stringtek.toy.framework.tomcat.core.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NioThreadFactory implements ThreadFactory {
    private final AtomicInteger id;
    private final String prefix;
    public NioThreadFactory(String prefix){
        id=new AtomicInteger();
        this.prefix=prefix;
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread thread=new Thread(r);
        thread.setName(prefix+":"+id.getAndIncrement());
        return thread;
    }
}
