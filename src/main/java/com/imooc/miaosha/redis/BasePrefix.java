package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: BasePrefix
 * @Author: jjt
 * @CreateTime: 2022/2/24 20:41
 * @Description:key的抽象类，里面写上公共的一些方法
 */
public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix){
        this(0,prefix);//默认的前缀，永不过期
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {//这个是过期时间，默认为0 表示永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();//获取类名
        return className+":"+prefix;//拼接之后返回
    }
}
