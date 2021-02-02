package com.irctn.service;

import java.util.List;

import com.irctn.vo.MessageVO;
import com.irctn.vo.ProgramVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.mobile.request.ResetPasswordVO;

public interface UserService {

	public UserVO verifyLogin(String email, String password);

	// public User findByEmail(String email);

	//public String saveUser(UserVO userVO);

	//public String randomPasswordGenerator();

	public UserVO getUserById(Integer userid);

	//public List<UserVO> getAllUser();

	public String deleteUser(Integer userid);

	public List<UserVO> getAllUser();

	public UserVO verifyEmail(String email);

	public List<UserVO> getUserWithNameLike(String search, Integer roleid);
	
	public List<UserVO> getUserWithNameAndRoleNameLike(String search, String roleName);
	
	public String getNameById(Integer userId);

	public MessageVO resetPassword(ResetPasswordVO vo);

	public String saveUser(UserVO userVO, String password);

	public MessageVO validateUser(UserVO userVO);

	public List<UserVO> getUsersWithRole(String roleDriver);

	public UserVO getUserByEmail(String email);

	/*
	 * public void deleteUser(Integer userid);
	 * 
	 * public User findByUserid(Integer userid);
	 */

}
