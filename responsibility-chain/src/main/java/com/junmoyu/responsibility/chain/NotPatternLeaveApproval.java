package com.junmoyu.responsibility.chain;

/**
 * 无设计模式的请假审批
 *
 * @author moyu.jun
 * @date 2021/8/25
 */
public class NotPatternLeaveApproval {

    public static void main(String[] args) {
        boolean process = process("", 3);
        System.out.println("请假审批结果：" + process);
    }

    public static boolean process(String request, int number) {
        boolean result = handleByDirector(request);
        if (!result) {
            return result;
        }
        if (number < 3) {
            return true;
        }

        result = handleByManager(request);
        if (!result) {
            return result;
        }
        if (number < 7) {
            return true;
        }

        result = handleByTopManager(request);
        return result;
    }

    public static boolean handleByDirector(String request) {
        // 主管审批
        return true;
    }

    public static boolean handleByManager(String request) {
        // 经理审批
        return true;
    }

    public static boolean handleByTopManager(String request) {
        // 总经理审批
        return true;
    }
}
