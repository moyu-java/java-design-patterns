package com.junmoyu.singleton;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * 单例测试 - 延迟加载、线程安全、反射测试
 *
 * @author James
 * @date 2021/4/20
 */
public class Application {

    private static final int THREADS_NUMBER = 10;

    public static void main(String[] args) throws Exception {
        // 序列化相关代码及测试请查看 com.junmoyu.singleton.serializable 包下代码

        // 枚举单例测试
        enumSingletonTest();

        // 饿汉式单例测试
//        eagerlySingletonTest();

        // 线程不安全的懒汉式单例测试
//        threadUnsafeLazyLoadedSingletonTest();

        // 线程安全的懒汉式单例测试
//        threadSafeLazyLoadedSingletonTest();

        // 双重校验锁的单例测试
//        doubleCheckLockingSingletonTest();

        // 静态内部类的单例测试
//        staticInnerClassSingletonTest();
    }

    /**
     * 枚举单例测试
     * 线程安全、非延迟加载
     */
    private static void enumSingletonTest() throws Exception {
        // 延迟加载测试
        System.out.println("代码启动");
        Thread.sleep(1000);

        // 反射测试
        // 枚举天然防止反射攻击
        Class<EnumSingleton> clazz = (Class<EnumSingleton>) Class.forName("com.junmoyu.singleton.EnumSingleton");
        Constructor<EnumSingleton> constructor = clazz.getDeclaredConstructor(null);

        EnumSingleton singleton1 = constructor.newInstance();
        EnumSingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);
    }

    /**
     * 饿汉式单例测试
     * 线程安全、非延迟加载
     */
    private static void eagerlySingletonTest() throws Exception {
        // 延迟加载测试
        System.out.println("代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < THREADS_NUMBER; i++) {
            new Thread(() -> printObject("多线程测试", EagerlySingleton.getInstance())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<EagerlySingleton> clazz = (Class<EagerlySingleton>) Class.forName("com.junmoyu.singleton.EagerlySingleton");
        Constructor<EagerlySingleton> constructor = clazz.getDeclaredConstructor(null);
        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        EagerlySingleton singleton1 = constructor.newInstance();
        EagerlySingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);
    }

    /**
     * 线程不安全的懒加载单例测试
     * 线程不安全、延迟加载
     */
    private static void threadUnsafeLazyLoadedSingletonTest() throws Exception {
        // 延迟加载测试
        System.out.println("代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < THREADS_NUMBER; i++) {
            new Thread(() -> printObject("多线程测试", ThreadUnsafeLazyLoadedSingleton.getInstance())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<ThreadUnsafeLazyLoadedSingleton> clazz = (Class<ThreadUnsafeLazyLoadedSingleton>) Class.forName("com.junmoyu.singleton.ThreadUnsafeLazyLoadedSingleton");
        Constructor<ThreadUnsafeLazyLoadedSingleton> constructor = clazz.getDeclaredConstructor(null);
        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        ThreadUnsafeLazyLoadedSingleton singleton1 = constructor.newInstance();
        ThreadUnsafeLazyLoadedSingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);
    }

    /**
     * 线程安全的懒加载单例测试
     * 线程安全、延迟加载
     */
    private static void threadSafeLazyLoadedSingletonTest() throws Exception {
        // 延迟加载测试
        System.out.println("代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < THREADS_NUMBER; i++) {
            new Thread(() -> printObject("多线程测试", ThreadSafeLazyLoadedSingleton.getInstance())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<ThreadSafeLazyLoadedSingleton> clazz = (Class<ThreadSafeLazyLoadedSingleton>) Class.forName("com.junmoyu.singleton.ThreadSafeLazyLoadedSingleton");
        Constructor<ThreadSafeLazyLoadedSingleton> constructor = clazz.getDeclaredConstructor(null);
        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        ThreadSafeLazyLoadedSingleton singleton1 = constructor.newInstance();
        ThreadSafeLazyLoadedSingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);
    }

    /**
     * 双重校验锁的单例测试
     * 线程安全、延迟加载
     */
    private static void doubleCheckLockingSingletonTest() throws Exception {
        // 延迟加载测试
        System.out.println("代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < THREADS_NUMBER; i++) {
            new Thread(() -> printObject("多线程测试", DoubleCheckLockingSingleton.getInstance())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<DoubleCheckLockingSingleton> clazz = (Class<DoubleCheckLockingSingleton>) Class.forName("com.junmoyu.singleton.DoubleCheckLockingSingleton");
        Constructor<DoubleCheckLockingSingleton> constructor = clazz.getDeclaredConstructor(null);
        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        DoubleCheckLockingSingleton singleton1 = constructor.newInstance();
        DoubleCheckLockingSingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);
    }

    /**
     * 静态内部类单例测试
     * 线程安全、延迟加载
     */
    private static void staticInnerClassSingletonTest() throws Exception {
        // 延迟加载测试
        System.out.println("代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < THREADS_NUMBER; i++) {
            new Thread(() -> printObject("多线程测试", StaticInnerClassSingleton.getInstance())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<StaticInnerClassSingleton> clazz = (Class<StaticInnerClassSingleton>) Class.forName("com.junmoyu.singleton.StaticInnerClassSingleton");
        Constructor<StaticInnerClassSingleton> constructor = clazz.getDeclaredConstructor(null);
        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        StaticInnerClassSingleton singleton1 = constructor.newInstance();
        StaticInnerClassSingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);
    }

    private static void printObject(String tag, Object obj) {
        System.out.println(tag + " - 对象内存地址：" + obj.getClass().getSimpleName() + "@" + obj.hashCode());
    }
}
