package com.ztgg.ecommerce.service;


import java.util.List;

import com.ztgg.ecommerce.entity.ProductCategory;

public interface ProductCategoryService {
	List<ProductCategory> getProductCategaryList(ProductCategory productCategory, int pageIndex, int pageSize);
}
