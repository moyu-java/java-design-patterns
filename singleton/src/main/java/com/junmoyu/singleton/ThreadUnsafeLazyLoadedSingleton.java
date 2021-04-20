package com.junmoyu.singleton;

/**
 * 懒汉式 - 线程不安全
 * 仅适应于单线程
 *
 * @author James
 * @date 2021/4/20
 */
public class ThreadUnsafeLazyLoadedSingleton {

    private static ThreadUnsafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadUnsafeLazyLoadedSingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 如不需要删除即可
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，内存地址为：" + hashCode());
    }

    /**
     * 线程安全的实例化，使用双重检查，避免每次获取实例时都加锁
     * 但这种模式依然是有隐患的，INSTANCE 常量必须添加 volatile 关键字才能避免指令重排，保持线程可见性
     * 而 volatile 在 JDK 1.5 之后才支持
     *
     * @return 单例实例
     */
    public static ThreadUnsafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadUnsafeLazyLoadedSingleton();
        }
        return INSTANCE;
    }
}
