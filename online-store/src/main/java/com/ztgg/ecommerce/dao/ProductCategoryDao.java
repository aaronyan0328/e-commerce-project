package com.ztgg.ecommerce.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ztgg.ecommerce.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * this function returns a list of ProductCategory
	 * @return ProductCategoryList
	 */
	List<ProductCategory> queryProductCategoryList(@Param("productCategaryCondition") ProductCategory productCategoryCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
}
