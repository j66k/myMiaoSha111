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
    public Result<Boolean> doLogin(@Valid LoginVo loginVo){

        log.info(loginVo.toString());

        //先进行参数校验
//        String passInput = loginVo.getPassword();
//        String mobile = loginVo.getMobile();
//        //需要保证不为空，且满足格式要求
//        if(StringUtils.isEmpty(passInput)){
//            //为空则返回一个错误页面
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        //判断手机号是否为空
//        if(StringUtils.isEmpty(mobile)){
//            //为空则返回一个错误页面
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        //下面判断格式是否符合要求
//        if(!ValidatorUtil.isMobile(mobile)){
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        //完成上面的检验之后，准备进行登录

        CodeMsg cm = miaoshaUserService.login(loginVo);
        if(cm.getCode()==0){
            return Result.success(true);
        }else{
            return Result.error(cm);
        }
    }

}
