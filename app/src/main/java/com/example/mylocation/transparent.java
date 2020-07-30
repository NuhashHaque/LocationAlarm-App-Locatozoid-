package com.example.mylocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class transparent extends Activity {

    DatabaseHelper myDb;
    //Vibrator v ;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        setContentView(R.layout.transparent);
        myDb=new DatabaseHelper(getApplicationContext());


//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();

//RINGTONE
       AudioManager audioManager= (AudioManager) getSystemService(AUDIO_SERVICE);
        Uri ringTonepath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        int volume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        if(volume==0)
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ringTonepath);
        if(r!=null){
            r.setStreamType(AudioManager.STREAM_ALARM);
            r.play();

           // isRinging = true;
        }




        //setContentView(R.layout.);
        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        Log.d("transparent","transparent");

        //Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibration();
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setTitle("Message");
        aBuilder.setMessage("You are Inside your designated area.");
        aBuilder.setPositiveButton("Turn Off",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int whichButton)
            {
                //v.cancel();
                //myDb.updateConditionLOW("");
                vibration().cancel();
                  finish();
                  r.stop();

                //vibration().cancel();
            }});

        aBuilder.create();
        aBuilder.show();
    }

    public Vibrator vibration() {

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        long[] pattern = { 0, 3000, 3000 };

        v.vibrate(pattern, 0);
        return v;

    }
}
