package com.junmoyu.singleton.serializable;

import java.io.*;

/**
 * 懒汉式最终解决方案 - 线程安全，延迟加载
 * 也叫双重校验锁
 * 仅适用于 JDK 1.5 以上，因为 JDK 1.5 以上才支持 volatile 关键字
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class DoubleCheckLockingSingleton implements Serializable {

    /**
     * 加入 volatile 保证线程可见性，防止指令重排导致实例被多次实例化
     * 否则线程不安全
     */
    private volatile static DoubleCheckLockingSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private DoubleCheckLockingSingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 最好放在开头，如不需要删除即可
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 线程安全的实例获取，使用双重检查，避免每次获取实例时都加锁
     * 但这种模式依然是有隐患的，INSTANCE 常量必须添加 volatile 关键字才能避免指令重排，保持线程可见性
     * 而 volatile 在 JDK 1.5 之后才支持
     *
     * @return 单例实例
     */
    public static DoubleCheckLockingSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (DoubleCheckLockingSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheckLockingSingleton();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 如果有序列化需求，需要添加此方法以防止反序列化时重新创建新实例
     * 如无序列化需求可不加，同时去除 implements Serializable
     *
     * @return 单例实例
     */
    private Object readResolve() {
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        DoubleCheckLockingSingleton osInstance = DoubleCheckLockingSingleton.getInstance();
        System.out.println("反序列化测试：osInstance hashCode：" + "@" + osInstance.hashCode());
        os.writeObject(osInstance);

        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        DoubleCheckLockingSingleton isInstance = (DoubleCheckLockingSingleton) is.readObject();
        // 查看 hashCode 是否相同
        System.out.println("反序列化测试：isInstance hashCode：" + "@" + isInstance.hashCode());
    }
}
