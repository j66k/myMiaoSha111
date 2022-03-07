package com.imooc.miaosha.controller;

import com.imooc.miaosha.access.AccessLimit;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.AccessKey;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.MiaoshaKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package: com.imooc.miaosha.controller
 * @ClassName: MiaoshaController
 * @Author: jjt
 * @CreateTime: 2022/2/27 20:11
 * @Description:
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
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

    @Autowired
    MQSender sender;


    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    //优化前1000*10  1960  加入缓存后变为3000
    //系统初始化的时候做的事情放在这里
    @Override
    public void afterPropertiesSet() throws Exception {

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        //当不为空的时候，把list里面的商品，一次加载到redis中来
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());//放到redis中去
            //写入完reddis之后，也写入到map中
            localOverMap.put(goods.getId(), false);//前面存id,后面表示还没用完
        }
    }




    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId") long goodsId,
                                   @PathVariable("path") String path
                                    ) {//这里是从request中获取id
        model.addAttribute(user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);//如果用户为空，说明没有登录，返回到登录页面
        }
        //收到请求之后，先验证这个path
       boolean check =  miaoshaService.checkPath(user,goodsId,path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);//地址错误，直接停止
        }

        //先从map中取 判断商品是否为空
        //使用内存标记减小对于redis的访问
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.MIAOSHA_OVER);//如果没有了直接结束，避免对于底下的redis的访问
        }


        //预减少库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        //判断库存
        if (stock < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAOSHA_OVER);//库存不足，秒杀失败
        }

        //判断是否已经秒杀到了
        //先获取秒杀订单
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (miaoshaOrder != null) {
            //说明已经秒杀过了
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }
        //到这里说明可以进入下一步的秒杀内容
        //先将请求入队
        //先把请求消息封装到message中
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setGoodsId(goodsId);
        mm.setUser(user);
        sender.sendMiaoshaMessage(mm);
        //返回排队中
        return Result.success(0);//0表示排队中


//        //1.判断库存
//        //先从service中拿到商品
//        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goods.getStockCount();//拿到库存
//        //判断是否还有库存
//        if (stock <= 0) {
//            return Result.error(CodeMsg.MIAOSHA_OVER);
//        }
//
//        //2.判断是否秒杀到了
//        //先获取秒杀订单
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//
//        if (miaoshaOrder != null) {
//            //说明已经秒杀过了
//            return Result.error(CodeMsg.MIAOSHA_REPEAT);
//
//        }
//
//        //3.到这个位置说明既有库存，之前也没有重复秒杀，下面可以正式进行秒杀
//        //步骤为减小库存，下订单，写入到秒杀订单
//        //调用miaoshaService中的方法
//        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);//两个参数，一个是用户，一个是商品,返回订单
//        //返回成功的订单
//        return Result.success(orderInfo);
    }

    /**
     * 如果秒杀成功，返回订单的id
     * 失败返回-1
     * 0表示还在排队中
     */
    @AccessLimit(seconds=5,maxCount=10,needLogin=true)//自定义的注解，来做拦截器
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {//这里是从request中获取id
        model.addAttribute(user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);//如果用户为空，说明没有登录，返回到登录页面
        }
        //判断用户是否秒杀到商品
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);

        //如果还没处理完，则返回0，客户端会继续轮询
        return Result.success(result);

    }


//    @RequestMapping(value = "/path",method = RequestMethod.GET)
//    @ResponseBody
//    public Result<String> getMiaoshaPath(Model model, MiaoshaUser user,
//                                         @RequestParam("goodsId") long goodsId,
//                                         @RequestParam("VerifyCode")int verifyCode
//                                         ) {//这里是从request中获取id
//        model.addAttribute(user);
//        if (user == null) {
//            return Result.error(CodeMsg.SESSION_ERROR);//如果用户为空，说明没有登录，返回到登录页面
//        }
//        //校验验证码
//        boolean check = miaoshaService.checkVerifyCode(user,goodsId,verifyCode);
//        if(!check){
//            return Result.error(CodeMsg.REQUEST_ILLEGAL);//返回非法请求
//        }
//
//        //调用service中的方法生成地址
//       String path =  miaoshaService.createMiaoshaPath(user,goodsId);
//        return Result.success(path);
//    }

    @AccessLimit(seconds=5,maxCount=5,needLogin=true)//自定义的注解，来做拦截器
    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode
    ) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
//        //做一个限流防刷  这里的功能通过注解实现了
//        //查询访问的次数
//       String uri =  request.getRequestURI();
//        String key = uri+"_"+user.getId();
//       Integer count =  redisService.get(AccessKey.access,key,Integer.class);
//       if(count==null){
//           //没有进行过访问
//           redisService.set(AccessKey.access,key,1);
//       }else if(count<5){
//           redisService.incr(AccessKey.access,key);
//       }else{
//           return Result.error(CodeMsg.REQUEST_TOMUCH);
//       }



        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path  =miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }



    //下面的方法生成验证码用
    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCode(HttpServletResponse response, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {//这里是从request中获取id
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);//如果用户为空，说明没有登录，返回到登录页面
        }

        BufferedImage image = miaoshaService.createVerifyCode(user,goodsId);
        try{
           OutputStream out =  response.getOutputStream();
            ImageIO.write(image,"JPEG",out);//把前面的图片用jpeg格式放到out中
            out.flush();//清空输出流
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }

    }
}
