package com.example.funtaipei.travel;

import java.io.Serializable;

public class Travel implements Serializable{
	private int travel_id;
	private String travel_name;
	private byte[] image;
	private int travel_status;
	private int mb_no;


	public Travel(int travel_id, String travel_name, byte[] image, int travel_status, int mb_no) {
		super();
		this.travel_id = travel_id;
		this.travel_name = travel_name;
		this.image = image;
		this.travel_status = travel_status;
		this.mb_no = mb_no;
	}

	public Travel(int travel_id, String travel_name, int travel_status, int mb_no) {
		super();
		this.travel_id = travel_id;
		this.travel_name = travel_name;
		this.travel_status = travel_status;
		this.mb_no = mb_no;
	}
	public Travel(int travel_id, String travel_name, int travel_status) {
		super();
		this.travel_id = travel_id;
		this.travel_name = travel_name;
		this.travel_status = travel_status;
	}
	public Travel(int mb_no, int travel_id, String travel_name) {
		super();
		this.mb_no = mb_no;
		this.travel_id = travel_id;
		this.travel_name = travel_name;

	}
	public Travel(int travel_id, String travel_name) {
		this.travel_id = travel_id;
		this.travel_name = travel_name;

	}
	public int getTravel_id() {
		return travel_id;
	}
	public void setTravel_id(int travel_id) {
		this.travel_id = travel_id;
	}
	public String getTravel_name() {
		return travel_name;
	}
	public void setTravel_name(String travel_name) {
		this.travel_name = travel_name;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public int getTravel_status() {
		return travel_status;
	}
	public void setTravel_status(int travel_status) {
		this.travel_status = travel_status;
	}
	public int getMb_no() {
		return mb_no;
	}
	public void setMb_no(int mb_no) {
		this.mb_no = mb_no;
	}



}
