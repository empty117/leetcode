import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        int number =0;
        for (int i = 0; i < n; i++) {
            String test = in.nextLine();
             boolean foundDigit = false;
            for(int j=0;j<test.length();j++){
                if(Character.isDigit(test.charAt(j))){
                    if(!foundDigit){
                        foundDigit = true;
                    }
                    else if(j == test.length()-1){
                        number ++;
                    }
                }
                else{
                    if(foundDigit){
                        foundDigit = false;
                        number++;
                    }
                }
            }
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        
        System.out.println(number);
    }
}