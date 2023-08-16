package com.example.busproject;

public class Busitem {
    String locationNo1;
    String plateNo1;
    String routeId;
    String predictTime1;
    String lowPlate1;

    public String getLowPlate1(){ return  lowPlate1; }

    public void setLowPlate1(String lowPlate1) { this.lowPlate1 =lowPlate1; }

    public String getLocationNo1() {
        return locationNo1;
    }

    public void setLocationNo1(String locationNo1) {
        this.locationNo1 = locationNo1;
    }

    public String getPlateNo1() {
        return plateNo1;
    }

    public void setPlateNo1(String plateNo1) {
        this.plateNo1 = plateNo1;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getPredictTime1() {
        return predictTime1;
    }

    public void setPredictTime1(String predictTime1) {
        this.predictTime1 = predictTime1;
    }


}