package com.junmoyu.factory.method.sender;

/**
 * 短信发送器
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public class SmsSender implements Sender {

    @Override
    public void sendMessage(String message) {
        System.out.println("使用短信发送了消息：" + message);
    }
}
