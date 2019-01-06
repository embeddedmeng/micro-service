package com.embeddedmeng.userapi.dao;

import com.embeddedmeng.userapi.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/8/22 0022.
 */
public interface UserDao {

    // 插入个人信息
    int insertUser(@Param("user") User user);

    // 根据用户id查询用户信息
    User selectUserByEmail(@Param("email") String email);

    // 获取个人信息
    User selectUserInfo(@Param("userId") String userId);

}