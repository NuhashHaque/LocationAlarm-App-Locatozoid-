package com.example.mylocation;

public class Data {
   private String Address;
    private double lat;
    private double lang;
    private int condition;
    private double range;

    public Data(){}

    public Data(String address, double lat, double lang, int condition,double range) {
        Address = address;
        this.lat = lat;
        this.lang = lang;
        this.condition=condition;
        this.range=range;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public int isCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getCondition() {
        return condition;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }
}
