package com.imooc.miaosha.controller;


import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Package: com.imooc.miaosha.controller
 * @ClassName: GoodsController
 * @Author: jjt
 * @CreateTime: 2022/2/26 14:07
 * @Description:
 */

@Controller
@RequestMapping("/user")
public class UserController {
//    private static Logger log = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;


    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoShaUser> info(Model model, MiaoShaUser user) {
        return Result.success(user);
    }


}
