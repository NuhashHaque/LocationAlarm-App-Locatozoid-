package com.example.mylocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.mylocation.MapsActivity.alldata;
public class Home extends AppCompatActivity {
    private static final String MY_DEBUG_TAG = "";
    //public static final int COL =11905817;
    DatabaseHelper myDb;
    Data data;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<Data>listitems=new ArrayList<>();
    public ToggleButton toggleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //public static final int COL =11905817;
        setContentView(R.layout.activity_home);
        //getWindow().getDecorView().setBackgroundColor(Color.);
        toggleButton=(ToggleButton)findViewById(R.id.toggle);
        myDb=new DatabaseHelper(getApplicationContext());
        //getResults();
        recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        adapter= new MyAdapter(getResults(),this);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.xyz",Context.MODE_PRIVATE);
        boolean tgpref = sharedPreferences.getBoolean("Save",true);


        if (tgpref){
            toggleButton.setChecked(true);
        }else {
            toggleButton.setChecked(false);
        }


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
                    editor.putBoolean("Save", true);
                    editor.commit();
                } else {
                    // The toggle is disabled
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
                    editor.putBoolean("Save", false);
                    editor.commit();

                }
            }
        });


    }






    public List<Data> getResults() {

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
                Log.e(MY_DEBUG_TAG, "Error " + e.toString());
            }

        }

        c.close();
       // db.close();

        return resultList;
    }









//    public void showDatabase(View view)
//    {
//        Cursor res=myDb.getAllData();
//        if(res.getCount()==0)
//        {
//            return;
//        }
//
//        StringBuffer buffer=new StringBuffer();
//        while(res.moveToNext())
//        {
//            buffer.append("\n"+"ID :"+res.getString(0)+"\n");
//            buffer.append("ADDRESS :"+res.getString(1)+"\n");
//            buffer.append("Latitude:"+res.getString(2)+"\n");
//            buffer.append("LONGITUDE :"+res.getString(3)+"\n"+"\n");
//            Toast.makeText(getApplicationContext(), "All DATA" + buffer.toString(),Toast.LENGTH_SHORT).show();
//
//        }
//    }

//    public void deleteDatabase(View view)
//    {
//        myDb.delete();
//
//    }
}
