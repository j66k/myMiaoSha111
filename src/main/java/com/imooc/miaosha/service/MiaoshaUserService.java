//package com.imooc.miaosha.service;
//
//import com.imooc.miaosha.dao.MiaoShaUserDao;
//import com.imooc.miaosha.domain.MiaoshaUser;
//import com.imooc.miaosha.exception.GlobleException;
//import com.imooc.miaosha.redis.MiaoshaUserKey;
//import com.imooc.miaosha.redis.RedisService;
//import com.imooc.miaosha.result.CodeMsg;
//import com.imooc.miaosha.util.MD5Util;
//import com.imooc.miaosha.util.UUIDUtil;
//import com.imooc.miaosha.vo.LoginVo;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @Package: com.imooc.miaosha.service
// * @ClassName: MiaoshaUserService
// * @Author: jjt
// * @CreateTime: 2022/2/25 16:32
// * @Description:对应的服务
// */
//
//@Service
//public class MiaoshaUserService {
//    //用常量存放cookie的名字
//    public static final String COOKI_NAME_TOKEN = "token";
//
//    //引入redis 把信息存到redis缓存中
//    @Autowired
//    RedisService redisService;
//
//
//    @Autowired
//    private MiaoShaUserDao miaoShaUserDao;
//
//    public MiaoshaUser getById(long id) {
//        //1.取出缓存
//        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);//从这里取出秒杀啊对象
//        //判断是否在缓存中
//        if (user != null) {
//            return user;
//        }
//        //2.如果缓存中不存在，那去数据库取
//        user = miaoShaUserDao.getById(id);
//        //如果数据库中能取到
//        if (user != null) {
//            //加入到缓存中
//            redisService.set(MiaoshaUserKey.getById, "" + id, MiaoshaUser.class);
//        }
//        return user;
//    }
//
//    //添加一个update方法
//    public boolean updatePassword(String token, long id, String passwordNew) {
//        //1.先取user对象，判断是否存在
//        MiaoshaUser user = getById(id);//调用上面的方法获取
//        if (user == null) {//说明不存在这个对象
//            throw new GlobleException(CodeMsg.MOBILE_NOTEXIST);
//        }
//        //到这里说明存在对象，可以进行下一步的更新操作
//        //创建新对象
//
//        //1.对数据库进行更新
//        MiaoshaUser toBeUpdate = new MiaoshaUser();
//        toBeUpdate.setId(id);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew, user.getSalt()));
//        //调用dao来更新对象
//        miaoShaUserDao.update(toBeUpdate);//创建新对象进行更新是为了减少sql操作，减少log，这里不是全量更新
//
//        //2.对缓存更新
//        redisService.delete(MiaoshaUserKey.getById, "" + id);
//        //token不能删掉，不然就不能登录了，应该进行更新操作
//        user.setPassword(toBeUpdate.getPassword());
//        redisService.set(MiaoshaUserKey.token, token, user);//调用时需要传入token
//
//        return true;
//    }
//
//
//    public String login(HttpServletResponse response, LoginVo loginVo) {
//        if (loginVo == null) {
//            throw new GlobleException(CodeMsg.SERVER_ERROR);
//        }
//        String mobile = loginVo.getMobile();
//        String formPass = loginVo.getPassword();
//        //判断手机号是否存在
//        //调用dao去数据库中查
//        MiaoshaUser user = getById(Long.parseLong(mobile));//前端传入的mobile参数
//        if (user == null) {
//            throw new GlobleException(CodeMsg.MOBILE_NOTEXIST);
//        }
//
//        //到这里说明手机号存在，下一步进行验证密码
//        String dbPass = user.getPassword();//数据库中的密码
//        String saltDB = user.getSalt();
//        if (MD5Util.formPassToDBPass(formPass, saltDB).equals(dbPass)) {//判断计算之后的密码和数据库中存的是不是相同
//            String token = UUIDUtil.uuid();//获取一个随机的uuid
//            addCookie(response, token, user);//调用创建cookie的方法
//            return token;//登录成功
//        } else {
//            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
//        }
//    }
//
//    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
//        //先进行参数验证
//        if (StringUtils.isEmpty(token)) {
//            return null;
//        }
//        //从redis数据库中取出
//        //直接返回
//        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
//        //延长有效期
//        //先判断用户是否为空，不为空才延长
//        if (user != null) {
//            addCookie(response, token, user);//生成一个新的cookie
//        }
//
//        return user;
//    }
//
//    //生成cookie的方法
//    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
//        //登陆成功，生成一个cookie
//        //放到redis缓存中
//        redisService.set(MiaoshaUserKey.token, token, user);//前缀，key,value
//        //生成cookie
//        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
//        //设置cookie的有效期
//        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());//cookie有效期和miaoshauserkey一样
//        cookie.setPath("/");//设置路径为根目录
//        response.addCookie(cookie);//把cookie放到response中
//    }
//}

package com.imooc.miaosha.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.imooc.miaosha.exception.GlobleException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.dao.MiaoShaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobleException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;

@Service
public class MiaoshaUserService {


    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    MiaoShaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, MiaoshaUser.class);
        if(user != null) {
            return user;
        }
        //取数据库
        user = miaoshaUserDao.getById(id);
        if(user != null) {
            redisService.set(MiaoshaUserKey.getById, ""+id, user);
        }
        return user;
    }
    // http://blog.csdn.net/tTU1EvLDeLFq5btqiK/article/details/78693323
    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        MiaoshaUser user = getById(id);
        if(user == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOTEXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }


    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    public String login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobleException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOTEXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)) {
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token	 = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisService.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}