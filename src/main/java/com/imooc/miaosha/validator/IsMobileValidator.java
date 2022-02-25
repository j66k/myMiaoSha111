package com.imooc.miaosha.validator;

import com.imooc.miaosha.util.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Package: com.imooc.miaosha.validator
 * @ClassName: IsMobileValidator
 * @Author: jjt
 * @CreateTime: 2022/2/25 20:47
 * @Description:
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;//字符是否能为空
    @Override
    public void initialize(IsMobile isMobile) {
       required =  isMobile.required();//表示能否为空
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //里面写具体的校验功能
        if(required){
            //如果参数必须，则调用之前写的工具类处理
            return ValidatorUtil.isMobile(s);//使用之前写好的来判断
        }else{
            if(StringUtils.isEmpty(s)){
                return true;//如果参数不需要，且为空，则返回true
            }else{//如果参数不必须且不为空，则需要进行判断
                return ValidatorUtil.isMobile(s);//使用之前写好的来判断
            }
        }
    }
}
