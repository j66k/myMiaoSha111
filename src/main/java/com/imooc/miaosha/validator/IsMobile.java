package com.imooc.miaosha.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Package: com.imooc.miaosha.validator
 * @ClassName: IsMobile
 * @Author: jjt
 * @CreateTime: 2022/2/25 20:41
 * @Description:
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}//这个地方写上IsMobileValidator
)
public @interface IsMobile {
    boolean required() default true;

    String message() default "手机号码格式有误";//这里写的是失败的信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
