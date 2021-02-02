package com.irctn.serviceimpl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.irctn.model.Role;
import com.irctn.model.User;
import com.irctn.repository.RoleRepository;
import com.irctn.repository.UserRepository;
import com.irctn.service.CentreService;
import com.irctn.service.UserService;
import com.irctn.util.AppConstants;
import com.irctn.vo.CentreVO;
import com.irctn.vo.ContributorVO;
import com.irctn.vo.MessageVO;
import com.irctn.vo.UserVO;
import com.irctn.vo.mobile.request.ResetPasswordVO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	CentreService centreService;

	@Autowired
	RedisTemplate<Object, Object> redisTemplate;

	@Override
	public UserVO verifyLogin(String email, String password) {
		UserVO vo = new UserVO();
		String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
			User user = userRepository.findByEmailAndPassword(email, encodedPassword);

			if (null == user) {

				System.out.println("User does not Exist for the Particular Emailid and Password");

				return new UserVO("Invalid User Credentials");

			} else {

				vo.setFirstName(user.getFirstname());
				vo.setLastName(user.getLastname());
				vo.setContact(user.getContact());
				vo.setUserid(user.getUserid());
				vo.setEmail(user.getEmail());
				Role role = roleRepository.findByRoleid(user.getRole().getRoleid());
				vo.setRoleName(role.getRolename());
				boolean isCentreHead = false;
				if("centre head".equalsIgnoreCase(vo.getRoleName())) {
					isCentreHead = true;
				}
				CentreVO centreVO = centreService.getCentreByHeadId(user.getUserid());
				if(null != centreVO) {
					vo.setCentreId(centreVO.getCentreId());
				} else if(isCentreHead) {
					System.out.println("Map a Centre for this Centre Head. No centre is assigned to this user.");
					return new UserVO("You have not been mapped to a Centre. Please contact Admin to get mapped to a Centre.");
				}
				
				//vo.setContributorId(user.getContributorid());
				//Role role = roleRepository.findByRoleid(user.getRoleid());
				vo.setResult(AppConstants.SUCCESS);
				return vo;
			}
		}

	

	@Override
	public String saveUser(UserVO userVO, String password) {

		User user = userRepository.findByUserid(userVO.getUserid());
		String success = "success";
		if (null == user) {
			String encodedRandomPassword = null;
			user = new User();			
			password = userVO.getContact();
			String lastFourDigits = "";
			if (password.length() > 4) {
				lastFourDigits = password.substring(password.length() - 4);
			} else {
				lastFourDigits = password;

			}
			encodedRandomPassword = Base64.getEncoder().encodeToString(lastFourDigits.getBytes());
			user.setPassword(encodedRandomPassword);

		} else {
			redisTemplate.opsForHash().delete("USER_BY_ID", userVO.getUserid());
			success = "updatesuccess";
			if(null != password) {
				user.setPassword(Base64.getEncoder().encodeToString(password.getBytes())); //reset password
			}
		}
		user.setFirstname(userVO.getFirstName());
		user.setLastname(userVO.getLastName());
		user.setContact(userVO.getContact());
		user.setEmail(userVO.getEmail());
		Role role = roleRepository.findByRolename(userVO.getRoleName());
		if (null == role) {
			return null;
		} else {
			user.setRoleid(role.getRoleid());
			user.setStatus(AppConstants.STATUS_ACTIVE);
			user.setLastLogin(null);
			user.setFailedattempts(null);
			userRepository.save(user);

			redisTemplate.opsForHash().delete("USERLIST", "USERS");
			return success;
		}
	}

	@Override
	public List<UserVO> getAllUser() {
		Object object = redisTemplate.opsForHash().get("USERLIST", "USERS");
		List<User> users = null;
		List<UserVO> voList = new ArrayList<UserVO>();
		List<UserVO> sortedList = null;
		if (null == object) {
			users = userRepository.findAll();
		} else {

			return (List<UserVO>) object;
		}
		if (null != users) {

			for (User user : users) {
				UserVO vo = new UserVO();
				vo.setFirstName(user.getFirstname());
				vo.setLastName(user.getLastname());
				vo.setEmail(user.getEmail());
				vo.setContact(user.getContact());
				vo.setUserid(user.getUserid());
				Role role = roleRepository.findByRoleid(user.getRole().getRoleid());
				if (null == role) {
					continue;
				} else {
					vo.setRoleName(role.getRolename());
					voList.add(vo);

				}

			}
			sortedList = voList.stream().sorted(Comparator.comparing(UserVO::getUserid).reversed())
					.collect(Collectors.toList());
			redisTemplate.opsForHash().put("USERLIST", "USERS", sortedList);
			return sortedList;

		} else {
			
			return null;
		}

	}

	@Override
	public UserVO getUserById(Integer userid) {
		Object object = redisTemplate.opsForHash().get("USER_BY_ID", userid);
		User user = null;
		UserVO vo = new UserVO();
		if (null == object) {
			user = userRepository.findByUserid(userid);
		} else {			
			return (UserVO) object;
		}

		if (null != user) {
			vo.setUserid(user.getUserid());
			vo.setFirstName(user.getFirstname());
			vo.setLastName(user.getLastname());
			vo.setContact(user.getContact());
			vo.setEmail(user.getEmail());
			Role role = roleRepository.findByRoleid(user.getRole().getRoleid());
			if (null == role) {
				return null;
			} else {
				vo.setRoleName(role.getRolename());

			}
			redisTemplate.opsForHash().put("USER_BY_ID", vo.getUserid(), vo);
			redisTemplate.opsForHash().delete("USERLIST", "USERS");
			return vo;

		} else {

			return null;

		}

	}

	@Override
	public String deleteUser(Integer userid) {
		userRepository.delete(userid);
		redisTemplate.opsForHash().delete("USER_BY_ID", userid);
		redisTemplate.opsForHash().delete("USERLIST", "USERS");
		return "success";
	}

	@Override
	public UserVO verifyEmail(String email) {
		User emailObject = userRepository.findByEmailIgnoreCase(email);
		if(null == emailObject){
			return new UserVO("failure");
		}else{
			return new UserVO("success");
		}
	}


	@Override
	public List<UserVO> getUserWithNameLike(String search, Integer roleid) {
		//List<User> matchUsers = userRepository.findByFirstnameOrLastnameContainingAndRoleid(search, search, roleid);
		List<User> matchUsers = userRepository.findByRoleidAndFirstnameContaining(roleid, search);
		System.out.println("matchUsers ----" + matchUsers);
		return getVOListfromModelList(matchUsers);
	}
	
	private UserVO getVOFromModel(User user) {
		if(null == user) return null;
		UserVO vo = new UserVO();
		vo.setUserId(user.getUserid());
		vo.setFirstName(user.getFirstname());
		vo.setLastName(user.getLastname());
		vo.setContact(user.getContact());
		vo.setEmail(user.getEmail());
		Role role = roleRepository.findByRoleid(user.getRole().getRoleid());
		if (null == role) {
			return null;
		} else {
			vo.setRoleName(role.getRolename());

		}
		return vo;		
	}
	
	private List<UserVO> getVOListfromModelList(List<User> users) {
		if(null == users) return null;
		List<UserVO> list = new ArrayList<UserVO>();
		for(User user : users) {
			list.add(getVOFromModel(user));
		}
		return list;
	}



	@Override
	public List<UserVO> getUserWithNameAndRoleNameLike(String search, String roleName) {
		Role role = roleRepository.findByRolename(roleName);
		return this.getUserWithNameLike(search, role.getRoleid());
				
	}


	@Override
	public String getNameById(Integer userId) {
		if(null == userId) return null;
		UserVO user = this.getUserById(userId);
		if(null != user) {
			return user.getFirstName() + " " + user.getLastName();
		} else {
			return null;
		}
	}


	@Override
	public MessageVO resetPassword(ResetPasswordVO vo) {
		if(null == vo) return new MessageVO(AppConstants.FAILURE, "Invalid Credentials. Please check.");
		UserVO userVO = this.verifyLogin(vo.getEmail(), vo.getOldPassword());
		if(null == userVO || null != userVO.getResult()) {
			return new MessageVO(AppConstants.FAILURE, "Invalid Credentials. Cannot Reset Password.");
		} else {			
			String result = this.saveUser(userVO, vo.getNewPassword());
			if(null == result) {
				return new MessageVO(AppConstants.FAILURE, "Cannot Reset Password.");
			} else {
				return new MessageVO(AppConstants.SUCCESS, "Password reset successfully.");
			}
		}		
	}


	@Override
	public MessageVO validateUser(UserVO userVO) {
		if(null == userVO) {
			return new MessageVO( "Invalid Data to save User.", AppConstants.FAILURE, "Invalid Data to save User.");
		} else {
			if(null == userVO.getEmail() || userVO.getEmail().isEmpty()) {
				return new MessageVO("Please enter a valid email.", AppConstants.FAILURE,  "Please enter a valid email.");
			} else {
				User user = userRepository.findByEmailIgnoreCase(userVO.getEmail());
				if(null == user) {
					return new MessageVO("This is a unique user.", AppConstants.SUCCESS,  "This is a unique user.");
				} else {
					return new MessageVO("Email is already taken by " + user.getFirstname() + " " + user.getLastname()  + " - " + user.getRole().getRoledescription()
											, AppConstants.FAILURE
											, "Email is already taken by " + user.getFirstname() + " " + user.getLastname());
				}
			}
		}		
	}

	@Override
	public List<UserVO> getUsersWithRole(String roleDriver) {
		Role role = roleRepository.findByRolename(roleDriver);
		if(null == role) return null;
		List<User> users = userRepository.findByRoleid(role.getRoleid());
		if(null == users || users.isEmpty()) {
			return null;
		} else {
			return this.getVOListfromModelList(users);
		}
	}

	@Override
	public UserVO getUserByEmail(String email) {
		if(null == email)	return null;
		return this.getVOFromModel( userRepository.findByEmailIgnoreCase(email) );
	}

}
