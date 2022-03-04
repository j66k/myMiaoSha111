package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: OrderKey
 * @Author: jjt
 * @CreateTime: 2022/2/24 20:53
 * @Description:
 */
public class OrderKey extends BasePrefix{
    public OrderKey(String prefix) {
        super( prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
