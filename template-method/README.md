# 模板方法模式（Template Method）

> 定义：一个操作中的算法骨架，而将算法的一些步骤延迟到子类中，使得子类可以不改变该算法结构的情况下重定义该算法的某些特定步骤。

## 1. 特点

模板方法模式的主要优点如下：

1. 封装不变部分，扩展可变部分； 
2. 提取公共代码，便于复用和维护；
3. 行为由父类控制，子类实现，符合开闭原则。

主要缺点如下：

1. 对每个不同的实现都需要定义一个子类，这会导致类的个数增加，系统更加庞大，设计也更加抽象，间接地增加了系统实现的复杂度。
2. 父类中的抽象方法由子类实现，子类执行的结果会影响父类的结果，这导致一种反向的控制结构，它提高了代码阅读的难度。
3. 由于继承关系自身的缺点，如果父类添加新的抽象方法，则所有子类都要改一遍。

## 2. 结构

模板方法的 UML 类图如下图所示：

![](https://i.loli.net/2021/04/25/PexN7QChy8jvlUw.png)

首先是一个抽象基类 `AbstractTemplateMethod`，它定义了一个模板方法 `templateMethod`，模板方法中定义了三个操作步骤，分别是抽象方法 `abstractStepOne`、`abstractStepTwo` 和 `abstractStepThree`。

```java
public abstract class AbstractTemplateMethod {

    public void templateMethod() {
        abstractStepOne();
        abstractStepTwo();
        abstractStepThree();
    }

    public abstract void abstractStepOne();
    public abstract void abstractStepTwo();
    public abstract void abstractStepThree();
}
```

在抽象基类中，这三个方法仅是抽象方法，没有被实现，该方法将由子类继承抽象基类后实现，也就是 `ConcreteClassOne` 和 `ConcreteClassTwo`。

```java
public class ConcreteClassOne extends AbstractTemplateMethod {
    @Override
    public void abstractStepOne() {
        System.out.println("操作步骤 1");
    }

    @Override
    public void abstractStepTwo() {
        System.out.println("操作步骤 2");
    }

    @Override
    public void abstractStepThree() {
        System.out.println("操作步骤 2");
    }
}
```

所以抽象基类 `AbstractTemplateMethod` 的模板方法 `templateMethod` 就是一个骨架或模板，它只定义了程序执行的步骤，但并不实现，而是延迟到子类 `ConcreteClassOne` 中去实现。

## 3. 实例

在我们日常开发中，有一个非常常见的需求，那就是登录的时候，可以选择验证码登录，并且很多应用都支持**短信**和**邮箱**两种验证方式。

一般验证码需要两个方法，一个是验证码生成并发送给用户，一个则是用户输入验证码然后校验正确性。

将**验证码生成并发送给用户**这个操作拆解一下，其操作步骤如下：

| 步骤 | 短信                         | 邮箱                         |
| ---- | ---------------------------- | ---------------------------- |
| 1    | 账号校验（验证账号为手机号） | 账号校验（验证账号为邮箱）   |
| 2    | 验证码生成（6位数字）        | 验证码生成（6位数字 + 字母） |
| 3    | 验证码保存                   | 验证码保存                   |
| 4    | 验证码发送（使用阿里SMS）    | 验证码发送（使用 Email）     |

如上可见，两种方式的操作步骤一样，但是每个操作在细节上又有不同，所以以此为例，操作步骤就可以做为模板或骨架，其具体的细节则由不同的子类去实现。这样就是一个模板方法模式了。

首先要创建一个抽象基类 `AbstractValidateCode`，定义模板方法，制定操作步骤。

```java
package com.junmoyu.template.method;
/**
 * 验证码抽象类
 * 定义模板方法和抽象策略
 */
public abstract class AbstractValidateCode {
    /**
     * 存储验证码
     */
    private static final Map<String, String> VALIDATE_CODE_MAP = new HashMap<>();

    /**
     * 模板方法 - 创建验证码
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
     */
    public final boolean validate(String account, String code) {
		// ... 此处具体实现请查看源代码
        // 此方法未调用抽象方法
    }

    /**
     * 私有方法 - 保存验证码
     */
    private void saveCode(String account, String code) {
        VALIDATE_CODE_MAP.put(account, code);
        System.out.println("验证码已保存");
    }

    /**
     * 抽象方法 - 校验参数，验证手机号或邮箱是否符合规则
     */
    public abstract boolean validateParam(String account);

    /**
     * 抽象方法 - 生成验证码
     */
    public abstract String generate();

    /**
     * 抽象方法 - 发送验证码
     */
    public abstract void send(String account, String code);
}
```

从上面代码可以看出，抽象基类中，一共有三种方法：

- 模板方法：定义了一个操作中的骨架，程序执行的步骤。其中调用了抽象方法；
- 私有方法：基类私有的方法，不需要公开，也不需要子类实现或重写；
- 抽象方法：需要子类去实现具体细节。

接下来创建短信验证码（SmsValidateCode）的类并继承抽象基类。

```java
package com.junmoyu.template.method;
/**
 * 短信验证码
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
    public void send(String account, String code) {
        // 发送验证码到手机，在此不做实现
        System.out.println("已将验证码发送到手机。手机号码：" + account + "，验证码：" + code);
    }
}
```

创建邮箱验证码（EmailValidateCode）的类并继承抽象基类。

```java
package com.junmoyu.template.method;
/**
 * 邮箱验证码
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
```

从代码上可以看出，两个类的具体实现是不一样的，而且他们不需要关心每个方法的执行步骤如何。最后创建一个测试类 `ApplicationTest`来测试一下。

```java
package com.junmoyu.template.method;
/**
 * 模板方法模式的测试类
 */
public class ApplicationTest {
    private static final String MOBILE_ACCOUNT = "13855287421";
    private static final String EMAIL_ACCOUNT = "example@email.com";

    public static void main(String[] args) {
        // 手机验证码创建测试
        smsValidateCodeTest();
        System.out.print("\n");

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
    }

    /**
     * 邮箱验证码测试
     */
    private static void emailValidateCodeTest() {
        System.out.println("开始邮箱验证码测试 --------- ");
        // 创建邮箱验证码
        AbstractValidateCode emailValidateCode = new EmailValidateCode();
        emailValidateCode.create(EMAIL_ACCOUNT);
    }
}
```

执行程序，可以看到如下打印信息：

```java
开始手机验证码测试 --------- 
手机号码校验通过
生成六位纯数字的手机验证码：754596
验证码已保存
已将验证码发送到手机。手机号码：13855287421，验证码：754596

开始邮箱验证码测试 --------- 
邮箱账号校验通过
生成六位英文 + 数字的邮箱验证码.8POj9C
验证码已保存
已将验证码发送到邮箱。邮箱账号：example@email.com，验证码：8POj9C
```

可以看到短信验证码和邮箱验证码的步骤是一样的，但是具体细节却是不同。

源代码中有完整的实现步骤，以及验证码的校验方面的代码，源代码更完整，在此不再赘述，需要了解的可直接查看源代码。完整代码的UML类图如下所示：

![](https://i.loli.net/2021/04/25/c1QERijLbDHCq5N.png)

## 4. Hook（钩子）











