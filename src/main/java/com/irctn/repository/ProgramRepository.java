package com.irctn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Program;

public interface ProgramRepository extends JpaRepository<Program, Integer> {

	public Program findByProgramid(Integer programid);

	public long countByStatus(Integer status);

	// public List<Program> findDistinctByProgramnameLike(String programName);

	public List<Program> findDistinctByprogramnameLike(String search);

	public List<Program> findByProgramnameLike(String name);

}
