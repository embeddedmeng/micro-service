package com.embeddedmeng.common.util;

import org.springframework.util.DigestUtils;

public class MD5 {

    private static final String slat = "iyy94le18^h#*$kpp)??";

    public static String transMd5(String input) {
        String base = input + "/" + slat;
        System.out.println(base);
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
