package com.example.systemx64;
public class Data {
    private String state;
    private String district;
    private int crimeCount;

    public Data(String state, String district, int crimeCount) {
        this.state = state;
        this.district = district;
        this.crimeCount = crimeCount;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public int getCrimeCount() {
        return crimeCount;
    }
}
