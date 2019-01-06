package com.embeddedmeng.userapi.service;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    // 通过邮箱获取验证码，设置有效时间5分钟
    public String getIdentifyCodeByEmail(String email);

    // 将获取到的验证码，邮箱，自定义密码上传设置密码
    public String setPassword(String email, String identifyCode, String pwd);

    // 通过邮箱密码进行登录获取token，有效时间七天，七天内登录刷新token，超过七天重新登录验证
    public String loginByEmailAndPwd(String email, String pwd);

    // 获取用户信息
    public String getUserInfo(HttpServletRequest request);

}
