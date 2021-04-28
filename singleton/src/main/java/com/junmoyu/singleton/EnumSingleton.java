package com.junmoyu.singleton;

import java.lang.reflect.Constructor;

/**
 * 枚举的单例实现 - 线程安全
 * 绝对防止多实例化，即使是在面反序列化和反射攻击时
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public enum EnumSingleton {

    /**
     * 唯一实例
     */
    INSTANCE;

    /**
     * 如果没有初始化的内容，可删除此方法
     */
    EnumSingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
        this.name = "君莫语";
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 方法调用测试
        System.out.println("name: " + EnumSingleton.INSTANCE.getName());
        EnumSingleton.INSTANCE.setName("junmoyu.com");
        System.out.println("name: " + EnumSingleton.INSTANCE.getName());

        // 反射测试
        // 枚举天然防止反射攻击
        Class<EnumSingleton> clazz = (Class<EnumSingleton>) Class.forName("com.junmoyu.singleton.EnumSingleton");
        Constructor<EnumSingleton> constructor = clazz.getDeclaredConstructor(null);
        // 这里将直接抛出异常，无法创建多个实例
        EnumSingleton singleton = constructor.newInstance();
    }
}
