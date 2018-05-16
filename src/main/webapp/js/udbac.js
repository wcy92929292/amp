/****
 * 格式化开始日期
 * @param date 传入的日期
 * @param type 1，2,3 表示返回不同的日期格式
 * @returns
 */
function cal_date_s(date, type) {
	var r_date;//return
	//返回格式为 yyyy-MM-dd
	if (type == "1") {
		var date = new Date();
		 date.setTime(date.getTime());
		r_date = date.pattern("yyyy-MM-dd");
	}
	//返回格式为 yyyy-MM-dd HH
	else if (type == "2") {
		r_date = date.pattern("yyyy-MM-dd HH");
	}
	//返回格式为 yyyy-MM-dd HH:mm
	else if (type == "3") {
		//小时减5个时辰
		var data = date.setTime(date.getTime() - 2000 * 60 * 60 * 12);
		r_date = date.pattern("yyyy-MM-dd HH:mm");
	}else if(type=="4"){
		var date = new Date();
		 date.setTime(date.getTime() - 1000 * 60 *60 *24 * 30);
		r_date = date.pattern("yyyy-MM-dd");
	} 	
	//返回格式为 yyyy-MM-dd
	else if (type == "5") {
		//减一天
		var data = date.setTime(date.getTime() - 1000 * 60 * 60 * 24);
		r_date = date.pattern("yyyy-MM-dd");
	}else {
		r_date = date.pattern("yyyy-MM-dd");
	}
	//console.log(r_date)
	return r_date;
}
/****
 * 格式化结束日期
 * @param date 传入的日期
 * @param type 1，2,3 表示返回不同的日期格式
 * @returns
 */
function cal_date_e(date, type) {
	var r_date;//return
	//返回格式为 yyyy-MM-dd
	if (type == "1") {
		var date = new Date();
		date.setTime(date.getTime());
		r_date = date.pattern("yyyy-MM-dd");
	} 
	//返回格式为 yyyy-MM-dd HH
	else if (type == "2") {
		r_date = date.pattern("yyyy-MM-dd HH");
	}
	//返回格式为 yyyy-MM-dd HH：mm
	else if (type == "3") {
		r_date = date.pattern("yyyy-MM-dd HH:mm");
	}else 
		//返回前一天
		if (type == "4") {
			var date = new Date();
			date.setTime(date.getTime() - 1000 * 60 * 60 * 24);
			r_date = date.pattern("yyyy-MM-dd");
		} 
	else {
		r_date = date.pattern("yyyy-MM-dd");
	}
	//console.log(r_date)
	return r_date;
}

// 格式化
Date.prototype.pattern = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, // 小时
		"H+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	var week = {
		"0" : "\u65e5",
		"1" : "\u4e00",
		"2" : "\u4e8c",
		"3" : "\u4e09",
		"4" : "\u56db",
		"5" : "\u4e94",
		"6" : "\u516d"
	};
	if (/(y+)/.test(fmt)) {
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	if (/(E+)/.test(fmt)) {
		fmt = fmt
				.replace(
						RegExp.$1,
						((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f"
								: "\u5468")
								: "")
								+ week[this.getDay() + ""]);
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
		}
	}
	return fmt;
}
function formatdate(strTime){//YYYY-MM-DD 
	var d = new Date(strTime);
	var year=d.getFullYear();
	var day=d.getDate();
	var month=+d.getMonth()+1;
	var hour=d.getHours();
	var minute=d.getMinutes();
	var second=d.getSeconds();
	var misec=d.getMilliseconds();
	var f=year+"-"+formate(month)+"-"+formate(day)+" "+formate(hour)+":"+formate(minute);
	return f;
 }
	 function formate(d){
		 return d>9?d:'0'+d;
	 }

// 页面置顶与置底 zhangchengtong 20150825
// 添加判断是否是indix 为fasle时执行置顶和置底 zhangshuru 20151013
$(function() {
	$("#template-nav").loadTemplate("../../common/navbar.html", {});
	var aname = location.href;
	var bname = aname.split("/");
	var cname = bname.slice(bname.length - 1, bname.length);

	if (cname != "index.html") {
		$(document.body)
				.append(
						'<div id="updown"><span class="up"></span><span class="down"></span></div>');
		$("#updown").css("top", window.screen.availHeight / 2 - 100 + "px");
		/**
		 * 
		
		$(window).scroll(function() {
			if ($(window).scrollTop() >= 100) {
				$('#updown').fadeIn(300);
			} else {
				$('#updown').fadeOut(300);

			}
		}); */
		$('#updown .up').click(function() {
			$('html,body').animate({
				scrollTop : '0px'
			}, 800);
		});
		$('#updown .down').click(function() {
			$('html,body').animate({
				scrollTop : document.body.clientHeight + 'px'
			}, 800);
		});
		
	}
/*	setTimeout(function() {

		var date=new Date();
		var datetime=formatdate(date);
		$("#datetimes").html(formatdate(date));
		var userName= $.cookie('userName');
		$("#currentUser").html(userName);

	}, 0);*/
});
/*表单验证 by linying*/
function formCheck(){
	var demo=$("#formId").Validform({
		tiptype:function(msg,o,cssctl){
			if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
				var objtip=o.obj.siblings(".Validform_checktip");
				cssctl(objtip,o.type);
				objtip.text(msg);
			}
		}
	});  
	return demo;
}
/*获取当前页面的路径 ，并且提取出页面Name 调用getHtmlhref()*/
var href = window.location.href;
var stringHref = href.split("/");
var a = stringHref ;
var a_i;
function getHtmlhref(){
  for(a_i in a){
    if(a[a_i].indexOf('.html')>=0){
      return a[a_i];
    }
  }
}
/*
function getHtmlsrc(){
  for(a_i in b){
    if(b[a_i].indexOf('.html')>=0){
      return b[a_i];
    }
  }
}*/
var resultJson;
var eccp_uid = '';
var eccp_prov = '';
var eccp_chn = '';

// 获取session中的用户信息 （与电子渠道对接时用到） by ningyexin and linying 20150927
function getSessionInfos(url) {
	$.ajax({
		type : "GET",
		url : url + "http_request/getSessionInfos.do",
		cache : false,
		async : false,
		success : function(xmlobj) {
			//var xmlobj = "{eccp_uid:'***',eccp_prov:'***',eccp_chn:'***'}";
			resultJson = eval("(" + xmlobj + ")");

			eccp_uid = resultJson.eccp_uid;
			eccp_prov = resultJson.eccp_prov;
			eccp_chn = resultJson.eccp_chn;
			// alert(resultJson.eccp_prov);
			//console.log("用户id:" + eccp_uid + "省份:" + eccp_prov + "渠道:"
					//+ eccp_chn);
		}

	});
	return resultJson;
}
// 判断滚动条的位置，设置底部是否显示 by linying 20151014
/*function setPositionSta() {
	if (document.documentElement.clientHeight < document.documentElement.offsetHeight) {
		$(".footers").css({
			'position' : 'static'
		});
		$(".contain").css({
			'padding-bottom' : '0'
		});
	} else {
		$(".footers").css({
			'position' : 'fixed'
		});
		$(".contain").css({
			'padding-bottom' : '95px'
		});
	}
	
}*/
/*******************************************************************************
 * 指定计时器判断是否已经30秒内查询出结果，如果没有结果进行系统提示并提供选择 by ningyexin linying 2015-10-17
 */
function timeOutLayer() {
	setTimeout(function() {

		if ($("#mb").css("display") != 'none') {
			// layer 提示框
			layer.confirm('很抱歉，当前查询数据较大是否继续等待，或返回首页？', {
				btn : [ '继续等待', '返回首页' ]
			// 按钮
			}, function() {
				// 继续等待的处理
				layer.msg('请继续等待！');
			}, function() {
				// 返回首页
				window.location.href = '../../../index.html';
			});
		}

	}, 30000);
}

//获取滚动条当前的位置 
function getScrollTop() { 
	var scrollTop = 0; 
	if (document.documentElement && document.documentElement.scrollTop) { 
		scrollTop = document.documentElement.scrollTop; 
	} 
	else if (document.body) { 
		scrollTop = document.body.scrollTop; 
	} 
	return scrollTop; 
} 

function docScroll(){
	// add by LiuJie
	$(document).scroll(function(){ 
		var x = 220;
		if(getScrollTop()>x){
			var m =$('#thead_1').html();
			$('#thead_2').empty().html(m);
			$('#div_1').show();

			$('[data-toggle="popover"]').popover({
				'animation':true,
				'trigger':'hover',
			});
		}
		else{
			$('#div_1').hide();
		}
		return;
	});
}

$(document).scroll(function(){
	
	/**
	if($(document).scrollTop() > 0 && $(document).scrollTop() < 70){
		var y2 = $(document).scrollTop();
		var y = 70 - y2;
		$(".menuBox").css({'top': y + 'px'});
	}else if($(document).scrollTop() > 130){
		$(".menuBox").css({'top': '0px'});
	}else if( $(document).scrollTop() == 0 ){
		$(".menuBox").css({'top': '70px'});
	}
	*/
	return;
});	

//用户退出方法 
function loginOut(){
	$.post("../../amp/loginout.do", {
	}, function(data) {
		window.location.replace = '../../login/login.html';
	//console.log()
	});
}

/***
 * 获取地址栏参数
 * @param name 要获取的名称
 * @author lily
 * @returns
 */
function getUrlParam(name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]);
	return null; //返回参数值
}
function getUrlActName(actName)
{
	var reg = new RegExp("(^|&)"+ actName +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]);
	return null; //返回参数值
}
function getUrlActCus(actCus)
{
	var reg = new RegExp("(^|&)"+ actCus +"=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r!=null) return unescape(r[2]);
	return null; //返回参数值
}
//格式化日期
function FormatDate (strTime) {
    var date = new Date(strTime);
    return date.getFullYear()+"-"+
    (date.getMonth()+1 < 10 ? "0" : "") + (date.getMonth()+1)+"-"+
    (date.getDate()+1 < 10 ? "0" : "") + date.getDate();
}
//
function FormatDate2seconds (strTime) {
    var date = new Date(strTime);
    return 	date.getFullYear()+"-"+
    		((date.getMonth()+1) < 10 ? "0" :"")+ (date.getMonth()+1) +"-"+
    		((date.getDate()) < 10 ? "0" : "") + date.getDate()+" "+
    		((date.getHours()) < 10 ? "0" : "") + date.getHours()+":"+
    		((date.getMinutes()) < 10 ? "0" : "") + date.getMinutes()+":"+
    		((date.getSeconds()) < 10 ? "0" : "") + date.getSeconds();
}

/************/
//日期格式化
Date.prototype.Format = function(fmt) { // author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]): (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

/*************/
var format = function(time, format){
    var t = new Date(time);
    var tf = function(i){return (i < 10 ? '0' : '') + i;};
    return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
        switch(a){
            case 'yyyy':
                return tf(t.getFullYear());
                break;
            case 'MM':
                return tf(t.getMonth() + 1);
                break;
            case 'mm':
                return tf(t.getMinutes());
                break;
            case 'dd':
                return tf(t.getDate());
                break;
            case 'HH':
                return tf(t.getHours());
                break;
            case 'ss':
                return tf(t.getSeconds());
                break;
        }
    });
};
