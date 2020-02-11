package com.example.funtaipei.group;

import java.io.Serializable;
import java.util.Date;

public class Group implements Serializable {
    private int MB_NO;
    private int GP_ID, TRAVEL_ID, GP_ENROLLMENT, GP_UPPER, GP_LOWER, GP_STATUS;
    private String GP_NAME, GP_NOTES;
    private Date GP_DATESTART, GP_DATEEND, GP_EVENTDATE;

    public Group(int gP_ID, int tRAVEL_ID, String gP_NAME, int gP_ENROLLMENT, int gP_UPPER, int gP_LOWER, Date gP_DATESTART, Date gP_DATEEND, Date gP_EVENTDATE, int gP_STATUS, String gP_NOTES, int mB_NO) {
        super();
        this.GP_ID = gP_ID;
        this.TRAVEL_ID = tRAVEL_ID;
        this.GP_ENROLLMENT = gP_ENROLLMENT;
        this.GP_UPPER = gP_UPPER;
        this.GP_LOWER = gP_LOWER;
        this.GP_STATUS = gP_STATUS;
        this.GP_NAME = gP_NAME;
        this.GP_NOTES = gP_NOTES;
        this.GP_DATESTART = gP_DATESTART;
        this.GP_DATEEND = gP_DATEEND;
        this.GP_EVENTDATE = gP_EVENTDATE;
        this.MB_NO = mB_NO;
    }
    public void setGroup(int tRAVEL_ID, String gP_NAME, int gP_ENROLLMENT, int gP_UPPER, int gP_LOWER, Date gP_DATESTART, Date gP_DATEEND, Date gP_EVENTDATE, int gP_STATUS, String gP_NOTES, int mB_NO){
        this.TRAVEL_ID = tRAVEL_ID;
        this.GP_ENROLLMENT = gP_ENROLLMENT;
        this.GP_UPPER = gP_UPPER;
        this.GP_LOWER = gP_LOWER;
        this.GP_STATUS = gP_STATUS;
        this.GP_NAME = gP_NAME;
        this.GP_NOTES = gP_NOTES;
        this.GP_DATESTART = gP_DATESTART;
        this.GP_DATEEND = gP_DATEEND;
        this.GP_EVENTDATE = gP_EVENTDATE;
        this.MB_NO = mB_NO;
    }



    public int getGP_ID() {
        return GP_ID;
    }
    public void setGP_ID(int gP_ID) {
        this.GP_ID = gP_ID;
    }
    public int getTRAVEL_ID() {
        return TRAVEL_ID;
    }
    public void setTRAVEL_ID(int tRAVEL_ID) {
        this.TRAVEL_ID = tRAVEL_ID;
    }
    public int getGP_ENROLLMENT() {
        return GP_ENROLLMENT;
    }
    public void setGP_ENROLLMENT(int gP_ENROLLMENT) {
        this.GP_ENROLLMENT = gP_ENROLLMENT;
    }
    public int getGP_UPPER() {
        return GP_UPPER;
    }
    public void setGP_UPPER(int gP_UPPER) {
        this.GP_UPPER = gP_UPPER;
    }
    public int getGP_LOWER() {
        return GP_LOWER;
    }
    public void setGP_LOWER(int gP_LOWER) {
        this.GP_LOWER = gP_LOWER;
    }
    public int getGP_STATUS() {
        return GP_STATUS;
    }
    public void setGP_STATUS(int gP_STATUS) {
        this.GP_STATUS = gP_STATUS;
    }
    public String getGP_NAME() {
        return GP_NAME;
    }
    public void setGP_NAME(String gP_NAME) {
        this.GP_NAME = gP_NAME;
    }
    public String getGP_NOTES() {
        return GP_NOTES;
    }
    public void setGP_NOTES(String gP_NOTES) {
        this.GP_NOTES = gP_NOTES;
    }
    public Date getGP_DATESTAR() {
        return GP_DATESTART;
    }
    public void setGP_DATESTAR(Date gP_DATESTAR) {
        this.GP_DATESTART = gP_DATESTAR;
    }
    public Date getGP_DATEEND() {
        return GP_DATEEND;
    }
    public void setGP_DATEEND(Date gP_DATEEND) {
        this.GP_DATEEND = gP_DATEEND;
    }
    public Date getGP_EVENTDATE() {
        return GP_EVENTDATE;
    }
    public void setGP_EVENTDATE(Date gP_EVENTDATE) {
        this.GP_EVENTDATE = gP_EVENTDATE;
    }
    public void setGP_NO(int mB_NO) {
        this.MB_NO = mB_NO;
    }
    public int getMB_NO() {
        return MB_NO;
    }



}

