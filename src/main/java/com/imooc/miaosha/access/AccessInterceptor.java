package com.imooc.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.AccessKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @Package: com.imooc.miaosha.access
 * @ClassName: AccessInterceptor
 * @Author: jjt
 * @CreateTime: 2022/3/7 19:49
 * @Description:拦截器
 */

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisService redisService;
    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            //先获取到user
            MiaoshaUser user = getUser(request,response);
            UserContext.setUser(user);//保存到leocalThread中和当前线程绑定

            HandlerMethod hm =(HandlerMethod) handler;
           AccessLimit accessLimit =  hm.getMethodAnnotation(AccessLimit.class);//获取方法中的注解
            if(accessLimit==null){
                return true;
            }

            //到这里说明存在注解，获取注解上的数值
           int seconds = accessLimit.seconds();
           int maxcount =  accessLimit.maxCount();
           boolean needLogin =  accessLimit.needLogin();

           String key = request.getRequestURI();
           //下面写拦截器的具体内容
           if(needLogin){
               if(user==null){
                   //给客户端进行提示
                   render(response,CodeMsg.SESSION_ERROR);
                   return false;
               }
               key+="-"+user.getId();//拼接key
           }else {
               //什么都不用做
           }
           AccessKey ak = AccessKey.withExpire(seconds);
            Integer count =  redisService.get(ak,key,Integer.class);
            if(count==null){
                //没有进行过访问
                redisService.set(ak,key,1);
            }else if(count<maxcount){
                redisService.incr(ak,key);
            }else{
                //输出提示，点击过于频繁
                render(response,CodeMsg.REQUEST_TOMUCH);
                return false;
            }

        }

        return true;
    }

    //错误提示
    private void render(HttpServletResponse response, CodeMsg cm)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
       OutputStream out = response.getOutputStream();//拿到输出流
       String str =  JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response){
        String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);//获取token
        String cookieToken = getCookieValue(request,MiaoshaUserService.COOKI_NAME_TOKEN);
        //先进行参数校验
        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
            return null;//参数缺失，回到登陆页面
        }
        //做一个优先级判断，如果参数token为空，则直接用cookietoken
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return miaoshaUserService.getByToken(response,token);
    }

    //找到对应的cookiename的value值进行返回
    private String getCookieValue(HttpServletRequest request, String cookiName) {
        //遍历所有的cookie,然后找到我们需要的那一个
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length<=0){
            return null;
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookiName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
