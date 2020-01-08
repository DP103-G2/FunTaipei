package com.example.funtaipei.place;

import java.io.Serializable;

public class Place implements Serializable {
    private int PC_ID;
    private String PC_NAME;
    private String PC_PHONE;
    private String PC_ADDRESS;
    private double LAT;
    private double LNG;
    private int VIEW_ALL;
    private int PC_STATUS;

    public Place(int id, String name, String phoneNo, String address,
                 double lat, double lng,int viewAll,int status) {
        this.PC_ID = id;
        this.PC_NAME = name;
        this.PC_PHONE = phoneNo;
        this.PC_ADDRESS = address;
        this.LAT = lat;
        this.LNG = lng;
        this.VIEW_ALL = viewAll;
        this.PC_STATUS = status;
    }

    public int getPC_ID() { return PC_ID; }

    public void setPC_ID(int pC_ID) {
        PC_ID = pC_ID;
    }

    public String getPC_NAME() {
        return PC_NAME;
    }

    public void setPC_NAME(String pC_NAME) {
        PC_NAME = pC_NAME;
    }

    public String getPC_PHONE() {
        return PC_PHONE;
    }

    public void setPC_PHONE(String pC_PHONE) {
        PC_PHONE = pC_PHONE;
    }

    public String getPC_ADDRESS() {
        return PC_ADDRESS;
    }

    public void setPC_ADDRESS(String pC_ADDRESS) {
        PC_ADDRESS = pC_ADDRESS;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double lAT) {
        LAT = lAT;
    }

    public double getLNG() {
        return LNG;
    }

    public void setLNG(double lNG) {
        LNG = lNG;
    }

    public int getVIEW_ALL() {
        return VIEW_ALL;
    }

    public void setVIEW_ALL(int vIEW_ALL) {
        VIEW_ALL = vIEW_ALL;
    }

    public int getPC_STATUS() {
        return PC_STATUS;
    }

    public void setPC_STATUS(int pC_STATUS) {
        PC_STATUS = pC_STATUS;
    }


}
