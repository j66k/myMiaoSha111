package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Package: com.imooc.miaosha.controller
 * @ClassName: SampleController
 * @Author: jjt
 * @CreateTime: 2022/2/24 10:17
 * @Description:
 */

@Controller
@RequestMapping("/demo")
public class SampleController {

    //把service注入进来
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;
    //具体的业务方法
    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    //底下是模板
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Joshua");
        return "hello";
    }


    //事务方法
    @RequestMapping("/db/tx")
    public Result<Boolean> dbTx(User user){
         userService.tx(user);
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user  = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("zhangsan");
        redisService.set(UserKey.getById,""+1,user);
        return Result.success(true);
    }

}
