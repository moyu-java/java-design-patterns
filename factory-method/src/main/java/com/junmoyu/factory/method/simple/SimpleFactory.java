package com.junmoyu.factory.method.simple;

import com.junmoyu.factory.method.sender.EmailSender;
import com.junmoyu.factory.method.sender.Sender;
import com.junmoyu.factory.method.sender.SmsSender;

/**
 * 简单工厂模式
 * 未被收录到 23 种设计模式中，简单工厂模式只算作是对代码的封装
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public class SimpleFactory {

    /**
     * 创建发送器
     *
     * @param type 发送器类型
     * @return 指定的发送器
     */
    public static Sender createSender(String type) {
        switch (type) {
            case "sms":
                return new SmsSender();
            case "email":
                return new EmailSender();
            case "other":
                // 当需要扩展的时候，需要修改此处，也就修改了原本的代码，不符合开闭原则。
            default:
                return null;
        }
    }
}
