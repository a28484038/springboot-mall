package com.candy.springboot_mall.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.candy.springboot_mall.model.Order;

public class OrderRowMapper implements RowMapper<Order> {

	@Override
	public Order mapRow(ResultSet resultSet, int i) throws SQLException {
		
		Order order = new Order();
		order.setOrder_id(resultSet.getInt("order_id"));
		order.setUser_id(resultSet.getInt("user_id"));
		order.setTotal_amount(resultSet.getInt("total_amount"));
		order.setCreatedDate(resultSet.getTimestamp("created_date"));
		order.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));
		
		return order;
	}

	
	
	

}
