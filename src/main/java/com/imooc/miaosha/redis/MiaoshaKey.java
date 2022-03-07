package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: UserKey
 * @Author: jjt
 * @CreateTime: 2022/2/24 20:52
 * @Description:
 */
public class MiaoshaKey extends BasePrefix {

    private MiaoshaKey( int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"go");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60,"mp");


}