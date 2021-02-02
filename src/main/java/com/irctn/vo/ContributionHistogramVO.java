package com.irctn.vo;

import java.io.Serializable;

public class ContributionHistogramVO implements Serializable {
	
	private String month;
	private Double totalContribution;
	private Double totalRecycled;
	private Double totalWaste;
	
	public ContributionHistogramVO() {
		
	}
	public ContributionHistogramVO(String month, Double totalContribution, Double totalRecycled, Double totalWaste) {
		super();
		this.month = month;
		this.totalContribution = totalContribution;
		this.totalRecycled = totalRecycled;
		this.totalWaste = totalWaste;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Double getTotalContribution() {
		return totalContribution;
	}
	public void setTotalContribution(Double totalContribution) {
		this.totalContribution = totalContribution;
	}
	public Double getTotalRecycled() {
		return totalRecycled;
	}
	public void setTotalRecycled(Double totalRecycled) {
		this.totalRecycled = totalRecycled;
	}
	public Double getTotalWaste() {
		return totalWaste;
	}
	public void setTotalWaste(Double totalWaste) {
		this.totalWaste = totalWaste;
	}
	
}