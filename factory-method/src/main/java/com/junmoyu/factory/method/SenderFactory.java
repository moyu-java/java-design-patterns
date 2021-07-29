package com.junmoyu.factory.method;

import com.junmoyu.factory.method.sender.Sender;

/**
 * 工厂方法模式
 * 发送器工厂接口
 *
 * @author moyu.jun
 * @date 2021/7/29
 */
public interface SenderFactory {

    /**
     * 创建发送器
     *
     * @return 指定发送器
     */
    Sender createSender();
}
