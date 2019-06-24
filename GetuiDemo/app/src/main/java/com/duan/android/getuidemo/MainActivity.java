package com.duan.android.getuidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.duan.android.getuidemo.getui.DemoIntentService;
import com.duan.android.getuidemo.getui.DemoPushService;
import com.igexin.sdk.PushManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(getApplicationContext(), DemoPushService.class);

        // 在个推SDK初始化后，注册上述 IntentService 类：
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), DemoIntentService.class);
    }
}
