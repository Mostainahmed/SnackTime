package com.w3.snacktime.others;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.w3.snacktime.R;
import com.w3.snacktime.activity.SplashActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    int lower_time_limit;
    int upper_time_limit;
    SharedPreferences sf;
    SharedPreferences confirm;
    Boolean order;
    int current_hour;
    String strDate;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 2000, 10*1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private Timer mTimer;

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            lower_time_limit = 10;
            upper_time_limit = 17;

            confirm = getSharedPreferences("confirmation",MODE_PRIVATE);

            sf = getSharedPreferences("menuOrdered", MODE_PRIVATE);
            order = sf.getBoolean("menuordered", false);

            Calendar current_time = Calendar.getInstance();
            current_hour = current_time.get(Calendar.HOUR_OF_DAY);

            Calendar date = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            strDate = sdf.format(date.getTimeInMillis());

            if (lower_time_limit <= current_hour && current_hour < upper_time_limit) {
                if (!order.equals(true)) {
                    notification();
                }

            }else{
//                Log.e("Log = ", "Running");
//                Log.e("Log", order.toString());

                sf = getSharedPreferences("menuOrdered", MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.putBoolean("menuordered",false);
                editor.commit();
                editor.apply();

                confirm = getSharedPreferences("confirmation", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sf.edit();
                editor1.putString("confirm","");
                editor1.commit();
                editor1.apply();

            }

        }


    };


        public void onDestroy() {
            try {
                mTimer.cancel();
                timerTask.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent("com.w3.SnackTime");
            sendBroadcast(intent);
        }

        public void notification() {

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("RSSPullService");

            Intent myIntent = new Intent(getBaseContext(), SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
            Context context = getApplicationContext();

            Notification.Builder builder;

            builder = new Notification.Builder(context)
                    .setContentTitle("Snack")
                    .setContentText("Please place your order before 12 am")
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.snacks_fresh_icon);

            Notification notification = builder.build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
            startForeground((int)System.currentTimeMillis(), notification);
        }

}


