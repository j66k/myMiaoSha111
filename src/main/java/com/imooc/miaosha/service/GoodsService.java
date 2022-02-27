package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.imooc.miaosha.service
 * @ClassName: GoodsService
 * @Author: jjt
 * @CreateTime: 2022/2/27 15:04
 * @Description:
 */

@Service
public class GoodsService {
    //先注入他的dao
    @Autowired
    GoodsDao goodsDao;

    //查出所有的商品列表
    public List<GoodsVo> listCoodsVo(){
        return goodsDao.listGoodsVo();
    }

    //查出对应的id的商品
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }
}
