package com.suron.ysyliving.user.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ysy
 * @version 1.0
 * ValidatorUtil: 完成一些校验工作,比如手机号码格式是否正确..
 * 提醒：java基础时，我们讲过正则表达式的使用，可以回顾.
 */
public class ValidatorUtil {

    //校验手机号码的正则表达式
    //13300000000 合格
    //11000000000 不合格
    private static final Pattern mobile_pattern = Pattern.compile("^[1][3-9][0-9]{9}$");


    //编写方法, 如果满足规则，返回T, 否则返回F
    public static boolean isMobile(String mobile) {
        if(!StringUtils.hasText(mobile)) {
            return false;
        }

        //进行正则表达式校验-java基础讲过
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }

    //测试一下校验方法
    @Test
    public void t1() {
        String mobile = "13300000009";
        System.out.println(isMobile(mobile));//
    }


}
