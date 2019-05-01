package com.friendlyphire.compass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewPasswordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_HINT = "com.example.android.wordlistsql.REPLYHINT";
    public static final String EXTRA_REPLY_PASSWORD = "com.example.android.wordlistsql.REPLYPASSWORD";

    private EditText mEditHintView;
    private EditText mEditPasswordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        mEditHintView = findViewById(R.id.edit_hint);
        mEditPasswordView = findViewById(R.id.edit_password);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditHintView.getText())||TextUtils.isEmpty(mEditPasswordView.getText())||mEditPasswordView.length()<6) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String hint = mEditHintView.getText().toString();
                    String password = mEditPasswordView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY_HINT, hint);
                    replyIntent.putExtra(EXTRA_REPLY_PASSWORD, password);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}