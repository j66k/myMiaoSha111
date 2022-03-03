package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Package: com.imooc.miaosha.controller
 * @ClassName: MiaoshaController
 * @Author: jjt
 * @CreateTime: 2022/2/27 20:11
 * @Description:
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Autowired
    MiaoshaUserService userService;
    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;
    //优化前1000*10  1960  加入缓存后变为3000

    @RequestMapping (value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {//这里是从request中获取id
        model.addAttribute(user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);//如果用户为空，说明没有登录，返回到登录页面
        }
        //1.判断库存
        //先从service中拿到商品
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();//拿到库存
        //判断是否还有库存
        if (stock <= 0) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        //2.判断是否秒杀到了
        //先获取秒杀订单
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);

        if (miaoshaOrder != null) {
            //说明已经秒杀过了
            return Result.error(CodeMsg.MIAOSHA_REPEAT);

        }

        //3.到这个位置说明既有库存，之前也没有重复秒杀，下面可以正式进行秒杀
        //步骤为减小库存，下订单，写入到秒杀订单
        //调用miaoshaService中的方法
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);//两个参数，一个是用户，一个是商品,返回订单
        //返回成功的订单
        return Result.success(orderInfo);
    }

}
