package com.example.funtaipei.favoritePlace;

public class InsertFavoritePlace {
    private int mb_no;
    private int pc_id;

    public InsertFavoritePlace(int mb_no, int pc_id) {
        this.mb_no = mb_no;
        this.pc_id = pc_id;
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
}

