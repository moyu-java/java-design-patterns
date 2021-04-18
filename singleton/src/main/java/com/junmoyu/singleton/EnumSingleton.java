package com.junmoyu.singleton;

import com.junmoyu.singleton.constant.SingletonConstants;

import java.io.*;

/**
 * 基于枚举的单例实现 - 线程安全
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public enum EnumSingleton {

    /**
     * 唯一实例
     */
    INSTANCE;

    /**
     * 如有需初始化的内容，可实现此构造器
     */
    EnumSingleton() {
        System.out.println("初始化内容");
        this.name = "莫语";
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);
        // 多线程测试
        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(EnumSingleton.INSTANCE.toString())).start();
        }
        // 方法调用
        EnumSingleton.INSTANCE.setName("君莫语");
        System.out.println(EnumSingleton.INSTANCE.getName());

        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempFile"));
        EnumSingleton instance = EnumSingleton.INSTANCE;
        System.out.println("反序列化对象1：" + instance.toString());
        oos.writeObject(instance);

        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        EnumSingleton newInstance = (EnumSingleton) ois.readObject();

        // 判断是否是同一个对象
        System.out.println("反序列化对象2：" + newInstance.toString());
        System.out.println("反序列化对象2：name = " + newInstance.getName());
    }
}
