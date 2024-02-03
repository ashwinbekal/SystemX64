package com.example.systemx64;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;

    public ArrayList<LatLng> locationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent=getIntent();
         locationList = (ArrayList<LatLng>) intent.getSerializableExtra("locationlist");



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override

    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;


        if (locationList != null) {

            for (LatLng latLng : locationList) {
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            }


            LatLng firstLocation = locationList.get(0);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
        }
    }

}