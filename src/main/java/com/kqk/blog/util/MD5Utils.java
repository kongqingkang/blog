package com.kqk.blog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @auhtor kqk
 * @date 2019/11/12 0012 - 20:09
 */
public class MD5Utils {
    /**
     * MD5加密
     *
     * @param str 加密前的字符串
     * @return 加密后的字符串
     */
    public static String code(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer sf = new StringBuffer("");
            for (int offest = 0; offest < byteDigest.length; offest++) {
                i = byteDigest[offest];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sf.append("0");
                sf.append(Integer.toHexString(i));
            }
            //32位加密
            return sf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(code("123456"));
    }
}
