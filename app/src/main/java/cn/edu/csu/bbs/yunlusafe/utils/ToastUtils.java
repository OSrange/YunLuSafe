package cn.edu.csu.bbs.yunlusafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Alan on 2016/8/12.
 */
public class ToastUtils {
    public static void show(Context cxt,String msg){
        Toast.makeText(cxt,msg,Toast.LENGTH_SHORT).show();
    }
}
