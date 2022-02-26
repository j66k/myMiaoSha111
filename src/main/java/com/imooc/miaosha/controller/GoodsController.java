package com.imooc.miaosha.controller;


import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * @Package: com.imooc.miaosha.controller
 * @ClassName: GoodsController
 * @Author: jjt
 * @CreateTime: 2022/2/26 14:07
 * @Description:
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {
//    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String list(Model model, MiaoShaUser user) {
        model.addAttribute("user", user);//往里面加
        return "goods_list";//返回页面
    }


}
