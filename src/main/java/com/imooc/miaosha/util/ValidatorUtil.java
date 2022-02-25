package com.imooc.miaosha.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Package: com.imooc.miaosha.util
 * @ClassName: ValidatorUtil
 * @Author: jjt
 * @CreateTime: 2022/2/25 15:59
 * @Description:判断格式是否正确
 */
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");//这是正则表达式
    public static Boolean isMobile(String src){

        if(StringUtils.isEmpty(src)){
            return false;
        }
        //判断是否满足正则表达式
        Matcher m = mobile_pattern.matcher(src);
        return m.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(isMobile("15058618775"));
//    }
}
