package com.junmoyu.factory.method;

import com.junmoyu.factory.method.sender.EmailSender;
import com.junmoyu.factory.method.sender.Sender;

/**
 * 工厂方法模式
 * 邮箱发送器工厂 - 创建邮箱发送器
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public class EmailSenderFactory implements SenderFactory {
    @Override
    public Sender createSender() {
        return new EmailSender();
    }
}
