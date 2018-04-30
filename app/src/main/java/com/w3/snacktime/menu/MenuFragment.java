package com.w3.snacktime.menu;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.w3.snacktime.activity.LoginActivity;
import com.w3.snacktime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.spark.submitbutton.SubmitButton;
import com.w3.snacktime.fragments.HomeFragment;
import com.w3.snacktime.others.MenuItemListener;
import com.w3.snacktime.others.MyBounceInterPolator;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements MenuItemListener {

    private static final String MENU_URL = "https://happy-snacks.000webhostapp.com/date.php";

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<Menu> menuList;
    private AlertDialog.Builder ab;
    private AlertDialog alertDialog;
    private ProgressBar mProgressBar;
    private Button submit, home, reload;
    private String items;
    private String user_name;
    private LoginActivity intent;
    private Dialog mDialog;
    private Button mButtonYes, mButtonNo;
    private static int SPLASH_TIME_OUT = 2750;
    private int lower_time_limit = 10;
    private int upper_time_limit = 20;
    private int current_hour;
    public TextView textView,textViewNoItem;
    public ImageView imageViewError;
    private MenuItemSubmitChecker menuItemSubmitChecker;

    public MenuFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        menuItemSubmitChecker = new MenuItemSubmitChecker(getContext(), this);

        recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView.setHasFixedSize(true);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);

        Calendar current_time = Calendar.getInstance();
        current_hour = current_time.get(Calendar.HOUR_OF_DAY);

        imageViewError = view.findViewById(R.id.noOrderImageView);
        imageViewError.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        menuList = new ArrayList<>();
        loadMenu();

        reload = view.findViewById(R.id.reload);
        submit = view.findViewById(R.id.submit);
        home = view.findViewById(R.id.goHome);
        Typeface CustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/PatchyRobots.ttf");
        submit.setTypeface(CustomFont);
        home.setTypeface(CustomFont);
        reload.setTypeface(CustomFont);

        textViewNoItem = view.findViewById(R.id.textViewNoItem);
        textView = view.findViewById(R.id.textViewCommand);
        textView.setText("Please wait while loading...");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
                MyBounceInterPolator interpolator = new MyBounceInterPolator(0.05, 20);
                myAnim.setInterpolator(interpolator);
                v.startAnimation(myAnim);

                if (lower_time_limit <= current_hour && current_hour < upper_time_limit) {
                    submit();
                } else {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View sub = new View(getContext());
                    View sub1 = inflater.inflate(R.layout.cust_toast_timesup_layout, (ViewGroup) sub.findViewById(R.id.relativeLayout3));

                    Toast toast = new Toast(getContext());
                    toast.setView(sub1);
                    toast.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Fragment doneFragment = new HomeFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.flContent, doneFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }

                    }, SPLASH_TIME_OUT);
                }
            }
        });

        return view;
    }

    private void submit() {

        user_name = getActivity().getIntent().getStringExtra("USER_NAME");
        items = menuAdapter.getItem();
        ab = new AlertDialog.Builder(getContext());
        ab.setTitle("Confirm").
                setMessage(user_name + ", Are you sure to order " + items + " for your evening snacks").
                setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), "You order " + items + " has been added to the queue ", Toast.LENGTH_LONG).show();
                        String type = "submit";
                        menuItemSubmitChecker.execute(type, user_name, items);
                        mProgressBar.setIndeterminate(true);
                        mProgressBar.setVisibility(View.VISIBLE);
                        submit.setEnabled(false);

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                submit.setEnabled(true);
            }
        });
        alertDialog = ab.create();
        if (items == null) {
            Toast.makeText(getContext(), "Please select an item", Toast.LENGTH_LONG).show();
        } else {
            alertDialog.show();
        }

    }

    private void loadMenu() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, MENU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mProgressBar.setVisibility(View.GONE);
                            JSONArray menus = new JSONArray(response);
                            if(menus.length() <= 0){
                                textView.setVisibility(View.GONE);
                                textViewNoItem.setText("There are no items available for today!!!");
                                textViewNoItem.setVisibility(View.VISIBLE);
                                imageViewError.setVisibility(View.VISIBLE);
                                home.setVisibility(View.VISIBLE);
                                reload.setVisibility(View.VISIBLE);
                                home.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Fragment doneFragment = new HomeFragment();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.flContent, doneFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                    }
                                });
                                reload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loadMenu();
                                        reload.setVisibility(View.GONE);
                                        home.setVisibility(View.GONE);
                                        mProgressBar.setVisibility(View.VISIBLE);
                                        imageViewError.setVisibility(View.GONE);
                                        textViewNoItem.setVisibility(View.GONE);
                                    }
                                });
                            }else{
                                textView.setText("Please select one item");
                                textView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < menus.length(); i++) {
                                    JSONObject menuObject = menus.getJSONObject(i);
                                    int id = menuObject.getInt("id");
                                    String menuitem = menuObject.getString("menuitem");
                                    String image = menuObject.getString("image");
                                    Menu menulist = new Menu(id, menuitem, image);
                                    menuList.add(menulist);
                                }
                            }
                            menuAdapter = new MenuAdapter(getContext(), menuList);
                            recyclerView.setAdapter(menuAdapter);
                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    submit.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLongClick(View view, int position) {
                                    submit.setVisibility(View.VISIBLE);
                                }
                            }));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        textView.setVisibility(View.GONE);
                        textViewNoItem.setText("An error occurred with the server!!! Please try again later");
                        textViewNoItem.setVisibility(View.VISIBLE);
                        imageViewError.setVisibility(View.VISIBLE);

                        reload.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        home.setVisibility(View.VISIBLE);
                        home.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment doneFragment = new HomeFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.flContent, doneFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                        reload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadMenu();
                                reload.setVisibility(View.GONE);
                                home.setVisibility(View.GONE);
                                mProgressBar.setVisibility(View.VISIBLE);
                                imageViewError.setVisibility(View.GONE);
                                textViewNoItem.setVisibility(View.GONE);
                            }
                        });
                    }
                });
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }


    @Override
    public void onSuccess() {

        int SCREEN_TIME_OUT = 2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Fragment doneFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, doneFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

        }, SCREEN_TIME_OUT);

        mProgressBar.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View sub = new View(getContext());
        View sub1 = inflater.inflate(R.layout.cust_toast_layout, (ViewGroup) sub.findViewById(R.id.relativeLayout1));

        Toast toast = new Toast(getContext());
        toast.setView(sub1);
        toast.show();

    }

    @Override
    public void onError() {
        submit.setEnabled(true);
        mProgressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        textViewNoItem.setText("An error occurred with the server!!! Please try again later");
        textViewNoItem.setVisibility(View.VISIBLE);
        imageViewError.setVisibility(View.VISIBLE);
        home.setVisibility(View.VISIBLE);
        reload.setVisibility(View.VISIBLE);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment doneFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, doneFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMenu();
                reload.setVisibility(View.GONE);
                home.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                imageViewError.setVisibility(View.GONE);
                textViewNoItem.setVisibility(View.GONE);
            }
        });
    }

    public void onUpdate(){

        int SCREEN_TIME_OUT = 2500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Fragment doneFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, doneFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }

        }, SCREEN_TIME_OUT);

        mProgressBar.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View sub = new View(getContext());
        View sub1 = inflater.inflate(R.layout.cust_toast_warning_layout, (ViewGroup) sub.findViewById(R.id.relativeLayout2));

        Toast toast = new Toast(getContext());
        toast.setView(sub1);
        toast.show();

    }
}


