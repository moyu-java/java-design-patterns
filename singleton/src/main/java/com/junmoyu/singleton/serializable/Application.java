package com.junmoyu.singleton.serializable;

import com.junmoyu.singleton.EagerlySingleton;
import com.junmoyu.singleton.EnumSingleton;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * 单例测试
 *
 * @author James
 * @date 2021/4/20
 */
public class Application {

    private static final int THREADS_NUMBER = 10;

    public static void main(String[] args) throws Exception {

        // 枚举单例测试
        enumSingletonTest();

        // 饿汉式单例测试
        eagerlySingletonTest();

        // 线程不安全的懒汉式单例测试
        threadUnsafeLazyLoadedSingletonTest();

        // 线程安全的懒汉式单例测试
        threadSafeLazyLoadedSingletonTest();

        // 双重校验锁的单例测试
        doubleCheckLockingSingletonTest();

        // 静态内部类的单例测试
        staticInnerClassSingletonTest();
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
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<com.junmoyu.singleton.EnumSingleton> clazz = (Class<com.junmoyu.singleton.EnumSingleton>) Class.forName("com.junmoyu.singleton.EnumSingleton");
        Constructor<com.junmoyu.singleton.EnumSingleton> constructor = clazz.getDeclaredConstructor(null);
        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        com.junmoyu.singleton.EnumSingleton singleton1 = constructor.newInstance();
        com.junmoyu.singleton.EnumSingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);

        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        com.junmoyu.singleton.EnumSingleton instance = com.junmoyu.singleton.EnumSingleton.INSTANCE;
        printObject("反序列化测试", instance);

        os.writeObject(instance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        com.junmoyu.singleton.EnumSingleton newInstance = (EnumSingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", instance);
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
            new Thread(() -> printObject("多线程测试", com.junmoyu.singleton.EagerlySingleton.getInstance())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<com.junmoyu.singleton.EagerlySingleton> clazz = (Class<com.junmoyu.singleton.EagerlySingleton>) Class.forName("com.junmoyu.singleton.EagerlySingleton");
        Constructor<com.junmoyu.singleton.EagerlySingleton> constructor = clazz.getDeclaredConstructor(null);
        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        com.junmoyu.singleton.EagerlySingleton singleton1 = constructor.newInstance();
        com.junmoyu.singleton.EagerlySingleton singleton2 = constructor.newInstance();

        printObject("反射测试", singleton1);
        printObject("反射测试", singleton2);

        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        com.junmoyu.singleton.EagerlySingleton instance = com.junmoyu.singleton.EagerlySingleton.getInstance();
        printObject("反序列化测试", instance);

        os.writeObject(instance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        com.junmoyu.singleton.EagerlySingleton newInstance = (EagerlySingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", instance);
    }

    /**
     * 线程不安全的懒加载单例测试
     * 线程不安全、延迟加载
     */
    private static void threadUnsafeLazyLoadedSingletonTest() throws Exception {

    }

    /**
     * 线程安全的懒加载单例测试
     * 线程安全、延迟加载
     */
    private static void threadSafeLazyLoadedSingletonTest() throws Exception {

    }

    /**
     * 双重校验锁的单例测试
     * 线程安全、延迟加载
     */
    private static void doubleCheckLockingSingletonTest() throws Exception {

    }

    /**
     * 静态内部类单例测试
     * 线程安全、延迟加载
     */
    private static void staticInnerClassSingletonTest() throws Exception {

    }

    private static void printObject(String tag, Object obj) {
        System.out.println(tag + " - 对象内存地址：" + obj.getClass().getSimpleName() + "@" + obj.hashCode());
    }
}
