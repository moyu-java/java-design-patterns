package com.junmoyu.responsibility.chain.chain;

/**
 * @author moyu.jun
 * @date 2021/8/25
 */
public class ResponsibilityChainTest {

    public static void main(String[] args) {
        Handler zhangSan = new DirectorHandler("张三");
        Handler liSi = new ManagerHandler("李四");
        Handler wangWu = new TopManagerHandler("王五");

        zhangSan.setNextHandler(liSi);
        liSi.setNextHandler(wangWu);

        boolean process = zhangSan.process(new LeaveRequest("GG", 1));
        System.out.println("最终结果：" + process + "\n");

        boolean process2 = zhangSan.process(new LeaveRequest("GG", 4));
        System.out.println("最终结果：" + process2 + "\n");

        boolean process3 = zhangSan.process(new LeaveRequest("GG", 8));
        System.out.println("最终结果：" + process3 + "\n");
    }
}
