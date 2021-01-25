$(function() {
	var shopId = getQueryString('shopId');
	var shopInfoUrl = '/online-store/shopadmin/getshopmanagementinfo?shopId=' + shopId;
	$.getJSON(shopInfoUrl, function(data) {
		if (data.redirect) {
			window.location.href = data.url;
		} else {
			if (data.shopId != undefined && data.shopId != null) {
				shopId = data.shopId;
			}
			$('#shopInfo')
					.attr('href', '/online-store/shopadmin/shopoperation?shopId=' + shopId);
		}
	});
});