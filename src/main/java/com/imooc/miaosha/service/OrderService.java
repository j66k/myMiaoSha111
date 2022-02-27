package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.dao.OrderDao;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Package: com.imooc.miaosha.service
 * @ClassName: OderService
 * @Author: jjt
 * @CreateTime: 2022/2/27 20:30
 * @Description:
 */

@Service
public class OrderService {
@Autowired
OrderDao orderDao;
    //通过用户id以及商品id来查询秒杀的订单
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {

        return orderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
    }

    //生成秒杀订单
    @Transactional//事务
    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();//生成一个订单对象
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);//渠道
        orderInfo.setStatus(0);//0表示新建订单，未支付
        orderInfo.setUserId(user.getId());

        //调用dao中的方法，把这个对象插入
       long orderId =  orderDao.insertOrder(orderInfo);//返回订单id

        //创建完正常的订单，再创建秒杀的订单
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setUserId(user.getId());

        //调用dao中插入秒杀订单的方法
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        return orderInfo;
    }
}
