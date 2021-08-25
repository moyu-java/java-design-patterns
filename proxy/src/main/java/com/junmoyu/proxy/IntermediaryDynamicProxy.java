package com.junmoyu.proxy;

import com.junmoyu.proxy.sample.RentHouse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 中介动态代理
 *
 * @author moyu.jun
 * @date 2021/8/25
 */
public class IntermediaryDynamicProxy implements InvocationHandler {

    private RentHouse target;

    public RentHouse getInstance(RentHouse target) {
        this.target = target;
        Class<?> clazz = target.getClass();
        return (RentHouse) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(this.target, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("动态代理");
        System.out.println("租房前准备，中介带去看房等");
    }

    private void after() {
        System.out.println("租房结束，交中介费");
    }
}
