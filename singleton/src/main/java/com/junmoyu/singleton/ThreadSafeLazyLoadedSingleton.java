package com.junmoyu.singleton;

import java.lang.reflect.Constructor;

/**
 * 懒汉式 - 线程安全，延迟加载
 * 但因为 getInstance() 方法加锁，导致多线程下性能较差，不推荐使用
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class ThreadSafeLazyLoadedSingleton {

    private static ThreadSafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadSafeLazyLoadedSingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 最好放在开头，如不需要删除即可
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 可被用户调用以获取类的实例 - 线程安全
     * 使用 synchronized 加锁以实现线程安全
     *
     * @return 单例实例
     */
    public static synchronized ThreadSafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadSafeLazyLoadedSingleton();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + ThreadSafeLazyLoadedSingleton.getInstance().hashCode())).start();
        }
        Thread.sleep(1000);

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此问题）
        Class<ThreadSafeLazyLoadedSingleton> clazz = (Class<ThreadSafeLazyLoadedSingleton>) Class.forName("com.junmoyu.singleton.ThreadSafeLazyLoadedSingleton");
        Constructor<ThreadSafeLazyLoadedSingleton> constructor = clazz.getDeclaredConstructor(null);

        ThreadSafeLazyLoadedSingleton singleton1 = constructor.newInstance();
        ThreadSafeLazyLoadedSingleton singleton2 = constructor.newInstance();

        System.out.println("反射测试：singleton1 hashCode：" + "@" + singleton1.hashCode());
        System.out.println("反射测试：singleton2 hashCode：" + "@" + singleton2.hashCode());
    }
}
