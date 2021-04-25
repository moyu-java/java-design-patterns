package com.junmoyu.template.method;

import com.junmoyu.template.method.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码抽象类
 * 定义模板方法和抽象策略
 *
 * @author moyu.jun
 * @date 2021/4/24
 */
public abstract class AbstractValidateCode {

    /**
     * 存储验证码
     */
    private static final Map<String, String> VALIDATE_CODE_MAP = new HashMap<>();

    /**
     * 模板方法 - 创建验证码
     *
     * @param account 账号
     */
    public final void create(String account) {
        // 1.账号校验
        if (!validateParam(account)) {
            throw new IllegalStateException("账号无效");
        }

        // 2.验证码生成
        String code = generate();

        // 3.验证码保存
        saveCode(account, code);

        // 4.验证码发送
        send(account, code);
    }

    /**
     * 模板方法 - 校验验证码
     *
     * @param account 账号
     * @param code    验证码
     * @return true: 匹配；false: 不匹配
     */
    public final boolean validate(String account, String code) {
        // 1.根据账号获取验证码
        String codeByMap = getCode(account);
        if (StringUtils.isEmpty(codeByMap)) {
            System.out.println("验证码不存在");
            return false;
        }

        // 2.检查验证码是否正确
        if (!codeByMap.equalsIgnoreCase(code)) {
            System.out.println("验证码不匹配，检验失败");
            return false;
        }

        // 3.验证通过后，清除验证码
        removeCode(account);
        System.out.println("验证码校验成功，验证通过");
        return true;
    }

    /**
     * 保存验证码
     */
    private void saveCode(String account, String code) {
        VALIDATE_CODE_MAP.put(account, code);
    }

    /**
     * 获取验证码
     */
    private String getCode(String account) {
        return VALIDATE_CODE_MAP.get(account);
    }

    /**
     * 移除验证码
     */
    private void removeCode(String account) {
        VALIDATE_CODE_MAP.remove(account);
    }

    /**
     * 抽象方法 - 校验参数，验证手机号或邮箱是否符合规则
     *
     * @param account 请求
     * @return 是否符合规则
     */
    public abstract boolean validateParam(String account);

    /**
     * 抽象方法 - 生成验证码
     *
     * @return 验证码
     */
    public abstract String generate();

    /**
     * 抽象方法 - 发送验证码
     *
     * @param account 账号
     * @param code    验证码
     */
    public abstract void send(String account, String code);
}
