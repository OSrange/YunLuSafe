package cn.edu.csu.bbs.yunlusafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Alan on 2016/9/16.
 */
public class Md5Util {
    public static String encoder(String psd)  {
        psd=psd+"syc";
        MessageDigest messageDigest= null;
        try {
            messageDigest = MessageDigest.getInstance("Md5");

        byte[] bs=messageDigest.digest(psd.getBytes());
        StringBuffer stringBuffer=new StringBuffer();
        for (byte b:bs){
            int i=b&0xff;
            String hexString=Integer.toHexString(i);
            if (hexString.length()<2){
                hexString="0"+hexString;
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
