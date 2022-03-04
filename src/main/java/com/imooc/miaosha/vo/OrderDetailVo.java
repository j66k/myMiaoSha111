package com.imooc.miaosha.vo;


import com.imooc.miaosha.domain.OrderInfo;

/**
 * @Package: com.imooc.miaosha.vo
 * @ClassName: OrderDetailVo
 * @Author: jjt
 * @CreateTime: 2022/3/3 16:14
 * @Description:这就是一个传数据的dto
 */
public class OrderDetailVo {

    private GoodsVo goods;//商品信息
    private OrderInfo order;//订单信息

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
