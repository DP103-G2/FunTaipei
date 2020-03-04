package com.example.funtaipei.group;

import java.io.Serializable;

public class JoinGroup implements Serializable {

    private int GP_ID, MB_NO, CHECK_IN, MASTER;
    private String MB_NAME;




    public JoinGroup (int mB_NO, String mB_NAME, int cHECK_IN) {
        super();
        this.MB_NO = mB_NO;
        this.MB_NAME = mB_NAME;
        this.CHECK_IN = cHECK_IN;
    }


    public JoinGroup(int gP_ID, int mB_NO, int cHECK_in, int mASTER) {
        super();
        this.GP_ID = gP_ID;
        this.MB_NO = mB_NO;
        this.CHECK_IN = cHECK_in;
        this.MASTER = mASTER;

    }

    public JoinGroup(int cHECK_IN, int mASTER) {
        super();
        this.CHECK_IN = cHECK_IN;
        this.MASTER = mASTER;
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

    public void setCHECK_IN(int cHECK_in) {
        this.CHECK_IN = cHECK_in;
    }


    public int getMASTER() {
        return MASTER;
    }


    public void setMASTER(int mASTER) {
        MASTER = mASTER;
    }


    public String getMB_NAME() {
        return MB_NAME;
    }


    public void setMB_NAME(String mB_NAME) {
        MB_NAME = mB_NAME;
    }


}
