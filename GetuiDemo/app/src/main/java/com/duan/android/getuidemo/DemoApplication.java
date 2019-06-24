package com.duan.android.getuidemo;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.duan.android.getuidemo.utils.AppUtils;

/**
 * <pre>
 * author : Duan
 * time : 2019/06/19
 * desc :
 * version: 1.0
 * </pre>
 */
public class DemoApplication extends Application {
    private static final String TAG = "GetuiDemo_Application";

    private static DemoHandler handler;
    public static GetuiDemoActivity demoActivity;

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
        Log.d(TAG, "DemoApplication onCreate");
        if (handler == null){
            handler = new DemoHandler();
        }
    }

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static class DemoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "============  handleMessage  ==============");
            switch (msg.what) {
                case 0: //  // 收到一条透传消息
                    if (demoActivity != null) {
                        payloadData.append((String) msg.obj);
                        payloadData.append("\n");
                        if (GetuiDemoActivity.tLogView != null) {
                            Log.e(TAG, "============  handleMessage  case 0 ==============");
                            GetuiDemoActivity.tLogView.append(msg.obj + "\n");
                        }
                    }
                    break;

                case 1:
                    if (demoActivity != null) {
                        if (GetuiDemoActivity.tLogView != null) {
                            Log.e(TAG, "============  handleMessage  case 1 ==============");
                            GetuiDemoActivity.tView.setText((String) msg.obj);
                        }
                    }
                    break;

                case 2: // 通知收到了消息
                    if (demoActivity != null){
                        Log.e(TAG, "============  demoActivity != null ==============");
                        GetuiDemoActivity.showPromptMessage1((String) msg.obj);
                    }else {
                        Log.e(TAG, "============  demoActivity == null ==============");
                    }
                    break;
            }
        }
    }

}
