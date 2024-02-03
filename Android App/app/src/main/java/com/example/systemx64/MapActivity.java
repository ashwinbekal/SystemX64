package com.example.systemx64;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;

    public ArrayList<LatLng> locationList;
    public ArrayList<String> districtList;
    public ArrayList<String> stateList;
    public ArrayList<Integer> rapeCountList;
    public ArrayList<Integer>  kidnapcount;
    public ArrayList<Integer> crueltycount;
    public ArrayList<Integer> dowrycount;
    public ArrayList<Integer> burglary;
    public ArrayList<Integer> dacoity;
    public int year;
    public String crimetype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            locationList = (ArrayList<LatLng>) extras.getSerializable("locationlist");
            districtList = extras.getStringArrayList("districtlist");
            crimetype=getIntent().getStringExtra("Crimetype");
            stateList = extras.getStringArrayList("statelist");
            year = extras.getInt("Year");
          dacoity=extras.getIntegerArrayList("dacoity");
          kidnapcount=extras.getIntegerArrayList("kidnaplist");
          crueltycount=extras.getIntegerArrayList("crueltylist");
          burglary=extras.getIntegerArrayList("burglary");
          dowrycount=extras.getIntegerArrayList("dowrylist");


            rapeCountList = extras.getIntegerArrayList("rapecountlist");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (locationList != null) {
            for (int i = 0; i < locationList.size(); i++) {
                LatLng latLng = locationList.get(i);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Marker " + (i + 1));
                googleMap.addMarker(markerOptions);
            }
            LatLng firstLocation = locationList.get(0);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = Integer.parseInt(marker.getTitle().split(" ")[1]) - 1;
                showMarkerDetailsDialog(position);
                return true;
            }
        });
    }

    private void showMarkerDetailsDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Marker Details")
                .setMessage(
                        "District: " + districtList.get(position) + "\n" +
                        "Murder Count: " + rapeCountList.get(position)+"\n"+
                        "Year: "+year+"\n"+
                        "Kidnap Count: "+kidnapcount.get(position)+"\n"+
                        "CRUELTY BY HUSBAND: "+crueltycount.get(position)+"\n"+
                        "Dowry Death Count: "+dowrycount.get(position)+"\n"+
                         "Burglary: "+burglary.get(position)+"\n"+
                        "Dacoity Count: "+dacoity.get(position))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
