package com.google.mgmg22demo.history;

import org.junit.Test;

import java.math.BigInteger;

/**
 * 求100的阶乘
 *
 * @Author shenxiaoshun
 * @Date 2021/2/9
 */
public class FactorialTest {
    public BigInteger sum(int i) {
        if (i == 1) {
            return BigInteger.ONE;
        }
        return BigInteger.valueOf(i).multiply(sum(i - 1));
    }


    @Test
    public void testFactorial() {
        try {
            System.out.println("计算结果：" + sum(50));
            System.out.println("计算结果：" + doFactorial(50));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int doFactorial(int n) {
        if (n < 0) {
            return -1;//传入的数据不合法
        }
        if (n == 0) {
            return 1;
        } else if (n == 1) {//递归结束的条件
            return 1;
        } else {
            return n * doFactorial(n - 1);
        }
    }
}
