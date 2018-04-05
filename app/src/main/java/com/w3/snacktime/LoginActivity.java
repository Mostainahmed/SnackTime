package com.w3.snacktime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private TextView RegText;
    private EditText UserName, UserPassword;
    private Button SignBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RegText = findViewById(R.id.register_txt);
        UserName = findViewById(R.id.user_name);
        UserPassword = findViewById(R.id.user_pass);
        SignBtn = findViewById(R.id.signin_btn);

        SignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //performLogin();
                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

    }
//    public void onLogin(View view){
//        String username = UserName.getText().toString();
//        String password = UserPassword.getText().toString();
//        String type = "login";
//        VerificationChecker verificationChecker = new VerificationChecker(this);
//        verificationChecker.execute(type, username, password);
//    }
}
