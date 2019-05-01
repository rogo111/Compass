package com.friendlyphire.compass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button screenlock;
    private SharedPreferences myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);

        myPref = getSharedPreferences(getString(R.string.sharedPrefName),MODE_PRIVATE);

        //easy setting
        myPref.edit().putInt(getString(R.string.numberOfCharacters),3).commit();

        if(!myPref.getBoolean(getString(R.string.screenlock_running),false)){
            startActivity(new Intent(this,ScreenLockActivity.class).putExtra(getString(R.string.screenlock_start_extra),"main"));
        }
        screenlock=findViewById(R.id.enableSwitch);
        if(myPref.getBoolean(getString(R.string.activated),false)&&myPref.getBoolean(getString(R.string.databasePopulated),false)){
            screenlock.setText("Screenlock\nON");
        }
        else{
            screenlock.setText("Screenlock\nOFF");
        }
    }

    private boolean back;
    @Override
    public void onBackPressed(){
        myPref.edit().putBoolean(getString(R.string.screenlock_running),false).commit();
        moveTaskToBack(true);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
//        if(!back) {
//            back=false;
//            Intent intent = new Intent(this,ScreenLockActivity.class);
//            PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
//            if(pm.isInteractive())
//                intent.putExtra(getString(R.string.screenlock_start_extra),"mainother");
//            startActivity(intent);
//        }
        super.onStop();
    }

    public void enableLock(View view) {
        //check if screenlockactivity is running, if not (=first run), run it on enabled
        if(!myPref.getBoolean(getString(R.string.activated),false)&&myPref.getBoolean(getString(R.string.databasePopulated),false)){
            myPref.edit().putBoolean(getString(R.string.activated),true).commit();
            screenlock.setText("Screenlock\nON");
        }
        else{
            myPref.edit().putBoolean(getString(R.string.activated),false).commit();
            screenlock.setText("Screenlock\nOFF");
        }
    }

    public void editPasswords(View view) {
        startActivity(new Intent(this,PasswordManagerActivity.class));
    }
}
