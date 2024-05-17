package com.suron.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ysy
 * @version 1.0
 * ysy
 * 1. 创建自定义的注解EnumValidate 参考@NotNull源码来开发
 * 2. @Constraint(validatedBy = {EnumConstraintValidator.class}) 可以指定该自定义注解和EnumConstraintValidator校验器关联
 * 3. String message() default "{?}"; 可以指定校验时，返回的信息
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumConstraintValidator.class})
public @interface EnumValidate {

    String message() default "{com.hspedu.common.valid.EnumValidate.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    //增加values属性
    int[] values() default {};

    //增加regexp
    String regexp() default "";
}
