package com.junmoyu.responsibility.chain.chain;

/**
 * @author moyu.jun
 * @date 2021/8/25
 */
public class LeaveRequest {

    private String name;

    private int days;

    public LeaveRequest() {
    }

    public LeaveRequest(String name, int days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "name='" + name + '\'' +
                ", days=" + days +
                '}';
    }
}
