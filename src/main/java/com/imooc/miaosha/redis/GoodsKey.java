package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: UserKey
 * @Author: jjt
 * @CreateTime: 2022/2/24 20:52
 * @Description:
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gt");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0,"gs");

}
