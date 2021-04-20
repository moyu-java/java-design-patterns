package com.junmoyu.singleton.serializable;

import java.io.Serializable;

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
        // 防止通过反射进行实例化从而破坏单例
        // 如不需要删除即可
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，内存地址为：" + hashCode());
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
     * 如果有序列化需求，需要添加此方法以防止反序列化时重新创建新实例
     * 如无序列化需求可不加，同时去除 implements Serializable
     *
     * @return 单例实例
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
