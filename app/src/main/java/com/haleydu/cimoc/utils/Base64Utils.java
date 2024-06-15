package com.haleydu.cimoc.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @auther shoulaxiao
 * @date 2024/6/15 10:40
 **/
public class Base64Utils {

    /**
     * Base64解密字符串
     *
     * @param content     -- 待解密字符串
     * @param charsetName -- 字符串编码方式
     * @return
     */
    public static String base64DecodeWithCharset(String content, String charsetName) {
        if (TextUtils.isEmpty(charsetName)) {
            charsetName = "UTF-8";
        }
        byte[] contentByte = Base64.decode(content, Base64.DEFAULT);
        try {
            return new String(contentByte, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String base64Decode(String content) {
        return base64DecodeWithCharset(content, null);
    }

}
