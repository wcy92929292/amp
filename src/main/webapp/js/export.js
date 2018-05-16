/**
 * 依次加载列表下拉元素的值
 */
$(function(){
	day();
	week();
	month();
	pinci();
	jiean();
	leiji();
	isRed();
	isRedLeiJi();
	queryALLCustomer();
	getCustomer();
	region();
	setTimeout("sendPost()",100);
	pz();
	endPoints();
	dataCaliber();
});

function sendPost(){
	$.post("/amp/weekExport/getUserInfoSeesion.do", {},function(data) {
        if(data == "error") { //用户未登录的情况
            location.href = "../login/login.html";
        }else{
        	var res = eval('(' + data + ')');
        	
        	if(res.userRoleId == "6"){ //角色是客户
        		
        		$("select").find("option").each(function(){ //日报
					if(this.innerText ==  res.customerName){
						$(this).attr("selected","selected");
						$(this).parent().attr("disabled","disabled");
					}		
				});
        		
        		//包含集团或咪咕的
        		if(res.customerName.indexOf("集团") != -1 || res.customerName.indexOf("咪咕") != -1){
        			$("#pinci").remove();
        		}else{//除此之外的客户
        			$("#dayEnd").remove();
        			$("#dayEndHidden").remove();
        		}
        		
        	}
        	
        }
    }); 
};
/**
 * 
 * 日报数据
 */
function day(){
	date = $(".calendar input").val();
	$("#beginDate").focus(function() {
		if ($(this).val() == date) {
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd")) {
			$("#day_msg").html("开始日期，不能大于当前时间");
		} else {
			date = $(this).val();
			$("#day_msg").html("");
		}
	});
	$("#endDate").focus(function() {
		if ($(this).val() == date) {
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd")) {
			$("#day_msg").html("结束日期，不能大于当前时间");
		} else {
			date = $(this).val();
			$("#day_msg").html("");
		}
	});
	$("#beginShijian").focus(function() {
		if ($(this).val() == date) {
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd HH")) {
			$("#day_msg").html("开始时间，不能大于当前时间");
		} else {
			date = $(this).val();
			$("#day_msg").html("");
		}
	});
	$("#endShijian").focus(function() {
		if ($(this).val() == date) {
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd HH")) {
			$("#day_msg").html("结束时间，不能大于当前时间");
		} else {
			date = $(this).val();
			$("#day_msg").html("");
		}
	});
	$("input[name='sysTime']").change( function() {
		var radia=$("input[name='sysTime']:checked").val();
		if(radia==1){//选择日期为当天时
			$("#beginDate").attr("disabled", true); 
			$("#endDate").attr("disabled", true); 
			$("#beginDate").val("");
        	$("#endDate").val("");
			$("#riqi").css("display","block");
			$("#shijian").css("display","none"); 
			$("#micDay").css("display","none");
		}else if(radia==2){//按小时筛选的条件
			$("#beginDate").attr("disabled",false);
			$("#endDate").attr("disabled",false);
			
			$("#micDay").css("display","block");
			$("#riqi").css("display","none");
			$("#shijian").css("display","block");
		  }else{
			  $("#beginDate").attr("disabled",false);
			  $("#endDate").attr("disabled",false);
			  $("#riqi").css("display","block");
			  $("#shijian").css("display","none"); 
			  $("#micDay").css("display","none");
		  }
		});
	
		$("input[name='jumpIf']").change( function() {
			var radia=$("#jumpIf")[0].checked ? 0 : 1;
			if(radia==1){
				$("#jumpRang").attr("disabled", true);
				$("#jumpRang").val("");
			}else{
				$("#jumpRang").attr("disabled",false);
			}
			});
		$("input[name='ctrIf']").change( function() {
			var radia=$("#ctrIf")[0].checked ? 0 : 1;
			if(radia==1){
				$("#ctrRangL").attr("disabled", true);
				$("#ctrRangM").attr("disabled", true);
				$("#ctrRangM").val("");
				$("#ctrRangL").val("");
			}else{
				$("#ctrRangL").attr("disabled", false);
				$("#ctrRangM").attr("disabled", false);
			}
			});
		$("input[name='bgIf']").change( function() {
			var radia=$("#bgIf")[0].checked ? 0 : 1;
			if(radia==1){
				$("#bgwcRangL").attr("disabled", true);
				$("#bgwcRangM").attr("disabled", true);
				$("#bgwcRangL").val("");
				$("#bgwcRangM").val("");
			}else{
				$("#bgwcRangL").attr("disabled", false);
				$("#bgwcRangM").attr("disabled", false);
			}
			});
		$("input[name='djIf']").change( function() {
			var radia=$("#djIf")[0].checked ? 0 : 1;
			if(radia==1){
				$("#djwcRangL").attr("disabled", true);
				$("#djwcRangM").attr("disabled", true);
				$("#djwcRangL").val("");
				$("#djwcRangM").val("");
			}else{
				$("#djwcRangL").attr("disabled", false);
				$("#djwcRangM").attr("disabled", false);
			}
			});
		
	
	$(".btn_day").click(function() {
		 $("#day_msg").html("");
		var sysTime=$("input[name='sysTime']:checked").val();
		//var customerName=$("#customer_id").find("option:selected").text();//获得当前的投放单位
		var customerName=$("#customer_id_input").val();
		//存放的是投放单位的编号的
		var str="";  
		$("[name='checkbox']:checked").each(function(){  
		str+="'"+$(this).val()+"',"; 		  
		});
		
		var beginDate=""; 
		var endDate="";
		var d1 =""; 
		var d2 ="";   
		//var customer_id=$("#customer_id_input").val();//投放单位的客户信息
		var customer_id=str;
		var actName = $("#actNameDay").val();//点击一次就会取得该值
		var actCode = $("#actCodeDay").val(); 
		var mics=$("#mics").val(); //营销识别码
		var jumpRang=$("#jumpRang").val();//跳出率界
		var ctrRangL=$("#ctrRangL").val();//ctr界下
		var ctrRangM=$("#ctrRangM").val();//ctr界上
		var wcRangL=$("#bgwcRangL").val();//完成率界下
		var wcRangM=$("#bgwcRangM").val();//完成率界上
		var djwcRangL=$("#djwcRangL").val();//点击完成率界下
		var djwcRangM=$("#djwcRangM").val();//点击完成率界上
		var jumpIf =$("#jumpIf")[0].checked ? "1" : "0";
		var ctrIf =$("#ctrIf")[0].checked ? "1" : "0";
		var bgIf =$("#bgIf")[0].checked ? "1" : "0";
		var djIf =$("#djIf")[0].checked ? "1" : "0";
		  if(sysTime==1){
			  //选择当天的日期
			}else if(sysTime==2){
				beginDate=$("#beginShijian").val(); 
				endDate=$("#endShijian").val(); 
				d1=new Date(beginDate.replace(/\-/g, "\/"));  
				d2=new Date(endDate.replace(/\-/g, "\/"));
			  //选择其他日期的时候，控制投放单位
				 if(customer_id==""){
					 $("#day_msg").html("请选择投放单位！");
					  return false;
				  }
			}else{
				beginDate=$("#beginDate").val(); 
				endDate=$("#endDate").val(); 
				d1=new Date(beginDate.replace(/\-/g, "\/"));  
				d2=new Date(endDate.replace(/\-/g, "\/"));
				if(customer_id==""){
					 $("#day_msg").html("请选择投放单位！");
					  return false;
				  }
			}
		   if(beginDate!=""&&endDate!=""&&d1 >d2) {//判断开始日期不能大于结束日期，小于等于结束时间
		  $("#day_msg").html("开始时间不能大于结束时间！");
		   return false;  
		  }
		   if(endDate!=""&&beginDate==""){
			   $("#day_msg").html("请填写开始时间！");
				   return false;  
			} 
		   if(ctrRangL!=""&&ctrRangM!=""&&Number(ctrRangL) >Number(ctrRangM)) {//判断开始日期不能大于结束日期，小于等于结束时间
				  $("#day_msg").html("ctr下界不能高于上界！");
				   return false;  
				  }
		   if(wcRangL!=""&&wcRangM!=""&&Number(wcRangL) >Number(wcRangM)) {//判断开始日期不能大于结束日期，小于等于结束时间
				  $("#day_msg").html("曝光完成率下界不能高于上界！");
				   return false;  
				  }
		   if(djwcRangL!=""&&djwcRangM!=""&&Number(djwcRangL) >Number(djwcRangM)) {//判断开始日期不能大于结束日期，小于等于结束时间
				  $("#day_msg").html("点击完成率下界不能高于上界！");
				   return false;  
				  }
			if(ctrRangL!=""&&ctrRangM==""|| ctrRangM!=""&&ctrRangL=="" || wcRangL!=""&&wcRangM=="" ||wcRangM!=""&&wcRangL=="" || djwcRangL!=""&&djwcRangM=="" ||djwcRangM!=""&&djwcRangL==""){
					   $("#day_msg").html("上下界须同时填写！");
						   return false;  
					}

		   if( $("#exportDay input").attr('class') == "long_button_style" ){
			   
			   $("#exportDay input").removeClass().addClass('disable_button_style audit_id');
			   
			   $.post("/amp/export/sumDay.do",{
				   'date':beginDate,
				   'actName':actName,
				   'actCode':actCode,
				   'endDate':endDate,
				   'customer_id':customer_id,
				   'sysTime':sysTime,
				   'mic':mics,
				   'jumpRang':jumpRang,
				   'ctrRangL':ctrRangL,
				   'ctrRangM':ctrRangM,
				   'wcRangL':wcRangL,
				   'wcRangM':wcRangM,
				   'djwcRangL':djwcRangL,
				   'djwcRangM':djwcRangM,
				   },function(data){
					if(data==2){
						  $("#day_msg").html("数据结果为0条！");
						  $("#exportDay input").removeClass().addClass('long_button_style');
					}else{
						$("#dayId").attr("action","/amp/export/exportDay.do?date="+beginDate+"&actName="
						+ actName + "&actCode=" + actCode+"&customerName="+customerName+"&endDate="+endDate+"&customer_id="+customer_id+"&sysTime="+sysTime+"&mic="+mics+
						"&jumpRang="+jumpRang+"&ctrRangL="+ctrRangL+"&ctrRangM="+ctrRangM+"&wcRangL="+wcRangL+"&wcRangM="+wcRangM+"&djwcRangL="+djwcRangL+"&djwcRangM="+djwcRangM);
						 $("#dayId").submit();
						}
					
					$("#exportDay input").removeClass().addClass('long_button_style');
				});
		   }
		    
			   
	});
	
}
//客户角色id为6的，并得到当前的省id
function queryCustomerProvince(province) {
	var cus = $("select[name='customer_ids']").find("option:selected").text();
	$.post('/amp/export/queryCustomer.do', {"customer_id" : cus,"province":province}, function(data) {
		for (var i = 0; i < data.length; i++) {
			$("select[name='customer_ids']").append("<option value=" + data[i]['customer_id'] + ">"+ data[i]['customer_name'] + "</option>");
			$("select[name='customer_ids']").find("option").eq(i + 1).attr("code",data[i]['customer_code']);
		}
	});
}

//查询所有的投放单位的信息
function queryALLCustomer() {
	var cus = $("select[name=customer_ids]").find("option:selected").text();
	$.post('/amp/amp/queryCustomer.do', {
		"customer_id" : cus
	}, function(data) {
		for (var i = 0; i < data.length; i++) {
			$("select[name=customer_ids]").append("<option value=" + data[i]['customer_id'] + ">"+ data[i]['customer_name'] + "</option>");
			$("select[name=customer_ids]").find("option").eq(i + 1).attr("code",data[i]['customer_code']);
		}
	});
}
/** *********************************分割线************************************* */
//查询所有客户---周报
function getCustomer(){
	
	$.post('/amp/amp/queryCustomer.do', {},function(data){
		for(var i =0;i<data.length;i++){
			$("#customer_id_week").append("<option value=" + data[i]['customer_code'] + ">"+ data[i]['customer_name'] + "</option>");
            //$(".customer_id_idWrapper_ul").append('<li><input type="checkbox" name="checkbox" id="'+ data[i]["customer_code"] +'" value="'+ data[i]["customer_code"] +'/><label for="'+data[i]["customer_code"] +'">'+data[i]['customer_name']+'</label><li>');
            $(".customer_id_idWrapper_ul").append("<li><label for='"+data[i]["customer_id"] +"'>"+data[i]['customer_name']+"</label><input id='"+data[i]["customer_id"] +"' value='"+data[i]["customer_id"] +"' type = 'checkbox' name ='checkbox'></li>");
			$("#month_customer").append("<option value=" + data[i]['customer_code'] + ">"+ data[i]['customer_name'] + "</option>");
			
		}
		//console.log($(".customer_id_idWrapper_ul li").length);
		(function (){
			$('.customer_id_idWrapper_ul2').css({'overflow':'hidden','height':'20%'});
			$('.customer_id_idWrapper_ul2>button').css({'display':'inline-block','height':'60%'});
			$('.customer_id_idWrapper_ul').css({'padding':'5px','overflow':'auto','height':'80%','box-sizing':'border-box'});
			$('.customer_id_idWrapper_ul li').css({'float':'left','width':'25%'});
			$('.customer_id_idWrapper_ul li input').css({'display':'inline-block','width':'10%','margin':'0','padding':'0','vertical-align':'middle'});
			$('.customer_id_idWrapper_ul li label').css({'text-align':'center','display':'inline-block','padding':'0 5px'});
		})();	
		//获取数据库是否连接异常
		 var res = eval('('+data+')');
		 if(res.dbError){
			 $(".region_msg").attr({"style":"display:inline;color:red;"}).text(res.dbError);
			 $(".week_msg").attr({"style":"display:inline;color:red;"}).text(res.dbError);
			 return;
		 }
	});
	
}

/**
 * 
 * 周报数据
 */
/** ************* 选择一个任意的日期，得出这个日期的起始周日期 ***************** */
var year;
var startDate;
var endDate;
var date_w;
var radioButton;
var cusName;
var actCode;
var cusCode;
var baiduRemove;
//var point;
var tcl;
var ctrs;
var ctre;
var bwcls;
var bwcle;
var dwcls;
var dwcle;
function week(){
	date_w = $(".calendar_week input").text();
	$(".calendar_week input").focus(function() {
		if ($(this).val() == date_w) {
			$(".week_msg").attr({"style":"display:none;"});
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd")) {
			$(".week_msg").attr({"style":"display:inline;color:red;"}).text("日期不能大于今天!");
			return;
		} else {
			$(".week_msg").attr({"style":"display:none;"});
			date_w = $(this).val();
		}
	});
	
	$("[name=data]").click(function(){
		radioButton = $(this).val();
	});
	//获取选择的客户名称
	$("[name=customer_id]").change(function(){
		cusName = $(this).find("option:selected").text();
		cusCode = $(this).find("option:selected").val();
	});
	
	$("#w_jumpIf").change( function() {
		var radia=$("#w_jumpIf")[0].checked ? 0 : 1;
		if(radia==1){
			$("#w_jumpRang").attr("disabled", true);
			$("#w_jumpRang").val("");
		}else{
			$("#w_jumpRang").attr("disabled",false);
		}
		});
	
	$("#w_ctrIf").change( function() {
		var radia=$("#w_ctrIf")[0].checked ? 0 : 1;
		if(radia==1){
			$("#w_ctrRangL").attr("disabled", true);
			$("#w_ctrRangM").attr("disabled", true);
			$("#w_ctrRangM").val("");
			$("#w_ctrRangL").val("");
		}else{
			$("#w_ctrRangL").attr("disabled", false);
			$("#w_ctrRangM").attr("disabled", false);
		}
		});
	$("#w_bgIf").change( function() {
		var radia=$("#w_bgIf")[0].checked ? 0 : 1;
		if(radia==1){
			$("#w_bgwcRangL").attr("disabled", true);
			$("#w_bgwcRangM").attr("disabled", true);
			$("#w_bgwcRangL").val("");
			$("#w_bgwcRangM").val("");
		}else{
			$("#w_bgwcRangL").attr("disabled", false);
			$("#w_bgwcRangM").attr("disabled", false);
		}
		});
	$("#w_djIf").change( function() {
		var radia=$("#w_djIf")[0].checked ? 0 : 1;
		if(radia==1){
			$("#w_djwcRangL").attr("disabled", true);
			$("#w_djwcRangM").attr("disabled", true);
			$("#w_djwcRangL").val("");
			$("#w_djwcRangM").val("");
		}else{
			$("#w_djwcRangL").attr("disabled", false);
			$("#w_djwcRangM").attr("disabled", false);
		}
		});
	
	$(".btn_week").click(function() {
		if( $(".btn_week input").attr('class') == "long_button_style" ){
			
			$(".btn_week input").removeClass().addClass('disable_button_style audit_id');
			
			 start_date = $("#startDate").val();
			 actCode = $("#week_actCode").val();
			 baiduRemove=$("input[name=baidurRemove]:checked").val();
//			 point = $('.point').prop('checked');
			 tcl = $("#w_jumpRang").val();
			 ctrs = $("#w_ctrRangL").val();
			 ctre = $("#w_ctrRangM").val();
			 bwcls = $("#w_bgwcRangL").val();
			 bwcle = $("#w_bgwcRangM").val();
			 dwcls = $("#w_djwcRangL").val();
			 dwcle = $("#w_djwcRangM").val();
				 $(".week_msg").hide();
				 
			//	 console.log(tcl +' '+ctrs +"  "+ctre +"  "+bwcls+"  "+bwcle)
				 if(cusName != undefined ){ //不能为请选择状态时提交
					 $(".week_msg").hide();
					  if("SH"== cusCode){
						  $.post('/amp/shweekExport/exportSHWeek.do',{'startDate':date_w,
								 'cusName':cusName,'cusCode':cusCode,'actCode':actCode,'mediaName':baiduRemove},function(data){
									 var dataObj = eval("("+data+")");
									 if(dataObj.msg == "1"){//两者都是为0条数据的时候
										 $(".week_msg").attr({"style":"display:inline;color:red;"}).text('数据查询结果为0条!');
										 $(".btn_week input").removeClass().addClass('long_button_style');
									 }else{
										  window.location.href =' /amp/weekExport/getExcelPath.do?filePath='+encodeURI(encodeURI(dataObj.savePath));
										  $(".btn_week input").removeClass().addClass('long_button_style');
									 }
							});
					  }else{
						  
						  $.post('/amp/weekExport/exportWeek.do',{'startDate':date_w,
								 'cusName':cusName,'cusCode':cusCode,'actCode':actCode,'mediaName':baiduRemove,
								 'tcl':tcl,'ctrs':ctrs,'ctre':ctre,'bwcls':bwcls,'bwcle':bwcle,'dwcls':dwcls,'dwcle':dwcle},function(data){
									 var dataObj = eval("("+data+")");
									 if(dataObj.errorMsg=="error"){
										 $(".week_msg").attr({"style":"display:inline;color:red;"}).text('程序异常,请联系相关维护人员！');
										 $(".btn_week input").removeClass().addClass('long_button_style');
										 return;
									 }
									 if(dataObj.msg == "1"){//两者都是为0条数据的时候
										 $(".week_msg").attr({"style":"display:inline;color:red;"}).text('数据查询结果为0条!');
										 $(".btn_week input").removeClass().addClass('long_button_style');
									 }else{
										  window.location.href =' /amp/weekExport/getExcelPath.do?filePath='+encodeURI(encodeURI(dataObj.savePath));
										  $(".btn_week input").removeClass().addClass('long_button_style');
									 }
							});
					  }
				 }else{
					 $(".week_msg").attr({"style":"display:inline;color:red;"}).text("请选择投放单位!");
				 }
		}
	});
};

/**start 月报**/
//src='/amp/page/schedule/upload.html'
var monthForm;
var monthFrame = $("<iframe name='formFrame'  width='0' heigh='0'><body></body>");
function month(){
	var month_date;
	var month_customer;
	
	$("#month_report").click(function(){
		
		month_date = $("#month_date").val();
		month_customer = $("#month_customer").val();
		month_location = 1;
		month_type = $("#month_type").val();
		month_file = $("#month_file").val();
		month_summary_data = $("#month_summary_data").val();
		
		$("#month_mes").html("");
		
		if(month_date == "" || month_customer == ""){
			$("#month_mes").html("请填写完整信息");
			return false;
		}
		monthForm = $("<form action='/amp/report/month.do' method='post' id='month'>");
		monthForm.append("<input value='"+ month_customer+"' name='month_customer'>");
		monthForm.append("<input value='"+ month_date+"' name='month_date'>");
		monthForm.append("<input value='"+ month_location+"' name='month_location'>");
		monthForm.append("<input value='"+ month_type+"' name='month_type'>");
		monthForm.append("<input value='"+ month_file+"' name='month_file'>");
		monthForm.append("<input value='"+ month_summary_data+"' name='month_summary_data'>");
		
		$("body").eq(0).append(monthFrame);
		
		$(formFrame.document.body).append(monthForm);

		 if( $("#month_report").attr('class') == "long_button_style" ){
			 $("#month_report").removeClass().addClass('disable_button_style audit_id');
			 monthForm[0].submit();
			 $("#month_report").removeClass().addClass('long_button_style');
		 }
	});
	
	//月报维度选择
	$("#month_file").change(function(){
		
		if(this.value == 1){
			$("#month_day").show();
		}else{
			$("#month_day").hide();
		}
	});//end $("#month_file").change(function(){

};



function setMes(mes){
	if(mes == 0){
		$("#month_mes").html("未找相关数据");
		$("#month_report").removeClass().addClass('long_button_style');
	}
	
	$("#month_report").removeClass().addClass('long_button_style');
}

/**end 月报**/
/** **********************************地域、频次导出************************************** */

var date_region; //选择的日期
var regoin; //选择的频次
var regoinProvince; //选择的客户名称
var regoinActCode; //选择的活动编号
var radio; //是否累计
var eDate;//累计截止日期
var customerName; //投放单位
var customer_id;
var city;//区别省，地市
function pinci(){
	//省
	 $.each(city.values, function (k, v1) {
	      var str2 ='<option value="'+k+'">'+k+'</option>';
	       $("#prov_prov").append(str2);
	  });
	 //地市
	 $("#prov_prov").change(function(){
		  var pid = $(this).val();
		  $.each(city.values,function(k,v1){
			  if(pid==k){
				  $("#prov_city").empty();	
				  for(var i=0;i<v1.length;i++){
					  var str2 ='<option value="'+v1[i]+'">'+v1[i]+'</option>';
					  $("#prov_city").append(str2);
				  }
				
				
			  }
			
		  })
	 });
       
        
	//各省的投放单位从json里面取得值里面取
		var str2;
	  $.each(org_name.values, function (k, v1) {
	      str2 ='<option value="'+k+'">'+v1+'</option>';
	       $("#customer_id_region").append(str2);
	  });
	  
	$.post('/amp/regoinExport/getProvince.do',{},function(data){
		for(var i=0;i<data.length;i++){
		//$("#customer_id_region").append("<option value=" + data[i]['province'] + ">"+ data[i]['province'] + "</option>");
		}
	});
	
	$("#frequency_id_region").change(function(){
		regoin = $(this).val(); //得到选择的频次，得到选择的日期在自然周或自然月算出后的日期
	});
	
	//获取选择的客户名称
	$("[name=customer_id_regoin]").change(function(){
		regoinProvince = $(this).find("option:selected").text();
	});
	$("#city").change(function(){
		regoinProvince = $(this).find("select[id='prov_city']").val();
	});
	$(".regoin_id").click(function() {
		var citys=$("input[name='city']:checked").val(); //省及地市
		date_region = $("#date_region").val();
		regoinActCode = $("#actCode_region").val(); //得到活动编号
		eDate = $("#eDate").val();//截止日期
		radio=$("input[name='data']:checked").val(); //是否累计
		customerName=$("#customer_id1").find("option:selected").text();//获得当前的投放单位
		customer_id=$("#customer_id").val();//投放单位的客户信息
		//endDateCity = $("#date_end").val();//地市的未累计的结束日期
		if(radio==0){
			if(date_region != null && date_region != ""){ //日期为空的情况
				if(date_region> new Date().Format("yyyy-MM-dd")){ //日期大于今天时
					$(".region_msg").attr({"style":"display:inline;color:red;"}).text("日期不能大于今天!");
				}else{
						if(regoin != undefined ||(regoin == undefined )){ //频次选择不能为请选择状态
							if(customerName!="--请选择--"){
//						 if(regoinProvince != undefined){ //不能为请选择状态时提交
							 $(".region_msg").attr({"style":"display:none;"});
							 
							 if( $(".regoin_id span input").attr('class') == "long_button_style" ){
								 
								 $(".regoin_id span input").removeClass().addClass('disable_button_style audit_id');
								 
								 $.post('/amp/regoinExport/regoin.do',
										 {'date':date_region,'regoin':regoin,'regoinProvince':regoinProvince,'actCode':regoinActCode,'customerName':customerName,'city':citys},
										 function(data){
										 var res = eval("("+data+")");
										 console.log(data);
										 if(res.dberror){
											 $(".region_msg").attr({"style":"display:inline;color:red;"}).text(res.dberror);
											 $(".regoin_id span input").removeClass().addClass('long_button_style');
											 return; //未查询到数据的时候
										 }
										 if(res.len){
											 $(".region_msg").attr({"style":"display:inline;color:red;"}).text(res.len);
											 $(".regoin_id span input").removeClass().addClass('long_button_style');
											 return; //未查询到数据的时候
										 }else{
											 $(".region_msg").attr({"style":"display:inline;color:red;"}).text("");
										 }
										 window.location.href =' /amp/regoinExport/getExcelPath.do?filePath='+encodeURI(encodeURI(res.savePath));
										 $(".regoin_id span input").removeClass().addClass('long_button_style');
								});
							 }
							 
//						 }else{
//							 $(".region_msg").attr({"style":"display:inline;color:red;"}).text("请选择投放省份!");
//						 }
							}else{
								$(".region_msg").attr({"style":"display:inline;color:red;"}).text("请选择投放单位!");
							}
					 }else{
						 $(".region_msg").attr({"style":"display:inline;color:red;"}).text("请选择频次!");
					  }
				}
			}else{
				$(".region_msg").attr({"style":"display:inline;color:red;"}).text("请选择日期!");
			}
		  }else{
			if(eDate != null && eDate != ""){ //日期为空的情况
				if(eDate> new Date().Format("yyyy-MM-dd")){ //日期大于今天时
					$(".region_msg").attr({"style":"display:inline;color:red;"}).text("日期不能大于今天!");
				}else{
					if(customerName!="--请选择--"){
//					 if(regoinProvince != undefined){ //不能为请选择状态时提交
						 $(".region_msg").attr({"style":"display:none;"});
						 
						 if( $(".regoin_id span input").attr('class') == "long_button_style" ){
							 $(".regoin_id span input").removeClass().addClass('disable_button_style audit_id');
								 $.post('/amp/regoinExport/regoin2.do',
										 {'date':eDate,'regoinProvince':regoinProvince,'actCode':regoinActCode,'customerName':customerName,'city':citys},
										 function(data){
										 var res = eval("("+data+")");
										 if(res.dberror){
											 $(".region_msg").attr({"style":"display:inline;color:red;"}).text(res.dberror);
											 $(".regoin_id span input").removeClass().addClass('long_button_style');
											 return; //未查询到数据的时候
										 }
										 if(res.len){
											 $(".region_msg").attr({"style":"display:inline;color:red;"}).text(res.len);
											 $(".regoin_id span input").removeClass().addClass('long_button_style');
											 return; //未查询到数据的时候
										 }else{
											 $(".region_msg").attr({"style":"display:inline;color:red;"}).text("");
										 }
										 window.location.href =' /amp/regoinExport/getExcelPath.do?filePath='+encodeURI(encodeURI(res.savePath));
										 $(".regoin_id span input").removeClass().addClass('long_button_style');
								});
						 	
						 	}
						
						 
//					 }else{
//						 $(".region_msg").attr({"style":"display:inline;color:red;"}).text("请选择投放省份!");
//					 }
				  }else{
					  $(".region_msg").attr({"style":"display:inline;color:red;"}).text("请选择投放单位!");	
				  }
				}
			}else{
				$(".region_msg").attr({"style":"display:inline;color:red;"}).text("请选择日期!");
			}
		   
		  }
	});
};


/**
 * 结案数据报表导出
 * 
 */
function  jiean(){	
	$("#actNameCLO").bind('keyup',function(){
		var actNames=$.trim($("#actNameCLO").val());//说明：根据模糊查询出来的结果，进行加载更多的活动名称
		if(actNames==""){
			  $(".clo_msg").css("display", "inline");
			  $(".clo_msg").text("请填写活动名称");
		   }else{
			   $(".clo_msg").css("display", "none");
			   $(".clo_msg").text("");
			   $.post("/amp/endExport/sumName.do",{'actName':actNames},function(data1){
						if(data1==2){
							  $(".clo_msg").css("display", "inline");
							  $(".clo_msg").text("请重新填写活动名称！");
						}else{
							  $(".clo_msg").css("display", "none");
							  queryName(actNames);

						}
					}
				);
		   }
	});	
	
	
	//获得查询的活动名称的方法
function queryName(actNames){
	$.ajaxSetup({
		async : false
	});
	 var array=[];
	  $.post("/amp/endExport/actNames.do",{
			'actName':actNames},function(data){
				for (var i = 0; i < data.length; i++) {//文本框动态拼接下拉框
					array.push(data[i]['activity_name']);

				};
			});

	 $("#actNameCLO").autocomplete(array, {   
	      width: 210,   
	      max:10, //下拉框最多显示数  
	      highlight: false,   
	      multiple: false, //是否输入一个字符就查询一次
	      multipleSeparator: "",   
	      scroll: true,   
	      scrollHeight: 600,  
	      matchCase:true, 
	      position:{my:"rigth top", at:"right bottom"},
	      matchContains:true//决定比较时是否与下拉框中的字符串内部查看匹配，如ba是否与foo bar中的ba匹配，测试过程中发现如果设置大小写不敏感，那么当再次和下拉框中匹配的时候会忽略matchcase属性  
	      });
	
	
}

	
$("#endFileSelect").click(function(){//当点击导出按钮的时候，判断
	var actName=$.trim($("#actNameCLO").val());//活动名称
	var actCode =$.trim($("#actCodeCLO").val());//活动编号
	//var customerName=$("#customer_id_end").find("option:selected").text();//获得当前的投放单位 
	//var customer_id=$("#customer_id_end").val();//投放单位的客户信息
	if(actName==""&&actCode==""){
	      $(".clo_msg").css("display", "inline");
		  $(".clo_msg").text("活动名称和活动编号不能同时为空");
		  return ;
	 }
	    
	     $(".clo_msg").css("display", "none");
	     $(".clo_msg").text("");
	     
	     if( $("#endFileSelect").attr('class') == "long_button_style" ){
	    	 $("#endFileSelect").removeClass().addClass('disable_button_style audit_id');
	    	 $.post("/amp/endExport/actSame.do",{'actName':actName,'actCode':actCode},function(data){
					if(data==1){
						  $(".clo_msg").css("display", "inline");
						  $(".clo_msg").text("请重新填写活动名称或活动编号！");
					}else if(data==2){
						 $(".clo_msg").css("display", "inline");
						 $(".clo_msg").text("确保活动的准确性,请填写活动编号！");	
					}else{
						 $(".clo_msg").css("display", "none");

								  $(".clo_msg").css("display", "none");

								  $("#hidecode").val($("#customer_id_end").val());
								  $("#hideselect").val($("#customer_id_end").find("option:selected").text());;
								  $("#dayEnd").submit();
							 }
					$("#endFileSelect").removeClass().addClass('long_button_style');
				}
			); 
	     }
		 
    });
};


var scrollFunc = function (e) {  
    e = e || window.event;  
    if (e.wheelDelta) {  //判断浏览器IE，谷歌滑轮事件               
        if (e.wheelDelta > 0) { //当滑轮向上滚动时  
           // alert("滑轮向上滚动");  
        	 $("#actNameCLO").blur();
        	 $("#city_msg").html("");		
        }  
        if (e.wheelDelta < 0) { //当滑轮向下滚动时  
           // alert("滑轮向下滚动");  
        	 $("#actNameCLO").blur();
        	 $("#city_msg").html("");
        }  
    } else if (e.detail) {  //Firefox滑轮事件  
        if (e.detail> 0) { //当滑轮向上滚动时  
           // alert("滑轮向上滚动");
        	 $("#actNameCLO").blur();
        	 $("#city_msg").html("");
        }  
        if (e.detail< 0) { //当滑轮向下滚动时  
           // alert("滑轮向下滚动"); 
        	 $("#actNameCLO").blur();
        	 $("#city_msg").html("");
        }  
    }  
};  
//给页面绑定滑轮滚动事件  
if (document.addEventListener) {//firefox  
    document.addEventListener('DOMMouseScroll', scrollFunc, false);  
}  
//滚动滑轮触发scrollFunc方法  //ie 谷歌  
window.onmousewheel = document.onmousewheel = scrollFunc; 

//地域频次积累的切换
function region(){
	//按是否累计切换
	$("input[name='data']").change( function() {
		var radia=$("input[name='data']:checked").val();
		var city=$("input[name='city']:checked").val();
		change(radia,city);
		});
  //集团累计报表按模板切换
  $("input[name='moban']").change(function(){
	  var radia=$("input[name='moban']:checked").val();
	  
		if(radia==1){
			$("#act").css("display", "none");
			$("#acts").css("display", "none");
		}else if(radia==2){
			$("#act").css("display", "none");
			$("#acts").css("display", "block");
		}else{
			$("#act").css("display", "block");
			$("#acts").css("display", "none");
		}
	  
  });
  //切换省模板及地市模板
  
  $("input[name='city']").change(function(){
	  var city=$("input[name='city']:checked").val();
	  var radia=$("input[name='data']:checked").val();
	  change(radia,city);
	  
  });
};

function isRed(){
	$("input[name='isRed']").change( function() {
		var radia=$("input[name='isRed']:checked").val();
		if(radia==1){
			$("#redIf").css("display", "block");
		}else{
			$("#redIf").css("display", "none");	
			
		}
		});
	$(".week_isRed").change(function(){ //周报的
		if($('.week_isRed').prop('checked') == true){
			$("#weekRed").css("display", "block");
		}else{
			$("#weekRed").css("display", "none");	
		}
	});
};

function isRedLeiJi(){
	$("input[name='isRedLeiji']").change( function() {
		var radia=$("input[name='isRedLeiji']:checked").val();
		if(radia==1){
			$("#redLeiji").css("display", "block");
		}else{
			$("#redLeiji").css("display", "none");	
			
		}
		});
};

//*************************累计报表导出************************************************************//
function leiji() {
$("#jilei_date").focus(function() {
	if ($(this).val() == date) {
		return;
	} else if ($(this).val() > new Date().Format("yyyy-MM-dd")) {
		 $("#jilei_msg").html("截止日期，不能大于今天");
	} else {
		date = $(this).val();
	
	}
});
$("input[name='jumpIfLeiji']").change( function() {
	var radia=$("#jumpIfLeiji")[0].checked ? 0 : 1;
	if(radia==1){
		$("#jumpLJ").attr("disabled", true);
		$("#jumpLJ").val("");
	}else{
		$("#jumpLJ").attr("disabled",false);
	}
	});
$("input[name='ctrIfLeiji']").change( function() {
	var radia=$("#ctrIfLeiji")[0].checked ? 0 : 1;
	if(radia==1){
		$("#ctrLJ1").attr("disabled", true);
		$("#ctrLJ2").attr("disabled", true);
		$("#ctrLJ1").val("");
		$("#ctrLJ2").val("");
	}else{
		$("#ctrLJ1").attr("disabled", false);
		$("#ctrLJ2").attr("disabled", false);
	}
	});
$("input[name='bgIfLeiji']").change( function() {
	var radia=$("#bgIfLeiji")[0].checked ? 0 : 1;
	if(radia==1){
		$("#wcLJ1").attr("disabled", true);
		$("#wcLJ2").attr("disabled", true);
		$("#wcLJ1").val("");
		$("#wcLJ2").val("");
	}else{
		$("#wcLJ1").attr("disabled", false);
		$("#wcLJ2").attr("disabled", false);
	}
	});
$("input[name='djIfLeiji']").change( function() {
	var radia=$("#djIfLeiji")[0].checked ? 0 : 1;
	if(radia==1){
		$("#djwcLJ1").attr("disabled", true);
		$("#djwcLJ2").attr("disabled", true);
		$("#djwcLJ1").val("");
		$("#djwcLJ2").val("");
	}else{
		$("#djwcLJ1").attr("disabled", false);
		$("#djwcLJ2").attr("disabled", false);
	}
	});
$("#leijiD").click(function(){
	$("#jilei_msg").html("");
	var customerName=$("#leijiCus").find("option:selected").text();//获得当前的投放单位
	var date=$("#jilei_date").val(); 
	var actCode =$("#leijiCode").val(); //活动编号可以为空
	var customer_id=$("#leijiCus").val();//投放单位的客户信息
	var jumpRang=$("#jumpLJ").val();//跳出率界
	var ctrRangL=$("#ctrLJ1").val();//ctr界下
	var ctrRangM=$("#ctrLJ2").val();//ctr界上
	var wcRangL=$("#wcLJ1").val();//曝光完成率界下
	var wcRangM=$("#wcLJ2").val();//曝光完成率界上
	var djwcRangL=$("#djwcLJ1").val();//点击完成率界下
	var djwcRangM=$("#djwcLJ2").val();//点击完成率界上
	if(customer_id==""){
		 $("#jilei_msg").html("请选择投放单位!");
		  return false;
	 }
	if(date==""){
		 $("#jilei_msg").html("请填写截止日期!");
		  return false;
		
	}
	/*if(actCode==""){
		 $("#jilei_msg").html("请填写活动编号!");
		  return false;
	}*/
	
	var dayIf=$("input[name='moban']:checked").val();
 if(dayIf==0){
	 if( $("#leijiD").attr('class') == "long_button_style" ){
		 $("#leijiD").removeClass().addClass('disable_button_style audit_id');
		 $.post("/amp/total/sumTotal.do",{
			   'date':date,
			   'actCode':actCode,
			   'customer_id':customer_id
			   },function(data){
				if(data==2){
					  $("#jilei_msg").html("数据结果为0条!");
				}else{
				$("#jilei_msg").html("");
				$("#leijifrom").attr("action","/amp/total/totalExport.do?date="+date+
						"&actCode=" + actCode+"&customerName="+customerName+"&customer_id="+customer_id+
						"&jumpRang="+jumpRang+"&ctrRangL="+ctrRangL+"&ctrRangM="+ctrRangM+"&wcRangL="+wcRangL+
						"&wcRangM="+wcRangM+"&djwcRangL="+djwcRangL+"&djwcRangM="+djwcRangM);
				$("#leijifrom").submit();
				}
				
				$("#leijiD").removeClass().addClass('long_button_style');
		});
	 }
	        
	}else{
		var isRegion=$("#isRegion")[0].checked ? "1" : "0";
		var isHB="0";
		var monthForm;
		var monthFrame = $("<iframe name='formFrame'  width='0' heigh='0'><body></body>");
			var month_date;
			var month_customer;
				month_date = $("#jilei_date").val(); 
				month_customer =$("#leijiCus").val();
				$("#jilei_msg").html("");//月报的提示信息做清除
				monthForm = $("<form action='/amp/total/months.do' method='post' id='month'>");
				monthForm.append("<input value='"+ month_customer+"' name='month_customer'>");
				monthForm.append("<input value='"+ month_date+"' name='month_date'>");
				monthForm.append("<input value='"+ actCode+"' name='actCode'>");
				monthForm.append("<input value='"+ dayIf+"' name='dayIf'>");
				monthForm.append("<input value='"+ isRegion+"' name='isRegion'>");
//				monthForm.append("<input value='"+ isHB+"' name='isHB'>");
//				alert(actCode);
				$("body").eq(0).append(monthFrame);
				$(formFrame.document.body).append(monthForm);
				monthForm[0].submit();
		
	        }
}) ;

};

function setTol(mes){
	if(mes == 0){
		$("#jilei_msg").html("未找相关数据");
	}
}

function removeDisabled(input){
	$(this).next().removeAttr('disabled');
	
}
/**
 * 集团品专数据报表
 */
function pz(){
	date = $(".calendar input").val();
	$("#beginPZ").focus(function() {
		if ($(this).val() == date) {
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd")) {
			$("#pz_msg").html("开始时间，不能大于当前时间");
		} else {
			date = $(this).val();
			
			$("#pz_msg").html("");
		}
	});
	$("#endPZ").focus(function() {
		if ($(this).val() == date) {
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd")) {
			$("#pz_msg").html("结束时间，不能大于当前时间");
		} else {
			date = $(this).val();
			
			$("#pz_msg").html("");
		}
	});
	//校验下活动编号：
	$("#PZCode").change(function(){
		var PZCode=$("#PZCode").val();
		$.post("/amp/pzExport/checkCode.do",{'actCode':PZCode,
			   },function(data){
				  if(data=='2'){//说明该活动编号不存在
					  $("#pz_msg").html("该活动编号不存在，请重新填写！");
				  }else{
					  $("#pz_msg").html("");
				  }
			   });
	});
	$("#PZFileSelect").click(function(){
		 $("#pz_msg").html("");
		var PZCode=$("#PZCode").val();
		beginDate=$("#beginPZ").val(); 
		endDate=$("#endPZ").val(); 
		d1=new Date(beginDate.replace(/\-/g, "\/"));  
		d2=new Date(endDate.replace(/\-/g, "\/"));
		 if(beginDate!=""&&endDate!=""&&d1 >d2) {//判断开始日期不能大于结束日期，小于等于结束时间
			  $("#pz_msg").html("开始时间不能大于结束时间！");
			   return false;  
			  }
			   if(beginDate==""){
				   $("#pz_msg").html("请填写开始时间！");
					   return false;  
				} 
			   if(endDate==""){
				   $("#pz_msg").html("请填写结束时间！");
					   return false;  
				} 
	          /* if(PZCode==""||PZCode==null){
			       $("#pz_msg").html("请填写活动编号！");
		  }*/
			  $("#PZFileSelect").removeClass().addClass('disable_button_style audit_id');
	       $.post("/amp/pzExport/sumPZ.do",{
			   'beginDate':beginDate,
			   'endDate':endDate,
			   'actCode':PZCode,
			   },function(data){
				if(data=='2'){
					  $("#pz_msg").html("数据结果为0条!");
					  $("#PZFileSelect").removeClass().addClass('long_button_style');
				}else{
				      $("#pz_msg").html("");
				      $("#PZId").attr("action","/amp/pzExport/exportPZ.do?beginDate="+beginDate+"&endDate="
								+ endDate + "&actCode=" + PZCode);
								 $("#PZId").submit();
						}
				 $("#PZFileSelect").removeClass().addClass('long_button_style');
		});
	});
	
	
}

//地域频次切换
function change(radia,city){		
	if(radia==1&&city==1){//累计地市	
		$("#zhouqi").css("display", "none");
		$("#xzrq").css("display", "none");
		/*$("#date_end").css("display", "none");*/
		$("#jzrq").css("display", "block");
		$("#city").css("display", "block");
		$("#prov").css("display", "none");
	}else if(city==1&&radia==0){//不累计地市	
		$("#zhouqi").css("display", "block");
		$("#xzrq").css("display", "block");
		/*$("#date_end").css("display", "inline-block");*/
		$("#jzrq").css("display", "none");
		$("#city").css("display", "block");
		$("#prov").css("display", "none");
	}else if(city==0&&radia==0){//不累计省	
		$("#zhouqi").css("display", "block");
		$("#xzrq").css("display", "block");
		/*$("#date_end").css("display", "none");*/
		$("#jzrq").css("display", "none");
		$("#city").css("display", "none");
		$("#prov").css("display", "block");
	}else if(city==0&&radia==1){//累计省	
		$("#zhouqi").css("display", "none");
		$("#xzrq").css("display", "none");
		/*$("#date_end").css("display", "none");*/
		$("#jzrq").css("display", "block");
		$("#city").css("display", "none");
		$("#prov").css("display", "block");
	}	
}		


//分端表
function endPoints(){
	$("#tqrq").focus(function() {
		if ($(this).val() == date) {
			return;
		} else if ($(this).val() > new Date().Format("yyyy-MM-dd")) {
			$("#city_msg").html("开始时间，不能大于当前时间");
		} else {
			date = $(this).val();
			$("#city_msg").html("");
		}
	});
	//检验下营销识别码
	$("#yxdm").change(function() {
		var mic = $("#yxdm").val();
		 $.post("/amp/pzExport/checkMic.do",{
			'mic' : mic,
		 },function(data){
			 if(data==2){
		     $("#city_msg").html("请填写正确的营销代码");
		     
			 }else{
			 $("#city_msg").html(""); 
			 }
		 });
	});
	$("#cityFileSelect").click(function(){
		 $("#city_msg").html("");
		var mic = $("#yxdm").val();
		var tqrq = $("#tqrq").val(); 
			   if(tqrq==""){
				   $("#city_msg").html("请填写开始时间！");
					   return false;  
				} 
			   if(mic==""){
				   $("#city_msg").html("请填写营销码！");
				   return false;  
				   
			   }
			   $.post("/amp/pzExport/endPoints.do",{
				   'tqrq':tqrq,
				   'mic':mic,
				   },function(data){
					if(data=='success'){
						  $("#city_msg").html("提交成功,稍后发邮件给您！");
						  $("#cityFileSelect").removeClass().addClass('long_button_style');
					}else{
					      $("#city_msg").html("提交失败，请联系系统管理员");									
							}
					 $("#cityFileSelect").removeClass().addClass('long_button_style');
			});
	   }
	);
	
}

// 口径设置
function dataCaliber(){
	$("#impDataCaliberCheckbox").change(function(){
		// 曝光口径的checkBox被check住，则可选
		if($("#impDataCaliberCheckbox").is(":checked")) {
			$("#impDataCaliberSelect").attr("disabled", false);
		} else {
			$("#impDataCaliberSelect").attr("disabled", true);
			$("#impDataCaliberSelect").val("");
		}
		$("#dataCaliberMessage").html("");
	});
	
	$("#clkDataCaliberCheckbox").change(function(){
		// 点击口径的checkBox被check住，则可选
		if($("#clkDataCaliberCheckbox").is(":checked")) {
			$("#clkDataCaliberSelect").attr("disabled", false);
		} else {
			$("#clkDataCaliberSelect").attr("disabled", true);
			$("#clkDataCaliberSelect").val("");
		}
		$("#dataCaliberMessage").html("");
	});
	
	$("#micStatMarkCheckbox").change(function(){
		// 点击口径的checkBox被check住，则可选
		if($("#micStatMarkCheckbox").is(":checked")) {
			$("#micStatMarkSelect").attr("disabled", false);
		} else {
			$("#micStatMarkSelect").attr("disabled", true);
			$("#micStatMarkSelect").val("");
		}
		$("#dataCaliberMessage").html("");
	});
	
	// 点击取消按钮
	$("#cancelId").click(function(){
		// 全部回到最初状态
		$("#dataCaliberTextareaId").val("");
		$("#impDataCaliberCheckbox").attr("checked", false);
		$("#clkDataCaliberCheckbox").attr("checked", false);
		$("#micStatMarkCheckbox").attr("checked", false);
		$("#impDataCaliberSelect").val("");
		$("#clkDataCaliberSelect").val("");
		$("#micStatMarkSelect").val("");
		$("#dataCaliberMessage").html("");
	});
	
	// 点击提交按钮
	$("#dataCaliberSubmit").click(function(){
		// check
		// 短代码不能为空
		if ($.trim($("#dataCaliberTextareaId").val()) == ""
			|| $.trim($("#dataCaliberTextareaId").val()) == null) {
			$("#dataCaliberMessage").html("短代码不能为空!");
			return;
		} else {
			// 清空message
			$("#dataCaliberMessage").html("");
		}
		
		// 曝光口径、点击口径和N口径至少要选择一个
		if (!$("#impDataCaliberCheckbox").is(":checked")
				&& !$("#clkDataCaliberCheckbox").is(":checked")
				&& !$("#micStatMarkCheckbox").is(":checked")) {
			$("#dataCaliberMessage").html("曝光口径、点击口径和N口径至少要选择一个!");
			return;
		} else {
			// 清空message
			$("#dataCaliberMessage").html("");
		}
		
		// 选中checkBox后，需要选择内容
		if ($("#impDataCaliberCheckbox").is(":checked")
				&& $("#impDataCaliberSelect").val() == "") {
			$("#dataCaliberMessage").html("请选择曝光口径的内容!");
			return;
		} else {
			// 清空message
			$("#dataCaliberMessage").html("");
		}
		if ($("#clkDataCaliberCheckbox").is(":checked")
				&& $("#clkDataCaliberSelect").val() == "") {
			$("#dataCaliberMessage").html("请选择点击口径的内容!");
			return;
		} else {
			// 清空message
			$("#dataCaliberMessage").html("");
		}
		if ($("#micStatMarkCheckbox").is(":checked")
				&& $("#micStatMarkSelect").val() == "") {
			$("#dataCaliberMessage").html("请选择N口径的内容!");
			return;
		} else {
			// 清空message
			$("#dataCaliberMessage").html("");
		}
		
		var dataCaliberMic = $.trim($("#dataCaliberTextareaId").val());
		// 短代码是否有效的check
		$.post("/amp/export/dataCaliberMicCheck.do",{
			'dataCaliberMic':dataCaliberMic,
		    },function(data){
		    	if (data != "") {
		    		$("#dataCaliberMessage").html("以下短代码：" + data + "不存在，请删除/修改后重新提交");
		    	} else {
		    		// 清空message
					$("#dataCaliberMessage").html("");
					// 更新曝光口径和点击口径
					$.post("/amp/export/updateDataCaliber.do",{
						'dataCaliberMic':dataCaliberMic,
						'impDataCaliber':$("#impDataCaliberSelect").val(),
						'clkDataCaliber':$("#clkDataCaliberSelect").val(),
						'micStatMark':$("#micStatMarkSelect").val(),
					    },function(data){
					    	if (data == 1) {
					    		parent.layer.msg('更新成功');
					    	} else {
					    		$("#dataCaliberMessage").html("更新失败，请联系管理员");
					    	}
					    });
		    	}
		    })
	});
}



