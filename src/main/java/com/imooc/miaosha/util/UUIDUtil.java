package com.imooc.miaosha.util;

import java.util.UUID;

/**
 * @Package: com.imooc.miaosha.util
 * @ClassName: UUIDUtil
 * @Author: jjt
 * @CreateTime: 2022/2/26 13:38
 * @Description:生成uuid的工具类
 */
public class UUIDUtil {
    public static String uuid(){
        //调用UUID类中的原生uuid生成方法，最后的replace是把生成的uuid中的横杠去掉
        return UUID.randomUUID().toString().replace("-","");
    }
}
