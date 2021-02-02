package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	public Role findByRolename(String roleName);

	public Role findByRoleid(Integer roleid);

}
