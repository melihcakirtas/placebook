package com.melih.placebook;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.ImageDecoder;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.parse.ParseUser;

        import java.io.IOException;

public class AddPlaceActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView ;
    EditText placename,details;
    ImageView imageView;
    Uri imagedata;
    Bitmap selectedimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        bottomNavigationView = findViewById(R.id.navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profil:
                        Intent intent1 = new Intent(AddPlaceActivity.this,FeedActivity.class);
                        startActivity(intent1);
                        finish();
                        item.setIcon(R.drawable.ic_home_black_24dp);
                        break;
                    case R.id.addplaces:
                        break;
                    case R.id.logout:
                        ParseUser.logOut();
                        Intent intent = new Intent(AddPlaceActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
        bottomNavigationView.setItemIconTintList(null);
        placename = findViewById(R.id.editText4);
        details = findViewById(R.id.editText5);
        imageView = findViewById(R.id.imageView2);
    }
   public void selectImage(View view){
       if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
       } else {
           Intent intenttogalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
           startActivityForResult(intenttogalery,2);
       }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intenttogalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intenttogalery,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2 && resultCode==RESULT_OK && data !=null){
            imagedata =data.getData();
            try {
                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imagedata);
                    selectedimage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedimage);
                } else{
                    selectedimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imagedata);
                    imageView.setImageBitmap(selectedimage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void next(View view){
        if(placename == null || details == null || selectedimage == null ){
            Toast.makeText(this, "Please fill required text.", Toast.LENGTH_SHORT).show();
        }else{
            PlacessClass placessClass = PlacessClass.getinstance();
            String placenamee = placename.getText().toString();
            String detailss = details.getText().toString();

            placessClass.setName(placenamee);
            placessClass.setDetail(detailss);
            placessClass.setImage(selectedimage);

            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(intent);
        }

    }
}
