package com.ztgg.ecommerce.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ztgg.ecommerce.dao.ProductCategoryDao;
import com.ztgg.ecommerce.entity.ProductCategory;
import com.ztgg.ecommerce.service.ProductCategoryService;
import com.ztgg.ecommerce.util.PageCalculator;

@Service
public class ProductCategaryServiceImp implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Override
	public List<ProductCategory> getProductCategaryList(ProductCategory productCategoryCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		return productCategoryDao.queryProductCategoryList(productCategoryCondition, rowIndex, pageSize);
	}
}
