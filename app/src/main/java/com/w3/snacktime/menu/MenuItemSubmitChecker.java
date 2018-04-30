package com.w3.snacktime.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.w3.snacktime.R;
import com.w3.snacktime.activity.LoginActivity;
import com.w3.snacktime.activity.MainActivity;
import com.w3.snacktime.fragments.HomeFragment;
import com.w3.snacktime.others.MenuItemListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static java.security.AccessController.getContext;

/**
 * Created by W3E16 on 10-Apr-18.
 */

class MenuItemSubmitChecker extends AsyncTask<String, Void, String> {
    private AlertDialog alertDialog;
    private Context context;
    private MainActivity activity;
    private MenuItemListener mListner;
    //String user_name;


    //String items;
    public MenuItemSubmitChecker(Context ctx, MenuItemListener listener) {
        context = ctx;
        mListner = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "https://happy-snacks.000webhostapp.com/admin.php";
        if (type.equals("submit")) {
            try {
                String user_name = params[1];
                String items = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" + URLEncoder.encode("menu_name", "UTF-8") + "=" + URLEncoder.encode(items, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
    }

    @Override
    protected void onPostExecute(String result) throws NullPointerException {
        //alertDialog.setMessage(result);
        try {
            if (result.equals("1")) {
//            alertDialog.setMessage("Your order has been confirmed");
//            alertDialog.show();
//            StyleableToast.makeText(context, "Your Order has been submitted successfully",R.style.mytoast).show();

                SharedPreferences sharedPref = context.getSharedPreferences("menuOrdered", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("menuordered", true);
                editor.commit();
                editor.apply();

                SharedPreferences sharedPref1 = context.getSharedPreferences("confirmation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPref1.edit();
                editor1.putString("confirm", "1");
                editor1.commit();
                editor1.apply();
                mListner.onSuccess();

                //toast.show();
                //TODO  comment freen

//            Intent i=new Intent(context,MainActivity.class);
//            context.startActivity(i);

            } else if (result.equals("2")) {

                SharedPreferences sharedPref = context.getSharedPreferences("menuOrdered", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("menuordered", true);
                editor.commit();
                editor.apply();

                SharedPreferences sharedPref1 = context.getSharedPreferences("confirmation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPref1.edit();
                editor1.putString("confirm", "2");
                editor1.commit();
                editor1.apply();
                mListner.onUpdate();

               // toast.show();

            } else {

                SharedPreferences sharedPref1 = context.getSharedPreferences("confirmation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPref1.edit();
                editor1.putString("confirm", "3");
                editor1.commit();
                editor1.apply();
                mListner.onError();

            }
        } catch (NullPointerException n) {
            alertDialog.setMessage("There is something wrong with the server! Please try again later!");
            alertDialog.show();
            mListner.onError();
        }
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
