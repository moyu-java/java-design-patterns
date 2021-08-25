package com.junmoyu.responsibility.chain.chain;

import java.util.Random;

/**
 * @author moyu.jun
 * @date 2021/8/25
 */
public class TopManagerHandler extends Handler {

    public TopManagerHandler(String name) {
        super(name);
    }

    @Override
    public boolean process(LeaveRequest request) {
        boolean result = (new Random().nextInt(10)) > 3;
        System.out.println(String.format("总经理 %s 审批 %s 的请假申请，请假天数：%d, 审批结果：%s",
                this.name, request.getName(), request.getDays(), result ? "批准" : "不批准"));
        return result;
    }
}
