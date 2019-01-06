package com.embeddedmeng.zuul.filter;

import com.embeddedmeng.common.base.BaseDto;
import com.embeddedmeng.common.enums.HttpEnum;
import com.embeddedmeng.common.enums.StateEnum;
import com.embeddedmeng.common.enums.TokenEnum;
import com.embeddedmeng.common.util.TokenUtils;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @Author: embeddedmeng
 * @Description:
 * @Date: Create in 下午6:13 2018/6/15
 */
@Slf4j
public class TokenFilter extends ZuulFilter {

//    private final Logger logger = LoggerFactory.getLogger(TokenFilter.class);
    private Gson gson = new Gson();

    @Override
    public String filterType() {
        return PRE_TYPE; // 可以在请求被路由之前调用
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1; // filter执行顺序，通过数字指定 ,优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true;// 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        log.info("--->>> TokenFilter {},{}", request.getMethod(), request.getRequestURL().toString());

        try {

            String uri = request.getRequestURI();
            System.out.println(uri);
            String[] uriArray = uri.split("/");
            System.out.println(uriArray[1]);
            if (uriArray[1].equals("web")) {
                System.out.println("请求web页面");

                ctx.setSendZuulResponse(true); //对请求进行路由
                return null;
            }

            if (uri.equals("/user/sys/identify") || uri.equals("/user/sys/setpwd") || uri.equals("/user/sys/login")) {
                System.out.println("绿灯");

                ctx.setSendZuulResponse(true); //对请求进行路由
                return null;
            }

            // 读取web或移动端设置的request head参数
            String token = request.getHeader("token");

            if (token == null || token.equals("")) {
                System.out.println("token为空");
                ctx.setSendZuulResponse(false); //不对其进行路由
                response.getWriter().write(gson.toJson(new BaseDto(HttpEnum.OK.getState(), StateEnum.FAIL.getState(), TokenEnum.UNPASS.getStateInfo())));
                return null;
            } else {
                // 获取token-signature防止单一用户一秒内多次访问
                String[] tokenArray = token.split("\\.");
                System.out.println(tokenArray.length);
                if (tokenArray.length == 3) {
                    String signature = tokenArray[2];
                    // uri用来记录访问的链接地址，配合起来防止某人单一接口一秒内多次访问
                    Object uriObject = redisTemplate.opsForValue().get(signature + "uri");
                    if (uriObject!=null) {
                        String lastUri = (String)uriObject;
                        if (lastUri.equals(uri)) { // 当前用户1秒内多次访问
                            System.out.println("一秒内访问多次");
                            ctx.setSendZuulResponse(false); //不对其进行路由
                            response.getWriter().write(gson.toJson(new BaseDto(HttpEnum.OK.getState(), StateEnum.FAIL.getState(), TokenEnum.FREQUENTLY.getStateInfo())));
                            return null;
                        }
                        System.out.println("多次访问");
                    }
                    System.out.println("超过一秒访问");
                    // 存入redis并设置超时时间为1秒
                    redisTemplate.opsForValue().set(signature + "uri", uri,1, TimeUnit.SECONDS);

                }
            }

            // 校验参数
            int result = TokenUtils.jwtParseToken(token);

            if (String.valueOf(result).equals(TokenEnum.SUCCESS.getState())) {

                String aud = TokenUtils.jwtParseTokenUser(token);
                if (aud == null || aud.equals("")) {
                    ctx.setSendZuulResponse(false); //不对其进行路由
                    response.getWriter().write(gson.toJson(new BaseDto(HttpEnum.OK.getState(), StateEnum.FAIL.getState(), TokenEnum.UNPASS.getStateInfo())));
                    return null;
                }
                // 设置解析出的aud(也就是用户在redis存储的唯一标识key)
//
                //  setAttribute 只是以request对象为载体在单个程序内传输的数据，并在http规范中，
                // 不会被转发到其他地方。只能采用requestHeader的方式来传输
//                request.setAttribute("aud", aud);

                // 通过上下文句柄设置request头可以实现向下传递
                ctx.addZuulRequestHeader("aud", aud);
                System.out.println("headAUD = " + ctx.getRequest().getHeader("aud"));

                System.out.println("验证通过了");

                ctx.setSendZuulResponse(true); //对请求进行路由
                ctx.setResponseStatusCode(200);
                ctx.set("isSuccess", true);
                return null;

            } else if (String.valueOf(result).equals(TokenEnum.TIMEOUT.getState())){ // 超时
                System.out.println("超时了");

                ctx.setSendZuulResponse(false); //不对其进行路由
                response.getWriter().write(gson.toJson(new BaseDto(HttpEnum.OK.getState(), StateEnum.SUCCESS.getState(), TokenEnum.TIMEOUT.getStateInfo())));
                return null;
            } else {
                System.out.println("未验证通过");

                ctx.setSendZuulResponse(false); //不对其进行路由
                response.getWriter().write(gson.toJson(new BaseDto(HttpEnum.OK.getState(), StateEnum.FAIL.getState(), TokenEnum.UNPASS.getStateInfo())));
                return null;
            }

        } catch (Exception e) {

            e.printStackTrace();
            ctx.setSendZuulResponse(false); //不对其进行路由
//            response.getWriter().write(gson.toJson(new BaseDto(HttpEnum.OK.getState(), StateEnum.INNER_ERROR.getState(),
//                    StateEnum.INNER_ERROR.getStateInfo())));
            return null;

        }

    }

}
