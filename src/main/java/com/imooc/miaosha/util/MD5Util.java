package com.imooc.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Package: com.imooc.miaosha.util
 * @ClassName: MD5Util
 * @Author: jjt
 * @CreateTime: 2022/2/25 13:43
 * @Description:工具类
 */
public class MD5Util {

    //转换字符串为md5编码的方法
    public static String md5(String src){
        return DigestUtils.md5Hex(src);//调用工具类的方法，来进行一次md5
    }

    //这里的固定的salt值与用户密码一会进行拼装
    private static final String salt = "1a2b3c4d";

    //第一层md5
    public static String inputPassToFormPass(String inputPass){
        //使用salt与用户的输入进行拼装
        String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);//调用md5编码
    }

    //第二层md5
    public static String formPassToDBPass(String formPass,String salt){
        //使用salt与用户的输入进行拼装
        String str = ""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);//调用md5编码
    }

    //用户直接调用的方法，输入明文密码，直接转换为数据库的密码
    public static String inputPassToDBPass(String str,String saltDB){
        return formPassToDBPass(inputPassToFormPass(str),saltDB);
    }
    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
