package com.irctn.vo;

import java.io.Serializable;

public class SchoolSortingAgentDetailsVO implements Serializable {
	
	private Integer agentId;
	private String agent;
	private Integer sortedBatches;
	private Double totalWeight;
	private Double totalRecycled;
	private Double totalUnusable;
	
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Integer getSortedBatches() {
		return sortedBatches;
	}
	public void setSortedBatches(Integer sortedBatches) {
		this.sortedBatches = sortedBatches;
	}
	public Double getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
	public Double getTotalRecycled() {
		return totalRecycled;
	}
	public void setTotalRecycled(Double totalRecycled) {
		this.totalRecycled = totalRecycled;
	}
	public Double getTotalUnusable() {
		return totalUnusable;
	}
	public void setTotalUnusable(Double totalUnusable) {
		this.totalUnusable = totalUnusable;
	}
}
