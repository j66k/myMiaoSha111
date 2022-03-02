package com.imooc.miaosha.controller;


import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.validation.constraints.AssertFalse;
import java.util.List;

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
    ApplicationContext applicationContext;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,Model model, MiaoshaUser user) {
        model.addAttribute("user",user);
        //先从goodsService中获取商品列表
        List<GoodsVo> goodsList = goodsService.listCoodsVo();
        model.addAttribute("goodsList", goodsList);//往里面加
        //return "goods_list";//返回页面
        //1.从缓存中取
       String html =  redisService.get(GoodsKey.getGoodsList,"",String.class);
       //如果不为空，直接返回
       if(!StringUtils.isEmpty(html)){
           return html;
       }
       //2.如果为空，手动渲染
        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
       //手动渲染
       html =  thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
       //需要两个参数一个是需要渲染的模板名称，一个是上下文
        if(!StringUtils.isEmpty(html)){
            //渲染完之后存到redis中
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        //返回渲染后的页面
        return html;
    }

    @RequestMapping("/to_detail/{goodsid}")
    public String detail(Model model, MiaoshaUser user,
                         @PathVariable("goodsid") long goodsId) {
        //传入了goodsid,通过goodsService中的方法获取
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("user",user);
        model.addAttribute("goods", goods);//放到页面的model中
        //获取秒杀的开始时间和结束时间
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();//取出毫秒
        long now = System.currentTimeMillis();
        //记录秒杀的状态
        int miaoshaStatus = 0;
        //记录倒计时
        int remainSeconds = 0;

        //判断秒杀是否开始
        if (now < startAt) {
            //未开始,进行倒计时
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt - now) / 1000);

        } else if (now > endAt) {
            //超出秒杀时间，结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            //进入秒杀
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        //这两个参数放到页面中
        model.addAttribute("miaoshaStatus",miaoshaStatus);//放到页面的model中
        model.addAttribute("remainSeconds",remainSeconds);//放到页面的model中

        return "goods_detail";//返回页面
    }

}
