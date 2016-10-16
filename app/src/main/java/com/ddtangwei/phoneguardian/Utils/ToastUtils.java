package com.ddtangwei.phoneguardian.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ddtangwei on 2016/10/16.
 */

public class ToastUtils {

    public static void Toast(Context context, String s) {

        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
