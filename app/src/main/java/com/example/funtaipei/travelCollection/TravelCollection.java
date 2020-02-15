package com.example.funtaipei.travelCollection;

import com.google.gson.JsonElement;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class TravelCollection implements Serializable {

	private int mb_no;
	private int travel_id;
	private String mb_email;
	private String mb_name;
	private int gp_id;
	private String gp_name;
	private String travel_name;
	private Timestamp GP_DATESTART, GP_DATEEND, GP_EVENTDATE;
	public TravelCollection(int mb_no, int travel_id, String mb_email, String mb_name, int gp_id, String gp_name, Date gP_DATESTART,
			Date gP_DATEEND, Date gP_EVENTDATE, String travel_name) {
		super();
		this.mb_no = mb_no;
		this.travel_id = travel_id;
		this.mb_email = mb_email;
		this.mb_name = mb_name;
		this.gp_id = gp_id;
		this.gp_name = gp_name;
		this.travel_name = travel_name;
		GP_DATESTART = (Timestamp) gP_DATESTART;
		GP_DATEEND = (Timestamp) gP_DATEEND;
		GP_EVENTDATE = (Timestamp) gP_EVENTDATE;
	}





	public TravelCollection(int mb_no, int travel_id, String mb_email, String mb_name, int gp_id, String gp_name, String travel_name, Timestamp GP_DATESTART, Timestamp GP_DATEEND, Timestamp GP_EVENTDATE) {
		this.mb_no = mb_no;
		this.travel_id = travel_id;
		this.mb_email = mb_email;
		this.mb_name = mb_name;
		this.gp_id = gp_id;
		this.gp_name = gp_name;
		this.travel_name = travel_name;
		this.GP_DATESTART = GP_DATESTART;
		this.GP_DATEEND = GP_DATEEND;
		this.GP_EVENTDATE = GP_EVENTDATE;
	}
	//Insert用
	public TravelCollection(String name) {
		this.travel_name = travel_name;
	}

	public String getTravel_Name(){
		return travel_name;
	}

	public void setTravelName(String travel_name){
		this.travel_name = travel_name;
	}

	public int getMb_no() {
		return mb_no;
	}

	public void setMb_no(int mb_no) {
		this.mb_no = mb_no;
	}

	public int getTravel_id() {
		return travel_id;
	}

	public void setTravel_id(int travel_id) {
		this.travel_id = travel_id;
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

	public String getTravel_name() {
		return travel_name;
	}

	public void setTravel_name(String travel_name) {
		this.travel_name = travel_name;
	}

	public Timestamp getGP_DATESTART() {
		return GP_DATESTART;
	}

	public void setGP_DATESTART(Timestamp GP_DATESTART) {
		this.GP_DATESTART = GP_DATESTART;
	}

	public Timestamp getGP_DATEEND() {
		return GP_DATEEND;
	}

	public void setGP_DATEEND(Timestamp GP_DATEEND) {
		this.GP_DATEEND = GP_DATEEND;
	}

	public Timestamp getGP_EVENTDATE() {
		return GP_EVENTDATE;
	}

	public void setGP_EVENTDATE(Timestamp GP_EVENTDATE) {
		this.GP_EVENTDATE = GP_EVENTDATE;
	}
}