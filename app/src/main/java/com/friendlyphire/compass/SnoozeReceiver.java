package com.friendlyphire.compass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SnoozeReceiver extends BroadcastReceiver {

    private enum LockState{UNLOCKED, LOCKED, FORCE_LOCKED}

    private LockState lockState = LockState.UNLOCKED;

    public void forceLock(){
        lockState=LockState.FORCE_LOCKED;
    }

    public void unlock(){
        lockState=LockState.UNLOCKED;
    }

    public boolean isLocked(){return lockState==LockState.LOCKED;}
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.sharedPrefName),Context.MODE_PRIVATE);
        if(lockState==LockState.FORCE_LOCKED)
            lockState=LockState.UNLOCKED;
        else if(intent.getAction()==Intent.ACTION_SCREEN_OFF && pref.getBoolean(context.getString(R.string.activated),false)){
            lockState=LockState.LOCKED;
            context.startActivity(new Intent(context,ScreenLockActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        }
    }
}
