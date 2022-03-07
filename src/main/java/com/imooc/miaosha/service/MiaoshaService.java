package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
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


    //创建地址
    public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
        //生成一个随机的串
        //然后进行一次md5编码
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        //然后存到redis中
        redisService.set(MiaoshaKey.getMiaoshaPath,""+user.getId()+"_"+goodsId,str);
        return str;
    }

    //验证传入的地址的合法性
    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if(user==null||path==null){
            return false;
        }
        //先从redis中取出对应的数据
      String pathOld =   redisService.get(MiaoshaKey.getMiaoshaPath,""+user.getId()+"_"+goodsId,String.class);
        //对比是否相同
        return path.equals(pathOld);
    }
}
