package org.shao.juc.singleton;

import org.shao.annoations.NotThreadSafe;

/**
 * 懒汉模式
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@NotThreadSafe
public class SingletonTest1 {

    // 私有构造函数
    private SingletonTest1() {
    }

    // 单列对象
    private static SingletonTest1 instance = null;

    // 静态的工厂方法
    public static SingletonTest1 getInstance() {
        // 导致线程不安全
        if (instance == null) {
            instance = new SingletonTest1();
        }
        return instance;
    }

}
