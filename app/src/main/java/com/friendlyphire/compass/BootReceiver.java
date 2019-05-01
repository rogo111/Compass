package com.friendlyphire.compass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.sharedPrefName),Context.MODE_PRIVATE);
        if(intent.getAction()==Intent.ACTION_BOOT_COMPLETED&&pref.getBoolean(context.getString(R.string.activated),false)) {
            context.startActivity(new Intent(context,ScreenLockActivity.class));
        }
    }
}
