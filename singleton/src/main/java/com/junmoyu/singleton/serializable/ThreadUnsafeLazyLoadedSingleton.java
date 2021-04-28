package com.junmoyu.singleton.serializable;

import java.io.*;

/**
 * 懒汉式 - 线程不安全
 * 非常不推荐使用
 *
 * @author James
 * @date 2021/4/20
 */
public class ThreadUnsafeLazyLoadedSingleton implements Serializable {

    private static ThreadUnsafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadUnsafeLazyLoadedSingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 最好放在开头，如不需要删除即可
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 可被用户调用以获取类的实例 - 线程不安全
     *
     * @return 单例实例
     */
    public static ThreadUnsafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadUnsafeLazyLoadedSingleton();
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
        StaticInnerClassSingleton osInstance = StaticInnerClassSingleton.getInstance();
        System.out.println("反序列化测试：osInstance hashCode：" + "@" + osInstance.hashCode());
        os.writeObject(osInstance);

        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        StaticInnerClassSingleton isInstance = (StaticInnerClassSingleton) is.readObject();
        // 查看 hashCode 是否相同
        System.out.println("反序列化测试：isInstance hashCode：" + "@" + isInstance.hashCode());
    }
}
