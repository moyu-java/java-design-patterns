package com.junmoyu.template.method;

import com.junmoyu.template.method.util.RandomUtils;
import com.junmoyu.template.method.util.RegexUtils;

/**
 * 短信验证码
 *
 * @author moyu.jun
 * @date 2021/4/24
 */
public class SmsValidateCode extends AbstractValidateCode {

    @Override
    protected boolean validateParam(String account) {
        if (RegexUtils.isMobile(account)) {
            return true;
        }
        return false;
    }

    @Override
    protected String generate() {
        String code = RandomUtils.random(6, true);
        System.out.println("生成六位纯数字的手机验证码：" + code);
        return code;
    }

    @Override
    protected void send(String account, String code) {
        // 发送验证码到手机，在此不做实现
        System.out.println("已将验证码发送到手机。手机账号：" + account + "，验证码：" + code);
    }
}
