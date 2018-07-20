package org.shao.juc.singleton;

import org.shao.annoations.ThreadSafe;

/**
 * 懒汉模式：性能太低
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@ThreadSafe
public class SingletonTest3 {

    // 私有构造函数
    private SingletonTest3() {
    }

    // 单列对象
    private static SingletonTest3 instance = null;

    // 静态的工厂方法 加synchronized保证线程安全 但性能太低
    public static synchronized SingletonTest3 getInstance() {
        // 导致线程不安全
        if (instance == null) {
            instance = new SingletonTest3();
        }
        return instance;
    }

}
