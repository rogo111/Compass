package com.friendlyphire.compass;

import android.app.admin.DevicePolicyManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ScreenLockActivity extends AppCompatActivity {


    private boolean receiveMode, isLocked;
    private SnoozeReceiver mReceiver;
    private SharedPreferences myPref;
    private PasswordUtility myUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_lock_transparent);
        myPref = getSharedPreferences(getString(R.string.sharedPrefName),MODE_PRIVATE);
        if(getIntent().getExtras()!=null&&getIntent().getExtras().getString(getString(R.string.screenlock_start_extra)).equals("main")){
            getIntent().putExtra(getString(R.string.screenlock_start_extra),"");
            myPref.edit().putBoolean(getString(R.string.screenlock_running),true).commit();
            registerReceiver();
            startActivity(new Intent(this,PasswordManagerActivity.class));
        }
        else{
            registerReceiver();
            moveTaskToBack(true);
        }
    }

    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mReceiver = new SnoozeReceiver();
        registerReceiver(mReceiver, filter);
    }

    private boolean snoozelaunch;
    @Override
    protected void onResume(){
        if(!mReceiver.isLocked()){
            snoozelaunch = false;
            startActivity(new Intent(this,PasswordManagerActivity.class));
        }
        else
            snoozelaunch = true;
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        forceLock();
    }

    private void forceLock(){
        ComponentName component = new ComponentName(this,AdminReceiver.class);
        DevicePolicyManager mDPM =
                (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        if(mDPM.isAdminActive(component)){
            moveTaskToBack(true);
            mDPM.lockNow();
            mReceiver.forceLock();
        }
    }

    @Override
    protected void onStop(){
        if(receiveMode){
            PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
            if(pm.isInteractive()&&isLocked){
                if(snoozelaunch)
                    forceLock();
                else
                    snoozelaunch =true;
            }
            else
                isLocked=true;
        }
        else{
            receiveMode=true;
            setContentView(R.layout.activity_screen_lock);
            EditText et = findViewById(R.id.passField);
            et.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        validatePassword();
                        return true;
                    }
                    return false;
                }
            });
            myUtility = new PasswordUtility(this,(TextView)findViewById(R.id.hintView),(TextView)findViewById(R.id.letterView));
        }
        super.onStop();
    }

    private void validatePassword() {
        EditText et = findViewById(R.id.passField);
        if(myUtility.validatePassword(et.getText().toString())){
            isLocked=false;
            mReceiver.unlock();
            moveTaskToBack(true);
        }
        else
            forceLock();
        et.setText("");
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(mReceiver);
        myPref.edit().putBoolean(getString(R.string.screenlock_running),false).commit();
        super.onDestroy();
    }
}