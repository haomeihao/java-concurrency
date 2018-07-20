package org.shao.juc.singleton;

import org.shao.annoations.Recommend;
import org.shao.annoations.ThreadSafe;

/**
 * 枚举实现单例 最安全的
 * Created by hmh on 2018/7/19.
 *
 * @author hmh
 */
@ThreadSafe
@Recommend
public class SingletonTest7 {

    // 私有构造函数
    private SingletonTest7() {
    }

    // 静态的工厂方法
    public static SingletonTest7 getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private SingletonTest7 singleton;

        // JVM保证这个方法绝对只调用一次
        Singleton() {
            singleton = new SingletonTest7();
        }

        public SingletonTest7 getInstance() {
            return singleton;
        }
    }

    public static void main(String[] args) {
        System.out.println(getInstance().hashCode());
        System.out.println(getInstance().hashCode());
    }

}
