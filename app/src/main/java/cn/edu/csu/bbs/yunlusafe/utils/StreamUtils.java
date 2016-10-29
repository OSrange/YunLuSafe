package cn.edu.csu.bbs.yunlusafe.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Alan on 2016/8/11.
 */
public class StreamUtils {
    /**
     *
     * @param is 流对象
     * @return   流转换成的字符串  返回null代表异常
     */
    public static String streamToString(InputStream is) {
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        StringBuilder response=new StringBuilder();
        String line;
        try {
            while ((line=reader.readLine())!=null){
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
