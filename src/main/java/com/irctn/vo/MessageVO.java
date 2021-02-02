package com.irctn.vo;

import java.io.Serializable;

public class MessageVO implements Serializable {

	private String message;

	private String result;

	private String description;

	private String name;
	private Double totalReusable;
	private Double totalWaste;

	private Integer batchNumber;

	public MessageVO(String message, String result, String description, String name) {
		this.message = message;
		this.result = result;
		this.description = description;
		this.name = name;
	}

	public MessageVO(String message, String result, Double totalReusable, Double totalWaste) {
		this.message = message;
		this.result = result;
		this.totalReusable = totalReusable;
		this.totalWaste = totalWaste;

	}

	public MessageVO(String message, String result, String description, Integer batchNumber, String name) {
		this.message = message;
		this.result = result;
		this.description = description;
		this.batchNumber = batchNumber;
		this.name = name;
	}

	public Double getTotalReusable() {
		return totalReusable;
	}

	public void setTotalReusable(Double totalReusable) {
		this.totalReusable = totalReusable;
	}

	public Double getTotalWaste() {
		return totalWaste;
	}

	public void setTotalWaste(Double totalWaste) {
		this.totalWaste = totalWaste;
	}

	public MessageVO(String message, String result, String description) {
		this.message = message;
		this.result = result;
		this.description = description;
	}

	public MessageVO() {
	}

	public MessageVO(String result, String message) {
		this.result = result;
		this.message = message;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

}