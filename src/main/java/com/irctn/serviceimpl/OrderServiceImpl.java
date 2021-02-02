package com.irctn.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irctn.model.Orders;
import com.irctn.repository.OrderRepository;
import com.irctn.service.OrderService;
import com.irctn.util.AppConstants;
import com.irctn.vo.OrderVO;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Override
	public String saveOrderDetails(OrderVO orderVO) {
		if (null != orderVO.getOrderId()) {
			return AppConstants.FAILURE;
		}
		Orders order = orderRepository.findByOrderid(orderVO.getOrderId());
		String success = "success";
		if (null == order) {
			order = new Orders();
		} else {
			order.setOrdername(orderVO.getOrderName());
			order.setCustomerid(orderVO.getCustomerId());
			order.setStatus(AppConstants.STATUS_ACTIVE);
			orderRepository.save(order);
		}
		return success;

	}

}
