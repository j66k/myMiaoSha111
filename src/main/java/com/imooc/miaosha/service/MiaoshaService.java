package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Package: com.imooc.miaosha.service
 * @ClassName: MiaoshaService
 * @Author: jjt
 * @CreateTime: 2022/2/27 20:41
 * @Description:
 */
@Service
public class MiaoshaService {

    //这个方法实现，减小库存，下订单，写入秒杀订单这些操作

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional//表示这是一个原子操作，可以进行回滚
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //1.减小库存 引入goodsService
        goodsService.reduceStock(goods);
        //2.下订单
        OrderInfo orderInfo = orderService.createOrder(user, goods);//返回订单
        return orderInfo;


    }
}
