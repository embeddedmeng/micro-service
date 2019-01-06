package com.embeddedmeng.user.serviceimpl;

import com.embeddedmeng.common.base.BaseDto;
import com.embeddedmeng.common.base.BaseServiceImpl;
import com.embeddedmeng.common.enums.HttpEnum;
import com.embeddedmeng.common.enums.StateEnum;
import com.embeddedmeng.common.enums.TokenEnum;
import com.embeddedmeng.common.util.MD5;
import com.embeddedmeng.common.util.TokenUtils;
import com.embeddedmeng.user.client.thrift.MessageThriftClient;

import com.embeddedmeng.userapi.dao.UserDao;
import com.embeddedmeng.userapi.entity.User;
import com.embeddedmeng.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    @Autowired
    private MessageThriftClient messageThriftClient;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${thrift.message.ip}")
    private String ip;

    @Override
    public String getIdentifyCodeByEmail(String email) {
        try {
            System.out.println("Controller ip = " + ip);

            List<String> list = new ArrayList<>();
            list.add(email);

            int identifyCode = (int)((Math.random()*9+1)*100000);
            for(int j = 0; j< 100; j++){
                System.out.println((int)((Math.random()*9+1)*100000));
            }
            String identifyStr = String.valueOf(identifyCode);

            redisTemplate.opsForValue().set(email, MD5.transMd5(identifyStr),300, TimeUnit.SECONDS);

            boolean is = messageThriftClient.getMessageService().sendEmail(list, "验证码", identifyStr, "esugar.xlsx", "/esugar/excel/esugar.xlsx");
            messageThriftClient.close();
            if (is) {
                return "发送邮件成功";
            } else {
                return "发送邮件失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "发送邮件异常";
        }
    }

    @Override
    public String setPassword(String email, String identifyCode, String pwd) {
        try {
            Object object = redisTemplate.opsForValue().get(email);
            String objectStr = (String)object;
            String identifyMd5Str = MD5.transMd5(identifyCode);
            System.out.println("inputCode = " + identifyCode + "; " + "redisCode = " + objectStr);

            User user = new User();
            user.setEmail(email);
            user.setPassword(MD5.transMd5(pwd));
            if (identifyMd5Str.equals(objectStr)) {
                int suc = userDao.insertUser(user);
                if (suc==1) {
                    return "插入成功";
                } else {
                    return "插入失败";
                }
            } else {
                return "验证失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "验证异常";
        }
    }

    @Override
    public String loginByEmailAndPwd(String email, String pwd) {
        try {
            User user = userDao.selectUserByEmail(email);
            if (user!=null) {
                if (user.getPassword().equals(MD5.transMd5(pwd))) {

                    long time = System.currentTimeMillis();
                    // 7天：如果用户7天没有进入app那么登录超时,需要重新登录
                    String expTime = Long.toString(time + 604800000L);
                    String userStr = String.valueOf(user.getUserId());
                    String token = TokenUtils.jwtProduceToken(userStr, expTime);

                    redisTemplate.opsForValue().set(userStr, token,7, TimeUnit.DAYS);

                    return "登录成功 : " + token;
                } else {
                    return "登录失败";
                }
            } else {
                return "登录失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "登录异常";
        }
    }

    @Override
    public String getUserInfo(HttpServletRequest request) {
        try {

            String token = request.getHeader("token");
            // 校验参数
            int result = TokenUtils.jwtParseToken(token);

            if (String.valueOf(result).equals(TokenEnum.SUCCESS.getState())) {

                String aud = TokenUtils.jwtParseTokenUser(token);

                User user = userDao.selectUserInfo(aud);
                if (user!=null) {

                    return "获取成功 : " + user.getEmail();

                } else {
                    return "获取失败";
                }

            } else {
                return "token失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "获取异常";
        }
    }
}
