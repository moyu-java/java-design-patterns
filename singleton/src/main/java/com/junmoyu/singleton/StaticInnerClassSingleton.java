package com.junmoyu.singleton;

import java.lang.reflect.Constructor;

/**
 * 静态内部类 - 线程安全，延迟加载
 * 写法简单，且可延迟加载，较推荐此种实现
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class StaticInnerClassSingleton {
    /**
     * 使用静态内部类来实现延迟加载
     */
    private static class HelperHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }

    /**
     * 私有构造方法
     */
    private StaticInnerClassSingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 最好放在开头，如不需要删除即可
        if (HelperHolder.INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 获取单例实例
     *
     * @return 单例实例
     */
    public static StaticInnerClassSingleton getInstance() {
        return HelperHolder.INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + StaticInnerClassSingleton.getInstance().hashCode())).start();
        }
        Thread.sleep(1000);

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此问题）
        Class<StaticInnerClassSingleton> clazz = (Class<StaticInnerClassSingleton>) Class.forName("com.junmoyu.singleton.StaticInnerClassSingleton");
        Constructor<StaticInnerClassSingleton> constructor = clazz.getDeclaredConstructor(null);

        StaticInnerClassSingleton singleton1 = constructor.newInstance();
        StaticInnerClassSingleton singleton2 = constructor.newInstance();

        System.out.println("反射测试：singleton1 hashCode：" + "@" + singleton1.hashCode());
        System.out.println("反射测试：singleton2 hashCode：" + "@" + singleton2.hashCode());
    }
}
