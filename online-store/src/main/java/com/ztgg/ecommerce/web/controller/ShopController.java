package com.ztgg.ecommerce.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztgg.ecommerce.dto.ShopDto;
import com.ztgg.ecommerce.entity.Shop;
import com.ztgg.ecommerce.entity.User;
import com.ztgg.ecommerce.enums.ShopStateEnum;
import com.ztgg.ecommerce.exceptions.ShopOperationException;
import com.ztgg.ecommerce.service.AreaService;
import com.ztgg.ecommerce.service.ProductCategoryService;
import com.ztgg.ecommerce.service.ShopCategoryService;
import com.ztgg.ecommerce.service.ShopService;
import com.ztgg.ecommerce.util.CodeUtil;
import com.ztgg.ecommerce.util.HttpServletRequestUtil;
import com.ztgg.ecommerce.entity.Area;
import com.ztgg.ecommerce.entity.ProductCategory;
import com.ztgg.ecommerce.entity.ShopCategory;


@Controller
@RequestMapping("/shopadmin")
public class ShopController {
	
	@Autowired
	ShopService shopService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	
	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//check Kaptcha
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Wrong Kapture!");
			return modelMap;
		}
		// 1.get and transform all input data
		//for shop object
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		// for image
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "can't upload empty image");
			return modelMap;
		}
		
		// 2.register shop
		if (shop != null && shopImg != null) {
			// TODO: manually set owner info here, but should get it from session in the future
//			User owner = (User) request.getSession().getAttribute("user");
			User owner = new User();
			owner.setUserId(1L);
			shop.setOwner(owner);
			ShopDto shopDto;
			
			try {
				shopDto = shopService.addShop(shop, shopImg.getInputStream(),shopImg.getOriginalFilename());
				if (shopDto.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(shopDto.getShop());
					request.getSession().setAttribute("shopList", shopList);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", shopDto.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "can't insert empty shop");
			return modelMap;
		}
	}
	
	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductCategoryList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				ProductCategory productCategoryCondition = new ProductCategory();
				Shop shop = shopService.getByShopId(shopId);
				productCategoryCondition.setShop(shop);
				modelMap.put("productCategoryList", productCategoryService.getProductCategaryList(productCategoryCondition, 0, 10));
				// put shoplist into session so that it will be accessible by this user in the session
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//check Kaptcha
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "Wrong Kapture!");
			return modelMap;
		}
		// 1.get and transform all input data
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}
		// 2.modify shop info
		if (shop != null && shop.getShopId() != null) {
			ShopDto shopDto;
			User owner = new User();
			owner.setUserId(1L);
			shop.setOwner(owner);
			try {
				if (shopImg == null) {
					shopDto = shopService.modifyShop(shop, null, null);
				} else {
					shopDto = shopService.modifyShop(shop, shopImg.getInputStream(),shopImg.getOriginalFilename());
				}
				if (shopDto.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", shopDto.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
			return modelMap;
		}
	}
	
	@RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// TODO: remve these 3 line when login/session code is complete
		User owner = new User();
		owner.setUserId(123L);
		request.getSession().setAttribute("user", owner);
		//get owner from session
		owner = (User) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(owner);
			ShopDto shopDto = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", shopDto.getShopList());
			// put shoplist into session so that it will be accessible by this user in the session
			request.getSession().setAttribute("shopList", shopDto.getShopList());
			modelMap.put("user", owner);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/online-store/shopadmin/shoplist");
			} else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("shopId", shopId);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
}
