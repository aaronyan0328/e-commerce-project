$(function() {
	getlist();
	
	function getlist(e) {
		$.ajax({
			url : "/online-store/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}
	
	function handleUser(data) {
		$('#user-name').text(data.name);
	}

	function handleList(data) {
		var html = '';
		data.map(function(item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
					+ item.shopName + '</div><div class="col-40">'
					+ shopStatus(item.enableStatus)
					+ '</div><div class="col-20">'
					+ goShop(item.enableStatus, item.shopId) + '</div></div>';

		});
		$('.shop-wrap').html(html);
	}

	function shopStatus(status) {
		if (status == 0) {
			return 'CHECKING';
		} else if (status == -1) {
			return 'OFFLINE';
		} else if (status == 1) {
			return 'SUCCESS';
		} else if (status == 2) {
			return 'PASS';
		}
	}

	function goShop(status, id) {
		if (status == 1) {
			return '<a href="/online-store/shopadmin/shopmanagement?shopId=' + id
					+ '">Manage Shop</a>';
		} else {
			return '';
		}
	}
});