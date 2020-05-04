package com.melih.placebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ListView listView;
    ArrayList<String> placenames;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profil:
                        break;
                    case R.id.addplaces:
                        Intent intent1 = new Intent(FeedActivity.this,AddPlaceActivity.class);
                        startActivity(intent1);
                        finish();
                        item.setIcon(R.drawable.ic_add_location_black_24dp);
                        break;
                    case R.id.logout:
                        ParseUser.logOut();
                        Intent intent = new Intent(FeedActivity.this,MainActivity.class);
                        startActivity(intent);
                        item.setIcon(R.drawable.ic_call_missed_outgoing_black_24dp);
                        finish();
                        break;
                }
                return false;
            }
        });
        bottomNavigationView.setItemIconTintList(null);
        listView = findViewById(R.id.listview);

        placenames = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,placenames);
        listView.setAdapter(arrayAdapter);
        download();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FeedActivity.this,DetailActivity.class);
                intent.putExtra("name",placenames.get(position));
                startActivity(intent);
            }
        });
    }

    public void download(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    placenames.clear();
                    if(objects.size() >0){
                        for(ParseObject object : objects){
                                placenames.add(object.getString("name"));
                                arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
                else{
                    Toast.makeText(FeedActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
