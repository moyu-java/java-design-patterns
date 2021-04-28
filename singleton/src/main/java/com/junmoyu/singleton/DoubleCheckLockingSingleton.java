package com.junmoyu.singleton;

import java.lang.reflect.Constructor;

/**
 * 懒汉式最终解决方案 - 线程安全，延迟加载
 * 也叫双重校验锁
 * 仅适用于 JDK 1.5 以上，因为 JDK 1.5 以上才支持 volatile 关键字
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class DoubleCheckLockingSingleton {

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

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 10; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + DoubleCheckLockingSingleton.getInstance().hashCode())).start();
        }
        Thread.sleep(1000);

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此问题）
        Class<DoubleCheckLockingSingleton> clazz = (Class<DoubleCheckLockingSingleton>) Class.forName("com.junmoyu.singleton.DoubleCheckLockingSingleton");
        Constructor<DoubleCheckLockingSingleton> constructor = clazz.getDeclaredConstructor(null);

        DoubleCheckLockingSingleton singleton1 = constructor.newInstance();
        DoubleCheckLockingSingleton singleton2 = constructor.newInstance();

        System.out.println("反射测试：singleton1 hashCode：" + "@" + singleton1.hashCode());
        System.out.println("反射测试：singleton2 hashCode：" + "@" + singleton2.hashCode());
    }
}
