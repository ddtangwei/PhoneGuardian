package com.ddtangwei.phoneguardian.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ddtangwei on 2016/10/24.
 */

public class SpUtils {

    private static SharedPreferences sp;

    public static void putBoolean(Context context,String key,Boolean value){

        if (sp == null){
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context,String key,Boolean defValue){

        if (sp == null){
            sp = context.getSharedPreferences("config",context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }

    public static void putString(Context context,String key,String value){

        if (sp == null){
            sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context,String key,String defValue){

        if (sp == null){
            sp = context.getSharedPreferences("config",context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
    }

}
