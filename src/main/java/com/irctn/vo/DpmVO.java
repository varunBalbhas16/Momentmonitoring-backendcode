package com.irctn.vo;

import java.io.Serializable;

public class DpmVO implements Serializable {

	private Integer dpmId;
	
	private String dpmName;
	
	private String dpmDepartment;
	
	private String dpmCentre;
	
	private String dpmContact;
	
	private String dpmZone;

	public Integer getDpmId() {
		return dpmId;
	}

	public void setDpmId(Integer dpmId) {
		this.dpmId = dpmId;
	}

	public String getDpmName() {
		return dpmName;
	}

	public void setDpmName(String dpmName) {
		this.dpmName = dpmName;
	}

	public String getDpmDepartment() {
		return dpmDepartment;
	}

	public void setDpmDepartment(String dpmDepartment) {
		this.dpmDepartment = dpmDepartment;
	}

	public String getDpmCentre() {
		return dpmCentre;
	}

	public void setDpmCentre(String dpmCentre) {
		this.dpmCentre = dpmCentre;
	}

	public String getDpmContact() {
		return dpmContact;
	}

	public void setDpmContact(String dpmContact) {
		this.dpmContact = dpmContact;
	}

	public String getDpmZone() {
		return dpmZone;
	}

	public void setDpmZone(String dpmZone) {
		this.dpmZone = dpmZone;
	}
	
	
	
}
