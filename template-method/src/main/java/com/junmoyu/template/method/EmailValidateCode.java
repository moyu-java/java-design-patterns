package com.junmoyu.template.method;

import com.junmoyu.template.method.util.RandomUtils;
import com.junmoyu.template.method.util.RegexUtils;

/**
 * 邮箱验证码
 *
 * @author moyu.jun
 * @date 2021/4/24
 */
public class EmailValidateCode extends AbstractValidateCode {

    @Override
    public boolean validateParam(String account) {
        if (RegexUtils.isEmail(account)) {
            System.out.println("邮箱账号校验通过");
            return true;
        }
        return false;
    }

    @Override
    public String generate() {
        String code = RandomUtils.random(6);
        System.out.println("生成六位英文 + 数字的邮箱验证码." + code);
        return code;
    }

    @Override
    public void send(String account, String code) {
        // 发送验证码到邮箱，在此不做实现
        System.out.println("已将验证码发送到邮箱。邮箱账号：" + account + "，验证码：" + code);
    }
}
