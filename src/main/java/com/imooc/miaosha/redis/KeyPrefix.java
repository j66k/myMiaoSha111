package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: Prefix
 * @Author: jjt
 * @CreateTime: 2022/2/24 20:39
 * @Description:前缀的接口，避免key的重复
 */
public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
