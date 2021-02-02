package com.irctn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irctn.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

	public Orders findByOrderid(Integer orderId);

}
