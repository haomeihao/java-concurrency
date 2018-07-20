package org.shao.juc.singleton;

import org.shao.annoations.ThreadSafe;

/**
 * 饿汉模式：内存浪费
 * 在类装载的时候进行创建
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@ThreadSafe
public class SingletonTest6 {

    // 私有构造函数
    private SingletonTest6() {
    }

    // 单列对象
    private static SingletonTest6 instance = null;

    // 类装载的时候创建可以保证线程安全
    static {
        instance = new SingletonTest6();
    }

    // 静态的工厂方法
    public static SingletonTest6 getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(getInstance().hashCode());
        //System.out.println(getInstance().hashCode());
    }

}
