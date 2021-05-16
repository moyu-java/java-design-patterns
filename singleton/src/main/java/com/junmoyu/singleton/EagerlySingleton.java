package com.junmoyu.singleton;

import java.lang.reflect.Constructor;

/**
 * 饿汉式单例模式 - 线程安全
 * 该类在程序加载时就已经初始化完成了
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class EagerlySingleton {

    /**
     * 初始化静态实例
     */
    private static final EagerlySingleton INSTANCE = new EagerlySingleton();

    /**
     * 私有构造函数，保证无法从外部进行实例化
     */
    private EagerlySingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 最好放在开头，如不需要删除即可
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 可被用户调用以获取类的实例
     *
     * @return 单例实例
     */
    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + EagerlySingleton.getInstance().hashCode())).start();
        }
        Thread.sleep(1000);

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此问题）
        Class<EagerlySingleton> clazz = (Class<EagerlySingleton>) Class.forName("com.junmoyu.singleton.EagerlySingleton");
        Constructor<EagerlySingleton> constructor = clazz.getDeclaredConstructor(null);

        EagerlySingleton singleton1 = constructor.newInstance();
        EagerlySingleton singleton2 = constructor.newInstance();

        System.out.println("反射测试：singleton1 hashCode：" + "@" + singleton1.hashCode());
        System.out.println("反射测试：singleton2 hashCode：" + "@" + singleton2.hashCode());
    }
}
