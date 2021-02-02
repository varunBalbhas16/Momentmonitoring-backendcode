package com.irctn.vo;

import java.io.Serializable;

public class ContributionsDoneVO implements Serializable {

	private int rank;
	private String contributor;
	private String type;
	private Double totalWeight;
	
	
	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public ContributionsDoneVO() {
		super();
	}

	public ContributionsDoneVO(int rank, String contributor, String type, Double totalWeight) {
		super();
		this.rank = rank;
		this.contributor = contributor;
		this.type = type;
		this.totalWeight = totalWeight;
	}
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getContributor() {
		return contributor;
	}
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
