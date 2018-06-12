package com.yd.test;

public class Contast {
	
	//判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 byte+byte = 1+1 = 2  
	public static final int HEADER_SIZE = 2;  
	
	public static final int MAX_FRAME_LENGTH = 1024 * 10;  
	public static final int LENGTH_FIELD_LENGTH = 2;  
	public static final int LENGTH_FIELD_OFFSET = 0;  
	public static final int LENGTH_ADJUSTMENT = 0;  
	public static final int INITIAL_BYTES_TO_STRIP = 0;  
    
	public static final int MAX_CLINET = 2048;  
	
	//YKT Message Type
	//request message	
	public static final String SIGN_REQ 		       = "0800";
	public static final String HEART_CHECK_REQ         = "0300";
	public static final String PARAMETER_DOWNLOAD_REQ  = "0700";
	public static final String SETTLE_REQ  			   = "0500";
	public static final String BATCH_REQ  			   = "1000";
	public static final String TRANSACT_REQ  		   = "0200";
	public static final String FLUSHES_REQ  		   = "0400";
	
	
	//response message 
	public static final String SIGN_RESP 		        = "0810";
	public static final String HEART_CHECK_RESP         = "0310";
	public static final String PARAMETER_DOWNLOAD_RESP  = "0710";
	public static final String SETTLE_RESP 			    = "0510";
	public static final String BATCH_RESP  			    = "1010";
	public static final String TRANSACT_RESP  		    = "0210";
	public static final String FLUSHES_RESP  		    = "0410";
	
	
	
	
	
	
	
	
}
