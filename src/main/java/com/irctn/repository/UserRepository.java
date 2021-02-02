package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	// public User findByEmailAndPassword(String email, String encodedPassword);

	public User findByEmailIgnoreCase(String email);

	// public User findByUsernameAndPassword(String username, String
	// encodedPassword);

	public User findByEmailAndPassword(String email, String encodedPassword);

	public User findByUserid(Integer userid);


	public User findByEmail(User email);
	
	public List<User> findByFirstnameOrLastnameContainingAndRoleid(String firstname, String lastname, Integer roleid);
	
	public List<User> findByRoleidAndFirstnameContaining(Integer roleid, String firstname);

	public List<User> findByRoleid(Integer roleid);

	// public User findByUsername(String username);

}
