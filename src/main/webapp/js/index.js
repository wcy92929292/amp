/****
 * 首页JS   lp   2016-04-26
 * 
 */
var cusArr = new Array();
jQuery(document).ready(function() {
	$(".headers").loadTemplate("../../common/header.html", {});
	//判断是否已经登录
	$.post("../../campaign/getUserSession.do", {}, function(data) {
		if (data == "error") {
			window.location.href = "../login/login.html";
		}
	});
	var datetime = new Date();
	var sdate = cal_date_s(datetime, 1);
	var cust = '';
	$.post("../../index/index.do", {
		'sdate' : sdate,
		'cust' : cust,
	}, function(data,status) {
		if(status == "success"){
			$(".zxy_mask").remove();
		}
		
//		$("#error").show();
		$(".div_left_content1").attr("style","");
		if (data != "" && data != null) {
			$(".div_left_content1").show();
			dataFor: for (var i = 0; i < data.length; i++) {
				//循环遍历按照客户归类 Start
				for (var j = 0; j < cusArr.length; j++) {
					if (cusArr[j].customer == data[i].customerName) {
						cusArr[j].pnum += parseInt(data[i].pnum);
						cusArr[j].pv += parseInt(data[i].pv);
						cusArr[j].acnum += 1;
						continue dataFor;
					}
				}
				var o = new Object();
				o.customer = data[i].customerName;
				o.pnum = parseInt(data[i].pnum);
				o.acnum = 1;
				o.pv = parseInt(data[i].pv);
				cusArr.push(o);

			}
			//end
			//計算總數
			var countCust = 0;
			var countPnum = 0;
			var countPv = 0;
			var countAcnum = 0;
			for (var j = 0; j < cusArr.length; j++) {
				countCust += 1;
				countPnum += cusArr[j].pnum;
				countPv += cusArr[j].pv;
				countAcnum += cusArr[j].acnum;
			}//end
			$("#countCust").html(countCust);
			$("#pronum").html(countPnum);
			$("#pv").html(countPv);
			$("#actnum").html(countAcnum);
			$("#dss").html($("#template").tmpl(cusArr));
		} else {
			$("#error").show();
		}
	});
});
//跳转详细
function to_popup(obj) {
	var cust = $(obj).children("ul").children("li").eq(0).text();
	window.location.href = "./index_popups.html?cust="
			+ encodeURIComponent(cust) + "";
}