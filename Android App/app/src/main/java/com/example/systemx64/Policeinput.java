package com.example.systemx64;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Policeinput extends AppCompatActivity {


    List<String[]> dataList2;
    double latitude,longitude;
    ArrayList<String> arr;
    ArrayList<LatLng> uu;
    ArrayList<String> states ;
    ArrayList<String> districts ;
    ArrayList<Integer> rapeCounts;
    String crimetypee,statee,districtee;
    int yearee;
    private static final String ARC_GIS_URL = "https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer/findAddressCandidates";
    Map<String ,Object> list = new HashMap<>();
    int i=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policeinput);
        Spinner spinner = findViewById(R.id.State);
        Spinner spinner1 = findViewById(R.id.district);
        Spinner spinner2 = findViewById(R.id.crimetype);
        Spinner spinner3 = findViewById(R.id.date);

        String fileName = "exxx.csv";
        arr = new ArrayList<>();
        uu = new ArrayList<>();
        states = new ArrayList<>();
        districts = new ArrayList<>();
        rapeCounts = new ArrayList<>();

        List<String[]> dataList = com.example.systemx64.CSVReader.readCSV(Policeinput.this, fileName);
        dataList2 = dataList;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choices, R.layout.custom_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.choices2, R.layout.custom_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.choices3, R.layout.custom_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.choices4, R.layout.custom_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter((adapter1));
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

        // Set OnClickListener for the submit button
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call the test method with selected values from dropdowns
                List<HashMap<String, Integer>> result = test(crimetypee, statee, districtee, yearee, "Male", "STRIKE", 52);
                List<HashMap<String, Object>> topDistrictDetails = getTopDistrictDetails("RAPE", "ANDHRA PRADESH", "NELLORE", 2011, "Male", "STRIKE", 52);

// ArrayLists to store state, district, and rape count


// Loop through all districts
                for (HashMap<String, Object> districtDetails : topDistrictDetails) {
                    String state = (String) districtDetails.get("STATE");
                    String district = (String) districtDetails.get("DISTRICT");
                    int rapeCount = (int) districtDetails.get("RAPE");

                    // Store state, district, and rape count in respective ArrayLists
                    states.add(state);
                    districts.add(district);
                    rapeCounts.add(rapeCount);
                    Log.d("Top District Details", "State: " + state);
                    Log.d("Top District Details", "District: " + district);
                    Log.d("Top District Details", "Rape Count: " + rapeCount);
                }

// Now you have states, districts, and rapeCounts ArrayLists containing the respective data



                List<String> districtList = new ArrayList<>(); // Create a new list to store district values
                for (int i = 0; i < Math.min(5, result.size()); i++) {
                    HashMap<String, Integer> map = result.get(i);
                    for (String district : map.keySet()) {
                        int crimeCount = map.get(district);
                        Log.d("TopDistricts", district + ": " + crimeCount);
                        districtList.add(district); // Add the district to the list
                    }
                }
                for (String district : districtList) {
                    Log.d("DistrictValue", district);
                }

                int j = 0;
                for (String district : districtList) {
                    main3(district);
                }
                if(uu.size()!=0){
                    Intent intent=new Intent(Policeinput.this,MapActivity.class);
                    intent.putExtra("locationlist",uu);
                    intent.putExtra("districtlist",districts);
                    intent.putExtra("statelist",states);
                    intent.putExtra("rapecountlist",rapeCounts);
                    logOrProcessLatLng(uu);
                    startActivity(intent);
                    finish();

                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                statee = selectedItem;
                Log.i("state", statee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                districtee = selectedItem;
                Log.i("district", districtee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                crimetypee = selectedItem;
                Log.i("crimetype", crimetypee);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                yearee = Integer.parseInt(selectedItem);
                Log.i("Year", String.valueOf(yearee));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public List<HashMap<String, Integer>> test(String crimetype, String state, String district, int year, String gender, String season, int age) {
        List<HashMap<String, Integer>> topDistricts = new ArrayList<>();

        Set<String> addedDistricts = new HashSet<>();

        for (String[] rowData : dataList2) {
            Data row = convertToData(rowData);
            if (row.getState().equals(state)) {
                String currentDistrict = row.getDistrict();
                if (!addedDistricts.contains(currentDistrict)) {
                    int crimeCount = 0;
                    for (String[] innerRowData : dataList2) {
                        Data innerRow = convertToData(innerRowData);
                        if (innerRow.getState().equals(state) && innerRow.getDistrict().equals(currentDistrict)) {
                            crimeCount += innerRow.getCrimeCount();
                        }
                    }
                    HashMap<String, Integer> districtMap = new HashMap<>();
                    districtMap.put(currentDistrict, crimeCount);
                    topDistricts.add(districtMap);
                    addedDistricts.add(currentDistrict);
                }
            }
        }

        topDistricts.sort(new Comparator<HashMap<String, Integer>>() {
            @Override
            public int compare(HashMap<String, Integer> o1, HashMap<String, Integer> o2) {
                return o2.values().iterator().next() - o1.values().iterator().next();
            }
        });

        return topDistricts.subList(0, Math.min(5, topDistricts.size()));
    }

    private Data convertToData(String[] rowData) {

        String state = rowData[0];
        String district = rowData[1];
        int crimeCount = 0;
        try {

            crimeCount = Integer.parseInt(rowData[3]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return new Data(state, district, crimeCount);
    }
    private class GeocodingTask extends AsyncTask<String, Void, LatLng> {
        @Override
        protected LatLng doInBackground(String... strings) {
            String placeName = strings[0];

            double latitude = 0, longitude = 0;

            try {
                URL url = new URL(ARC_GIS_URL + "?singleLine=" + URLEncoder.encode(placeName, "UTF-8") + "&f=json&maxLocations=1");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                conn.disconnect();

                // Parse JSON response to get latitude and longitude
                String jsonResponse = response.toString();
                int latIndex = jsonResponse.indexOf("\"y\":") + 4;
                int longIndex = jsonResponse.indexOf("\"x\":") + 4;
                int endIndex = jsonResponse.indexOf("}", latIndex);
                latitude = Double.parseDouble(jsonResponse.substring(latIndex, endIndex));
                longitude = Double.parseDouble(jsonResponse.substring(longIndex, jsonResponse.indexOf(",", longIndex)));

                Log.d("Latitude", String.valueOf(latitude));
                Log.d("Longitude", String.valueOf(longitude));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return new LatLng(latitude, longitude);
        }

        @Override
        protected void onPostExecute(LatLng latLng) {

            Log.d("Coordinates", latLng.toString());
            uu.add(latLng);
            if (uu.size() == arr.size()) {
                // All LatLng values have been obtained, so log or process them here
                logOrProcessLatLng(uu);
            }
        }
    }
    private void logOrProcessLatLng(ArrayList<LatLng> uu) {
        for (LatLng latLng : uu) {

            Log.d("LatLng", latLng.latitude + ", " + latLng.longitude);
        }
    }

    public void main3(String placeName) {
        new GeocodingTask().execute(placeName);
    }
    public List<HashMap<String, Object>> getTopDistrictDetails(String crimetype, String state, String district, int year, String gender, String season, int age) {
        List<HashMap<String, Object>> topDistrictDetails = new ArrayList<>();
        Set<String> addedDistricts = new HashSet<>();

        for (String[] rowData : dataList2) {
            Data row = convertToData(rowData);
            if (row.getState().equals(state)) {
                String currentDistrict = row.getDistrict();
                if (!addedDistricts.contains(currentDistrict)) {
                    int totalIPCcrimes = 0;
                    int rapeCount = 0;
                    // You may need to define other variables here depending on your dataset

                    for (String[] innerRowData : dataList2) {
                        Data innerRow = convertToData(innerRowData);
                        if (innerRow.getState().equals(state) && innerRow.getDistrict().equals(currentDistrict)) {
                            totalIPCcrimes += innerRow.getCrimeCount();
                            if (innerRow.getState().equals(state) && innerRow.getDistrict().equals(currentDistrict)) {
                                rapeCount += innerRow.getCrimeCount();
                            }
                            // Update other attributes accordingly
                        }
                    }
                    HashMap<String, Object> districtDetails = new HashMap<>();
                    districtDetails.put("STATE", state);
                    districtDetails.put("DISTRICT", currentDistrict);
                    districtDetails.put("RAPE", rapeCount);
                    // Add other attributes here
                    districtDetails.put("TOTAL IPC CRIMES", totalIPCcrimes);
                    topDistrictDetails.add(districtDetails);
                    addedDistricts.add(currentDistrict);
                }
            }
        }

        topDistrictDetails.sort(new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                return (int) o2.get("TOTAL IPC CRIMES") - (int) o1.get("TOTAL IPC CRIMES");
            }
        });

        return topDistrictDetails.subList(0, Math.min(5, topDistrictDetails.size()));
    }


}