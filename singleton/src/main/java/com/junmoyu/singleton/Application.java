package com.junmoyu.singleton;

import com.junmoyu.singleton.constant.SingletonConstants;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * 单例测试
 *
 * @author James
 * @date 2021/4/20
 */
public class Application {

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);
        // 多线程测试
        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> printObject(EagerlySingleton.getInstance())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<EagerlySingleton> clazz = (Class<EagerlySingleton>) Class.forName("com.junmoyu.singleton.EagerlySingleton");
        Constructor<EagerlySingleton> constructor = clazz.getDeclaredConstructor(null);

        // 赋予反射对象超级权限，绕过权限检查
        constructor.setAccessible(true);

        EagerlySingleton singleton1 = constructor.newInstance();
        EagerlySingleton singleton2 = constructor.newInstance();

        System.out.println("反射对象1：" + singleton1.toString());
        System.out.println("反射对象2：" + singleton2.toString());

        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        EagerlySingleton instance = EagerlySingleton.getInstance();
        System.out.println("反序列化对象1：" + instance.toString());
        os.writeObject(instance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        EagerlySingleton newInstance = (EagerlySingleton) is.readObject();
        // 判断是否是同一个对象
        System.out.println("反序列化对象2：" + newInstance.toString());
    }

    private static void printObject(Object obj) {
        String objectName = obj.getClass().getSimpleName();
        int memoryAddress = obj.hashCode();
        System.out.println("对象内存地址：" + objectName + "@" + memoryAddress);
    }
}