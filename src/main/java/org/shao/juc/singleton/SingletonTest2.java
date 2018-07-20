package org.shao.juc.singleton;

import org.shao.annoations.ThreadSafe;

/**
 * 饿汉模式：内存浪费
 * 在类装载的时候进行创建
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@ThreadSafe
public class SingletonTest2 {

    // 私有构造函数
    private SingletonTest2() {
    }

    // 单列对象 类装载的时候创建可以保证线程安全
    private static SingletonTest2 instance = new SingletonTest2();

    // 静态的工厂方法
    public static SingletonTest2 getInstance() {
        return instance;
    }

}
