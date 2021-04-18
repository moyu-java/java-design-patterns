package com.junmoyu.singleton;

import com.junmoyu.singleton.constant.SingletonConstants;

import java.lang.reflect.Constructor;

/**
 * 懒汉式单例模式
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class LazyLoadedSingleton {

    /**
     * 这种写法也是线程不安全的，可能因指令重排导致实例被多次实例化
     */
    private static LazyLoadedSingleton INSTANCE = null;

    /**
     * 加入 volatile 保证线程可见性，防止指令重排导致实例被多次实例化
     */
//    private volatile static LazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造函数，保证无法从外部进行实例化
     */
    private LazyLoadedSingleton() {
        System.out.println("LazyLoadedSingleton 被实例化");
        // 防止通过反射进行实例化
        if (INSTANCE == null) {
            INSTANCE = this;
        } else {
            throw new IllegalStateException("Already initialized.");
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程不安全的实例化
     *
     * @return 单例实例
     */
    public static LazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LazyLoadedSingleton();
        }
        return INSTANCE;
    }

    /**
     * 线程安全的实例化，但每次获取实例都会加锁，严重影响并发性能
     *
     * @return 单例实例
     */
//    public static synchronized LazyLoadedSingleton getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new LazyLoadedSingleton();
//        }
//        return INSTANCE;
//    }

    /**
     * 线程安全的实例化（要加 volatile），使用双重检查，避免每次获取实例时都加锁
     * 但这种模式依然是有隐患的，INSTANCE 常量必须添加 volatile 关键字才能避免指令重排，保持线程可见性
     * 而 volatile 在 JDK 1.5 之后才支持
     *
     * @return 单例实例
     */
//    public static LazyLoadedSingleton getInstance() {
//        if (INSTANCE == null) {
//            synchronized (LazyLoadedSingleton.class){
//                if (INSTANCE == null) {
//                    INSTANCE = new LazyLoadedSingleton();
//                }
//            }
//        }
//        return INSTANCE;
//    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);

        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(LazyLoadedSingleton.getInstance().toString())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<LazyLoadedSingleton> clazz = (Class<LazyLoadedSingleton>) Class.forName("com.junmoyu.singleton.LazyLoadedSingleton");
        Constructor<LazyLoadedSingleton> constructor = clazz.getDeclaredConstructor(null);

        LazyLoadedSingleton lazyLoadedSingleton1 = constructor.newInstance();
        LazyLoadedSingleton lazyLoadedSingleton2 = constructor.newInstance();

        System.out.println(lazyLoadedSingleton1.toString());
        System.out.println(lazyLoadedSingleton2.toString());
    }
}
