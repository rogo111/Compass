package com.friendlyphire.compass;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PasswordManagerActivity extends AppCompatActivity {

    public static final int NEW_PASSWORD_ACTIVITY_REQUEST_CODE = 1;
    private PasswordViewModel mPasswordViewModel;
    private SharedPreferences myPref;
    private Switch screenlock;
    private TextView nChars;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PASSWORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Password password = new Password(data.getStringExtra(NewPasswordActivity.EXTRA_REPLY_HINT),data.getStringExtra(NewPasswordActivity.EXTRA_REPLY_PASSWORD));
            mPasswordViewModel.insert(password);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void deletePassword(Password password){
        mPasswordViewModel.delete(password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager);

        myPref = getSharedPreferences(getString(R.string.sharedPrefName),MODE_PRIVATE);
        if(!myPref.getBoolean(getString(R.string.screenlock_running),false)){
            startActivity(new Intent(this,ScreenLockActivity.class).putExtra(getString(R.string.screenlock_start_extra),"main"));
            finish();
        }
        else{
            screenlock=findViewById(R.id.enableLock);
            nChars=findViewById(R.id.nCharsView);
            nChars.setText(myPref.getInt(getString(R.string.numberOfCharacters),3)+"");
            if(myPref.getBoolean(getString(R.string.activated),false)&&myPref.getBoolean(getString(R.string.databasePopulated),false)){
                screenlock.setChecked(true);
            }
            else{
                screenlock.setChecked(false);
            }

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            final PasswordListAdapter adapter = new PasswordListAdapter(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            mPasswordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);

            mPasswordViewModel.getPasswords().observe(this, new Observer<List<Password>>() {
                @Override
                public void onChanged(@Nullable final List<Password> passwords) {
                    adapter.setPasswords(passwords);
                    if(passwords==null || passwords.size()==0){
                        getSharedPreferences(getString(R.string.sharedPrefName),MODE_PRIVATE).edit().putBoolean(getString(R.string.databasePopulated),false).commit();
                        getSharedPreferences(getString(R.string.sharedPrefName),MODE_PRIVATE).edit().putBoolean(getString(R.string.activated),false).commit();
                    }
                    else if(passwords.size()>0){
                        getSharedPreferences(getString(R.string.sharedPrefName),MODE_PRIVATE).edit().putBoolean(getString(R.string.databasePopulated),true).commit();
                    }
                }
            });


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PasswordManagerActivity.this, NewPasswordActivity.class);
                    startActivityForResult(intent, NEW_PASSWORD_ACTIVITY_REQUEST_CODE);
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        myPref.edit().putBoolean(getString(R.string.screenlock_running),false).commit();
        moveTaskToBack(true);
    }

    public void increase(View view){
        int current = myPref.getInt(getString(R.string.numberOfCharacters),3);
        if(current<6){
            myPref.edit().putInt(getString(R.string.numberOfCharacters),current+1).commit();
            nChars.setText(current+1+"");
        }
        else{
            Toast.makeText(this,"You cannot select more than 6 characters",Toast.LENGTH_LONG).show();
        }
    }

    public void decrease(View view){
        int current = myPref.getInt(getString(R.string.numberOfCharacters),3);
        if(current>1){
            myPref.edit().putInt(getString(R.string.numberOfCharacters),current-1).commit();
            nChars.setText(current-1+"");
        }
        else{
            Toast.makeText(this,"You cannot select less than 1 character",Toast.LENGTH_LONG).show();
        }
    }

    public void enableLock(View view) {
        if(!myPref.getBoolean(getString(R.string.activated),false)&&myPref.getBoolean(getString(R.string.databasePopulated),false)){
            myPref.edit().putBoolean(getString(R.string.activated),true).commit();
            screenlock.setChecked(true);
        }
        else{
            myPref.edit().putBoolean(getString(R.string.activated),false).commit();
            screenlock.setChecked(false);
        }
    }
}