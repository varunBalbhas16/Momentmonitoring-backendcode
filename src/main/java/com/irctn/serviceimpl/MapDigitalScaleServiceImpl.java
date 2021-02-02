
package com.irctn.serviceimpl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.MapDigitalScale;
import com.irctn.repository.MapDigitalScaleRepository;
import com.irctn.service.MapDigitalScaleService;
import com.irctn.util.AppConstants;
import com.irctn.vo.MapDigitalScaleVO;
import com.irctn.vo.MessageVO;

@Service
public class MapDigitalScaleServiceImpl implements MapDigitalScaleService {

	@Autowired
	MapDigitalScaleRepository mapDigitalScaleRepository;

	@Override
	public String saveDigitalScaleNumber(MapDigitalScaleVO mapDigitalScaleVO) {
		if(null == mapDigitalScaleVO || null == mapDigitalScaleVO.getSortingUserId()) return AppConstants.FAILURE;
		MapDigitalScale mapDigitalScale = mapDigitalScaleRepository.findBySortinguserid(mapDigitalScaleVO.getSortingUserId());
		if(null == mapDigitalScale) {
			mapDigitalScale = new MapDigitalScale();
		}		
		mapDigitalScale.setDigitalscale(mapDigitalScaleVO.getDigitalScale());
		mapDigitalScale.setSortinguserid(mapDigitalScaleVO.getSortingUserId());
		mapDigitalScale.setWeight(0.0D);
		mapDigitalScaleRepository.save(mapDigitalScale);
		return "success";
	}

	@Override
	public MessageVO getWeightInfo() {
		
		Random random = new Random();
		int number = random.nextInt(5);
		double value = 1 + (number/1.2);
		 DecimalFormat df = new DecimalFormat("#.##");

		    df.setRoundingMode(RoundingMode.FLOOR);

		    double result = new Double(df.format(value));
		    String s = ""+ result;
		return new MessageVO("success","random numbers generated",s);
	}

	@Override
	public Double getScaleWeight(Integer scale) {
		MapDigitalScale scaleObject = mapDigitalScaleRepository.findByDigitalscale(scale);
		Double weight = 0.0D;
		if(null != scaleObject) {
			weight = scaleObject.getWeight();
		}
		return weight;
	}

	@Override
	public MessageVO saveWeight(Integer scale, Double weight) {
		if(null == scale || null == weight) {
			return new MessageVO(AppConstants.FAILURE, "Cannot save null values for scale or weight");
		}
		MapDigitalScale digitalScale = mapDigitalScaleRepository.findByDigitalscale(scale);
		if(null != digitalScale) {
			digitalScale.setWeight(weight);
			mapDigitalScaleRepository.save(digitalScale);
			return new MessageVO(AppConstants.SUCCESS, "Saved weight for scale successfully.");
		} else {
			System.out.println("Unable to find the digital scale mapped to device "+ scale.intValue());
			return new MessageVO(AppConstants.FAILURE, "Unable to find the digital scale mapped to device "+ scale.intValue());
		}
	}

	@Override
	public Double getScaleWeightByUser(Integer sortinguserid) {		
		Double result = 0.0D;
		if(null != sortinguserid) {
			MapDigitalScale digitalScale = mapDigitalScaleRepository.findBySortinguserid(sortinguserid);
			if(null !=  digitalScale) {
				result = digitalScale.getWeight();				
			}
		}
		return result;
	}



}
