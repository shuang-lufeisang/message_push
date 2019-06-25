package com.duan.android.jpushdemo;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duan.android.jpushdemo.jpush.ExampleUtil;
import com.duan.android.jpushdemo.jpush.PushSetActivity;
import com.duan.android.jpushdemo.utils.SoundUtils;

import cn.jpush.android.api.JPushInterface;

/**  将 极光官方DEMO 转化为 AndroidStudio 项目  */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mInit;
    private Button mSetting;
    private Button mStopPush;
    private Button mResumePush;
    private Button mGetRid;
    private TextView mRegId;
    private EditText msgText;

    public static boolean isForeground = false;

    private Context context;
    private static Context cxt;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        cxt = this;
        ExampleApplication.demoActivity = this;

        initView();


        PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(android.Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            //PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        }


        registerMessageReceiver();  // used for receive msg
    }

    private void initView(){
        TextView mImei = (TextView) findViewById(R.id.tv_imei);
        String udid =  ExampleUtil.getImei(getApplicationContext(), "");
        if (null != udid) mImei.setText("IMEI: " + udid);

        TextView mAppKey = (TextView) findViewById(R.id.tv_appkey);
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
        if (null == appKey) appKey = "AppKey异常";
        mAppKey.setText("AppKey: " + appKey);

        mRegId = (TextView) findViewById(R.id.tv_regId);
        mRegId.setText("RegId:");

        String packageName =  getPackageName();
        TextView mPackage = (TextView) findViewById(R.id.tv_package);
        mPackage.setText("PackageName: " + packageName);

        String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
        TextView mDeviceId = (TextView) findViewById(R.id.tv_device_id);
        mDeviceId.setText("deviceId:" + deviceId);

        String versionName =  ExampleUtil.GetVersion(getApplicationContext());
        TextView mVersion = (TextView) findViewById(R.id.tv_version);
        mVersion.setText("Version: " + versionName);

        mInit = (Button)findViewById(R.id.init);
        mInit.setOnClickListener(this);

        mStopPush = (Button)findViewById(R.id.stopPush);
        mStopPush.setOnClickListener(this);

        mResumePush = (Button)findViewById(R.id.resumePush);
        mResumePush.setOnClickListener(this);

        mGetRid = (Button) findViewById(R.id.getRegistrationId);
        mGetRid.setOnClickListener(this);

        mSetting = (Button)findViewById(R.id.setting);
        mSetting.setOnClickListener(this);

        msgText = (EditText)findViewById(R.id.msg_rec);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.init:     // 点击了"INITPUSH"
                init();
                break;
            case R.id.setting:  // 点击了高级功能
                showPromptMessage("点击了高级功能");
                Intent intent = new Intent(MainActivity.this, PushSetActivity.class);
                startActivity(intent);
                break;
            case R.id.stopPush:  // 点击了"STOPPUSH"
                JPushInterface.stopPush(getApplicationContext());
                break;
            case R.id.resumePush:// 点击了"RESUMEPUSH"
                JPushInterface.resumePush(getApplicationContext());
                break;
            case R.id.getRegistrationId:// 点击了"GET_REG_ID"
                String rid = JPushInterface.getRegistrationID(getApplicationContext());
                if (!rid.isEmpty()) {
                    mRegId.setText("RegId:" + rid);
                } else {
                    Toast.makeText(this, "Get registration fail, JPush init failed!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init(){
        JPushInterface.init(getApplicationContext());
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }





    private static final int REQUEST_PERMISSION = 0;
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                //PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            } else {
                Log.e(TAG, "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                //PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public static void showPromptMessage1(String message){
        Toast.makeText(cxt, message, Toast.LENGTH_LONG).show();
        playMusic(message);   // 接收到推送播放提示音
    }

    /**  播放声音 */
    private static String titleOrdered= "12";
    private static String titleQuote = "11";
    private static void playMusic(String message){
        Log.e(TAG, "=========  播放声音  =============");
        if (message == null){
            SoundUtils.playSound(R.raw.test);
        }else {
            if (TextUtils.equals(titleOrdered, message)){
                SoundUtils.playSound(R.raw.ordered);
            }else if (TextUtils.equals(titleQuote, message)){
                SoundUtils.playSound(R.raw.quote);
            }
        }

    }












    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String message = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + message + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCustomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

    private void setCustomMsg(String msg){
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }
    }


    private void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
