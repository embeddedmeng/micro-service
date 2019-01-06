package com.embeddedmeng.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: embeddedmeng
 * @Description:
 * @Date: Create in 下午2:14 2018/6/13
 */
public class TokenUtils {

    private static final String MAC_INSTANCE_NAME = "HMacSHA256";
    private static final String secret = "idjflie@#&o**0^p";

    public static String Hmacsha256(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac_sha256 = Mac.getInstance(MAC_INSTANCE_NAME);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), MAC_INSTANCE_NAME);
        hmac_sha256.init(key);
        byte[] buff = hmac_sha256.doFinal(message.getBytes());

        return Base64.encode(buff);
    }

// 获取时间戳
// 方法一：System.currentTimeMillis(); // 最快
// 方法二：Calendar.getInstance().getTimeInMillis(); // 最慢
// 方法三：new Date().getTime();

    // 1.Header，包含JWT基础声明，加密算法与类别
// 2.Payload，存放有效信息的地方
// 包含 Claim ，它可以一些实体（通常指的用户）的状态和额外的元数据，有三种类型
// 2.1.Reserved claims JWT标准里面定好的claim，内容如下：
// 2.2.Public claims
// 2.3.Private claims
// 建议的 Claims 不是强制使用的，完全可以按照自己的需求自定义playload，如果是自定义的claims名，您使用的实现库是不会主动去验证它们的
    // java jwt

    /**
     * 生成token
     * @param user 用户id
     * @param expTime 进入app超时时间
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static String jwtProduceToken(String user, String expTime) throws InvalidKeyException, NoSuchAlgorithmException {

        Gson gson = new Gson();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("type", "JWT"); // 类别
        headerMap.put("alg", "HS256"); // 加密算法

        // claim内容可自定义
        Map<String, String> claimMap = new HashMap<String, String>();
        claimMap.put("aud", user); // 接受者
        claimMap.put("iss", " n"); // 签发者
        claimMap.put("exp", expTime); // 过期时间
//        claimMap.put("wxrefreshtime", wxRefreshTime); // 微信自动刷新时间
//        claimMap.put("wxexp", wxExpTime); // 微信过期时间，需要手动登录
//        claimMap.put("sub", "yyy"); // 主题
//        claimMap.put("iat", "9999999999"); // 签发时间
//        claimMap.put("jti", "jwt_embeddedmeng"); // JWT的唯一身份标识
//        claimMap.put("nbf", "9998888999"); // 定义在什么时间之前，该jwt都是不可用的

        String base64Header = Base64.encode(gson.toJson(headerMap).getBytes());
        String base64Claim = Base64.encode(gson.toJson(claimMap).getBytes());
        String signature = TokenUtils.Hmacsha256(secret, base64Header + "." + base64Claim);

        String jwt = base64Header + "." + base64Claim  + "." + signature;
        return jwt;
    }

    public static int jwtParseToken(String token) {
        Gson gson = new Gson();

        if (token == null || token.equals("")) {
            return 0;
        }

        // 用 . 进行字符串分割，需要用 \\.
        String[] list = token.split("\\.");
        System.out.println("list=" + list[0]);
//        // 解析头部
//        String header = new String(Base64.decode(list[0]));
//        Map<String, String> headerMap = gson.fromJson(header, HashMap.class);
//        System.out.println("headerMap=" + headerMap.get("alg"));

        if (list.length == 3) {
            String claim = new String(Base64.decode(list[1]));
            JsonElement je = new JsonParser().parse(claim);
            BigInteger expTime = je.getAsJsonObject().get("exp").getAsBigInteger();
            if (expTime.longValue() < System.currentTimeMillis()) { // 超时了
                return 2;
            }

            try {
                String signature = TokenUtils.Hmacsha256(secret, list[0] + "." + list[1]);
                if (signature.equals(list[2])) {
                    System.out.println("返回1");
                    return 1;
                } else {
                    System.out.println("返回0-0");
                    return 0;

                }
            } catch (InvalidKeyException i) {
                System.out.println("返回0-1");
                return 0;
            } catch (NoSuchAlgorithmException n) {
                System.out.println("返回0-2");
                return 0;
            } catch (Exception e) {
                System.out.println("返回0-5");
                e.printStackTrace();
                return 0;
            }

        } else {
            System.out.println("返回0-4");
            return 0;
        }

    }


    /**
     * 从token中解析出user
     * @param token
     * @return user唯一名称标识，用来登录的名称
     */
    public static String jwtParseTokenUser(String token) {
        Gson gson = new Gson();

        // 用 . 进行字符串分割，需要用 \\.
        String[] list = token.split("\\.");
        System.out.println("list=" + list[0]);
        if (list.length == 3) {
            String claim = new String(Base64.decode(list[1]));
            JsonElement je = new JsonParser().parse(claim);
            String aud = je.getAsJsonObject().get("aud").getAsString();
            return aud;

        } else {
            System.out.println("返回0-4");
            return null;
        }

    }


    /**
     * 从token中解析出user
     * @param token
     * @return user唯一名称标识，用来登录的名称
     */
    public static JsonObject jwtParseJsonObject(String token) {
        // 用 . 进行字符串分割，需要用 \\.
        String[] list = token.split("\\.");
        System.out.println("list=" + list);
        if (list.length == 3) {
            String claim = new String(Base64.decode(list[1]));
            JsonElement je = new JsonParser().parse(claim);
            return je.getAsJsonObject();

        } else {
            System.out.println("返回0-4");
            return null;
        }
    }
}
