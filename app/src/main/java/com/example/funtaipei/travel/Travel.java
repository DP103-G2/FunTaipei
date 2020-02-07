package com.example.funtaipei.travel;

import java.io.Serializable;

public class Travel implements Serializable {
	private int travel_id;
	private String travel_name;
	private int travel_status;

	public Travel(int travel_id, String travel_name, byte[] image, int travel_status) {
		super();
		this.travel_id = travel_id;
		this.travel_name = travel_name;
		this.travel_status = travel_status;
	}

	public Travel(int travel_id, String travel_name){
		super();
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
	public void setTravel_name(int travel_id, String travel_name) {
		this.travel_name = travel_name;
	}
	public int getTravel_status() {
		return travel_status;
	}
	public void setTravel_status(int travel_status) {
		this.travel_status = travel_status;
	}


}
