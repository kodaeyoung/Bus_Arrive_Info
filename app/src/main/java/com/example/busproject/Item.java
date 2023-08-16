package com.example.busproject;

public class Item {
    String stationName;
    String mobileNo;
    String stationId;

    public String getStationId(){
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationId(String stationId){
        this.stationId = stationId;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}