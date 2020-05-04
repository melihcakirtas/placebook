package com.melih.placebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    TextView signup,reset;
    EditText usernamelogin,passwordlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.textView2);
        reset = findViewById(R.id.textView3);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        reset.setPaintFlags(reset.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,ResetPasswordActivity.class);
                startActivity(intent1);
            }
        });

        usernamelogin = findViewById(R.id.editText);
        passwordlogin = findViewById(R.id.editText2);

        ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser != null){
            Intent intent = new Intent(MainActivity.this,FeedActivity.class);
            startActivity(intent);
        }
    }
    public void signin(View view){
        ParseUser.logInInBackground(usernamelogin.getText().toString(), passwordlogin.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    if(user.getBoolean("emailVerified")) {
                        alertDisplayer("Login Sucessful", "Welcome, " + usernamelogin.getText().toString() + "!", false);
                    }
                    else
                    {
                        ParseUser.logOut();
                        alertDisplayer("Login Fail", "Please Verify Your Email first", true);
                    }
                } else {
                    ParseUser.logOut();
                    alertDisplayer("Login Fail", e.getMessage() + " Please re-try", true);
                }
            }
        });
    }
    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error) {
                            Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}
