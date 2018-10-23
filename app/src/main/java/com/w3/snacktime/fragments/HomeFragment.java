package com.w3.snacktime.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.w3.snacktime.R;
import com.w3.snacktime.menu.MenuFragment;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ImageView imageView;
    TextView textView;
    int lower_time_limit = 10;
    int upper_time_limit = 12;
    int current_hour;
    Button buttonGo;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = v.findViewById(R.id.imageView2);
        textView = v.findViewById(R.id.textView2);
        buttonGo = v.findViewById(R.id.goButton);
        Typeface CustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/PatchyRobots.ttf");
        buttonGo.setTypeface(CustomFont);
        final SharedPreferences sharedPref = getContext().getSharedPreferences("confirmation", Context.MODE_PRIVATE);
        Calendar current_time = Calendar.getInstance();
        current_hour = current_time.get(Calendar.HOUR_OF_DAY);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {


        if(sharedPref.getString("confirm","").equals("2") && lower_time_limit <= current_hour && current_hour < upper_time_limit){

            imageView.setImageResource(R.drawable.updated);
            textView.setText("You have already submitted and updated your snacks!!! You have a 10 am - 12 am window to update it again");

        }else if(sharedPref.getString("confirm","").equals("1") && lower_time_limit <= current_hour && current_hour < upper_time_limit){

            imageView.setImageResource(R.drawable.smiley_snacks);
            textView.setText("You have submitted your snacks!!! You have a 10 am - 12 am window to update it.");

        } else if(sharedPref.getString("confirm","").equals("") && lower_time_limit <= current_hour && current_hour < upper_time_limit){

            imageView.setImageResource(R.drawable.tension);
            textView.setText("You haven't submitted your snacks choice!!! You have a 10 am - 12 am window to submit it.");

        }else{

            imageView.setImageResource(R.drawable.timesup);
            textView.setText("The order time is over!!! The window will again open after 10am tomorrow.");
            buttonGo.setVisibility(View.GONE);

        }

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lower_time_limit <= current_hour && current_hour < upper_time_limit){
                    Fragment doneFragment = new MenuFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.flContent, doneFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return v;
    }

}
