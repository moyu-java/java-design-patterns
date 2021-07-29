package com.junmoyu.factory.method.test;

import com.junmoyu.factory.method.EmailSenderFactory;
import com.junmoyu.factory.method.SenderFactory;
import com.junmoyu.factory.method.SmsSenderFactory;
import com.junmoyu.factory.method.simple.SimpleFactory;
import com.junmoyu.factory.method.sender.EmailSender;
import com.junmoyu.factory.method.sender.Sender;
import com.junmoyu.factory.method.sender.SmsSender;

/**
 * 测试类
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public class FactoryTest {
    public static void main(String[] args) {
        noDesignPattern();

        simpleFactoryTest();

        factoryMethodTest();
    }

    /**
     * 无设计模式的测试
     */
    public static void noDesignPattern() {
        System.out.println("无设计模式的测试");

        Sender smsSender = new SmsSender();
        smsSender.sendMessage("Hello.");

        Sender emailSender = new EmailSender();
        emailSender.sendMessage("Hello.");
    }

    /**
     * 简单工厂模式的测试
     */
    public static void simpleFactoryTest() {
        System.out.println("简单工厂模式的测试");

        Sender smsSender = SimpleFactory.createSender("sms");
        if (smsSender != null) {
            smsSender.sendMessage("Hello.");
        }

        Sender emailSender = SimpleFactory.createSender("email");
        if (emailSender != null) {
            emailSender.sendMessage("Hello.");
        }
    }

    /**
     * 工厂方法模式的测试
     */
    public static void factoryMethodTest() {
        System.out.println("工厂方法模式的测试");

        SenderFactory smsSenderFactory = new SmsSenderFactory();
        Sender smsSender = smsSenderFactory.createSender();
        smsSender.sendMessage("Hello.");

        SenderFactory emailSenderFactory = new EmailSenderFactory();
        Sender emailSender = emailSenderFactory.createSender();
        emailSender.sendMessage("Hello.");
    }
}
