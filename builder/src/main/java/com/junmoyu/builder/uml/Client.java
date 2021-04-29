package com.junmoyu.builder.uml;

/**
 * @author moyu.jun
 * @date 2021/4/29
 */
public class Client {
    public static void main(String[] args) {
        Builder builder = new ConcreteBuilder();
        Director director = new Director(builder);
        Product product = director.construct();
        product.show();
    }
}
