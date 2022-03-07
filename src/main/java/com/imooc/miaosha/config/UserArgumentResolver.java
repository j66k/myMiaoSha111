package com.imooc.miaosha.config;

import com.imooc.miaosha.access.UserContext;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Package: com.imooc.miaosha.config
 * @ClassName: UserArgumentResolver
 * @Author: jjt
 * @CreateTime: 2022/2/26 16:23
 * @Description:
 */

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //获取参数的类型
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

//        //获取res,req
//        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
//        String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);//获取token
//        String cookieToken = getCookieValue(request,MiaoshaUserService.COOKI_NAME_TOKEN);
//        //先进行参数校验
//        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
//            return null;//参数缺失，回到登陆页面
//        }
//        //做一个优先级判断，如果参数token为空，则直接用cookietoken
//        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        return miaoshaUserService.getByToken(response,token);

        return UserContext.getUser();//直接从threadLocal中取出需要的user对象  对象的创建过程放在了accessInterceptor里面了
        //那边先创建对象然后保存到thredlocal里面，这边从threadLocal里面直接取
    }

//    //找到对应的cookiename的value值进行返回
//    private String getCookieValue(HttpServletRequest request, String cookiName) {
//        //遍历所有的cookie,然后找到我们需要的那一个
//        Cookie[] cookies = request.getCookies();
//        if(cookies==null||cookies.length<=0){
//            return null;
//        }
//        for(Cookie cookie : cookies){
//            if(cookie.getName().equals(cookiName)){
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }
}
