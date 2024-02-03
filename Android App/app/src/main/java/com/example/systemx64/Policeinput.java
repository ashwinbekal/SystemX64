package com.example.systemx64;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Policeinput extends AppCompatActivity {
    public String crimetype,state,district,year,season,age;
    List<String[]> dataList2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policeinput);
        Spinner spinner = findViewById(R.id.State);
        Spinner spinner1=findViewById(R.id.district);
        String fileName = "exxx.csv";
        List<String[]> dataList = com.example.myapplicationdh.CSVReader.readCSV(Policeinput.this, fileName);
        dataList2 = dataList;

        // Call your test method
        List<HashMap<String, Integer>> result = test("RAPE", "ANDHRA PRADESH", "NELLORE", 2011, "Male", "STRIKE", 52);

        // Log the top 5 districts along with their crime counts
        for (int i = 0; i < Math.min(5, result.size()); i++) {
            HashMap<String, Integer> map = result.get(i);
            for (String district : map.keySet()) {
                int crimeCount = map.get(district);
                Log.d("TopDistricts", district + ": " + crimeCount);
            }
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choices, R.layout.custom_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.choices2, R.layout.custom_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter((adapter1));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Do something with the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Do something with the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

    }
    private List<HashMap<String, Integer>> test(String crimetype, String state, String district, int year, String gender, String season, int age) {
        List<HashMap<String, Integer>> topDistricts = new ArrayList<>();
        Set<String> addedDistricts = new HashSet<>();
        for (String[] rowData : dataList2) {
            Data row = convertToData(rowData);
            if (row.getState().equals(state)) {
                String currentDistrict = row.getDistrict();
                if (!addedDistricts.contains(currentDistrict)) { // Check if district is already added
                    int crimeCount = 0;
                    for (String[] innerRowData : dataList2) {
                        Data innerRow = convertToData(innerRowData);
                        if (innerRow.getState().equals(state) && innerRow.getDistrict().equals(currentDistrict)) {
                            // Accumulate the crime count instead of assigning
                            crimeCount += innerRow.getCrimeCount();
                        }
                    }
                    // Create a new HashMap entry for each district with its accumulated crime count
                    HashMap<String, Integer> districtMap = new HashMap<>();
                    districtMap.put(currentDistrict, crimeCount);
                    topDistricts.add(districtMap);
                    addedDistricts.add(currentDistrict); // Add district to the set
                }
            }
        }

        // Sort the top districts based on crime count
        topDistricts.sort(new Comparator<HashMap<String, Integer>>() {
            @Override
            public int compare(HashMap<String, Integer> o1, HashMap<String, Integer> o2) {
                return o2.values().iterator().next() - o1.values().iterator().next();
            }
        });

        // Return the top districts list
        return topDistricts.subList(0, Math.min(5, topDistricts.size()));
    }

    private Data convertToData(String[] rowData) {
        // Assuming the structure of each row in dataList is appropriate for your CSV file
        String state = rowData[0];
        String district = rowData[1];
        int crimeCount = 0;
        try {
            // Assuming the crime count is at index 32
            crimeCount = Integer.parseInt(rowData[3]);
        } catch (NumberFormatException e) {
            // Handle the case where parsing fails, e.g., if the value is not a valid integer
            e.printStackTrace();
        }
        return new Data(state, district, crimeCount);
    }

}