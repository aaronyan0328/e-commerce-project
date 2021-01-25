package com.ztgg.ecommerce.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * this class is to redirect request to view (V in MVC) in WEB-INF/
 * @author youzhedou
 *
 */
@Controller
@RequestMapping(value = "shopadmin", method = { RequestMethod.GET })
public class ShopAdminController {
	@RequestMapping(value = "/shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}
	
	@RequestMapping(value = "/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}
	
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement() {
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value = "/productcategorylist")
	public String productcategorylist() {
		return "shop/productcategorylist";
	}
}
