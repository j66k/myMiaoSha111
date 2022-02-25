package com.imooc.miaosha.redis;

/**
 * @Package: com.imooc.miaosha.redis
 * @ClassName: UserKey
 * @Author: jjt
 * @CreateTime: 2022/2/24 20:52
 * @Description:
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName= new UserKey("name");

}
