package com.ztgg.ecommerce.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ztgg.ecommerce.dao.ShopDao;
import com.ztgg.ecommerce.dto.ShopDto;
import com.ztgg.ecommerce.entity.Shop;
import com.ztgg.ecommerce.enums.ShopStateEnum;
import com.ztgg.ecommerce.exceptions.ShopOperationException;
import com.ztgg.ecommerce.service.ShopService;
import com.ztgg.ecommerce.util.ImageUtil;
import com.ztgg.ecommerce.util.PageCalculator;
import com.ztgg.ecommerce.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional
	public ShopDto addShop(Shop shop, InputStream shopImgInputStream, String fileName) {
		// empty shop input
		if (shop == null) {
			return new ShopDto(ShopStateEnum.NULL_SHOP);
		}
		
		try {
			// set status to CHECK
			shop.setEnableStatus(0);
			shop.setTimeCreated(new Date());
			shop.setTimeUpdated(new Date());
			
			int effectedNum = shopDao.insertShop(shop);
			
			if (effectedNum <= 0) {
				throw new ShopOperationException("error creating new shop");
			} else {
				// success case: insert image
				if (shopImgInputStream != null) {
					// shop image
					try {
						addShopImg(shop, shopImgInputStream, fileName);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// update shop image
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("update ShopImg failed");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		return new ShopDto(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	public ShopDto modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopDto(ShopStateEnum.NULL_SHOP);
		} else {
			// 1.is image processing needed?
			try {
				if (shopImgInputStream != null && fileName != null) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					// delete old image if exist
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, shopImgInputStream,fileName);
				}
				// 2.update shop info
				shop.setTimeUpdated(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopDto(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopDto(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new ShopOperationException("modifyShop error:" + e.getMessage());
			}
		}
	}

	@Override
	public ShopDto getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		//pageIndex -> rowIndex
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopDto se = new ShopDto();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
}
