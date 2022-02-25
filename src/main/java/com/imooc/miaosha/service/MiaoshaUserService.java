package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.MiaoShaUserDao;
import com.imooc.miaosha.domain.MiaoShaUser;
import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Package: com.imooc.miaosha.service
 * @ClassName: MiaoshaUserService
 * @Author: jjt
 * @CreateTime: 2022/2/25 16:32
 * @Description:对应的服务
 */

@Service
public class MiaoshaUserService {

    @Autowired
    private MiaoShaUserDao miaoShaUserDao;

    public MiaoShaUser getById(long id) {
        return miaoShaUserDao.getById(id);
    }

    public CodeMsg login(LoginVo loginVo) {
        if (loginVo == null) {
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        //调用dao去数据库中查
        MiaoShaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            return CodeMsg.MOBILE_NOTEXIST;
        }

        //到这里说明手机号存在，下一步进行验证密码
        String dbPass = user.getPassword();//数据库中的密码
        String saltDB= user.getSalt();
        if(MD5Util.formPassToDBPass(formPass,saltDB).equals(dbPass)){//判断计算之后的密码和数据库中存的是不是相同

            return CodeMsg.SUCCESS;//登录成功
        }else{
            return CodeMsg.PASSWORD_ERROR;
        }
    }
}
