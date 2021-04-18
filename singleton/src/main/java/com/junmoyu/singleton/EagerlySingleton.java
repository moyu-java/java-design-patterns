package com.junmoyu.singleton;

import com.junmoyu.singleton.constant.SingletonConstants;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 * 饿汉式单例模式 - 线程安全
 * 该类在程序加载时就已经初始化完成了
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class EagerlySingleton implements Serializable {

    /**
     * 初始化静态实例
     */
    private static final EagerlySingleton INSTANCE = new EagerlySingleton();

    /**
     * 私有构造函数，保证无法从外部进行实例化
     */
    private EagerlySingleton() {
        System.out.println("EagerlySingleton 被实例化");
        // 防止通过反射进行实例化
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可被用户调用以获取类的实例
     *
     * @return 单例实例
     */
    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }

    /**
     * 添加此方法以防止反序列化破坏单例模式
     *
     * @return 单例实例
     */
    private Object readResolve() {
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
        // 多线程测试
        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println("多线程对象：" +EagerlySingleton.getInstance().toString())).start();
        }

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<EagerlySingleton> clazz = (Class<EagerlySingleton>) Class.forName("com.junmoyu.singleton.EagerlySingleton");
        Constructor<EagerlySingleton> constructor = clazz.getDeclaredConstructor(null);

        EagerlySingleton eagerlySingleton1 = constructor.newInstance();
        EagerlySingleton eagerlySingleton2 = constructor.newInstance();

        System.out.println("反射对象1：" + eagerlySingleton1.toString());
        System.out.println("反射对象2：" + eagerlySingleton2.toString());

        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempFile"));
        EagerlySingleton instance = EagerlySingleton.getInstance();
        System.out.println("反序列化对象1：" + instance.toString());
        oos.writeObject(instance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        EagerlySingleton newInstance = (EagerlySingleton) ois.readObject();
        // 判断是否是同一个对象
        System.out.println("反序列化对象2：" + newInstance.toString());
    }
}
