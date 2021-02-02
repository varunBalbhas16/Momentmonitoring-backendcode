package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.Centre;
import com.irctn.repository.CentreRepository;
import com.irctn.service.CentreService;
import com.irctn.service.ZoneService;
import com.irctn.util.AppConstants;
import com.irctn.vo.CentreVO;
import com.irctn.vo.MessageVO;

@Service
public class CentreServiceImpl implements CentreService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.CentreServiceImpl");

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	CentreRepository centreRepository;
	
	@Autowired
	ZoneService zoneService;
	
	@Override
	public String saveCentre(CentreVO centreVO) {
		Centre centre = centreRepository.findByCentreid(centreVO.getCentreId());
		String success = AppConstants.SUCCESS ;
		if(null == centre) {
			centre = new Centre();
		} else {
			redisTemplate.opsForHash().delete("CENTRE_BY_ID", centreVO.getCentreId());
			success = "updatesuccess";
		}
		centre.setCentrename(centreVO.getCentreName());
		centre.setCentrecode(centreVO.getCentreCode());
		centre.setAddress(centreVO.getAddress());
		centre.setLandline(centreVO.getLandline());
		centre.setCentreid(centreVO.getCentreId());
		centre.setHeadid(centreVO.getHeadId());
		centre.setLatitude(centreVO.getLatitude());
		centre.setLongitude(centreVO.getLongitude());
		centre.setZoneid(centreVO.getZoneId());
		
		Centre savedCentre = centreRepository.save(centre);
		LOGGER.debug("The Centre is saved with id : " + savedCentre.getCentreid() + " and centre " + savedCentre.getCentrename());
		redisTemplate.opsForHash().delete("CENTRELIST", "CENTRES");
		return success;

	}

	@Override
	public List<CentreVO> getAllCentres() {
		Object object = redisTemplate.opsForHash().get("CENTRELIST", "CENTRES");
		List<CentreVO> voList = new ArrayList<CentreVO>();
		List<Centre> centreList = null;
		List<CentreVO> sortedCentreList = null;
		if (null == object) {
			centreList = centreRepository.findAll();
		} else {
			return (List<CentreVO>) object;
		}
		
		if (null != centreList) {
			for (Centre centre : centreList) {
				CentreVO vo = new CentreVO();
				vo.setCentreId(centre.getCentreid());
				vo.setCentreName(centre.getCentrename());
				vo.setCentreCode(centre.getCentrecode());
				vo.setAddress(centre.getAddress());
				vo.setLandline(centre.getLandline());
				vo.setCentreId(centre.getCentreid());
				vo.setHeadId(centre.getHeadid());
				vo.setLatitude(centre.getLatitude());
				vo.setLongitude(centre.getLatitude());
				vo.setZoneId(centre.getZoneid());
				voList.add(vo);
			}
			sortedCentreList = voList.stream().sorted(Comparator.comparing(CentreVO::getCentreId).reversed())
					.collect(Collectors.toList());
			redisTemplate.opsForHash().put("CENTRELIST", "CENTRES", sortedCentreList);
			return sortedCentreList;

		} else {
			return null;
		}
	}

	@Override
	public String deleteCentre(Integer centreid) {
		centreRepository.delete(centreid);
		redisTemplate.opsForHash().delete("CENTRE_BY_ID", centreid);
		redisTemplate.opsForHash().delete("CENTRELIST", "CENTRES");
		return "success";
	}

	@Override
	public List<CentreVO> getCentreNameLike(String search) {
		List<Centre> centreList = centreRepository.findByCentrenameContaining(search);
		if(null != centreList) System.out.println("Centres mathcing the search are : " + centreList.size());
		return getVOListFromModel(centreList);
	}

	@Override
	public CentreVO getCentresByNames(String search) {
		Centre centre = centreRepository.findByCentrename(search); 
		return getVOFromModel(centre);
	}

	@Override
	public CentreVO getCentreById(Integer centreid) {
		
		Object object = redisTemplate.opsForHash().get("CENTRE_BY_ID", centreid);
		Centre centre = null;
		if (null == object) {
			centre = centreRepository.findByCentreid(centreid);
		} else {
			return (CentreVO) object;
		}
		if (null != centre) {
			CentreVO vo = getVOFromModel(centre);
			redisTemplate.opsForHash().put("CENTRE_BY_ID", vo.getCentreId(), vo);
			redisTemplate.opsForHash().delete("CENTRELIST", "CENTRES");
			return vo;

		} else {
			return null;
		}
	}

	@Override
	public MessageVO isCentreUnique(String name, String code, Integer centreid) {
		CentreVO centreToBeUpdated = null;
		CentreVO centreFromDB = null;
		if(null == centreid) return new MessageVO(AppConstants.FAILURE, "Server Error. Please refresh and retry.");
		else 
			centreToBeUpdated = getCentreById(centreid);
		
		if(null == name) return new MessageVO(AppConstants.FAILURE, "Please enter a Centre name.");
		else if(null == code) return new MessageVO(AppConstants.FAILURE, "Please enter a valid Centre Code.");
		else if(null != (centreFromDB = getCentresByNames(name)) && centreFromDB.getCentreId() != centreid) {
			return new MessageVO(AppConstants.FAILURE, "Centre already exists. Please check.");
		} else if(null != (centreFromDB = getCentreByCode(code)) && centreFromDB.getCentreId() != centreid) {
			return new MessageVO(AppConstants.FAILURE, "Centre Code already exists. Please check.");
		}
		else {
			return new MessageVO(AppConstants.SUCCESS, "Please add the centre.");
		}
	}
	
	
	private CentreVO getVOFromModel(Centre centre){	
		if(null == centre) return null;
		CentreVO vo = new CentreVO();
		vo.setCentreId(centre.getCentreid());
		vo.setCentreName(centre.getCentrename());
		vo.setCentreCode(centre.getCentrecode());
		vo.setAddress(centre.getAddress());
		vo.setLandline(centre.getLandline());
		vo.setCentreId(centre.getCentreid());
		vo.setHeadId(centre.getHeadid());
		vo.setLatitude(centre.getLatitude());
		vo.setLongitude(centre.getLongitude());
		vo.setZoneId(centre.getZoneid());
		vo.setZone(zoneService.getZoneById(vo.getZoneId()).getZone());
		return vo;		
	}
	
	private List<CentreVO> getVOListFromModel(List<Centre> centres){
		if(null == centres)return null;
		List<CentreVO> list = new ArrayList<CentreVO>();
		centres.forEach(centre -> {			
			list.add(getVOFromModel(centre));
		});
		return list;		
	}
	
	public CentreVO getCentreByCode(String code) {
		if(null == code) return null;
		return getVOFromModel(centreRepository.findByCentrecode(code));
	}

	@Override
	public MessageVO isCentreNameCodeUnique(String name, String code) {
		if(null == name) return new MessageVO(AppConstants.FAILURE, "Please enter a Centre name.");
		else if(null == code) return new MessageVO(AppConstants.FAILURE, "Please enter a Centre code.");
		else if(null != this.getCentresByNames(name)) {
			return new MessageVO(AppConstants.FAILURE, "Centre already exists. Please check.");
		} else if(null != this.getCentreByCode(code)) {
			return new MessageVO(AppConstants.FAILURE, "Centre Code already exists. Please check.");
		} else {
			return new MessageVO(AppConstants.SUCCESS, "Please add the centre.");
		}
	}

	@Override
	public CentreVO getCentreByHeadId(Integer headId) {
		if(null == headId) return null;
		return getVOFromModel(centreRepository.findByHeadid(headId));
	}

}
