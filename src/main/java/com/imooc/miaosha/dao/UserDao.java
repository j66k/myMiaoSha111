package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Package: com.imooc.miaosha.dao
 * @ClassName: userDao
 * @Author: jjt
 * @CreateTime: 2022/2/24 11:11
 * @Description:user的接口类
 */

@Mapper
public interface UserDao {

    @Select("select*from user where id=#{id}")
    public User getById(@Param("id")int id);

    @Insert("insert into user(id,name)values(#{id},#{name})")
    public int insert(User user);
}
