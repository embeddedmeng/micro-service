package com.embeddedmeng.user.controller;

import com.embeddedmeng.common.base.BaseController;
import com.embeddedmeng.common.base.BaseDto;
import com.embeddedmeng.common.enums.HttpEnum;
import com.embeddedmeng.common.enums.StateEnum;
import com.embeddedmeng.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController  // 把当前UserController放入容器中 接口暴露层
@RequestMapping("/sys/")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("identify")
    public String getIdentifyByEmail(@RequestParam("email") String email) {
        try {
            String resultStr = userService.getIdentifyCodeByEmail(email);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(new BaseDto(HttpEnum.OK.getState(),
                    StateEnum.DATA_REWRITE.getState(), StateEnum.DATA_REWRITE.getStateInfo()));
        }
    }

    @GetMapping("setpwd")
    public String setPassword(@RequestParam("email") String email, @RequestParam("identify") String identify, @RequestParam("pwd") String pwd) {
        try {
            String resultStr = userService.setPassword(email, identify, pwd);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(new BaseDto(HttpEnum.OK.getState(),
                    StateEnum.DATA_REWRITE.getState(), StateEnum.DATA_REWRITE.getStateInfo()));
        }
    }

    @GetMapping("login")
    public String loginByEmail(@RequestParam("email") String email, @RequestParam("pwd") String pwd) {
        try {
            String resultStr = userService.loginByEmailAndPwd(email, pwd);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(new BaseDto(HttpEnum.OK.getState(),
                    StateEnum.DATA_REWRITE.getState(), StateEnum.DATA_REWRITE.getStateInfo()));
        }
    }

    @GetMapping("userinfo")
    public String getUserInfo(HttpServletRequest request) {
        try {
            String resultStr = userService.getUserInfo(request);
            return resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(new BaseDto(HttpEnum.OK.getState(),
                    StateEnum.DATA_REWRITE.getState(), StateEnum.DATA_REWRITE.getStateInfo()));
        }
    }

}
