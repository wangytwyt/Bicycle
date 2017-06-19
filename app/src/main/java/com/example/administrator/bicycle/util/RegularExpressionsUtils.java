package com.example.administrator.bicycle.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/19.
 * 正则表达式
 */

public class RegularExpressionsUtils {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    public static boolean isIdnumber(String mobiles) {

        Pattern p = Pattern
                .compile("\\d{15}|\\d{18}");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }


    public static boolean isIdcard15(String mobiles) {
        Pattern p = Pattern
                .compile("/^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$/");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }
    public static boolean isIdcard18(String mobiles) {
        Pattern p = Pattern
                .compile("/^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$/");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

}