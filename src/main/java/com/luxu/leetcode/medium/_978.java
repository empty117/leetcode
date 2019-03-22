package com.luxu.leetcode.medium;


/**
 * @author xulu
 * @date 3/15/2019
 */
public class _978 {
    public int maxTurbulenceSize(int[] A) {
        if(A.length <2){
            return A.length ;
        }
        int sum = 0,count = 1;
        int type = 0;
        for(int i=0;i<A.length-1;i++){
            int a = A[i],b = A[i+1];
            //第一次记录方向，跳出
            if(type == 0){
                type = a-b;
                if(type!=0){
                    count++;
                }
                type = -type;
                continue;
            }
            //满足要求
            if((a<b && type <0) || (a>b && type>0)){
                count++;
            }
            //不满足要求
            else{
                sum = Math.max(sum,count);
                type = b-a;
                if(type==0){
                    count = 1;
                }
                else{
                    count = 2;
                }
                continue;
            }
            type = -type;
        }
        sum = Math.max(sum,count);
        return sum;
    }

    public static void main(String[] args){
        _978 test = new _978();

        int result = test.maxTurbulenceSize(new int[]{0,1,1,0,1,0,1,1,0,0});
        System.out.println(result);

    }
}
