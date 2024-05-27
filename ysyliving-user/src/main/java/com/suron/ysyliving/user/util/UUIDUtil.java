package com.suron.ysyliving.user.util;

import java.util.UUID;

/**
 * @author ysy
 * @version 1.0
 * UUIDUtil: 生成uuid的工具类
 */
public class UUIDUtil {

    public static String uuid() {
        //默认下: 生成的字符串形式 xxxx-yyyy-zzz-ddd
        //老师不想要- ,所以使用 replace("-","")
        return UUID.randomUUID().toString().replace("-","");
    }


}
