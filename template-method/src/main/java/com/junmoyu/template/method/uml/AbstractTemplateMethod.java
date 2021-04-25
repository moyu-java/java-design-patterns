package com.junmoyu.template.method.uml;

/**
 * @author James
 * @date 2021/4/25
 */
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
