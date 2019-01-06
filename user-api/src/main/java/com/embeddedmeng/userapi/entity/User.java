package com.embeddedmeng.userapi.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/8/21 0021.
 */
@Data
public class User {

    private int userId;
    private String email;
    private String password;
}
