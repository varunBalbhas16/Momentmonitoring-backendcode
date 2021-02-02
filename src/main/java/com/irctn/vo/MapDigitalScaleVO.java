package com.irctn.vo;

import java.io.Serializable;

public class MapDigitalScaleVO implements Serializable {

	private Integer mapDigitalScaleId;

	private Integer digitalScale;
	
	private Integer sortingUserId;

	public Integer getSortingUserId() {
		return sortingUserId;
	}

	public void setSortingUserId(Integer sortingUserId) {
		this.sortingUserId = sortingUserId;
	}

	public Integer getMapDigitalScaleId() {
		return mapDigitalScaleId;
	}

	public void setMapDigitalScaleId(Integer mapDigitalScaleId) {
		this.mapDigitalScaleId = mapDigitalScaleId;
	}

	public Integer getDigitalScale() {
		return digitalScale;
	}

	public void setDigitalScale(Integer digitalScale) {
		this.digitalScale = digitalScale;
	}

}
