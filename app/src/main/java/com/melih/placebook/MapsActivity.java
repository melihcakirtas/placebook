package com.melih.placebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    String latitudestring,longitudestring;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        aSwitch = findViewById(R.id.dark_mode);

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
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (aSwitch.isChecked()){
                    try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success;
                        if (googleMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.mapstyle))) success = true;
                        else success = false;

                        if (!success) {
                            Log.e("MapsActivity", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("MapsActivity", "Can't find style. Error: ", e);
                    }
                    aSwitch.setText("Back to Standart Mode");
                }
                else{
                    try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success;
                        if (googleMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.mapstylenormal))) success = true;
                        else success = false;

                        if (!success) {
                            Log.e("MapsActivity", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("MapsActivity", "Can't find style. Error: ", e);
                    }
                }
                aSwitch.setText("Enable Dark Mode");
            }
        });

        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                SharedPreferences sharedPreferences = MapsActivity.this.getSharedPreferences("com.melih.placebook",MODE_PRIVATE);
                boolean firstTime = sharedPreferences.getBoolean("notFirsttime",false);
                if(firstTime){
                    LatLng userlocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(userlocation).title("You are here"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userlocation,15));
                    sharedPreferences.edit().putBoolean("notFirsttime",true);
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            mMap.clear();
            Location lastknown= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastknown != null){
                LatLng lastuserlocation = new LatLng(lastknown.getLatitude(),lastknown.getLongitude());
                mMap.addMarker(new MarkerOptions().position(lastuserlocation).title("You are here"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastuserlocation,15));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length>0){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    mMap.clear();
                    Location lastknown= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(lastknown != null){
                        LatLng lastuserlocation = new LatLng(lastknown.getLatitude(),lastknown.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(lastuserlocation).title("You are here"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastuserlocation,15));
                    }
                }
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Double latitude = latLng.latitude;
        Double longitude = latLng.longitude;

        latitudestring = latitude.toString();
        longitudestring = longitude.toString();
        LatLng target = new LatLng(latitude,longitude);
        MarkerOptions options = new MarkerOptions().position(target).title("New Place");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target,15));
        Toast.makeText(this, "Click on save", Toast.LENGTH_SHORT).show();

    }
    public void upload(View view){
        PlacessClass placessClass = PlacessClass.getinstance();
        String placename = placessClass.getName();
        String placedetail = placessClass.getDetail();
        Bitmap placeimage = placessClass.getImage();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        placeimage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ParseFile parseFile = new ParseFile("image.png",bytes);

        ParseObject object = new ParseObject("Places");
        object.put("name",placename);
        object.put("detail",placedetail);
        object.put("latitude",latitudestring);
        object.put("longitude",longitudestring);
        object.put("image",parseFile);
        object.put("username", ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(MapsActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MapsActivity.this, "Added Succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this,FeedActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
