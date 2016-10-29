package cn.edu.csu.bbs.yunlusafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alan on 2016/9/15.
 */
public class SpUtil {
    private static SharedPreferences sp;

    public static void putBoolean(Context ctx,String key,boolean value){
        if (sp==null){
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static boolean getBoolean(Context ctx,String key,boolean defValue){
        if (sp==null){
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }

    public static void putString(Context ctx,String key,String value){
        if (sp==null){
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context ctx,String key,String defValue){
        if (sp==null){
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    public static void remove(Context ctx, String key) {
        if (sp==null){
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor=sp.edit();
        editor.remove(key);
        editor.commit();
    }
}
