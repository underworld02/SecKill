package com.suron.ysyliving.user.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author ysy
 * @version 1.0
 */
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    //准备一个salt [前端使用盐]
    private static final String SALT = "4tIY5VcX";

    //加密加盐, 完成的任务就是 md5(password明文+salt1)
    public static String inputPassToMidPass(String inputPass) {
        System.out.println("SALT.charAt(0)->" + SALT.charAt(0));//c
        System.out.println("SALT.charAt(6)->" + SALT.charAt(6));//T
        String str = SALT.charAt(0) + inputPass + SALT.charAt(6);
        return md5(str);
    }

    //加密加盐, 完成的任务就是把(MidPass +salt2) 转成DB中的密码
    // md5(md5(password明文+salt1)+salt2)
    public static String midPassToDBPass(String midPass, String salt) {
        System.out.println("salt.charAt(1)->" + salt.charAt(1));//L
        System.out.println("salt.charAt(5)->" + salt.charAt(5));//m
        String str = salt.charAt(1) + midPass + salt.charAt(5);
        return md5(str);
    }


    //编写一个方法，可以将password明文，直接转成DB中的密码
    public static String inputPassToDBPass(String inputPass, String salt) {
        String midPass = inputPassToMidPass(inputPass);
        String dbPass = midPassToDBPass(midPass, salt);

        return dbPass;
    }

}