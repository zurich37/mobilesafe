package com.zurich.mobile.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

    /**
     * 默认的密码字符串组合，apache校验下载的文件的正确性用的就是默认的这个组合
     */
    private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * md5加密的工具方法
     *
     * @return
     */
    public static String encode(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
            //can't reach
        }
    }

    /**
     * 向getMD5方法传入一个你需要转换的原始字符串,将返回字符串的MD5码
     * @param str 原始字符串
     * @return 返回字符串的MD5码
     * @author: waylau.com
     * date: 2013-11-19 下午03:25:30
     */
    public static String getMD5(String str) {
        if (StringUtils.isNotEmpty(str)) {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                System.err.println(Md5Utils.class.getName() + "初始化失败, MessageDigest不支持Md5Utils");
                e.printStackTrace();
            }
            if (digest != null) {
                byte[] bytes = str.getBytes();
                byte[] results = digest.digest(bytes);
                StringBuilder sb = new StringBuilder();
                for (byte result : results) {
                    // 将byte数组转化为16进制字符存入StringBuilder中
                    sb.append(String.format("%02x", result));
                }
                return sb.toString();
            }
        }
        return null;
    }



    private static String bufferToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            appendHexPair(bytes[i], sb);
        }
        return sb.toString();
    }

    private static void appendHexPair(byte b, StringBuilder sb) {
        char c0 = hexDigits[(b & 0xf0) >> 4];
        char c1 = hexDigits[b & 0xf];
        sb.append(c0).append(c1);
    }

    public static String getMD5String(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return getMD5String(str.getBytes());
        }
        return null;
    }

    public static String getMD5String(byte[] bytes) {
        if (!ArrayUtils.isEmpty(bytes)) {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                System.err.println(Md5Utils.class.getName() + "初始化失败, MessageDigest不支持Md5Utils");
                e.printStackTrace();
            }
            if (digest != null) {
                digest.update(bytes);
                return bufferToHex(digest.digest());
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // 字符'a'的MD5码是0cc175b9c0f1b6a831c399e269772661
        System.out.println(Md5Utils.getMD5("6C-0B-84-3C-0E-4A") );
        System.out.println(Md5Utils.getMD5("74-29-AF-55-3D-65").substring(11, 27).toUpperCase());

    }
}
