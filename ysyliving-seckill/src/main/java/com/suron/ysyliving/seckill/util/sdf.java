package com.suron.ysyliving.seckill.util;

import org.bouncycastle.pqc.math.linearalgebra.Vector;

import java.util.LinkedList;

/**
 * @author Suron
 * @version 1.0
 */
public class sdf {
    public static void main(String[] args) {
        // 从'A'开始传球，已传球次数为1（起始传球）
        int totalWays = countPasses('A', "A", 0);
        System.out.println("Total ways to pass the ball back to A after 5 passes: " + totalWays);
    }

    /**
     * 递归计算传球方式
     *
     * @param current  当前持球者
     * @param sequence 传球序列
     * @param depth    当前传球深度
     * @return 返回当前配置下，传球回到A的方法数
     */
    public static int countPasses(char current, String sequence, int depth) {
        // 如果已经传了5次球
        if (depth == 5) {
            // 检查是否回到了A
            return current == 'A' ? 1 : 0;
        }

        int count = 0;
        // 定义传球规则
        char[] nextPlayers;
        if (current == 'A') {
            nextPlayers = new char[]{'B', 'C'};
        } else if (current == 'B') {
            nextPlayers = new char[]{'A', 'C'};
        } else {
            nextPlayers = new char[]{'A', 'B'};
        }

        // 遍历可能的传球对象
        for (char player : nextPlayers) {
            // 避免球直接传回给上上个人

            // 递归计算
            count += countPasses(player, sequence + player, depth + 1);
        }

        return count;
    }
}
