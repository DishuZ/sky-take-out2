package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据 openid 查询用户
     * @param openid
     * @return
     */
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * 新增用户
     * @param user
     */
    void insert(User user);

    /**
     * 根据 id 查询用户
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * 统计某时间段内每天新增的用户
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
