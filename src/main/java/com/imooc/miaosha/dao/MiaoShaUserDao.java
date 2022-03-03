package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Package: com.imooc.miaosha.dao
 * @ClassName: MiaoShaUserDao
 * @Author: jjt
 * @CreateTime: 2022/2/25 16:22
 * @Description:
 */
@Mapper
public interface MiaoShaUserDao {

    @Select("select*from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id")long id);

    //更新数据库
    @Update("update miaosha_user set password = #{password} where id = #{id}")//这里只更新一部分
   public void update(MiaoshaUser toBeUpdate);
}
