package com.irctn.vo.mobile;

import java.io.Serializable;

public class MobileResponseVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	
	private String result;

	public MobileResponseVO() {
		
	}
	
	public MobileResponseVO(String result , String message) {
		super();
		this.message = message;
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
