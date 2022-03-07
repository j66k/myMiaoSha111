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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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
        if(user==null||goodsId<=0){
            return null;
        }
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

    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user==null||goodsId<=0){
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

//    public static void main(String[] args) {
//        System.out.println(calc("1+3-4*7"));
//    }

    //计算验证码表达式的结果
    //调用js里面的引擎计算验证码的数值
    private static int calc(String exp) {
        try{
            ScriptEngineManager manager = new ScriptEngineManager();
           ScriptEngine engine =  manager.getEngineByName("JavaScript");
          return (Integer) engine.eval(exp);//用他的引擎来计算表达式的数值
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    //生成验证码
    //只做加减乘
    //定义一个char数组，存放操作符号
    private static char[] ops = new char[]{'+','-','*'};
    private String generateVerifyCode(Random rdm) {
       int nums1 =  rdm.nextInt(10);//生成10以内的随机数
       int nums2 =  rdm.nextInt(10);//生成10以内的随机数
       int nums3 =  rdm.nextInt(10);//生成10以内的随机数
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        //拼接成表达式
        String exp = ""+nums1+op1+nums2+op2+nums3;
        return exp;
    }

    //检查验证码的方法
    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        //先进行参数验证
        if(user==null||goodsId<=0){
            return false;
        }
        //从redis中取出验证码，然后进行对比
        Integer old = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(old==null||old!=verifyCode){
            return false;
        }
        //验证完之后，把这个值从redis中删掉
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }
}
