package com.junmoyu.factory.method.sender;

/**
 * 发送器接口
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public interface Sender {

    /**
     * 发送消息
     *
     * @param message 消息
     */
    void sendMessage(String message);
}
