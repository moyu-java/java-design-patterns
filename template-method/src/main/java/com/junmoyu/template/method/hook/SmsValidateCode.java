package com.junmoyu.template.method.hook;

import com.junmoyu.template.method.util.RandomUtils;
import com.junmoyu.template.method.util.RegexUtils;
import com.junmoyu.template.method.util.StringUtils;

import java.util.Scanner;

/**
 * 短信验证码 - 添加 Hook
 *
 * @author moyu.jun
 * @date 2021/4/24
 */
public class SmsValidateCode extends AbstractValidateCode {

    @Override
    public boolean validateParam(String account) {
        if (RegexUtils.isMobile(account)) {
            System.out.println("手机号码校验通过");
            return true;
        }
        return false;
    }

    @Override
    public String generate() {
        String code = RandomUtils.random(6, true);
        System.out.println("生成六位纯数字的手机验证码：" + code);
        return code;
    }

    @Override
    public void setMessageTemplate() {
        System.out.println("设置了自定义的短信模板");
    }

    @Override
    public void send(String account, String code) {
        // 发送验证码到手机，在此不做实现
        System.out.println("已将验证码发送到手机。手机号码：" + account + "，验证码：" + code);
    }

    @Override
    public boolean needCustomizeTemplate() {
        String answer = getUserInput();
        if (answer.toLowerCase().startsWith("y")) {
            return true;
        }
        return false;
    }

    private String getUserInput() {
        String answer = null;
        System.out.print("请问您要设置自定义模板嘛（y/n）？: ");
        Scanner s = new Scanner(System.in);
        answer = s.nextLine();
        if (StringUtils.isEmpty(answer)) {
            return "no";
        }
        if (!answer.toLowerCase().startsWith("y") && !answer.toLowerCase().startsWith("n")) {
            return "no";
        }
        return answer;
    }
}
