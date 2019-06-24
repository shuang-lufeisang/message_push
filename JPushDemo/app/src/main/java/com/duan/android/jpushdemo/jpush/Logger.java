package com.duan.android.jpushdemo.jpush;

import android.util.Log;

/**
 * <pre>
 * author : Duan
 * time : 2019/06/24
 * desc :   logger for jpush
 * version: 1.0
 * </pre>
 */
public class Logger {

    //设为false关闭日志
    private static final boolean LOG_ENABLE = true;

    public static void i(String tag, String msg){
        if (LOG_ENABLE){
            Log.i(tag, msg);
        }
    }
    public static void v(String tag, String msg){
        if (LOG_ENABLE){
            Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg){
        if (LOG_ENABLE){
            Log.d(tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (LOG_ENABLE){
            Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg){
        if (LOG_ENABLE){
            Log.e(tag, msg);
        }
    }


}
