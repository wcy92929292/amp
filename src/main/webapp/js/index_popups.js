/****
 * 省份活动展示  lp  2016-04-26
 */

//获取滚动条当前的位置 
function getScrollTop() {
	var scrollTop = 0;
	if (document.documentElement && document.documentElement.scrollTop) {
		scrollTop = document.documentElement.scrollTop;
	} else if (document.body) {
		scrollTop = document.body.scrollTop;
	}
	return scrollTop;
}

function init(sdate, cust) {
	// 加载等待效果
	layer.load(2);
	$.post("../../index/index.do", {
		'sdate' : sdate,
		'cust' : cust,
	}, function(data) {
		//关闭等待效果
		layer.closeAll();
		if (data != '' && data != null) {
			$("#tbody_id").loadTemplate($("#template"), eval(data));
		} else {
			$("#tbody_id").html(
					"<td colspan=4 id='sumtd'><span>未查询到相关数据!</span></td>");
		}
	});
}

jQuery(document).ready(function() {
	$(".headers").loadTemplate("../../common/header.html", {});
	//判断是否已经登录
	$.post("../../campaign/getUserSession.do", {}, function(data) {
		if (data == "error") {
			window.location.href = "../login/login.html";
		}
	});
	var date = new Date();
	var sdate = cal_date_s(date, 1);
	//获取URL后面参数
	var url = document.location.href;
	var sub1 = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
	var sub2 = url.substring(url.lastIndexOf("=") + 1);
	var cust = decodeURIComponent(sub2);
	init(sdate, cust);
	$("#cancal_id").on("click", function() {
		window.location.href = "./index.html";
	});
	//add by LiuJie
	$('[data-toggle="popover"]').popover({
		'animation' : true,
		'trigger' : 'hover',
	});

	// add by LiuJie
	$(document).scroll(function() {
		var x = 220;
		if (getScrollTop() > x) {
			var m = $('#thead_1').html();
			$('#thead_2').empty().html(m);
			$('#div_1').show();

			$('[data-toggle="popover"]').popover({
				'animation' : true,
				'trigger' : 'hover',
			});
		} else {
			$('#div_1').hide();
		}
		return;
	})

});