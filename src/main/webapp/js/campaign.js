/*******************************************************************************
 * 营销活动JS lp 2016-04-13
 * 
 * @param sdate
 * @param edate
 * 
 */

var startDateStr = '';
var endDateStr = '';
var unit = '';
var activityCode = '';

function init(sdate, edate) {
	startDateStr = $('#sdate_id').val();
	endDateStr = $('#edate_id').val();
	unit = $('#unit').val();
	activityCode = $('#activity_code').val();
	// 存入Cookie，方便回调
	setCookie('startDateStr', startDateStr);
	setCookie('endDateStr', endDateStr);
	setCookie('unit', unit);
	setCookie('activityCode', activityCode);

	if (location.toString().indexOf("back") == -1) {
		location = "/amp/page/campaign/campaign.html?back";
	}
	var da = new Array();
	$("#mb").css("display", "block");
	// 加载等待效果
	layer.load(2);
	$.post(
					"../../campaign/queryCampaignList.do",
					{
						'sdate' : startDateStr,
						'edate' : endDateStr,
						'unit' : unit,
						'activityCode' : activityCode
					},
					function(result, resultState) {
						if (resultState == "success") {
							// 关闭等待效果
							layer.closeAll();
							$("#mb").css("display", "none");
							if (result != "" && result != null) {
								for (i = 0; i < result.length; i++) {
									da[i] = {
										activityCode : result[i]["activityCode"],
										activityName : result[i]["activityName"],
										customerName : result[i]['customer']['customerName'],
										realityStartDate : FormatDate(result[i]["realityStartDate"]),
										activityEndDate : FormatDate(result[i]["activityEndDate"]),
										countMedia : result[i]["countMedia"],
										activityState : to_String(result[i]["activityState"]),
										realName : result[i]["realName"]
									};
								}
								$("#tbody_id").loadTemplate($("#template"),
										eval(da));
								$(".audi_id").attr("disabled", true);
								$(".audi_id")
										.css("background-color", "#B7B7B7");
								$(".url_id").attr("disabled", true);
								$(".url_id").css("background-color", "#B7B7B7");
								$
										.post(
												"../../campaign/getUserSession.do",
												{},
												function(data) {
													if (data.indexOf("接口人") >= 0) {
														$(".url_id").attr("disabled",false);
														$(".url_id").css("background-color","#3F97DD");
													}
													if (data.indexOf("后端支撑") >= 0) {
														$.post(
																		"../../campaign/getUpdateUrl.do",
																		{},
																		function(data) {
																			$.each(data,function(i,item) {
																				$("#tbody_id tr").each(
																												function() {
																													var a = $(this).find("td:first-child").text();
																													if (item.activityCode == a) {
																														var audi = $(this).find(".audi_id");
																														audi.attr("disabled",false);
																														audi.css("background-color","#3F97DD");
																													}
																												});
																							});
																		});
													}
												});
							} else {
								$("#tbody_id").html("<td colspan=9 id='sumtd'><span>未查询到相关数据!</span></td>");
							}

						} else {
							$("#tbody_id").html("<td colspan=9 id='sumtd'><span>未查询到相关数据!</span></td>");
						}
					});
	/*setPositionSta();*/
	timeOutLayer();
}
// 返回活动状态
function to_String(str) {
	if (str == "0") {
		return "待审核";
	} else if (str == "1") {
		return "具备上线类型";
	} else if (str == "2") {
		return "不具备上线类型";
	} else if (str == "3") {
		return "活动排期";
	} else if (str == "4") {
		return "确认排期";
	} else if (str == "5") {
		return "通知上线";
	}else if (str == "6") {
		return "已上线";
	}else if (str == "7") {
		return "已结束";
	}


}
function to_data(obj) {
	var context = $(obj).parents("tr").children("td").first().text();
	var realityStartDate =$(obj).parents("tr").children("td").eq(3).text();
	window.location.href = "./campMonitingData.html?activityCode=" + context+"&realityStartDate="+realityStartDate;
			+ "";
}

function downSch(a) {
	var actCode = $($(a).parents("tr").find("td")[0]).text();
	window.location.href = "./campSchedule.html?actCode="+actCode;
}

function closeLayer() {
	layer.close(pageii);
}

//URL变更
function urlUpdate(act) {
	setCookie("url_Acode",$(act).parents("tr").find("td").eq(0).text());
	setCookie("url_Aname",$(act).parents("tr").find("td").eq(1).text());
	window.location.href = "../check/updateurl.html?new";
}

function audit(act) {
	setCookie("url_Acode",$(act).parents("tr").find("td").eq(0).text());
	setCookie("url_Aname",$(act).parents("tr").find("td").eq(1).text());
	window.location.href = "../check/updateurl.html?check";

}
// 格式化时间
function FormatDate(strTime) {
	var date = new Date(strTime);
	return date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
			+ date.getDate();
}

jQuery(document).ready(function() {
	/* $("#template-nav").loadTemplate("navbar.html", {}); */
	// $(".footers").loadTemplate("../footer.html", {});
	$(".headers").loadTemplate("../../common/header.html", {});

	var date = new Date();
	var sdate = cal_date_s(date, 1);
	var edate = "";
	var channel = "全部";

	$("#sdate_id").val(sdate);
	$("#edate_id").val(edate);

	// 判断用户是否登录
	$.post("../../campaign/getUserSession.do", {}, function(data) {
		if (data == "error") {
			window.location.href = "../login/login.html";
		}
	});
	// 判断上个页面是否为同一个模块，否则，清理查询条件Cookie
	if (location.toString().indexOf("back") == -1) {
		delCookie("startDateStr");
		delCookie("endDateStr");
		delCookie("unit");
		delCookie("activityCode");
	}

	// 从cookie中读取查询条件，作为回显
	startDateStr = getCookie("startDateStr");
	endDateStr = getCookie("endDateStr");
	unit = getCookie("unit");
	activityCode = getCookie("activityCode");

	if (activityCode != "" && activityCode != null) {
		$("#activity_code").val(activityCode);
	}
	if (startDateStr != "" && startDateStr != null) {
		$("#sdate_id").val(startDateStr);
	}
	if (endDateStr != "" && endDateStr != null) {
		$("#edate_id").val(endDateStr);
	}
	if (unit != "" && unit != null) {
		$("#unit").val(unit);
	}
	// 查询
	init(sdate, edate);
	var nowDate = FormatDate(new Date());
	$("#query_id").on("click", function() {

		var _sdate = $("#sdate_id").val();
		var _edate = $("#edate_id").val();
		if (_edate != "" && _edate != null) {
			if (_edate < _sdate) {
				layer.tips('开始时间不能大于结束时间', '#edate_id', {
					tips : [ 1, '#FF0000' ]
				});
				return false;
			}
		}
		init(sdate, edate);
	});

	// add by LiuJie
	$('[data-toggle="popover"]').popover({
		'animation' : true,
		'trigger' : 'hover',
	});

	docScroll();
	//缺少req定义
	var req = /^[a-zA-Z0-9]+$/;
	$("#activity_code").on("blur", function() {
		var activitycode = $("#activity_code").val();
		if (activitycode != "" && activitycode != null) {
			if (!req.test(activitycode)) {
				layer.tips('请输入数字和英文组合', '#activity_code', {
					tips : [ 2, '#FF0000' ]
				});
				$("#activity_code").val("");
			} else if (activitycode.length > "20") {
				layer.tips('活动编号不能超过20位', '#activity_code', {
					tips : [ 2, '#FF0000' ]
				});
				$("#activity_code").val("");
			} else {
				$.post("../../campaign/QueryActivitycode.do", {
					'activitycode' : activitycode,
				}, function(data) {
					if (data == "0") {
						layer.tips('活动编号不存在', '#activity_code', {
							tips : [ 2, '#FF0000' ]
						});
					} else {
						
					}
				});
			}
		}

	});

	$("#unit").on("blur", function() {
		var unit = $("#unit").val();
		if (unit != "" && unit != null) {
			if (unit.length > "10") {
				layer.tips('投放单位长度应<=10', '#unit', {
					tips : [ 2, '#FF0000' ]
				});
				$("#unit").val("");
			} else {
				$.post("../../campaign/QueryUnit.do", {
					'unit' : unit,
				}, function(data) {
					if (data == "0") {
						layer.tips('投放单位不存在', '#unit', {
							tips : [ 2, '#FF0000' ]
						});
					}
				});
			}
		}
	});
});