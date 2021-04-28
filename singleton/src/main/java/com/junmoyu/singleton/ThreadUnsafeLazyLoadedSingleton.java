package com.junmoyu.singleton;

import java.lang.reflect.Constructor;

/**
 * 懒汉式 - 线程不安全
 * 非常不推荐使用
 *
 * @author James
 * @date 2021/4/20
 */
public class ThreadUnsafeLazyLoadedSingleton {

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

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + ThreadUnsafeLazyLoadedSingleton.getInstance().hashCode())).start();
        }
        Thread.sleep(1000);

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此问题）
        Class<ThreadUnsafeLazyLoadedSingleton> clazz = (Class<ThreadUnsafeLazyLoadedSingleton>) Class.forName("com.junmoyu.singleton.ThreadUnsafeLazyLoadedSingleton");
        Constructor<ThreadUnsafeLazyLoadedSingleton> constructor = clazz.getDeclaredConstructor(null);

        ThreadUnsafeLazyLoadedSingleton singleton1 = constructor.newInstance();
        ThreadUnsafeLazyLoadedSingleton singleton2 = constructor.newInstance();

        System.out.println("反射测试：singleton1 hashCode：" + "@" + singleton1.hashCode());
        System.out.println("反射测试：singleton2 hashCode：" + "@" + singleton2.hashCode());
    }
}
