package com.example.mylocation;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;


public class ExampleJobService extends JobService {


    public static final String TAG = "EXAMPLE JOB SERVICE";
    private boolean jobcancelled = false;
    private LocationListener locationListener;
    private LocationManager locationManager;
    //public Location location;
    DatabaseHelper myDb;
    static Location location;
    //private List<LatLng> locations = new ArrayList<>();
    public List<Data>alldata=new ArrayList<>();
    public LatLng latLng;
    public static final int TIMEOUT = 10000;
    float[] distance = new float[2];
    //AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        myDb=new DatabaseHelper(getApplicationContext());
        Log.d(TAG, "JOB STARTED");
        myDb = new DatabaseHelper(getApplicationContext());

        doBackgroudwork(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "JOB CANCELLED BEFORE COMPLETION");
        jobcancelled = true;
        return false;
    }

    private void doBackgroudwork(final JobParameters jobParameters) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {

                    alldata=getResults();

                        for(int i=0;i<alldata.size();i++) {
                        if (jobcancelled) {
                            return;
                        }
                        try {


                            double latitude = Double.parseDouble(getSharedPreferences("MY_PREFERENCE1", MODE_PRIVATE).getString("latitude", "1.1"));
                            double longitude = Double.parseDouble(getSharedPreferences("MY_PREFERENCE2", MODE_PRIVATE).getString("longitude", "1.1"));


                            //find distance
                            //static Location location;

                            location.distanceBetween(alldata.get(i).getLat(),alldata.get(i).getLang(),latitude,longitude, distance);
                            //if(distance[0]<=alldata.get(i).getRange())

                           // Log.d(TAG, "run" + i + " lati:" + latitude + " longi" + longitude + "");
                            Thread.sleep(1000);

                            if(distance[0]<=alldata.get(i).getRange()&& alldata.get(i).getCondition()==1) {


                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.xyz",Context.MODE_PRIVATE);
                                boolean tgpref = sharedPreferences.getBoolean("Save",true);

                                if(tgpref)
                                {
                                    silent();
                                }
                                else
                                {
                                    sendNotification("You are inside your designated location!!", "ATTENTION", "hello3", "hello4");

                                }
                               // sendNotification("You are inside your designated location!!", "ATTENTION", "hello3", "hello4");
                                myDb.updateConditionLOW(alldata.get(i).getAddress());
                                SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz"+i, MODE_PRIVATE).edit();
                                editor.putBoolean("NameOfThingToSave"+i, false);
                                editor.commit();
                                Log.d(TAG, "run" + i + " lati:" + latitude + " longi" + longitude + " "+alldata.get(i).getCondition()+" ");

                                distance[0]=0;
                            }
                            //Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.d(TAG, "job FINISHED");
                    jobFinished(jobParameters, false);
                }


            }
        }).start();
    }
    private void sendNotification(String message,String title,String id,String book) {
        //smLocalStore=new SMLocalStore(this);
        Intent intent = new Intent(this, transparent.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.i("id:",id);
        intent.putExtra("id",id);
        intent.putExtra("val","1");
        intent.putExtra("book",book);
        int requestCode = 0;
        //notification sending
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Log.d("alarm manager","alarm manager");
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);


        Log.d(TAG, "sendNotification: nuhash: " + mgr);
        mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TIMEOUT, pendingIntent);
        Log.d(TAG, "sendNotification: nuhash1: " + mgr);



        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSound(sound)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setFullScreenIntent(pendingIntent,true)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);



//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();


        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, noBuilder.build()); //0 = ID of notification
        //mgr.cancel();
    }
    public void silent()
    {
        AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }
    public List<Data> getResults() {

        Data data;

        //MyDb db = new MyDb(this); //my database helper file
        //myDb.open();

        List<Data> resultList = new ArrayList<Data>();


        Cursor c = myDb.getAllData();
        if(c.getCount()==0)
        {
            return resultList ;

        }
////function to retrieve all values from a table- written in MyDb.java file
        while (c.moveToNext())
        {

            String address = c.getString(1);
            Double lat=c.getDouble(2);
            Double lang=c.getDouble(3);
            int condition=c.getInt(4);
            double range=c.getDouble(5);
            //boolean condition=c.getInt(bool)
            //Integer condition=c.getInt(3)>0;
            //boolean value = cursor.getInt(boolean_column_index) > 0;

            try {
                // int condition;
                data = new Data(address, lat, lang, condition,range);
                //o.setDate(date);// setDate function is written in Class file
                data.setAddress(address);
                resultList.add(data);
                //  alldata.add(data);
            }

            catch (Exception e) {
                //String MY_DEBUG_TA="sa";
                Log.e("sa", "Error " + e.toString());
            }

        }

        //c.close();
        // db.close();

        return resultList;
    }


}