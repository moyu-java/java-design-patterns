package com.junmoyu.singleton;

import com.junmoyu.singleton.constant.SingletonConstants;

import java.lang.reflect.Constructor;

/**
 * 懒汉式单例模式 - 线程安全
 * 这种实现也被叫做双重校验锁（Double Check Locking）
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class ThreadSafeLazyLoadedSingleton {

    /**
     * 加入 volatile 保证线程可见性，防止指令重排导致实例被多次实例化
     * 否则线程不安全
     */
    private volatile static ThreadSafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadSafeLazyLoadedSingleton() {
        System.out.println("ThreadSafeLazyLoadedSingleton 被实例化");
        // 防止通过反射进行实例化
        if (INSTANCE == null) {
            INSTANCE = this;
        } else {
            throw new IllegalStateException("Already initialized.");
        }
    }

    /**
     * 线程安全的实例化，使用双重检查，避免每次获取实例时都加锁
     * 但这种模式依然是有隐患的，INSTANCE 常量必须添加 volatile 关键字才能避免指令重排，保持线程可见性
     * 而 volatile 在 JDK 1.5 之后才支持
     *
     * @return 单例实例
     */
    public static ThreadSafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (ThreadSafeLazyLoadedSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadSafeLazyLoadedSingleton();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);

        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(ThreadSafeLazyLoadedSingleton.getInstance().toString())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<ThreadSafeLazyLoadedSingleton> clazz = (Class<ThreadSafeLazyLoadedSingleton>)
                Class.forName("com.junmoyu.singleton.ThreadSafeLazyLoadedSingleton");
        Constructor<ThreadSafeLazyLoadedSingleton> constructor = clazz.getDeclaredConstructor(null);

        ThreadSafeLazyLoadedSingleton eagerlySingleton1 = constructor.newInstance();
        ThreadSafeLazyLoadedSingleton eagerlySingleton2 = constructor.newInstance();

        System.out.println(eagerlySingleton1.toString());
        System.out.println(eagerlySingleton2.toString());
    }
}
