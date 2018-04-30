package com.w3.snacktime.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.w3.snacktime.R;

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
import java.util.Calendar;

/**
 * Created by W3E16 on 04-Apr-18.
 */

public class VerificationChecker extends AsyncTask<String,Void,String> {
    AlertDialog alertDialog;
    Context context;
    String user_name;
    int current_hour;
    int lower_time_limit;
    int upper_time_limit;
    VerificationChecker (Context ctx){
        context = ctx;

    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "https://happy-snacks.000webhostapp.com/login.php";
        if(type.equals("login")) {
            try {
                user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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

            } catch (IOException f) {
                f.printStackTrace();
            } catch (NullPointerException g){
                g.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) throws NullPointerException {
        try {
            if (result.equals("1")) {

                alertDialog.setMessage("Login Successfull");
                alertDialog.show();

                Calendar current_time = Calendar.getInstance();
                current_hour = current_time.get(Calendar.HOUR_OF_DAY);

                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("USER_NAME", user_name);
                    context.startActivity(i);
                    ((Activity) context).overridePendingTransition(R.anim.activitygoup, R.anim.activitygodown);
                    alertDialog.cancel();
                    ((Activity) context).finish();


            } else {
                try {
                    alertDialog.setMessage("There might be a problem with the server. Please try again after sometime.");
                    alertDialog.show();
                } catch (NullPointerException g) {
                    g.printStackTrace();
                }
            }
        } catch (NullPointerException n) {
            //n.printStackTrace();
            alertDialog.setMessage("There is something wrong with the server. Please try again later!");
            alertDialog.show();
        }

    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
