package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: UserKey
 * @Author: jjt
 * @CreateTime: 2022/2/24 20:52
 * @Description:
 */
public class AccessKey extends BasePrefix {

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    public static AccessKey access = new AccessKey(5,"access");

    //设置过期时间的方法
    public static AccessKey withExpire(int expireSeconds){

        return new AccessKey(expireSeconds,"access");
    }



}
