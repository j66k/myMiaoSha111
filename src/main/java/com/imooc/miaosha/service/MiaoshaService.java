package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
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

    @Autowired
    RedisService redisService;

    @Transactional//表示这是一个原子操作，可以进行回滚
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //1.减小库存 引入goodsService
       boolean success = goodsService.reduceStock(goods);//判断是否减小库存成功
        //2.下订单
        if(success){
            OrderInfo orderInfo = orderService.createOrder(user, goods);//返回订单
             return orderInfo;
        }else {
            setGoodsOver(goods.getId());//做一个标记来判断是没处理到，还是秒杀失败
            return null;
        }

    }



    //判断是否秒杀成功
    public long getMiaoshaResult(Long userid, long goodsId) {
        //直接调用orderService
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userid,goodsId);
        if(order !=null){
            //秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);//判断是不是没库存了
            if(isOver){
                return -1;//说明秒杀失败
            }else {
                return  0 ;//说明还没处理到，继续轮询
            }

        }

    }

    //对库存做标记 这个标记放在redis中
    private void setGoodsOver(Long goodsId) {

        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
       return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);//有这个key说明卖完了
    }
}
