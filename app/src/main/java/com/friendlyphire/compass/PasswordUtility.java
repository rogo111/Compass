package com.friendlyphire.compass;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PasswordUtility {

    private String character;
    private Password current;
    private SharedPreferences myPref;
    private List<Password> passwordList;
    private PasswordViewModel mPasswordViewModel;
    private Context context;
    private TextView hintView,letterView;

    protected PasswordUtility(String password, String character){
        current = new Password("",password);
        this.character = character;
    }

    public PasswordUtility(AppCompatActivity activity, final TextView hintView, final TextView letterView){
        context = activity;
        this.hintView = hintView;
        this.letterView = letterView;

        myPref = activity.getSharedPreferences(activity.getString(R.string.sharedPrefName),activity.MODE_PRIVATE);
        mPasswordViewModel = ViewModelProviders.of(activity).get(PasswordViewModel.class);
        mPasswordViewModel.getPasswords().observe(activity, new Observer<List<Password>>() {
            @Override
            public void onChanged(@Nullable final List<Password> passwords) {
                if(passwordList==null&&passwords!=null){
                    passwordList = passwords;
                    setUpNewPass();
                }
                passwordList = passwords;
            }
        });
        setUpNewPass();
    }

    public static String[] determineUniqueChars(String string){
        Set<String> chars = new HashSet<>();
        for(int i=0;i<string.length();i++){
            if(i<string.length()-1)
                chars.add(string.substring(i,i+1));
            else
                chars.add(string.substring(i));
        }
        String[] unique = new String[chars.size()];
        int i=0;
        for(String s:chars)
            unique[i++]=s;
        return unique;
    }

    public String[] getChallenge(){
        String[] challenge = {current.getHint(),character};
        return challenge;
    }

    private void setUpNewPass(){
        if(passwordList!=null&&passwordList.size()>0){
            Random random = new Random();
            current = passwordList.get(random.nextInt(passwordList.size()));
            String[] uniques = determineUniqueChars(current.getPassword());
            character = uniques[random.nextInt(uniques.length)];
            hintView.setText(current.getHint());
            letterView.setText(character);
        }
    }

    public boolean validatePassword(String password){
        if(password==null)
            return false;

        int numberOfChars = myPref.getInt(context.getString(R.string.numberOfCharacters),3);
        int indexOfStart = current.getPassword().indexOf(character);
        String validationPass = null;
        if(indexOfStart+numberOfChars<current.getPassword().length()){
            validationPass = current.getPassword().substring(indexOfStart+1,indexOfStart+1+numberOfChars);
        }
        else if(indexOfStart==current.getPassword().length()-1){
            validationPass = current.getPassword().substring(0,numberOfChars);
        }
        else{
            validationPass = current.getPassword().substring(indexOfStart+1);
            validationPass += current.getPassword().substring(0,numberOfChars-validationPass.length());
        }

        setUpNewPass();
        if(validationPass!=null&&validationPass.equals(password)){
            return true;
        }
        else{
            return false;
        }
    }
}