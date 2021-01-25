package com.ztgg.ecommerce.util;


public class PageCalculator {
	
	// pageindex - > rowindex
	public static int calculateRowIndex(int pageIndex, int pageSize) {
		return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
	}
}
