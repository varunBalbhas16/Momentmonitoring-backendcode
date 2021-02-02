package com.irctn.serviceimpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.DoNotDisturb;
import com.irctn.model.DriverAttendance;
import com.irctn.model.VehicleDriver;
import com.irctn.repository.CentreDepartmentRepository;
import com.irctn.repository.DoNotDisturbRepository;
import com.irctn.repository.DriverAttendanceRepository;
import com.irctn.repository.TicketRepository;
import com.irctn.repository.VehicleDriverRepository;
import com.irctn.service.CentreService;
import com.irctn.service.DepartmentService;
import com.irctn.service.MobileService;
import com.irctn.service.TicketService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.vo.DriverAttendanceVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.TicketVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.VehicleDriverVO;
import com.irctn.vo.mobile.DoNotDisturbVO;
import com.irctn.vo.mobile.DriverAttendanceListResponseVO;
import com.irctn.vo.mobile.DriverListResponseVO;
import com.irctn.vo.mobile.MobileResponseVO;
import com.irctn.vo.mobile.ResetPasswordWrapper;
import com.irctn.vo.mobile.TicketProgressWrapper;
import com.irctn.vo.mobile.UserShortDetailVO;
import com.irctn.vo.mobile.UserVOMobile;
import com.irctn.vo.mobile.request.ResetPasswordVO;
import com.irctn.vo.mobile.request.VehicleDriverRequestVO;

@Service
public class MobileServiceImpl implements MobileService {

	private static final Logger LOGGER = LoggerFactory.getLogger("com.irctn.serviceimpl.MobileServiceImpl");

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	CentreService centreService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	TicketService ticketService;
	
	@Autowired
	CentreDepartmentRepository centreDepartmentRepository;
	
	@Autowired
	TicketRepository ticketRepository;
	
	@Autowired
	DoNotDisturbRepository doNotDisturbRepository;
	
	@Autowired
	DriverAttendanceRepository driverAttendanceRepository;
	
	@Autowired
	VehicleDriverRepository vehicleDriverRepository;
	
	@Override
	public UserVOMobile getUser(UserVO user) {
		if(null == user) return null;
		UserVOMobile userVOMobile = new UserVOMobile();
		userVOMobile.setUserId(user.getUserid());
		userVOMobile.setFirstName(user.getFirstName());
		userVOMobile.setLastName(user.getLastName());
		userVOMobile.setStatus(AppConstants.SUCCESS);
		userVOMobile.setEmail(user.getEmail());
		userVOMobile.setRoleName(user.getRoleName());
		userVOMobile.setContact(user.getContact());
		List<TicketVO> ticketList = ticketService.getTicketListForDPM(user.getUserId());
		if(null != ticketList && !ticketList.isEmpty()) {
			userVOMobile.setOpenTickets(ticketList.size());
		} else {
			userVOMobile.setOpenTickets(0);
		}
		TicketProgressWrapper current = ticketService.getCurrentTicketForDPM(user.getUserid());
		if(null != current) {
			userVOMobile.setCurrentTicket(1);
		}
		else {
			userVOMobile.setCurrentTicket(0);
		}
		
		ticketList = ticketService.getClosedTicketListForDPM(user.getUserid());
		if(null == ticketList) {
			userVOMobile.setFieldTrips(0);
		} else {
			userVOMobile.setFieldTrips(ticketList.size());
		}
		
		DoNotDisturbVO dnd = null;
		if(null != (dnd = this.getActiveDndByUserId(user.getUserid())) ){
			Date endTime = dnd.getEndDateTime();
			if(endTime.before(new Date())) {
				System.out.println("..............................Deactive the DND. Time has elapsed for the user on :" + endTime.toGMTString());				
				this.deactivateDND(user.getUserid());
				userVOMobile.setDoNotDisturb(AppConstants.STATUS_INACTIVE_STR);
				
			} else {
				System.out.println("The user has active DND .................................");
				userVOMobile.setDoNotDisturb(AppConstants.STATUS_ACTIVE_STR);
				userVOMobile.setDoNotdisturbStart(dnd.getStartDateTime());
				userVOMobile.setDoNotDisturbEnd(dnd.getEndDateTime());
			}
			
		} else {
			userVOMobile.setDoNotDisturb(AppConstants.STATUS_INACTIVE_STR);
		}
		return userVOMobile;
	}

	@Override
	public ResetPasswordWrapper resetPassword(ResetPasswordVO vo) {
		if(null == vo)return null;
		ResetPasswordWrapper wrapper = new ResetPasswordWrapper();
		if(null == vo.getEmail() || null == vo.getOldPassword() || null == vo.getNewPassword()) {
			wrapper.setResult(AppConstants.FAILURE);
			wrapper.setErrorMessage("Cannot Reset Password. Insufficient fields.");
		} 
		UserVO userVO = userService.verifyLogin(vo.getEmail(), vo.getOldPassword());
		if(null == userVO || null != userVO.getResult()) {
			wrapper.setResult(AppConstants.FAILURE);
			wrapper.setErrorMessage("Invalid Credentials. Cannot Reset Password.");
		} else {
			MessageVO messageVO = userService.resetPassword(vo);
			wrapper.setEmailId(vo.getEmail());
			wrapper.setResult(AppConstants.SUCCESS);
			wrapper.setErrorMessage("Reset Password Successful.");
		}
		return wrapper;
			
	}

	@Override
	public MessageVO saveDoNotDisturb(DoNotDisturbVO vo) {
		if(null == vo)	return null;
		if(null == vo.getUserId() || null == vo.getStartDateTime() || null == vo.getEndDateTime()) {
			return new MessageVO(AppConstants.FAILURE, "Invalid inputs. Please check.");
		}
		//get the active dnd
		if(null == this.getActiveDndByUserId(vo.getUserId()) ) {
			//save the dnd
			DoNotDisturb savedDND = doNotDisturbRepository.save(getModelFromVO(vo));
			System.out.println("------------> DND activated for user " + savedDND.getUserid() + " from " + savedDND.getDndstart().toGMTString());
			redisTemplate.opsForHash().put("DND", vo.getUserId(), getDndVOFromModel(savedDND));
			return new MessageVO(AppConstants.SUCCESS, "Do Not Disturb details saved successfully.");
		} else {
			return new MessageVO(AppConstants.FAILURE, "Do Not Disturb is active, Plz try again.");
		}
	}
	
	private DoNotDisturb getModelFromVO(DoNotDisturbVO vo) {
		if(null == vo) return null;
		DoNotDisturb dnd = new DoNotDisturb();
		dnd.setDndstart(vo.getStartDateTime());
		dnd.setDndstop(vo.getEndDateTime());
		dnd.setUserid(vo.getUserId());
		dnd.setStatus(AppConstants.STATUS_ACTIVE);
		if(null != vo.getDoNotDisturbId()) {
			dnd.setDndid(vo.getDoNotDisturbId());
		}
		return dnd;
	}

	@Override
	public DoNotDisturbVO getActiveDndByUserId(Integer userId) {
		if(null == userId) return null;
		Object activeDND = redisTemplate.opsForHash().get("DND", userId);
		if(null != activeDND) {
			System.out.println("Return ing from REDIS........." + userId);
			return (DoNotDisturbVO) activeDND;
		}
		
		List<DoNotDisturb> list = doNotDisturbRepository.findByUseridAndStatus(userId, AppConstants.STATUS_ACTIVE);
		if(null == list || list.size() == 0) {
			return null;
		} else {
			System.out.println("Return ing from DB........." + userId);
			redisTemplate.opsForHash().put("DND", userId, getDndVOFromModel(list.get(0)));
			return getDndVOFromModel(list.get(0));
		}
	}

	private DoNotDisturbVO getDndVOFromModel(DoNotDisturb doNotDisturb) {
		if(null == doNotDisturb) return null;
		DoNotDisturbVO vo = new DoNotDisturbVO();
		vo.setDoNotDisturbId(doNotDisturb.getDndid());
		vo.setUserId(doNotDisturb.getUserid());
		vo.setStartDateTime(doNotDisturb.getDndstart());
		vo.setEndDateTime(doNotDisturb.getDndstop());
		vo.setEmailId(userService.getUserById(doNotDisturb.getUserid()).getEmail());
		if(AppConstants.STATUS_ACTIVE.equals(doNotDisturb.getStatus()) ) {
			vo.setStatus(AppConstants.STATUS_ACTIVE_STR);
		} else {
			vo.setStatus(AppConstants.STATUS_INACTIVE_STR);
		}
		return vo;
	}
	
	@Override
	public MessageVO deactivateDND(Integer userId) {
		if(null == userId) return null;
		DoNotDisturbVO vo = this.getActiveDndByUserId(userId);
		if(null == vo) {
			return new MessageVO(AppConstants.FAILURE, "There are no active DND to deactivate.");
		} else {
			DoNotDisturb model = getModelFromVO(vo);
			model.setStatus(AppConstants.STATUS_INACTIVE);
			doNotDisturbRepository.save(model);
			redisTemplate.opsForHash().delete("DND", vo.getUserId());
			return new MessageVO(AppConstants.SUCCESS, "Deactivated DND record.");
		}
	}

	

}
