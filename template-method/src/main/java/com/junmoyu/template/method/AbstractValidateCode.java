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

    private static final Map<String, String> VALIDATE_CODE_MAP = new HashMap<>();

    public void create(String account) {
        // 1.账号校验
        if (!validateParam(account)) {
            throw new IllegalStateException("账号无效");
        }

        // 2.验证码生成
        String code = generate();

        // 3.验证码保存
        save(account, code);

        // 4.验证码发送
        send(account, code);
    }

    public boolean validate(String account, String code) {
        // 1.根据账号获取验证码
        String codeByMap = VALIDATE_CODE_MAP.get(account);
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
        VALIDATE_CODE_MAP.remove(account);
        System.out.println("验证码校验成功，验证通过");
        return true;
    }

    /**
     * 保存验证码
     *
     * @param account 账号
     * @param code    验证码
     */
    private void save(String account, String code) {
        VALIDATE_CODE_MAP.put(account, code);
    }

    /**
     * 校验参数，验证手机号或邮箱是否符合规则
     *
     * @param account 请求
     * @return 是否符合规则
     */
    protected abstract boolean validateParam(String account);

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    protected abstract String generate();

    /**
     * 发送验证码
     *
     * @param account 账号
     * @param code    验证码
     */
    protected abstract void send(String account, String code);

}
