package com.suron.ysyliving.seckill.util;


import org.junit.jupiter.api.Test;

/**
 * @author ysy
 * @version 1.0
 * MD5UtilTest 测试 MD5Util方法的使用
 */
public class MD5UtilTest {

    @Test
    public void f1() {

        //密码明文 "12345"
        //1. 获取到密码明文 "12345" 的中间密码[即客户端加密加盐后], 在网络上传输的密码
        //2. 即第一次加密加盐处理
        //3. 这个加密加盐的工作，会在客户端/浏览器完成
        System.out.println(MD5Util.inputPassToMidPass("13388584660"));
        //
        ////中间密码“ba3cd05deb76b10870843aa07654840e” 的对应的DB密码
        ////这里对中间密码进行加密加盐时，这个salt可能不一样
        System.out.println(MD5Util.midPassToDBPass("58259c25364e593f9cc9b44a485f245d", "GfBiQk1X"));
        //
        ////密码明文 "12345" -> 得到存放在DB密码
        System.out.println(MD5Util.inputPassToDBPass("12345", "GfBiQk1X"));
    }
}
