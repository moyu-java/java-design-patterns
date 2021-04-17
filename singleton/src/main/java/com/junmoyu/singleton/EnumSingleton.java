package com.junmoyu.singleton;

import com.junmoyu.singleton.constant.SingletonConstants;

/**
 * 基于枚举的单例实现 - 线程安全
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public enum EnumSingleton {

    /**
     * 唯一实例
     */
    INSTANCE;

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) {
        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(EnumSingleton.INSTANCE.toString())).start();
        }
    }
}
