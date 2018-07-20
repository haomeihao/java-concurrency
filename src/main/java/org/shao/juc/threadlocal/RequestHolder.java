package org.shao.juc.threadlocal;

/**
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
public class RequestHolder {

    private final static ThreadLocal<Long> requestHolder = new ThreadLocal();

    public static void add(Long id){
        requestHolder.set(id);
    }

    public static Long getId(){
        return requestHolder.get();
    }

    public static void remove(){
        requestHolder.remove();
    }
}
