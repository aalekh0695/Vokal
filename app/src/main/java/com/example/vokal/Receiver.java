package com.example.vokal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
    MainActivity ins;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        String smsSender = "";
        String smsBody = "";
        for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            smsSender = smsMessage.getDisplayOriginatingAddress();
            smsBody += smsMessage.getMessageBody();
        }

//        Toast.makeText(context, smsSender, Toast.LENGTH_SHORT).show();
        ins = MainActivity.getInstace();
        if(ins!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ins.fetchSMSandUpdate();
                }
            }, 1000);
        }

        new NotificationHelper(context).createNotification(smsSender, smsBody);
    }
}
