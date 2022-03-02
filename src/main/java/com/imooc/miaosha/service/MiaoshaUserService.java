package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.MiaoShaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobleException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Package: com.imooc.miaosha.service
 * @ClassName: MiaoshaUserService
 * @Author: jjt
 * @CreateTime: 2022/2/25 16:32
 * @Description:对应的服务
 */

@Service
public class MiaoshaUserService {
    //用常量存放cookie的名字
    public static final String COOKI_NAME_TOKEN = "token";

    //引入redis 把信息存到redis缓存中
    @Autowired
    RedisService redisService;


    @Autowired
    private MiaoShaUserDao miaoShaUserDao;

    public MiaoshaUser getById(long id) {
        return miaoShaUserDao.getById(id);
    }

    public String login(HttpServletResponse response,LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobleException(CodeMsg.SERVER_ERROR) ;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        //调用dao去数据库中查
        MiaoshaUser user = getById(Long.parseLong(mobile));//前端传入的mobile参数
        if (user == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOTEXIST) ;
        }

        //到这里说明手机号存在，下一步进行验证密码
        String dbPass = user.getPassword();//数据库中的密码
        String saltDB= user.getSalt();
        if(MD5Util.formPassToDBPass(formPass,saltDB).equals(dbPass)){//判断计算之后的密码和数据库中存的是不是相同
            String token = UUIDUtil.uuid();//获取一个随机的uuid
            addCookie(response,token,user);//调用创建cookie的方法
            return token;//登录成功
        }else{
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        //先进行参数验证
        if(StringUtils.isEmpty(token)){
            return null;
        }
        //从redis数据库中取出
        //直接返回
        MiaoshaUser user =  redisService.get(MiaoshaUserKey.token,token, MiaoshaUser.class);
        //延长有效期
        //先判断用户是否为空，不为空才延长
        if(user!=null){
            addCookie(response,token,user);//生成一个新的cookie
        }

        return user;
    }

    //生成cookie的方法
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user){
        //登陆成功，生成一个cookie
        //放到redis缓存中
        redisService.set(MiaoshaUserKey.token,token,user);//前缀，key,value
        //生成cookie
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
        //设置cookie的有效期
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());//cookie有效期和miaoshauserkey一样
        cookie.setPath("/");//设置路径为根目录
        response.addCookie(cookie);//把cookie放到response中
    }
}
