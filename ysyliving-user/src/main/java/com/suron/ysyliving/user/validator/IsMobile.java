package com.suron.ysyliving.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ysy
 * @version 1.0
 * IsMobile: 开发一个自定义的注解
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class})
public @interface IsMobile {
    String message() default "手机号码格式错误";

    boolean required() default true;

    Class<?>[] groups() default { };//默认参数

    Class<? extends Payload>[] payload() default { };//默认参数
}
