package com.luxu.leetcode.easy;

/**
 * @author xulu
 * @date 3/19/2019
 */
public class _984 {
    public String strWithout3a3b(int A, int B) {
        StringBuilder sb = new StringBuilder(A+B);
        int aLeft = A, bLeft = B;
        while(aLeft>0||bLeft>0){
            if(aLeft==0){
                for(int i=0;i<bLeft;i++){
                    sb.append('b');
                }
                break;
            }
            if(bLeft==0){
                for(int i=0;i<aLeft;i++){
                    sb.append('a');
                }
                break;
            }
            if(aLeft<bLeft){
                sb.append("bba");
                bLeft-=2;
                aLeft--;
            }
            else if(bLeft<aLeft){
                sb.append("aab");
                aLeft-=2;
                bLeft--;
            }
            else{
                if(sb.toString().endsWith("b")){
                    sb.append("ab");
                    aLeft--;
                    bLeft--;
                }
                else{
                    sb.append("ba");
                    aLeft--;
                    bLeft--;
                }
            }

        }
        return sb.toString();
    }

    public static void main(String[] args){
        System.out.println(new _984().strWithout3a3b(4,1));
    }
}
