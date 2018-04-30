package com.w3.snacktime.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.spark.submitbutton.SubmitButton;

import com.w3.snacktime.R;
import com.w3.snacktime.others.MyBounceInterPolator;

public class LoginActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    private EditText UserName, UserPassword;
    private CheckBox checkBox;
    private Button SignBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //setupWindowAnimations();

        checkBox = findViewById(R.id.rememberCheckbox);
        UserName = findViewById(R.id.user_name);
        UserPassword = findViewById(R.id.user_pass);
        SignBtn = findViewById(R.id.signin_btn);
        Typeface CustomFont = Typeface.createFromAsset(getAssets(), "fonts/PatchyRobots.ttf");
        SignBtn.setTypeface(CustomFont);

    }



    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public void onLogin(View view) throws Exception {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterPolator interpolator = new MyBounceInterPolator(0.05, 20);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);
        alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("");
        try {
            if (isNetworkConnected()) {
                if (UserName.getText().toString().equals("") || UserPassword.getText().toString().equals("")) {
                    alertDialog.setMessage("Please fill up all the credentials");
                    alertDialog.show();
                } else if (checkBox.isChecked()) {
                    SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    String un = UserName.getText().toString();
                    String pw = UserPassword.getText().toString();
                    editor.putString("username", un);
                    editor.putString("password", pw);
                    editor.putBoolean("status", true);
                    editor.commit();
                    editor.apply();
//                String username = un;
//                String password = pw;
                    String type = "login";
                    VerificationChecker verificationChecker = new VerificationChecker(this);
                    verificationChecker.execute(type, un, pw);
                } else {
                    String username = UserName.getText().toString();
                    String password = UserPassword.getText().toString();
                    String type = "login";
                    VerificationChecker verificationChecker = new VerificationChecker(this);
                    verificationChecker.execute(type, username, password);
                    Log.d("hello", "It's working");
                }
            } else {
                alertDialog.setMessage("Your Internet Connectivity is not working!");
                alertDialog.show();
            }
        }catch(Exception e){
            e.printStackTrace();

        }
    }
}
