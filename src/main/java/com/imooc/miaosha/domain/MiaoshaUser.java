package com.imooc.miaosha.domain;

import java.util.Date;

/**
 * @Package: com.imooc.miaosha.domain
 * @ClassName: MiaoShaUser
 * @Author: jjt
 * @CreateTime: 2022/2/25 16:16
 * @Description:与数据库中的表对应的实体类
 */
public class MiaoshaUser {
    private Long id;
    private String nickname;
    private String password;
    private String salt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
