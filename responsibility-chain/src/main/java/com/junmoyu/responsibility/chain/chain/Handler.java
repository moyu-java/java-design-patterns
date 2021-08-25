package com.junmoyu.responsibility.chain.chain;

/**
 * @author moyu.jun
 * @date 2021/8/25
 */
public abstract class Handler {
    /**
     * 处理者姓名
     */
    protected String name;

    /**
     * 下一个处理者
     */
    protected Handler nextHandler;

    public Handler(String name) {
        this.name = name;
    }

    /**
     * 请假处理
     *
     * @param request 请求参数
     * @return 处理结果
     */
    public abstract boolean process(LeaveRequest request);

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
