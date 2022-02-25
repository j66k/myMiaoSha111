package com.imooc.miaosha.vo;

/**
 * @Package: com.imooc.miaosha.vo
 * @ClassName: LoginVo
 * @Author: jjt
 * @CreateTime: 2022/2/25 15:12
 * @Description:
 */
public class LoginVo {
    private String mobile;
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
