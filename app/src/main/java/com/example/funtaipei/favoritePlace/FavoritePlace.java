package com.example.funtaipei.favoritePlace;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class FavoritePlace implements Serializable {
    private int mb_no;
    private String pc_name;
    private int pc_id;
    private String mb_email;
    private String mb_name;
    private int gp_id;
    private String gp_name;
    private Timestamp GP_DATESTART, GP_DATEEND, GP_EVENTDATE;
    private String pc_address;
    private String PC_PHONE;
    private double LAT;
    private double LNG;

    public FavoritePlace(int mb_no, int pc_id, String mb_email, String mb_name, int gp_id, String gp_name, Date gP_DATESTART,
                         Date gP_DATEEND, Date gP_EVENTDATE,String  pc_name, String pc_address, String phoneNo, double lat, double lng) {
        super();
        this.mb_no = mb_no;
        this.pc_id = pc_id;
        this.mb_email = mb_email;
        this.mb_name = mb_name;
        this.gp_id = gp_id;
        this.gp_name = gp_name;
        GP_DATESTART = (Timestamp) gP_DATESTART;
        GP_DATEEND = (Timestamp) gP_DATEEND;
        GP_EVENTDATE = (Timestamp) gP_EVENTDATE;
        this.pc_name = pc_name;
        this.pc_address = pc_address;
        this.PC_PHONE = phoneNo;
        this.LAT = lat;
        this.LNG = lng;

    }
    public int getMb_no() {
        return mb_no;
    }
    public void setMb_no(int mb_no) {
        this.mb_no = mb_no;
    }
    public int getPc_id() {
        return pc_id;
    }
    public void setPc_id(int pc_id) {
        this.pc_id = pc_id;
    }
    public String getMb_email() {
        return mb_email;
    }
    public void setMb_email(String mb_email) {
        this.mb_email = mb_email;
    }
    public String getMb_name() {
        return mb_name;
    }
    public void setMb_name(String mb_name) {
        this.mb_name = mb_name;
    }
    public int getGp_id() {
        return gp_id;
    }
    public void setGp_id(int gp_id) {
        this.gp_id = gp_id;
    }
    public String getGp_name() {
        return gp_name;
    }
    public void setGp_name(String gp_name) {
        this.gp_name = gp_name;
    }
    public Timestamp getGP_DATESTART() {
        return GP_DATESTART;
    }
    public void setGP_DATESTART(Timestamp gP_DATESTART) {
        GP_DATESTART = gP_DATESTART;
    }
    public Timestamp getGP_DATEEND() {
        return GP_DATEEND;
    }
    public void setGP_DATEEND(Timestamp gP_DATEEND) {
        GP_DATEEND = gP_DATEEND;
    }
    public Timestamp getGP_EVENTDATE() {
        return GP_EVENTDATE;
    }
    public void setGP_EVENTDATE(Timestamp gP_EVENTDATE) {
        GP_EVENTDATE = gP_EVENTDATE;
    }
    public String getPc_name() {
        return pc_name;
    }
    public void setPc_name(String pc_name) {
        this.pc_name = pc_name;
    }
    public String getPc_address() {return pc_address; }
    public void setPc_address(String pc_address) {this.pc_address = pc_address; }
    public String getPC_PHONE() {
        return PC_PHONE;
    }
    public void setPC_PHONE(String pC_PHONE) {
        PC_PHONE = pC_PHONE;
    }
    public double getLAT() { return LAT; }
    public void setLAT(double lAT) {
        LAT = lAT;
    }
    public double getLNG() {
        return LNG;
    }
    public void setLNG(double lNG) {
        LNG = lNG;
    }
}




