package com.junmoyu.factory.method;

import com.junmoyu.factory.method.sender.Sender;
import com.junmoyu.factory.method.sender.SmsSender;

/**
 * 工厂方法模式
 * 短信发送器工厂 - 创建短信发送器
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public class SmsSenderFactory implements SenderFactory {
    @Override
    public Sender createSender() {
        return new SmsSender();
    }
}
