package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.MiaoshaUser;

/**
 * @Package: com.imooc.miaosha.vo
 * @ClassName: GoodsDetailVo
 * @Author: jjt
 * @CreateTime: 2022/3/3 16:14
 * @Description:这就是一个传数据的dto
 */
public class GoodsDetailVo {

    //记录秒杀的状态
    private int miaoshaStatus = 0;
    //记录倒计时
    private int remainSeconds = 0;
    private GoodsVo goods;
    private MiaoshaUser user;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
}
