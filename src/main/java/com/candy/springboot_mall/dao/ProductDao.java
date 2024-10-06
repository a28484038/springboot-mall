	package com.candy.springboot_mall.dao;
	
	import com.candy.springboot_mall.model.Product;
	
	public interface ProductDao {
	
		Product getProductById(Integer productId);
	}
