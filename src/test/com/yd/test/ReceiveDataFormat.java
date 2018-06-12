package com.yd.test;

public class ReceiveDataFormat {
	
	public int     recevieDataLength;
	public String  receiveDateContent;
	public String  strMsgLength;
	
	public ReceiveDataFormat (int length, String data) {
		
		this.recevieDataLength  = length;
		this.receiveDateContent = data;
	}
		
	public void setStrMsgLength (String length) {
		 this.strMsgLength = length;
	}
	
	public String getStrMsgLength () {
		return strMsgLength;
	}
	
}
