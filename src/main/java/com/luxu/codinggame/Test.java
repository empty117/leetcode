package com.luxu.codinggame;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xulu
 * @date 10/12/2018
 */
public class Test {
    public static void main(String[] args) {
        String value = "122112121212-reg-PLMN-PLMN/MRBTS-1";
//        value = value.substring(value.indexOf("-")+1);
//        System.out.println(value);
        String oper = "reg";
        String fqdn = value.substring(value.indexOf(oper)+oper.length()+1);
        System.out.println(fqdn);
    }

}
