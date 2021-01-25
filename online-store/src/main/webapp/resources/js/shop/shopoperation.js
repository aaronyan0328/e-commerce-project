/**
 * 1. get shop_category and area from DB
 * 2. call controller and register a shop when submitted
 */
$(function() {
	var initUrl = '/online-store/shopadmin/getshopinitinfo';
	var registerShopUrl = '/online-store/shopadmin/registershop';
	
	var shopId = getQueryString('shopId')
	var isEdit = shopId ? true : false;
	var shopInfoUrl = "/online-store/shopadmin/getshopbyid?shopId=" + shopId;
	var editShopUrl = '/online-store/shopadmin/modifyshop'
	
	if (!isEdit) {
		getShopInitInfo();
	} else {
		getShopInfo(shopId);
	}
	
	alert('js triggered')
	
	function getShopInfo(shopId) {
		$.getJSON(shopInfoUrl, function(data) {
			if (data.success) {
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory = '<option data-id="'
						+ shop.shopCategory.shopCategoryId + '" selected>'
						+ shop.shopCategory.shopCategoryName + '</option>';
				var tempAreaHtml = '';
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				// change shopcategory not allowed
				$('#shop-category').attr('disabled', 'disabled');
				$('#area').html(tempAreaHtml);
				// use old area id for default
				$("#area option[data-id='" + shop.area.areaId + "']").attr(
						"selected", "selected");
			}
		});
	}
	
	
	// 1. get shop_category and area from DB
	function getShopInitInfo() {
		$.getJSON(initUrl, function(data) {
			if (data.success) {
				var tempShopCategoryHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item, index) {
					tempShopCategoryHtml += '<option data-id="' + item.shopCategoryId
							+ '">' + item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(tempShopCategoryHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
	
	// 2. call controller and register a shop when submitted
	$('#submit').click(function() {
		var shop = {};
		if (isEdit) {
			shop.shopId = shopId;
		}
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();

		shop.shopCategory = {
			shopCategoryId : $('#shop-category').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
	
		shop.area = {
			areaId : $('#area').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
	
		//image file
		var shopImg = $('#shop-img')[0].files[0];
		var formData = new FormData();
		formData.append('shopImg', shopImg);
		formData.append('shopStr', JSON.stringify(shop));
		
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append('verifyCodeActual', verifyCodeActual);
		
		// call backend controller
		$.ajax({
			url : (isEdit ? editShopUrl : registerShopUrl),
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('Success！');
				} else {
					$.toast('Fail！' + data.errMsg);
				}
		
				$('#captcha_img').click();
			}
		});
	});
})