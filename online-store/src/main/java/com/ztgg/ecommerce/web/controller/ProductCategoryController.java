package com.ztgg.ecommerce.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztgg.ecommerce.entity.ProductCategory;
import com.ztgg.ecommerce.entity.Shop;
import com.ztgg.ecommerce.entity.User;
import com.ztgg.ecommerce.service.ProductCategoryService;

@Controller
@RequestMapping("/productcategoryadmin")
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value = "/getproductCategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductCategoryList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// TODO: remve these 3 line when login/session code is complete
		User owner = new User();
		owner.setUserId(123L);
		request.getSession().setAttribute("user", owner);
		//get owner from session
		owner = (User) request.getSession().getAttribute("user");
		try {
			ProductCategory productCategoryCondition = new ProductCategory();
			Shop shop = new Shop();
			shop.setShopId(240L);
			// how to get shopID from session
			productCategoryCondition.setShop(shop);
			modelMap.put("productCategoryList", productCategoryService.getProductCategaryList(productCategoryCondition, 0, 10));
			// put shoplist into session so that it will be accessible by this user in the session
			request.getSession().setAttribute("shopList", productCategoryService.getProductCategaryList(productCategoryCondition, 0, 10));
			modelMap.put("user", owner);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
}
