	package com.candy.springboot_mall.dao;
	
	import java.util.List;

import com.candy.springboot_mall.constant.ProductCategory;
import com.candy.springboot_mall.dto.ProductRequest;
import com.candy.springboot_mall.model.Product;
import com.candy.springboot_mall.service.ProductService;
	
	public interface ProductDao {
		
		Integer countProduct(ProductQueryParams productQueryParams);
	
		List<Product> getProducts(ProductQueryParams productQueryParams);
		
		Product getProductById(Integer productId);
		
		//這個方法返回值為Integer類型，參數就是前端回傳的ProductRequest
		Integer createProduct (ProductRequest productRequest);
		
		void updateProduct(Integer productId, ProductRequest productRequest);
		
		void updateStock(Integer productId, Integer stock);
		
		void deleteProductById(Integer productId);
	}
