package com.luxu.leetcode.hard;

/**
 * @author xulu
 * @date 2/13/2019
 */
public class _854 {

    int result = 0;
    String target;
    String source;

    public int kSimilarity(String A, String B) {
        k1(A, B);
        int mResult = result;
        result = 0;
        k1(B, A);
        return Math.min(mResult,result);
    }

    private void k1(String A, String B) {
        target = B;
        source = A;
        int j = source.length()-1;
        while(j>0){
            change(j);
            j--;
        }
    }

    private void change(int targetIndex){
        int endIndex = 0;
        char targetChar = target.charAt(targetIndex);
        for(int i = targetIndex; i>=0; i--){
            if(source.charAt(i) == targetChar){
                endIndex = i;
                break;
            }
        }
        char[] chars = source.toCharArray();
        for(int i= targetIndex;i>endIndex;i--){
            if(chars[i]!=chars[i-1]){
                char temp  = chars[i];
                chars[i] = chars[i-1];
                chars[i-1] = temp;
                result++;
            }
        }
        source = String.valueOf(chars);
    }

    public static void main(String[] args){
        _854 test = new _854();
        int a = test.kSimilarity("abc","bca");
        System.out.println(a);
    }
}
