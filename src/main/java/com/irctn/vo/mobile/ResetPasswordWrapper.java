package com.irctn.vo.mobile;

import java.io.Serializable;

public class ResetPasswordWrapper implements Serializable {

	private String emailId;
	
	private String name;
	
	private String result;
	
	private String errorMessage;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getResult() {
		return result;
		
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
