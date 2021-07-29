package com.junmoyu.factory.method.sender;

/**
 * 邮箱发送器
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public class EmailSender implements Sender {
    @Override
    public void sendMessage(String message) {
        System.out.println("使用邮箱发送了消息：" + message);
    }
}
