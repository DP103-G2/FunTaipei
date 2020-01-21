package com.example.funtaipei.travel;

public class Image {
	private int IG_NO;
	private int PC_ID;
	private byte[] image;
	public Image(int iG_NO, int pC_ID, byte[] iG) {
		super();
		IG_NO = iG_NO;
		PC_ID = pC_ID;
		this.image = iG;
	}
	public int getIG_NO() {
		return IG_NO;
	}
	public void setIG_NO(int iG_NO) {
		IG_NO = iG_NO;
	}
	public int getPC_ID() {
		return PC_ID;
	}
	public void setPC_ID(int pC_ID) {
		PC_ID = pC_ID;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	

}
