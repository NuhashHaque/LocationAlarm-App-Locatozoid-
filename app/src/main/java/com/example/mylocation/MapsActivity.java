package com.example.mylocation;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import com.google.android.gms.location.LocationRequest;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG ="MAINACTIVITY" ;
    DatabaseHelper myDb;
    String address;
    String data;
    private GoogleMap mMap;
    public GoogleMap mMap1;
    //public List<LatLng> locations;
    private List<LatLng> locations = new ArrayList<>();
    public CircleOptions circleOptions = new CircleOptions();

    static List<Data>alldata=new ArrayList<>();//my all data from database

    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1; // 1 second
    private final long MIN_DIST = 5; // 5 Meters
    private double latMosque=22.898920;
    private double langMosque=89.505197;

    Marker marker;
    Marker mylocation;
    LatLng mosque=new LatLng(latMosque,langMosque);
    float[] distance = new float[2];
    public Button markerClear;
    //markerClear = (Button) findViewById(R.id.removeMark);
    private LatLng latLng;
    //private Path googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
          //permisson for location
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
          //permission for Silenting device
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        //Initialize Database
         myDb=new DatabaseHelper(getApplicationContext());
        //fetch data from database
         alldata=getResults();
         //
         scheduleJob();


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info=new JobInfo.Builder(123,componentName)
                .setPersisted(true)
                .setOverrideDeadline(0)
                .setPeriodic(15*60*1000)
                .build();


        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        //Map style loaded

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

       mMap.setMyLocationEnabled(true);
        //get users location
        locationSetUp();
        //setup cirles in map
        setupMarkers();



    }

    public void setupMarkers() {
        alldata=getResults();
        for(int i=0;i<alldata.size();i++)
        {
            if(alldata.get(i).getCondition()==1) {
                drawCircle(alldata.get(i).getLat(), alldata.get(i).getLang(), alldata.get(i).getRange());
            }
        }

    }

    private void locationSetUp() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    // mMap.clear();

                    if (mylocation != null) {
                        mylocation.remove();
                    }

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18.0f));

                      //saving lat lang for background

                    getSharedPreferences("MY_PREFERENCE1", MODE_PRIVATE).edit().putString("latitude",""+location.getLatitude()).commit();
                    getSharedPreferences("MY_PREFERENCE2", MODE_PRIVATE).edit().putString("longitude",""+location.getLongitude()).commit();

                    // float zoomLevel = (float) 14.0;
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                }
                catch (SecurityException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }

        //function to get the address and location of a touched location
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                }

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                //markerOptions.title(latLng.latitude + " : " + latLng.longitude);


                //GET INFORMATION ABOUT LOCATION
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if(listAddress!=null && listAddress.size()>0)
                    {
                        Log.i("PlaceInfo",listAddress.get(0).toString());
                        //LatLng latLng1=new LatLng(latLng.latitude,latLng.longitude);
                        //markerOptions.position(latLng1);
                         address ="";
                        if(listAddress.get(0).getAddressLine(0)!=null)
                        {
                            address+=listAddress.get(0).getAddressLine(0);
                            Log.e("address1","address1");
                            markerOptions.title("Address:"+address+"  \n  "+latLng.latitude + " : " + latLng.longitude);
                           //marker Dialogue Button
                            final EditText taskEditText = new EditText(MapsActivity.this);

                            new AlertDialog.Builder(MapsActivity.this)
                                    .setTitle("Want to add The location?")
                                    .setMessage(address+ "\n"+"Latitude:"+latLng.latitude+"\n"+"Longitude:"+latLng.longitude+ "\nEnter your Range...")
                                    .setView(taskEditText)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //SQLiteDatabase db=this.getWritableDatabase();
                                           // myDb.insertData()
                                            locations.add(latLng);
                                            int condition=1;
                                            double range=0;
                                            //String exceptionMessage = " ";
                                            String sUsername = taskEditText.getText().toString();
                                            if (sUsername.matches("")) {
                                                Toast.makeText(MapsActivity.this, "You didn't enter Range", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            range = Double.parseDouble(taskEditText.getText().toString());
                                            boolean inserted= myDb.insertData(address,latLng.latitude,latLng.longitude,condition,range);

                                            if(inserted==true)
                                            {

                                                Toast.makeText(MapsActivity.this,"DATA INSERTED",Toast.LENGTH_LONG).show();

                                            }
                                            else
                                            {

                                                Toast.makeText(MapsActivity.this,"DATA NOT INSERTED",Toast.LENGTH_LONG).show();

                                            }
                                            marker.remove();

                                            refresh();

                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                            marker.remove();
                                        }
                                    })
                                    .show();
                        }

                    }

                } catch (IOException e) {
                    Log.e("address4","address4");
                    Toast.makeText(MapsActivity.this,"Enable data connection.",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }



                // Clears the previously touched position
                ///mMap.clear();
                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                marker=mMap.addMarker(markerOptions);
            }
        });
    }
    //map view to home view
    public void goHome(View view)
    {
        Intent activity2Intent = new Intent(getApplicationContext(), Home.class);
        startActivity(activity2Intent);
    }
    //reloac the activity
    public void refresh()
    {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }



    public void drawCircle(double latitude,double longitude,double range){

        // Instantiating CircleOptions to draw a circle around the marker
       // CircleOptions circleOptions = new CircleOptions();
       LatLng point=new LatLng(latitude,longitude);
        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(range);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }
    //function to get all data from database and put them in arraylist.
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

        return resultList;
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
        setupMarkers();
    }


}



