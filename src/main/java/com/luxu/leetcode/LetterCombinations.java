package com.luxu.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombinations {
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<String>();

        return null;
    }

    public Map<Character, Character> letterMap(){
        Map<Character,Character> letterMap = new HashMap<Character, Character>();
        return letterMap;
    }

    public static char[] getCharsFromNum(char num){
        switch (num){
            case '2':
                return new char[]{'a','b','c'};
            case '3':
                return new char[]{'d','e','f'};
            case '4':
                return new char[]{'g','h','i'};
            case '5':
                return new char[]{'j','k','l'};
            case '6':
                return new char[]{'m','n','o'};
            case '7':
                return new char[]{'p','q','r','s'};
            case '8':
                return new char[]{'t','u','v'};
            case '9':
               return new char[]{'w','x','y','z'};
            default:
                return null;
        }
    }
    public static void main(String[] args){
        System.out.println(getCharsFromNum('2'));
    }
}