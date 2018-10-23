package com.w3.snacktime.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.w3.snacktime.R;

public class SplashActivity extends AppCompatActivity {

    CheckBox checkBox;
    TextView splashText;
    ImageView imageViewPro;
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        splashText = findViewById(R.id.splashText);
        Typeface CustomFont = Typeface.createFromAsset(getAssets(), "fonts/PatchyRobots.ttf");
        splashText.setTypeface(CustomFont);
        //CircularImageView imageView = findViewById(R.id.splashImage);

        imageViewPro = findViewById(R.id.progressBar);






        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.uptodown);






        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.downtoup);
        imageViewPro.startAnimation(animation2);














        splashText.startAnimation(animation1);
        //imageView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                if(sharedPref.getBoolean("status",false)){
                    Intent Intent = new Intent(SplashActivity.this, MainActivity.class);
                    Intent.putExtra("USER_NAME", sharedPref.getString("username",""));
                    startActivity(Intent);
//                    setupWindowAnimations();
                    overridePendingTransition(R.anim.activitygoup,R.anim.activitygodown);
                    finish();
                }else {
                    Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
//                    setupWindowAnimations();
                    overridePendingTransition(R.anim.activitygoup,R.anim.activitygodown);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

}
