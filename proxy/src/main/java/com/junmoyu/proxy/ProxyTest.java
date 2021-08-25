package com.junmoyu.proxy;

import com.junmoyu.proxy.sample.RentHouse;
import com.junmoyu.proxy.sample.RentHouseImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author moyu.jun
 * @date 2021/8/25
 */
public class ProxyTest {

    public static void main(String[] args) {
        dynamicProxyTest();
    }

    /**
     * 静态代理测试
     */
    public static void staticProxyTest() {
        RentHouse rentHouse = new RentHouseImpl();
        RentHouse intermediary = new IntermediaryStaticProxy(rentHouse);
        intermediary.rentHouse();
    }

    public static void dynamicProxyTest() {
        // 定义一个handler
        IntermediaryDynamicProxy proxy = new IntermediaryDynamicProxy();
        RentHouse rentHouse = proxy.getInstance(new RentHouseImpl());
        rentHouse.rentHouse();
    }
}
