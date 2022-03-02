package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
