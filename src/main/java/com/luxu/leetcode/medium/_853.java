package com.luxu.leetcode.medium;

import java.util.*;

/**
 * @author xulu
 * @date 1/31/2019
 */
public class _853 {


    public int carFleet(int target, int[] position, int[] speed) {
        int len = position.length;
        List<Car> cars = new ArrayList<>();
        for(int i = 0;i<len;++i){
            cars.add(new Car(position[i],speed[i]));
        }

        Collections.sort(cars);

        double lastTime = -1d;
        int result = 0;

        for(int i = 0;i<len;++i){
            Car car = cars.get(i);
            double time = (target - car.position)/(double)car.speed;
            if(time > lastTime){
                result++;
                lastTime = time;
            }
        }

        return result;
    }


    private class Car implements Comparable<Car>{
        int position;
        int speed;

        Car(int position,int speed){
            this.position = position;
            this.speed = speed;
        }

        @Override
        public int compareTo(Car o) {
            int gap = o.position - position;
            if (gap != 0) {
                return gap;
            }
            return  speed - o.speed;
        }
    }

    public static void main(String[] args){
        _853 test  = new _853();
        int[] a = {6,8};
        int[] b = {3,2};
        System.out.println(test.carFleet(10,a,b));
    }
}
