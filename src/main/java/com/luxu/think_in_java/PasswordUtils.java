package com.luxu.think_in_java;

import java.util.List;

/**
 * Created by xulu on 2017/8/1.
 */
public class PasswordUtils {

    @UseCase(id =47, description = "password must contain at lease one number")
    public boolean validatePassword(String password){
        return password.matches("\\w\\d\\w*");
    }

    @UseCase(id =48)
    public String encryptPassword(String password){
        return new StringBuilder(password).reverse().toString();
    }

    @UseCase(id =49, description = "new password cannot equal previously used ones")
    public boolean checkForNewPassword(List<String> prevPassword , String password){
        return !prevPassword.contains(password);
    }
}
