package com.google.mgmg22demo.history;

import org.junit.Test;

/**
 * 剑指 Offer 10- I. 斐波那契数列
 */
public class FibTest {


    @Test
    public void test() {
        System.out.println("" + fib(45));
    }

    public int fib(int N) {
        if (N == 0) return 0;
        if (N == 1 || N == 2) return 1;
        int[] dp = new int[N+1];
        dp[1] = dp[2] = 1;
        for (int i = 3; i <= N; i++) {
            dp[i] = (dp[i - 1] + dp[i - 2])%1000000007;
        }
        return dp[N];
    }

}
