package com.example.funtaipei.travel;

public class Place {
	private int PC_ID;
	private int travel_id;
	private String PC_NAME;


	
	public Place(int id, String name, String phoneNo, String address,
			double lat, double lng,int viewAll,int status) {
		this.PC_ID = id;
		this.PC_NAME = name;
	}

	public int getPC_ID() {
		return PC_ID;
	}

	public void setPC_ID(int pC_ID) {
		PC_ID = pC_ID;
	}

	public String getPC_NAME() {
		return PC_NAME;
	}

	public void setPC_NAME(String pC_NAME) {
		PC_NAME = pC_NAME;
	}

	public int getTravel_id() {
		return travel_id;
	}

	public void setTravel_id(int travel_id) {
		this.travel_id = travel_id;
	}
}
