package com.luxu.leetcode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by xulu on 2017/9/27.
 */
public class KeyboardRow {

    public String[] findWords(String[] words) {
        String keyboardRegex = "[qwertyuiop]*|[asdfghjkl]*|[zxcvbnm]*";
        return Stream.of(words)
                .filter(word -> word.toLowerCase().matches(keyboardRegex))
                .toArray(String[]::new);
    }
}
