package com.imooc.miaosha.controller;

import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.util.ValidatorUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Package: com.imooc.miaosha.controller
 * @ClassName: LoginController
 * @Author: jjt
 * @CreateTime: 2022/2/25 14:08
 * @Description:负责登录的控制
 */

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";//返回页面
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){

        log.info(loginVo.toString());
       String token = miaoshaUserService.login(response,loginVo);
       log.info(token);
        return Result.success(token);
    }

}
