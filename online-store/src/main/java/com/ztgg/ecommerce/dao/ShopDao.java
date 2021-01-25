package com.ztgg.ecommerce.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ztgg.ecommerce.entity.Shop;


public interface ShopDao {

	/**
	 * register a new shop
	 * 
	 * @param shop
	 * @return
	 */
	int insertShop(Shop shop);

	/**
	 * update existing shop
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
	
	/**
	 * query shop by id
	 * 
	 * @param shopId
	 * @return Shop
	 */
	Shop queryByShopId(long shopId);
	
	/**
	 * queryShopList with paginated view
	 * with filters: shopname, status, category, areaid, owner
	 * 
	 * @param shopCondition
	 * @param rowIndex 
	 * @param pageSize          
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/**
	 * return total number of shops
	 * 
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
}
