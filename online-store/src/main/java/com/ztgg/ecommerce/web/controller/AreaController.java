package com.ztgg.ecommerce.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztgg.ecommerce.entity.Area;
import com.ztgg.ecommerce.service.AreaService;


@Controller
@RequestMapping("/admin")
public class AreaController {
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value = "/hello",method = RequestMethod.GET)
	@ResponseBody
	private String testHello() {
		return "HelloWorld from spring controller";
	}
	
	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listArea(){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		List<Area> list = new ArrayList<Area>();
		try {
			list = areaService.getAreaList();
			modelMap.put("data", list);
			modelMap.put("total", list.size());
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg",e.toString());
		}
		return modelMap;
	}
}
