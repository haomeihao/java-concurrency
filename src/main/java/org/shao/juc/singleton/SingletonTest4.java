package org.shao.juc.singleton;

import org.shao.annoations.NotThreadSafe;

/**
 * 懒汉模式 -> 双重同步锁单例模式 不安全是因为指令重排序
 * Created by hmh on 2018/7/19.
 * @author hmh
 */
@NotThreadSafe
public class SingletonTest4 {

    // 私有构造函数
    private SingletonTest4() {
    }

    // 1.memory = allocate() 分配对象的内存空间 SingletonTest4 instance = null;
    // 2.ctorInstance() 初始化对象 instance = new SingletonTest4();
    // 3.instance = memory 设置instance指向刚分配的内存

    // JVM和CPU优化，发生了指令重排序
    // 1.memory = allocate() 分配对象的内存空间 SingletonTest4 instance = null;
    // 3.instance = memory 设置instance指向刚分配的内存
    // 2.ctorInstance() 初始化对象 instance = new SingletonTest4();

    // 单列对象
    private static SingletonTest4 instance = null;

    // 静态的工厂方法
    public static SingletonTest4 getInstance() {
        // 导致线程不安全
        if (instance == null) { // 双重检测机制
            synchronized (SingletonTest4.class) {
                if (instance == null) {
                    instance = new SingletonTest4();
                }
            }
        }
        return instance;
    }

}
