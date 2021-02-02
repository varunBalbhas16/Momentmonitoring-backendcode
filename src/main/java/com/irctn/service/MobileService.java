package com.irctn.service;

import com.irctn.vo.MessageVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.mobile.DoNotDisturbVO;
import com.irctn.vo.mobile.ResetPasswordWrapper;
import com.irctn.vo.mobile.UserVOMobile;
import com.irctn.vo.mobile.request.ResetPasswordVO;

public interface MobileService {

	public UserVOMobile getUser(UserVO user);

	public ResetPasswordWrapper resetPassword(ResetPasswordVO vo);

	public MessageVO saveDoNotDisturb(DoNotDisturbVO vo);

	public MessageVO deactivateDND(Integer userid);

	public DoNotDisturbVO getActiveDndByUserId(Integer userId);
	
}
