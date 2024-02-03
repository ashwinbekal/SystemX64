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
    int ryear;
    ArrayList<Integer> rkidnap ;
    ArrayList<Integer> rdacoity;
    ArrayList<Integer> rdowry;
    ArrayList<Integer> rcruelty ;
    ArrayList<Integer> rburglary;







    ArrayList<String> states ;
    ArrayList<Integer> simple ;
    ArrayList<Integer> yearr ;

    ArrayList<String> districts ;
    ArrayList<Integer> rapeCounts;
    String crimetypee,statee,districtee,sex;
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
      yearr = new ArrayList<>();
        uu = new ArrayList<>();
        states = new ArrayList<>();
        rburglary=new ArrayList<>();
        rcruelty=new ArrayList<>();
        rdowry=new ArrayList<>();
        rdacoity=new ArrayList<>();
        rkidnap=new ArrayList<>();


        simple = new ArrayList<>();
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
                List<HashMap<String, Integer>> result = test("RAPE", statee, districtee, yearee, sex, "STRIKE", 52);


// ArrayLists to store state, district, and rape count


// Loop through all districts

// Now you have states, districts, and rapeCounts ArrayLists containing the respective data



                List<String> districtList = new ArrayList<>(); // Create a new list to store district values
                for (int i = 0; i < Math.min(5, result.size()); i++) {
                    HashMap<String, Integer> map = result.get(i);
                    for (String district : map.keySet()) {
                        int crimeCount = map.get(district);
                        simple.add(crimeCount);

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
                    intent.putExtra("Crimetype",crimetypee);
                    intent.putExtra("Year",ryear);
                    intent.putExtra("dowrylist",rdowry);
                    intent.putExtra("kidnaplist",rkidnap);
                    intent.putExtra("crueltylist",rcruelty);
                    intent.putExtra("burglary",rburglary);
                    intent.putExtra("dacoity",rdacoity);


                    intent.putExtra("rapecountlist",simple);
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
                sex = selectedItem;
                Log.i("Sex", sex);
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
                    int rapeCount=0;
                    int robberyCount=0;
                    int kidnapcount=0;
                    int crueltycount=0;
                    int dowrycount=0;
                    int burglary=0;
                    int dacoity=0;
                    int year1=0;
                    for (String[] innerRowData : dataList2) {
                        Data innerRow = convertToData(innerRowData);
                        String m="MURDER";
                        if (innerRow.getState().equals(state) && innerRow.getDistrict().equals(currentDistrict)) {


                                crimeCount += innerRow.getCrimeCount();
                                rapeCount=innerRow.getRapecount();
                                robberyCount=innerRow.getRobberyCount();
                                year1=innerRow.getYear();
                                kidnapcount=innerRow.getKidnapcount();
                                crueltycount=innerRow.getCruelty();
                                dowrycount=innerRow.getDowrycount();
                                dacoity=innerRow.getDacotycount();











                        }
                    }
                    HashMap<String, Integer> districtMap = new HashMap<>();
                    districtMap.put(currentDistrict, crimeCount);

                    districts.add(currentDistrict);
                    yearr.add(year1);
                    rapeCounts.add(crimeCount);
                    rkidnap.add(kidnapcount);
                    rdowry.add(dowrycount);
                    rdacoity.add(dacoity);
                    rburglary.add(burglary);
                    rcruelty.add(crueltycount);
                    ryear=year;
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
        int burglary=0;

        int rapeCount= 0;
        int year=0;

        int crimeCount = 0;
        int robberycount=0;
        int dacoty=0;
        int kidnapcount=0;
        int dowrydeath=0;
        int burgalry=0;
        int cruelty=0;
        try {
            cruelty= Integer.parseInt(rowData[12]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {
            burgalry= Integer.parseInt(rowData[8]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {
            dowrydeath= Integer.parseInt(rowData[7]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {
            dacoty= Integer.parseInt(rowData[6]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {
            kidnapcount= Integer.parseInt(rowData[5]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {
            burglary= Integer.parseInt(rowData[8]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {
        year= Integer.parseInt(rowData[2]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {

            rapeCount = Integer.parseInt(rowData[7]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {

            robberycount = Integer.parseInt(rowData[5]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        try {

            crimeCount = Integer.parseInt(rowData[3]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return new Data(state, district, crimeCount,rapeCount,robberycount,year,kidnapcount,dacoty,dowrydeath,cruelty,burglary);
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


}