package com.junmoyu.singleton;

import com.junmoyu.singleton.constant.SingletonConstants;

/**
 * 通过静态内部类实现的懒加载单例模式 - 线程安全
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class StaticInnerClassLazyLoadedSingleton {

    /**
     * 私有构造方法
     */
    private StaticInnerClassLazyLoadedSingleton() {
        System.out.println("StaticInnerClassLazyLoadedSingleton 被实例化");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例实例
     *
     * @return 单例实例
     */
    public static StaticInnerClassLazyLoadedSingleton getInstance() {
        return HelperHolder.INSTANCE;
    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    /**
     * 使用静态内部类来实现延迟加载
     */
    private static class HelperHolder {
        private static final StaticInnerClassLazyLoadedSingleton INSTANCE = new StaticInnerClassLazyLoadedSingleton();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);

        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(StaticInnerClassLazyLoadedSingleton.getInstance().toString())).start();
        }
    }
}
