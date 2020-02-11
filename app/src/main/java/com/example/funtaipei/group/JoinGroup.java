package com.example.funtaipei.group;

import java.io.Serializable;

public class JoinGroup implements Serializable{

    private int GP_ID, MB_NO, CHECK_IN, GP_ENROLLMENT;


    public JoinGroup(int gP_ID, int mB_NO, int cHECK_NO) {
        super();
        this.GP_ID = gP_ID;
        this.MB_NO = mB_NO;
        this.CHECK_IN = cHECK_NO;

    }


    public int getGP_ID() {
        return GP_ID;
    }

    public void setGP_ID(int gP_ID) {
        this.GP_ID = gP_ID;
    }

    public int getMB_NO() {
        return MB_NO;
    }

    public void setMB_NO(int mB_NO) {
        this.MB_NO = mB_NO;
    }

    public int getCHECK_IN() {
        return CHECK_IN;
    }

    public void setCHECK_IN(int cHECK_NO) {
        this.CHECK_IN = cHECK_NO;
    }

    public int getGP_ENROLLMENT() {
        return GP_ENROLLMENT;
    }

    public void setGP_ENROLLMENT(int gP_ENROLLMENT) {
        this.GP_ENROLLMENT = gP_ENROLLMENT;
    }


}
