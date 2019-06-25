package com.duan.android.jpushdemo;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.duan.android.jpushdemo.utils.AppUtils;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.Logger;

/**
 * <pre>
 * author : Duan
 * time : 2019/06/24
 * desc :
 * version: 1.0
 * </pre>
 */
public class ExampleApplication extends Application {

    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onCreate() {
        Logger.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();
        AppUtils.init(this);

        JPushInterface.setDebugMode(true); 	        // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        if (handler == null){
            handler = new DemoHandler();
        }
    }

    private static DemoHandler handler;
    public static MainActivity demoActivity;

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static class DemoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "============  handleMessage  ==============");
            switch (msg.what) {
                case 12: // 通知收到了消息
                    if (demoActivity != null){
                        Log.e(TAG, "============  demoActivity != null ==============");
                        MainActivity.showPromptMessage1((String) msg.obj);
                    }else {
                        Log.e(TAG, "============  demoActivity == null ==============");
                    }
                    break;
            }
        }
    }

}
