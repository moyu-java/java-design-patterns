package com.junmoyu.singleton;

/**
 * 静态内部类 - 线程安全，延迟加载
 * 写法简单，且可延迟加载，较推荐此种实现
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class StaticInnerClassSingleton {

    /**
     * 私有构造方法
     */
    private StaticInnerClassSingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 如不需要删除即可
        if (getInstance() != null) {
            throw new IllegalStateException("Already initialized.");
        }

        System.out.println(getClass().getCanonicalName() + " 被实例化，内存地址为：" + hashCode());
    }

    /**
     * 获取单例实例
     *
     * @return 单例实例
     */
    public static StaticInnerClassSingleton getInstance() {
        return HelperHolder.INSTANCE;
    }

    /**
     * 使用静态内部类来实现延迟加载
     */
    private static class HelperHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
}
