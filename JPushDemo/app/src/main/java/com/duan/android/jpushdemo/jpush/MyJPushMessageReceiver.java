package com.duan.android.jpushdemo.jpush;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.duan.android.jpushdemo.ExampleApplication;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.helper.*;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * <pre>
 * author : Duan
 * time : 2019/06/24
 * desc :  * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 * version: 1.0
 * </pre>
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "JIGUANG-MessageReceiver";
    private static final String KEY_MSG_TYPE = "tag";   // 标志消息类别的key(播放不同的消息提示声)

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        super.onNotifyMessageArrived(context, message);
        Log.d(TAG, "onNotificationMessageArrived -> "  + message.toString());
        Log.d(TAG, "onNotificationMessageArrived message.notificationExtras：-> "  + message.notificationExtras);
        //sendMessage(message.getTitle(), 2);     // 通知收到了消息
        String extra = message.notificationExtras;

        JsonElement jsonElement = new JsonParser().parse(extra);
        Log.d(TAG, "[onNotifyMessageArrived] jsonElement.isJsonObject():  "+ jsonElement.isJsonObject());
        Log.d(TAG, "[onNotifyMessageArrived] jsonElement.getAsJsonObject().get(\"key\"): "+ jsonElement.getAsJsonObject().get(KEY_MSG_TYPE)); // key

        // 获取指定的消息类别
        try {
            String value = jsonElement.getAsJsonObject().get(KEY_MSG_TYPE).toString();    // 指定类
            Log.d(TAG, KEY_MSG_TYPE + " ========== value:  "+ value );
            value = value.replace("\"","");
            Log.d(TAG, KEY_MSG_TYPE + " ========== value:  "+ value );

            sendMessage(value, 12);
        }catch (Exception e){
            Log.e(TAG, "[onNotifyMessageArrived] catch exception: "+ e.toString());
        }


    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context,jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context,jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context,jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context,jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    private void sendMessage(String data, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
        ExampleApplication.sendMessage(msg);
    }


}