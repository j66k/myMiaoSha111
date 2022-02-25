package com.imooc.miaosha.domain;

/**
 * @Package: com.imooc.miaosha.domain
 * @ClassName: User
 * @Author: jjt
 * @CreateTime: 2022/2/24 11:07
 * @Description:与数据库中的user表对应
 */
public class User {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
