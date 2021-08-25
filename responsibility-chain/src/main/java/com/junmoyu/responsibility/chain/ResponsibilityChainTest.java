package com.junmoyu.responsibility.chain;

import com.junmoyu.responsibility.chain.chain.*;

/**
 * @author moyu.jun
 * @date 2021/8/25
 */
public class ResponsibilityChainTest {

    public static void main(String[] args) {
        Handler handler = new DirectorHandler("张三")
                .appendNext(new ManagerHandler("李四"))
                .appendNext(new TopManagerHandler("王五"));


        boolean process = handler.process(new LeaveRequest("GG", 1));
        System.out.println("最终结果：" + process + "\n");

        boolean process2 = handler.process(new LeaveRequest("GG", 4));
        System.out.println("最终结果：" + process2 + "\n");

        boolean process3 = handler.process(new LeaveRequest("GG", 8));
        System.out.println("最终结果：" + process3 + "\n");
    }
}
