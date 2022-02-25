package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.UserDao;
import com.imooc.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Package: com.imooc.miaosha.service
 * @ClassName: UserService
 * @Author: jjt
 * @CreateTime: 2022/2/24 11:16
 * @Description:
 */

@Service
public class UserService {

    //把userdao注入进来
    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    //把底下放在一个事务中，如果出错，就会回滚这个事务
    //Transactional
    public boolean tx(User user) {
        User user1 = new User();
        user1.setId(2);
        user1.setName("222");
        userDao.insert(user1);

        User user2 = new User();
        user2.setId(1);
        user2.setName("112");
        userDao.insert(user2);

        return true;
    }
}
