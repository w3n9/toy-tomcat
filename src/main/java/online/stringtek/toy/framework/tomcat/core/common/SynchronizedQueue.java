package online.stringtek.toy.framework.tomcat.core.common;

import lombok.Getter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

@Getter
public class SynchronizedQueue<T> {
    private final Object mutex;
    private final Queue<T> queue;
    public SynchronizedQueue(){
        queue=new LinkedList<>();
        mutex=new Object();
    }

    public boolean offer(T t) {
        synchronized (mutex){
            return queue.offer(t);
        }
    }

    public T poll() {
        synchronized (mutex){
            return queue.poll();
        }
    }

    public boolean isContainsIfNotThenOffer(T o) {
        synchronized (mutex){
            boolean contain=queue.contains(o);
            if(!contain)
                offer(o);
            return contain;
        }
    }

}
