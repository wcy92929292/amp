//判断是否已经登录
$("head").append($("<script src='/amp/js/cookie.js'>"));

var realName = getCookie("realName");
var role = getCookie("roleName");
var local = location.toString();

//if(realName == null || 'null' == realName || role == null || 'null' == role){
	$.post("/amp/amp/isLogin.do",{},function(data){
		
		if(data == null || 'null' == data){
			location = "/amp/page/login/login.html";
		}
			setCookie("realName",data);
	});
//};



var pages = new Array();
pages["概览"]="/index/index.html";
pages["监测一览页面"]="/monitorPlan/monitorPlan.html";
pages['监测方案审核']="monitorPlan/auditMonitorPlan.html";
pages['添加监测方案']="/monitorPlan/addMonitorPlan.html";
pages['修改监测方案']="/monitorPlan/modifyMonitorPlan.html";
pages['上线核查']="/check/check.html";
pages['实时数据']="/check/realData.html";
pages['营销活动']="/campaign/campaign.html";
pages['营销活动监测数据']="/campaign/campMonitingData.html";
pages['营销活动排期信息']="/campaign/campSchedule.html";
pages['URL变更']="/check/updateurl.html?new";
pages['URL变更审核']="check/updateurl.html?check";
pages['报告导出']="/page/schedule/exportReport.html";
pages['排期插码']="/schedule/mutiScheduleUpload.html";
pages['查询用户']="/user/queryUser.html";
pages['编辑用户']="/user/editUser.html";
pages['添加用户']="/user/addUser.html";
pages['todo']="/user/mytodo.html";
pages['监测备注']="/monitorPlan/memoMonitorPlan.html";
pages['概览问题']="index/index_popups.html";
pages['活动详细信息']="monitorPlan/monitorPlanDetail.html";
pages['后端数据导入']="/afterData/ButtonPop.html";


function isOk(page){
	for(var i=0;i<page.length;i++){
		if(local.indexOf(pages[page[i]]) >= 0 ){
			return true;
		};
	}
	return false;
}

var ok = false;
if(role.indexOf("接口人") >= 0){
	ok = isOk(["监测一览页面","概览","上线核查","营销活动","URL变更","添加监测方案","修改监测方案","营销活动监测数据","编辑用户","报告导出","概览问题",
		      "排期插码","营销活动排期信息","todo","监测备注","实时数据","活动详细信息"]);
}
if(!ok && role.indexOf("前端支撑") >= 0){
	ok = isOk(["监测一览页面","概览","上线核查","营销活动","营销活动监测数据","编辑用户","报告导出","排期插码","营销活动排期信息","概览问题","todo","实时数据","活动详细信息","监测备注"]);
}
if(!ok && role.indexOf("后端支撑") >= 0){
	ok = isOk(["监测一览页面","概览","上线核查","营销活动","营销活动监测数据","编辑用户","URL变更审核","监测方案审核","概览问题","todo","实时数据","活动详细信息","监测备注","后端数据导入"]);
}
if(!ok && role.indexOf("监测中心") >= 0){
	ok = isOk(["上线核查","监测一览页面","概览","营销活动","报告导出","编辑用户","概览问题","todo","实时数据","活动详细信息"]);
}
if(!ok && role.indexOf("管理员") >= 0){
	ok = isOk(["监测一览页面","概览","查询用户","编辑用户","添加用户","营销活动","概览问题","todo","活动详细信息"]);
}
if(!ok && role.indexOf("客户") >= 0){
	ok = isOk(["报告导出","编辑用户","概览","监测一览页面","营销活动","上线核查","实时数据","营销活动排期信息","活动详细信息","概览问题"]);
}
if(!ok){
	history.go(-1);
}

