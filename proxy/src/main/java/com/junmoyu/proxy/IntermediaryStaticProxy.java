package com.junmoyu.proxy;

import com.junmoyu.proxy.sample.RentHouse;

/**
 * 中介代理
 *
 * @author moyu.jun
 * @date 2021/8/25
 */
public class IntermediaryStaticProxy implements RentHouse {

    private RentHouse rentHouse;

    public IntermediaryStaticProxy(RentHouse rentHouse) {
        this.rentHouse = rentHouse;
    }

    @Override
    public void rentHouse() {
        System.out.println("静态代理");
        System.out.println("租房前准备，中介带去看房等");
        rentHouse.rentHouse();
        System.out.println("租房结束，交中介费");
    }
}
