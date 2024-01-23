package com.google.mgmg22demo.history;

import org.junit.Test;

/**
 * 5. 最长回文子串
 *
 * @Author shenxiaoshun
 * @Date 2021/3/15
 */
public class LongestTest {

    @Test
    public void test() {
        System.out.println(longestPalindrome("babad"));
    }

    public String longestPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        int start = 0;
        int end = 0;
        int longestStart = 0;
        int longestEnd = 0;
        for (int i = 0; i < s.length(); i++) {
            // 回文长度为奇数
            for (int j = 0; ((i - j) >= 0 && (i + j) < s.length()); j++) {
                if (s.charAt(i - j) != s.charAt(i + j)) {
                    break;
                }
                start = i - j;
                end = i + j;
                if ((end - start) > (longestEnd - longestStart)) {
                    longestStart = start;
                    longestEnd = end;
                }
            }
            // 回文长度为偶数
            for (int j = 0; ((i - j) >= 0 && (i + j + 1) < s.length()); j++) {
                if (s.charAt(i - j) != s.charAt(i + j + 1)) {
                    break;
                }
                start = i - j;
                end = i + j + 1;
                if ((end - start) > (longestEnd - longestStart)) {
                    longestStart = start;
                    longestEnd = end;
                }
            }
        }
        return s.substring(longestStart, longestEnd + 1);
    }
}
