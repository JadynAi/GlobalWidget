package com.example.jadynai.globalwidget;

import android.text.TextUtils;

/**
 * @version:
 * @FileDescription: 字符串工具类
 * @Author:jing
 * @Since:2017/9/15
 * @ChangeList:
 */
public class StringUtils {

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().length() == 0;
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static boolean differsExcludeNull(CharSequence a, CharSequence b) {
        if (StringUtils.isEmpty(a) || StringUtils.isEmpty(b)) {
            return false;
        }
        String a1 = a.toString().trim();
        String b1 = b.toString().trim();
        if (StringUtils.isBlank(a1) || StringUtils.isBlank(b1)) {
            return false;
        }
        return !TextUtils.equals(a1, b1);
    }
}
