var actCode;
var results;
var operation;

if(location.toString().indexOf("?new") > 0){
	operation = 0;
}else if(location.toString().indexOf("?check") > 0){
	operation = 1;
}else{
	location = "/amp/page/campaign/campaign.html?back";
}

//涉及指标改变
function changeInvolveIndex(index){
	i = parseInt(index.value) * 10 + 1;
	var max = i + 10;
	var butType = $(index).parents("tr").find("select[name='buttonType']").html("");
	for(;i< max;i++){
		if(buttonTypes[i] != undefined){
			butType.append("<option value='"+i+"'>" + buttonTypes[i] + "</option>");
		}
	}
}//end 涉及指标改变


$(function(){
	var i =0;
	//后端人员列表
	var roleIds = ["1","9","10","11"];
	for(i=0;i<roleIds.length;i++){
		$.post("/amp/user/queryUserById.do",{roleId:roleIds[i]},function(data){
			if(data == '401'){
				location = "/amp/page/login/login.html";
			}
			for(i=0;i<data.length;i++){
				$("#after").append("<option value="+ data[i].user_id +">"+ data[i].real_name +"</option>");
			}
		});
	}
	
	//活动名称/活动编号
	actCode = getCookie("url_Acode");
	var actName = getCookie("url_Aname");
	
	if(actCode == null || actName == null){
		location = "/amp/page/campaign/campaign.html";
	}
	$("input[name='actCode']").val(actCode);
	$("input[name='actName']").val(actName);
	
	//新增URL变更
	if(operation == 0){
		$("input[name='buttonID']").attr("disabled","disabled");
		$(".comfirm_id").eq(1).css("display","none");
		$("button[name='comfirm_id']").eq(1).css("display","none"); //审核不通过按钮剔除
		//创建人
		$("#table_id1 tr").eq(3).find("td").eq(1).find("input").val(getCookie("userName"));
	}
	//URL变更审核初始化
	else if(operation == 1){
		$("#addtr").remove();
		$("#update_id").remove();
	}//end URL变更审核初始化
	else{
		location = "/amp/page/campaign/campaign.html?back";
	}
	
	setTimeout("initcheck()",300);
	
	//涉及指标
	for(i=1; i< involves.length; i++){
		$("select[name='involveIndex']").append("<option value='"+i+"'>" + involves[i] + "</option>");
	}
	
	/////////////////////////////////////涉及指标///////////////////////////////////////////////////////////////////////
	
	//按钮类别
	for(i=11;i<20;i++){
		if(buttonTypes[i] != undefined){
			$("select[name='buttonType']").append("<option value='"+i+"'>" + buttonTypes[i] + "</option>");
		}
	}
	
	var trHtml = $(".tab").html();
	$(".tab").html("");
	//添加一行按钮信息输入项
	$("#addtr").click(function(){
		$(".tab").append(trHtml);
	});
	
	//选择不同批次
	$("#updateBatchSelect").change(function(){
		var val = this.value;
		if(val == -1){
			$("#state").html("新增");
			$("input[name='createUser']").val(getCookie("userName"));
			$("textarea[name='memo']").val("");
			$("textarea[name='memo']").removeAttr("disabled");
			$("#after").removeAttr("disabled");
			$("textarea[name='mics']").val("");
			$("textarea[name='mics']").removeAttr("disabled");
			$("input[name='newUrl']").val("");
			$("input[name='newUrl']").removeAttr("disabled");
			$("#sj").val("");
			$("#sj").removeAttr("disabled");
			$("#comfirm_id").css("display","inline");
			$(".tab tr").remove();
			$("#addtr").css("display","inline");
			$("#result").css("display","none");
			$("#update_id").hide();
		}else{
			$("#addtr").css("display","none");
			if(operation == 0){
				$("#comfirm_id").css("display","none");
			}
			refresh(results[val]);
		}
	});
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//提交更改URL变更信息
$("button[name='comfirm_id']").click(function(){
		
		actCode = $("input[name='actCode']").val();
		var urlUpdateTime = $("input[name='urlUpdateTime']").val();
		var newUrl = $("input[name='newUrl']").val();
		var checkUserID = $("#after").val();
		var mics = $("textarea[name='mics']").val();
		var memo = $("textarea[name='memo']").val();
		var createUser = $("input[name='createUser']").val();
		
		var urlUpdateTimeReg = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/;
		if(!urlUpdateTimeReg.test(urlUpdateTime)){
			$("#message").html("生效时间不正确！");
			return false;
		}
	
		var urlEndReg = /^[\w#/]$/;
		if(newUrl.indexOf("http")!=0 || !urlEndReg.test(newUrl.charAt(newUrl.length - 1)) || newUrl.indexOf("<")>=0 || newUrl.indexOf(">") >= 0){
			
			$("#message").html("新链接格式不正确，请以http开头，并以 <字母,数字,/,#>结尾！");
			return false;
		}
		if(mics == ""){
			$("#message").html("短代码不能为空！");
			return false;
		}	
		if(checkUserID == "-1"){
			$("#message").html("请选择后端审核人员！");
			return false;
		}
	
		var butInfo = "";
		var buttonName,buttonID,buttonEvent,involveIndex,buttonType;
		var butInfoState = 1;	//用于判断每行按钮信息是否有效
		//按钮信息
		$(".tab tr").each(function(){
			buttonName = $(this).find("input[name='buttonName']").val().replace(/ /g,"");
			buttonID = $(this).find("input[name='buttonID']").val().replace(/ /g,"");
			buttonEvent = $(this).find("input[name='buttonEvent']").val().replace(/ /g,"");
			involveIndex = $(this).find("select[name='involveIndex']").val();
			buttonType = $(this).find("select[name='buttonType']").val();
			
			if((operation == 0 && (buttonName == "" || buttonEvent == ""))	//新增URL变更信息
					||
				(operation == 1 && buttonID == "" && this.value == 2) 	//审核URL变更信息
				){
					butInfoState = 0;
					return false;
			}
			
			
			//拼接按钮信息
			butInfo += buttonName;
			butInfo += "&,&";
			butInfo += buttonID;
			butInfo += "&,&";
			butInfo += buttonEvent;
			butInfo += "&,&";
			butInfo += involveIndex;
			butInfo += "&,&";
			butInfo += buttonType;
			butInfo += "],[";
		}); 
		
		if(butInfoState == 0 && operation == 0){
			$("#message").html("按钮名称以及按钮事件不能为空！");
			return false;
		}
		
		if(butInfoState == 0 && operation == 1){
			$("#message").html("按钮ID不能为空");
			return false;
		}
		
		var url;
		var params;
		if(location.toString().indexOf("new") > 0){
			url = "/amp/updateUrl/newUrl.do";
			params = {
				actCode:actCode,
				urlUpdateTime:urlUpdateTime,
				checkUserID:checkUserID,
				newUrl:newUrl,
				mics:mics,
				memo:memo,
				butInfo:butInfo,
				actName:actName,
			};
		}else if(operation == 1){
			url = "/amp/updateUrl/checkUrl.do";
			params = {
				updateBatch:$("#updateBatch").val(),
				checkState:this.value,
				butInfo:butInfo,
				actCode:actCode,
				actName:actName,
				createUser:createUser,
			};
		}else{
			$("#message").html("操作不合法");
			return false;
		}
		
		$.post(url,params,function(result){
			result = decodeURI(result);
			switch(result){
				case "1":
					setTimeout("initcheck()",100);
					result = "操作完成";
					break;
				case "500": 
					delCookie("url_Acode");
					delCookie("url_Aname");
					location = "/amp/page/error/error.html";
					break;
				case "400":
					delCookie("url_Acode");
					delCookie("url_Aname");
					location = "/amp/page/login/login.html";
					break;
				case "401":
					result = "已经退出登陆，请重新登陆！";
					break;
				default: break;
			}
			$("#message").html(result.replace(/<%2FBR>/g, "</BR>"));
		});//end post
		
	});//end 交更改URL变更信息

	$("#update_id").click(function(){
		updateTime();
	});
});

var initcheck = function(){
	$.post("/amp/updateUrl/findUrlByActCode.do",{actCode:actCode,operation:operation},function(result){
		
		if(result == "401" || result == "404"){
			location = "/amp/page/campaign/campaign.html?back";
		}
		
		
		results = result;
		var res = result[0];
		
		$("#updateBatchSelect option").remove();
		if(operation == 0){
			$("#updateBatchSelect").append("<option value='-1'>"+ "-----新增 -----"+"</option>");
		}else{
			refresh(res);
		}
		
		for(var i=0;i<result.length;i++){
			$("#updateBatchSelect").append("<option value='"+i+"'>"+ "修改批次:"+ result[i].updateBatch+"</option>");
		}
	});//end post
};//end function initcheck()

//更新页面数据
var refresh = function(res){
	//更改状态
	$("#state").html(updateURLState[res.updateState]);
	//修改批次
	$("#updateBatch").val(res.updateBatch);
	//创建人
	$("#table_id1 tr").eq(3).find("td").eq(1).find("input").val(res.createUser);
	//生效时间
	$("#sj").val(FormatDate2seconds(res.urlUpdateTime));
	//审核通过以及待审核有变更权限
	if(res.updateState == 1 || res.updateState == 2){
		$("#update_id").show();
		$("#sj").removeAttr("disabled");
	}else{
		$("#update_id").hide();
		$("#sj").attr("disabled","disabled");
	}
	
	//新链接
	$("input[name='newUrl']").val(res.urlUpdate);
	$("input[name='newUrl']").attr("disabled","disabled");
	//后端人员
	$("#after").val(res.checkUser);
	$("#after").attr("disabled","disabled");
	//短代码
	var mics = res.mics;
	var micsVal = "";
	var i = 0;
	var afterUrl = "";
	var dissupportClickMic = "";
	for(i=0;i<mics.length;i++){
		//根据审核状态状态，接口人能够看到需要媒体更换链接的点位
		if(operation == 0 ){
			if(mics[i].supportClick == 0){
				dissupportClickMic += mics[i].mic + "</BR>";
			}
			afterUrl += setAfterUrl(res['urlUpdate'],mics[i].mic) +"</BR>";
		}
		
		micsVal =micsVal + (mics[i].mic+"\n");
	}
	
	$("textarea[name='mics']").val(micsVal);
	$("textarea[name='mics']").attr("disabled","disabled");
	
	//将不支持点击的点位通知接口人，接口人再通知媒体更换URL
	$("#result div").eq(0).html("");
	if(afterUrl != ""){
		$("#result div").eq(0).html(afterUrl);
		$("#result").css("display","block");
		$("#result div").eq(1).html(dissupportClickMic);
	}else{
		$("#result").css("display","none");
	}
	
	//备注
	$("textarea[name='memo']").val(res['memo']);
	$("textarea[name='memo']").attr("disabled","disabled");
	
	$(".tab tr").remove();
	//按钮信息
	var buts = res.buttonInfo;
	var tds;
	for(i=0;i<buts.length;i++){
		
		//过滤无效按钮信息
		if(buts[i].buttonName == null){
			continue;
		}
		
		$("#addtr").click();
		
		tds = $(".tab tr").eq(i).find("td");
		
		tds.eq(0).find("input").val(buts[i].buttonName);
		tds.eq(0).find("input").attr("disabled","disabled");
		
		tds.eq(1).find("input").val(buts[i].buttonId);
		
		//状态不等于待审核，不让操作
		if(res.updateState != 1){
			tds.eq(1).find("input").attr("disabled","disabled");
			$("button[name='comfirm_id']").css("display","none");
		}else if(operation == 1){
			$("button[name='comfirm_id']").css("display","inline");
		}
		
		tds.eq(2).find("input").val(buts[i].buttonEvent);
		tds.eq(2).find("input").attr("disabled","disabled");
		tds.eq(3).find("select").val(buts[i].involveIndex);
		tds.eq(3).find("select").attr("disabled","disabled");
		if(buts[i].involveIndex == 2){
			changeInvolveIndex(tds.eq(3).find("select")[0]);
		}
		tds.eq(4).find("select").val(buts[i].buttonType);
		tds.eq(4).find("select").attr("disabled","disabled");
		tds.eq(5).find("button").attr("disabled","disabled");
	}//end for
	
	//状态不等于待审核，不让操作
	if(res.updateState != 1){
		$("button[name='comfirm_id']").css("display","none");
	}else if(operation == 1){
		$("button[name='comfirm_id']").css("display","inline");
	}
};//end refresh()

//设置后端URL
function setAfterUrl(url,mic){
	var index1 = url.indexOf("?");
	var index2 = url.indexOf("#");
	
	var sb = url;
	
	//原有的连接中存在WT.mc_id,并且WT.mc_id 在 #之前
	if((url.indexOf("WT.mc_id") > 0) && url.indexOf("WT.mc_id") < index2){
		var start  = url.indexOf("WT.mc_id") + 9;
		var end = url.indexOf("&",start);
		//WT.mc_id 在 URL中后面没有参数
		if(end == -1){
			end = url.indexOf("#",start);
			if(end == -1){
				end = url.length()-1;
			}
		}
		sb.replace(start, end + 1, mic);
	}else if(index1==-1 && index2==-1){
		sb+="?WT.mc_id=";
		sb+=mic;
	}else if(index1==-1 && index2 > -1){
		sb = insert(sb,index2,"?WT.mc_id="+mic);
	}else if(index1 > -1 && index2==-1){
		sb+="&WT.mc_id=";
		sb+=mic;
	}else{
		if(index1 > index2){
			sb = insert(sb,index2,"?WT.mc_id="+mic);
		}else{
			sb = insert(sb,index2,"&WT.mc_id="+mic);
		}
	}
	
	return sb;
}//end setAfterUrl()


function insert(str,index,newStr){
	var startTmp = str.substring(0,index);
	var endTmp = str.substring(index);
	
	return startTmp + newStr + endTmp;
}


function updateTime(){
	if($("#updateBatchSelect").val() == -1){
		return;
	}
	var updateBatch = results[0].updateBatch;
	var urlUpdateTime = $("input[name='urlUpdateTime']").val();
	var createUser = $("input[name='createUser']").val();
	
	$.post("/amp/updateUrl/update.do",
			{updateBatch:updateBatch,urlUpdateTime:urlUpdateTime,createUser:createUser},
			function(result){
				switch (result) {
				case '1':
					$("#message").html("已更改");
					break;
				case '2':
					$("#message").html("更改失败，时间不能早于当前时间5分钟!");
					break;
				default:
					$("#message").html("更改失败");
					break;
				}
	});
}