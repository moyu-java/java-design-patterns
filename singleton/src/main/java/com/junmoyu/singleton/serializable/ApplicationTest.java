package com.junmoyu.singleton.serializable;


import java.io.*;

/**
 * 单例测试 - 反序列化测试
 *
 * @author James
 * @date 2021/4/20
 */
public class ApplicationTest {

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
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        EnumSingleton osInstance = EnumSingleton.INSTANCE;
        printObject("反序列化测试", osInstance);

        os.writeObject(osInstance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        EnumSingleton isInstance = (EnumSingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", isInstance);
    }

    /**
     * 饿汉式单例测试
     * 线程安全、非延迟加载
     */
    private static void eagerlySingletonTest() throws Exception {
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        EagerlySingleton osInstance = EagerlySingleton.getInstance();
        printObject("反序列化测试", osInstance);

        os.writeObject(osInstance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        EagerlySingleton isInstance = (EagerlySingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", isInstance);
    }

    /**
     * 线程不安全的懒加载单例测试
     * 线程不安全、延迟加载
     */
    private static void threadUnsafeLazyLoadedSingletonTest() throws Exception {
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        ThreadUnsafeLazyLoadedSingleton osInstance = ThreadUnsafeLazyLoadedSingleton.getInstance();
        printObject("反序列化测试", osInstance);

        os.writeObject(osInstance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        ThreadUnsafeLazyLoadedSingleton isInstance = (ThreadUnsafeLazyLoadedSingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", isInstance);
    }

    /**
     * 线程安全的懒加载单例测试
     * 线程安全、延迟加载
     */
    private static void threadSafeLazyLoadedSingletonTest() throws Exception {
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        ThreadSafeLazyLoadedSingleton osInstance = ThreadSafeLazyLoadedSingleton.getInstance();
        printObject("反序列化测试", osInstance);

        os.writeObject(osInstance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        ThreadSafeLazyLoadedSingleton isInstance = (ThreadSafeLazyLoadedSingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", isInstance);
    }

    /**
     * 双重校验锁的单例测试
     * 线程安全、延迟加载
     */
    private static void doubleCheckLockingSingletonTest() throws Exception {
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        DoubleCheckLockingSingleton osInstance = DoubleCheckLockingSingleton.getInstance();
        printObject("反序列化测试", osInstance);

        os.writeObject(osInstance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        DoubleCheckLockingSingleton isInstance = (DoubleCheckLockingSingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", isInstance);
    }

    /**
     * 静态内部类单例测试
     * 线程安全、延迟加载
     */
    private static void staticInnerClassSingletonTest() throws Exception {
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        StaticInnerClassSingleton osInstance = StaticInnerClassSingleton.getInstance();
        printObject("反序列化测试", osInstance);

        os.writeObject(osInstance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        StaticInnerClassSingleton isInstance = (StaticInnerClassSingleton) is.readObject();
        // 查看是否是同一个对象
        printObject("反序列化测试", isInstance);
    }

    private static void printObject(String tag, Object obj) {
        System.out.println(tag + " - 对象内存地址：" + obj.getClass().getSimpleName() + "@" + obj.hashCode());
    }
}
