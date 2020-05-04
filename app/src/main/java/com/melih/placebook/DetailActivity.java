package com.melih.placebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView name,detail;
    ImageView imageView;
    private GoogleMap mMap;
    String placename;
    String latitudestring,longitudestring;
    Double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapdetail);
        mapFragment.getMapAsync(this);

        name = findViewById(R.id.textView_name);
        detail = findViewById(R.id.textView_detail);
        imageView = findViewById(R.id.imageView_place);

        Intent intent = getIntent();
        placename = intent.getStringExtra("name");
        //  Toast.makeText(this, placename, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getData();
    }
    public void getData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername()).whereEqualTo("name",placename).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(DetailActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }else{
                    if(objects.size() >0){
                        for(final ParseObject object : objects){
                            ParseFile parseFile = (ParseFile) object.get("image");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                        imageView.setImageBitmap(bitmap);
                                        name.setText(placename);
                                        detail.setText(object.getString("detail"));
                                        latitudestring = object.getString("latitude");
                                        longitudestring = object.getString("longitude");

                                        latitude = Double.parseDouble(latitudestring);
                                        longitude = Double.parseDouble(longitudestring);

                                        LatLng placelocation = new LatLng(latitude,longitude);
                                        mMap.addMarker(new MarkerOptions().position(placelocation).title(placename));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placelocation,15));
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
