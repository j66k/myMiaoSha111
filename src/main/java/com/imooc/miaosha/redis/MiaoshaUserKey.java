package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: MiaoshaUserKey
 * @Author: jjt
 * @CreateTime: 2022/2/26 13:46
 * @Description:
 */
//public class MiaoshaUserKey extends BasePrefix{
//    private MiaoshaUserKey(String prefix) {
//        super(prefix);
//    }
//
//    public static MiaoshaUserKey token = new MiaoshaUserKey("tk");
//
//}

public class MiaoshaUserKey extends BasePrefix{

    //这里设置过期时间
    public static final int TOKEN_EXPIRE = 3600*24 * 2;
    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
    public static MiaoshaUserKey getById= new MiaoshaUserKey(0, "id");//0表示永久有效

}