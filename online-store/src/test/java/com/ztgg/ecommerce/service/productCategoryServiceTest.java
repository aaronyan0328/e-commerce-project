package com.ztgg.ecommerce.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ztgg.ecommerce.entity.ProductCategory;
import com.ztgg.ecommerce.entity.Shop;

public class productCategoryServiceTest {
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Test
	public void testGetProductCategoryList() {
		ProductCategory productCategoryCondition = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(240L);
		productCategoryCondition.setShop(shop);
		List<ProductCategory> productList = productCategoryService.getProductCategaryList(productCategoryCondition, 0, 100);
		System.out.println("shop count this page：" + productList.size());
		// more test
	}
}
