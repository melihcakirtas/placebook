package com.melih.placebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText resetemail;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetemail = findViewById(R.id.editText6);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(resetemail.getText().toString())){
                    Toast.makeText(ResetPasswordActivity.this, "Please enter your email first", Toast.LENGTH_SHORT).show();
                }
                else{
                    ParseUser.requestPasswordResetInBackground(resetemail.getText().toString(), new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Toast.makeText(ResetPasswordActivity.this, "An email was successfully sent with reset instructions.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ResetPasswordActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
