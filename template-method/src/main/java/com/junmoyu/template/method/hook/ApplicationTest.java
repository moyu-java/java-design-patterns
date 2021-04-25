package com.junmoyu.template.method.hook;

import com.junmoyu.template.method.util.StringUtils;

import java.util.Scanner;

/**
 * 模板方法模式的测试类 - Hook 测试
 *
 * @author moyu.jun
 * @date 2021/4/24
 */
public class ApplicationTest {

    private static final String MOBILE_ACCOUNT = "13855287421";
    private static final String EMAIL_ACCOUNT = "example@email.com";

    public static void main(String[] args) {

        // 手机验证码创建测试
        smsValidateCodeTest();
        System.out.println("\n");

        // 邮箱验证码测试
        emailValidateCodeTest();
    }

    /**
     * 手机验证码测试
     */
    private static void smsValidateCodeTest() {
        System.out.println("开始手机验证码测试 --------- ");
        // 创建手机验证码
        AbstractValidateCode smsValidateCode = new SmsValidateCode();
        smsValidateCode.create(MOBILE_ACCOUNT);

        Scanner s = new Scanner(System.in);
        System.out.print("请输入六位手机验证码：");
        while (true) {
            String code = s.nextLine();
            if (StringUtils.isNotEmpty(code) && code.length() == 6) {
                // 校验手机验证码
                if (smsValidateCode.validate(MOBILE_ACCOUNT, code)) {
                    System.out.println("验证码校验成功，验证通过");
                }
                break;
            } else {
                System.out.print("请输入六位手机验证码：");
            }
        }
    }

    /**
     * 邮箱验证码测试
     */
    private static void emailValidateCodeTest() {
        System.out.println("开始邮箱验证码测试 --------- ");
        // 创建邮箱验证码
        AbstractValidateCode emailValidateCode = new EmailValidateCode();
        emailValidateCode.create(EMAIL_ACCOUNT);

        Scanner s = new Scanner(System.in);
        System.out.print("请输入六位邮箱验证码：");
        while (true) {
            String code = s.nextLine();
            if (StringUtils.isNotEmpty(code) && code.length() == 6) {
                // 校验邮箱验证码
                if (emailValidateCode.validate(EMAIL_ACCOUNT, code)) {
                    System.out.println("验证码校验成功，验证通过");
                }
                break;
            } else {
                System.out.print("请输入六位邮箱验证码：");
            }
        }
    }
}
