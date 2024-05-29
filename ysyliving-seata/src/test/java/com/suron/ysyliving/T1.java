package com.suron.ysyliving;

/**
 * @author 韩顺平
 * @version 1.0
 */
public class T1 {
    public static void main(String[] args) {

        /**
         * public static Long valueOf(long l) {
         *         final int offset = 128;
         *         if (l >= -128 && l <= 127) { // will cache
         *             return LongCache.cache[(int)l + offset];
         *         }
         *         return new Long(l);
         * }
         */
        Long a = 128l;
        Long b = 128l;
        System.out.println(a == b);
    }
}
