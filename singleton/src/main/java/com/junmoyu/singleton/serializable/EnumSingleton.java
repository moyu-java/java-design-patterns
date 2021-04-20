package com.junmoyu.singleton.serializable;

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
     * 如有需初始化的内容，可实现此构造器
     * 如没有初始化的内容，可删除省略此方法
     */
    EnumSingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，内存地址为：" + hashCode());
        this.name = "莫语";
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
