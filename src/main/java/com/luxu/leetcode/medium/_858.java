package com.luxu.leetcode.medium;

/**
 * @author xulu
 * @date 2/20/2019
 */
public class _858 {

    public int mirrorReflection(int p, int q) {
        //最大公约数
        int g = gcd(p, q);
        p /= g;
        p %= 2;
        q /= g;
        q %= 2;

        //都是奇数
        if (p == 1 && q == 1) return 1;
        return p == 1 ? 0 : 2;
    }

    public int gcd(int a, int b) {
        if (a == 0) return b;
        return gcd(b % a, a);
    }
}
