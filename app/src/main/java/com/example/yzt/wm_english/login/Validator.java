package com.example.yzt.wm_english.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YZT on 2017/1/26.
 */

public class Validator {
    //邮箱,手机,账号,密码等的验证
    public static boolean checkEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public static boolean checkUsername(String s) {
        return s.matches("^[a-zA-Z][a-zA-Z0-9_]{4,15}$");
    }

    public static boolean checkPassword(String s) {
        return s.matches("^[a-zA-Z0-9]{6,16}$");
    }
}
