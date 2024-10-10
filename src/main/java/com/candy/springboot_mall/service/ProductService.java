package com.candy.springboot_mall.service;

import java.util.List;

import com.candy.springboot_mall.dao.ProductQueryParams;
import com.candy.springboot_mall.dto.ProductRequest;
import com.candy.springboot_mall.model.Product;

public interface ProductService {

	Integer countProduct(ProductQueryParams productQueryParams);
	
	List<Product> getProducts(ProductQueryParams productQueryParams);
	
	Product getProductById(Integer productId);
	
	//這個方法返回值為Integer類型，參數就是前端回傳的ProductRequest
	Integer createProduct(ProductRequest productRequest);
	
	void updateProduct(Integer productId, ProductRequest productRequest);
	
	void deleteProductById(Integer productId);
	
	
}
