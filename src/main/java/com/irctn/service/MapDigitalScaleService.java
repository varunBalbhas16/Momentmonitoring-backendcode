package com.irctn.service;

import com.irctn.vo.MapDigitalScaleVO;
import com.irctn.vo.MessageVO;

public interface MapDigitalScaleService {

	public String saveDigitalScaleNumber(MapDigitalScaleVO mapDigitalScaleVO);

	public MessageVO getWeightInfo();

	public Double getScaleWeight(Integer scale);
	
	public MessageVO saveWeight(Integer scale, Double weight);
	
	public Double getScaleWeightByUser(Integer sortinguserid);
}
