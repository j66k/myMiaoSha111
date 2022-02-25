package com.imooc.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: RedisService
 * @Author: jjt
 * @CreateTime: 2022/2/24 16:55
 * @Description:
 */

@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;


    /**
     * 获取单个对象
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix,String key,  Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();//获取jedis
            //生成真正的key 加上前缀才是真的key
            String realKey = keyPrefix.getPrefix() + key;
            String  str = jedis.get(realKey);
            T t =  stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix,String key,T value) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();//获取jedis
            String str = beanToString(value);
            if(str==null||str.length()<=0){
                return false;
            }
            //生成真正的key
            String realKey = keyPrefix.getPrefix()+ key;
            int seconds = keyPrefix.expireSeconds();
            if(seconds<=0){
                jedis.set(realKey,str);
            }else {
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }


    //判断key是否存在
    public <T> boolean exists(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();//获取jedis

            //生成真正的key
            String realKey = keyPrefix.getPrefix()+ key;
           return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    //增加值 对key进行增加减少
    public <T> Long incr(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();//获取jedis

            //生成真正的key
            String realKey = keyPrefix.getPrefix()+ key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    //减少值 修改key用的
    public <T> Long decr(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();//获取jedis

            //生成真正的key
            String realKey = keyPrefix.getPrefix()+ key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);//放到连接池中
        }
    }


    //把bean类型转换为string类型
    private <T> String beanToString(T value) {
        if(value==null) return null;
        Class<?> clazz = value.getClass();//获取传入的值的类型
        if(clazz == int.class||clazz == Integer.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String)value;
        }else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }else{
            return JSON.toJSONString(value);
        }
    }


    //把string转化成指定的bean类型
    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null||str.length()<=0){
            return null;
        }
        if(clazz == int.class||clazz == Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(str);
        }else{
            return (T)JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if(jedis!=null){
            jedis.close();
        }
    }



}
