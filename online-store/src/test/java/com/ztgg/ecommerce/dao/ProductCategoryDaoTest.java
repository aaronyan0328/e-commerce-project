package com.ztgg.ecommerce.dao;


import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ztgg.ecommerce.BaseTest;
import com.ztgg.ecommerce.entity.ProductCategory;
import com.ztgg.ecommerce.entity.Shop;

public class ProductCategoryDaoTest extends BaseTest {
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
//	@Ignore
	public void testQueryProductCategaryListAndCount() {
		ProductCategory productCategoryCondition = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(240L);
		productCategoryCondition.setShop(shop);
		List<ProductCategory> productList = productCategoryDao.queryProductCategoryList(productCategoryCondition, 0, 100);
		System.out.println("shop count this page：" + productList.size());		
	}
}
