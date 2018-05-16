/**
 * 监测方案JS
 * 
 */  
	 //保存数据
	 var a= new Array();
	  
	//更改上线状态
	var message = "";
	var brotherTd;	//同一行的所有TD
	var actState;	//当前活动状态
	var actCode;
	var cusCode;
	var actOnlineDate;
	var updateState ;
	function comfirmOnline(obj){
		
		brotherTd = $(obj).parents("tr").find("td");
		actState = brotherTd.eq(7).attr("actstatecode");
		updateState = obj.value;
		
		//正常上线
		switch(actState){
			case '1':message = "确认提交正常上线";break;	//正常上线
			case '2':message = "强制上线上传图片";break;	//强制上线
			case '3':message = "确认排期？";break;	
			case '4':message = "通知上线？";break;
			case '5':message = "已上线？";break;
			case '6':message = "准备结案基础数据？";break;
			case '8':
				if(updateState == 5){
					message = "已结束?";
				}else if(updateState == 4){
					message = "已上线?";
				}
				
				break;
			case '7':return;
			default:obj.checked=false;return false;
		}
		
		layer.confirm(message, {
		   btn: ['确定','取消'] //按钮
		}, 
		
		//确定回调函数
		function(){
			actCode = brotherTd.eq(1).text();
			cusCode = brotherTd.eq(3).attr("cuscode");
			actOnlineDate = brotherTd.eq(5).attr("actOnlineDate");	//判断该检测方案是否已经插码
			
			if(actOnlineDate == '--' && actState == 3){
				$("#message").text(actCode+"未插码！");
			}else{
				$("#message").text("");
				updateStateFrame.window.online(actCode,actState,cusCode,updateState);
			}
			
			obj.checked = false;
//			layer.msg(message, {icon: 1});
			//layer.msg();
			$("div[id*=layui-layer]").remove();
		},//end 确定回调 
		//取消按钮回调
		function(){
			obj.checked = false;
		});
	}//end comfirmOnline()
	
	
	var i =0;
	//上线状态更改回调
	function onlineState(state){
		//反馈错误信息
		if(state.indexOf("error") != -1){
			
			if(state.indexOf("401")!= -1){
				location = "/amp/page/login/login.html";
			}
			
			
			$("#message").text(state);
			brotherTd.eq(8).find("input")[0].checked = false;
		}
		else{
			//正常上线
			if(actState == 1){
				brotherTd.eq(6).text(goLiveTypeArr[0]);	//上线类型
				brotherTd.eq(6).attr("onlinetype",goLiveTypeArr[0]);
				actState = 3;
			}
			//强制上线
			else if(actState == 2){
				brotherTd.eq(6).text(goLiveTypeArr[1]);	//上线类型
				brotherTd.eq(6).attr("onlinetype",goLiveTypeArr[1]);
				actState = 3;
			}
			//其它上线状态
			else if(3 <= actState && actState <= 5) {
				actState = 1 + parseInt(actState);
			}else if(6 == actState){
				actState = 8;
			}else if(8 == actState){
				if(updateState == 5){
					actState = 7;
				}else if(updateState == 4){
					actState = 6;
				}else {
					$("message").text("活动状态异常");
				}
			}
			else{
				$("message").text("活动状态异常");
			}
		}//end if-else
		
		brotherTd.eq(7).text(actStateArr[actState]);	//活动状态
		brotherTd.eq(7).attr("actstatecode",actState);
		
		//更改活动状态
		if(actState < actStateArr.length){
			//如果是已经插码，则将活动上线时间以及上线类型显示
			if(actState >= 4){
				brotherTd.eq(5).text(brotherTd.eq(5).attr("actonlinedate"));
				brotherTd.eq(6).text(brotherTd.eq(6).attr("onlinetype"));
			}
			dataStr = "";
			//已上线
			if(actState == 6){
				dataStr = "<input style='margin-left:-55px;width:25px;margin:auto;' type='radio' value='6' onchange='comfirmOnline(this);'/>" + updateStates[6];
			}
			//准备基础数据
			else if(actState == 8){
				dataStr = "<input style='margin-left:-55px;width:25px;margin:auto;' type='radio' value='4' onchange='comfirmOnline(this);'/>" + updateStates[4] +
				"<input style='margin-left:-55px;width:25px;margin:auto;' type='radio' value='5' onchange='comfirmOnline(this);'/>" + updateStates[5];
			}
			//活动未结束
			else if(actState < 7){
				dataStr = "<input style='margin-left:-55px;width:25px;margin:auto;' type='radio' value='"+ 
				(parseInt(actState) - 1) +"' onchange='comfirmOnline(this);'/>" + updateStates[actState-1];
			}else{
				dataStr = "--";
			}
			brotherTd.eq(8).html(dataStr);
		}
		
		else{
			brotherTd.eq(8).html("--");
		}
		//将按修改钮置为无效
		//brotherTd.find("input[value='修改']").attr("class","disable_button_style");
		
		//判断下一个活动状态是否有效
		isOnlineState();
		
		$("#stateForm").html("");
		$("#stateForm").html("<iframe name='updateStateFrame' src='/amp/page/monitorPlan/updateStateForm.html?"+new Date()+"'></iframe>");
		$("#message").text("操作成功！");
	}//end onlineState()
	
	
		function init(sdate,edate,channel){
			$("#mb").css("display", "block");
			// 加载等待效果
			 layer.load(2);
		}		
		
		function quote(obj){
			location = "./memoMonitorPlan.html?actCode="+$($(obj).parents("tr").find("td")[1]).text();
		}
		
		function audit(obj){
			location = "./auditMonitorPlan.html?actCode="+$($(obj).parents("tr").find("td")[1]).text();
			    
		}
	/*	function schedule(){
			location = "../schedule/scheduleUpload.html";
			 
		}*/
		
		var pageii;
		function add_data(sdate,edate,channel){
			location = "./addMonitorPlan.html";
			 
		}
		
		function addSubAct(obj){
			location = "./addMonitorPlan.html?actCode="+$($(obj).parents("tr").find("td")[1]).text();
		}
		
		/* 根据活动编号修改活动方案 */
		function modifyAct(mod){
			location ="./modifyMonitorPlan.html?actCode="+$($(mod).parents("tr").find("td")[1]).text();
		}
		
		function actDetail(mod){
			location ="./monitorPlanDetail.html?actCode="+$($(mod).parents("tr").find("td")[1]).text();
		}
		
		function modify_data(sdate,edate,channel){
			location = "./modifyMonitorPlan.html";
			   
		}
		function closeLayer(){
			layer.close(pageii);
		}
		
       /**
       		根据查询条件获取监测方案活动信息
       */
       var startDateStr = "";
       var endDateStr = "";
       var activityCode = "";
       var portUser = "";
       
       //获取监测方案数据
       function getData(){
    	   
    	   startDateStr = $("#sdate_id").val();
    	   endDateStr = $("#edate_id").val();
    	   portUser = $("#portUser").val();
    	   activityCode = $("#pageName").val();
    	   activityState = $("activity_state").val();
    	   
    	   setCookie("startDateStr",startDateStr);
    	   setCookie("endDateStr",endDateStr);
    	   setCookie("portUser",portUser);
    	   setCookie("activityCode",activityCode);
    	   setCookie("activityState",activityState);
    	   
    	   activityCode = "%"+$("#pageName").val()+"%";
    	   
    	   if(location.toString().indexOf("back") == -1){
    		   location = "/amp/page/monitorPlan/monitorPlan.html?back";
    	   }
    	   
    	   	//查询监测方案
    	   $.post("/amp/monitorPlan/list.do",
    			   {startDateStr:startDateStr,endDateStr:endDateStr,activityCode:activityCode,activityState:activityState,portUser:portUser},
    			   function(data){
	        	a = new Array();
	        	//未找到数据
	        	$("#div_table").find("div").remove();
	        	if(data[0] == null || data[0] == ''){
	        		$("#div_table").append("<div style='text-align:center;font-weight: 300;color: #efefef;'>未找到相关数据</div>");
	        	}
	        	
	        	//错误401信息
	        	if(data[0]['error'] == "401"){
	        		location = "/amp/page/login/login.html";
	        	}
	        	//错误500信息
	        	else if(data[0]['error'] == "500"){
	        		delCookie("startDateStr");
	        		delCookie("endDateStr");
	        		delCookie("portUser");
	        		delCookie("activityCode");
	        		delCookie("activityState");
	        		location = "/amp/page/error/error.html";
	        	}
	        	//其它错误信息
	        	else{
	        		$("#message").text(data[0]['error']);
	        	}
	        	var cd;
	        	for(var i=0;i < data[0].length;i++){
	        		cd = FormatDate(data[0][i]['createDate']);
	        		cd = cd.toString().indexOf("NaN") >= 0 ? data[0][i]['createDate'].substring(0,10) : cd; 
	        		
		        	a[i]={
		        		createDate:cd,
		        		code:data[0][i]['activityCode'],
		        		actName:data[0][i]['activityName'],
		        		province:data[0][i]['customer']['customerName'],
		        		preOnlineDate:FormatDate(data[0][i]['predictStartDate']),
		        		actOnlineDate:FormatDate(data[0][i]['realityStartDate']),
//		        		activityEndDate:FormatDate(data[0][i]['activityEndDate']),
		        		onlineType:goLiveTypeArr[data[0][i]['goLiveType']],
		        		actStateCode:data[0][i]['activityState'],
		        		actStat:actStateArr[data[0][i]['activityState']],
		        		submitStat:goLiveTypeArr[data[0][i]['goLiveType']],
		        		auditStaff:data[0][i]['portPeople']['realName'],
		        		parentIdf:data[0][i]['parentIdf'],
		        		customerCode:data[0][i]['customer']['customerCode']
		        		//parentActivityCode
		        	}; 
	    	   	}//end for
	        	
        		$("#tbody_id").html($("#template").tmpl(eval(a)));
       	        $("#mb").css("display","none");
       	     	//关闭等待效果
       			layer.closeAll();
       			timeOutLayer();
       			
       			$(".m_s_title span").text(data[data.length - 1].userID);
       			setCookie("userID",data[data.length - 1].userID);
       			
       			//权限控制
       			var checkInput;
       		  	var roal = data[data.length-1].role;
       			//非接口人
	    	  	if(roal.indexOf(roles[0]) < 0){
	    	  		//不能更改提交活动上线
	    	  		$("tr").each(function(){
	    				$(this).find("td").eq(8).each(function(){
	    					this.innerText="--";
	    				});
	    			});
	    	  		
	    	  		//不能修改方案信息
	    	  		checkInput = $("input[value='修改']");
	    			checkInput.attr("disabled","disabled");
	    			checkInput.attr("class","disable_button_style modify_id");
	    			
	    			//不能添加新方案
	    			$("#add_id").attr("disabled","disabled");
	    			$("#add_id").css("background-color","#ccc");
	    			
	    			//不能添加子方案
	    			checkInput = $("input[value='添加子方案']");
	    			checkInput.attr("disabled","disabled");
	    			checkInput.attr("class","disable_button_style modify_id");
	    		}
       		  	//非 WT支撑中心
	    	  	if(roal.indexOf(roles[2]) < 0){
	    			checkInput = $("input[value='审核']");
	    			checkInput.attr("disabled","disabled");
	    			checkInput.attr("class","disable_button_style audit_id");
	    	   	}
       		  	
	    	  	//WT支撑中心
	    	  	if(roal.indexOf(roles[2]) >= 0){
	    			//活动状态为待审核，对应的审核按钮才可点击
	    			$("tr").each(function(){
	    				$(this).find("td").eq(7).each(function(){
		    				if($(this).text() != actStateArr[0]){
		    					checkInput = $(this).parents("tr").find("input[value='审核']");
			    				checkInput.attr("disabled","disabled");
			    				checkInput.attr("class","disable_button_style modify_id");
		    				}
		    			});
	    			});
	    	  	}
	    	  	
	    	  	//接口人
	    	  	else if(roal.indexOf(roles[0]) >= 0){
	    	  		//是否为子监测方案,子方案不能继续添加子方案
	    	  		$("tr").each(function(){
	    	  			$(this).find("td").eq(2).each(function(){
		    	  			if($(this).attr("isParent") == '1'){
		    	  			//	$(this).attr("onclick","addSubAct(this)");
//		    	  				$(this).attr("onclick","quote(this)");
//		    	    	   		$(this).attr("class","mouse_hover");
		    	  				checkInput = $(this).parents("tr").find("input[value='添加子方案']");
		    	    			checkInput.attr("disabled","disabled");
		    	    			checkInput.attr("class","disable_button_style modify_id");
		    	  			}
	    	  			});
	    	  		});
	    	  		
	    	  		//活动是否为上线状态，到达上线状态，不能进行修改
	    	  		$("td[actstatecode]").each(function(){
	    	  			if($(this).attr("actstatecode") == actStateArr.length - 1){
	    	  				checkInput = $(this).parents("tr").find("input[value='修改']");
	    	  				checkInput.attr("disabled","disabled");
	    	  				checkInput.attr("class","disable_button_style");
	    	  			}
	    	  		});
	    	   	};
	    	   	
	    	   	isOnlineState();
	        });//end post(/monitorPlan/list.do)
    	   
       }//end $(function(){})
       
       jQuery(document).ready(function() {
    	   
           /* $("#template-nav").loadTemplate("../common/navbar.html", {});  */
          // $(".footers").loadTemplate("../footer.html", {});
           $(".headers").loadTemplate("../../common/header.html", {});
           
           var date = new Date();
           var sdate= cal_date_s(date,1);
           var edate= cal_date_e(date,1);
           
           //判断上个页面是否为同一个模块，否则，清理查询条件Cookie
           if(location.toString().indexOf("back") == -1){
        	   delCookie("startDateStr");
        	   delCookie("endDateStr");
        	   delCookie("portUser");
        	   delCookie("activityCode");
        	   delCookie("activityState");
           }
           
           //从cookie中读取查询条件，作为回显
		   startDateStr = getCookie("startDateStr");
		   endDateStr = getCookie("endDateStr");
		   portUser = getCookie("portUser");
		   activityCode = getCookie("activityCode");
		   activityState = getCookie("activityState");
			   
		   if(startDateStr != null && startDateStr != ""){
			   $("#sdate_id").val(startDateStr);
			   sdate = startDateStr;
		   }
		   if(endDateStr != null && startDateStr != ""){
			   $("#edate_id").val(endDateStr);
			   edate = endDateStr;
		   }
		   if(portUser != "" || portUser != null){
			   $("#portUser").val(portUser);
		   }
		   if(activityCode != "" || activityCode != null){
			   $("#pageName").val(activityCode);
		   }
           
		 
           var channel="全部";
           init(sdate,edate,channel);
           
           $("#sdate_id").val(sdate);
           $("#edate_id").val(edate);
           $("#channel_id").val(channel);
           
           //页面加载完，查询数据
		   getData();
		   
           var nowDate = FormatDate(new Date());
           
           $("#query_id").on("click",function() {
           		var _sdate=$("#sdate_id").val();
            	var _edate=$("#edate_id").val();
            	var _mes = $("#message");
            	if(_sdate > nowDate){
            		_mes.text("开始时间不能大于当前日期");
            		return false;
            	}
            	
				if(_edate > nowDate){
					_mes.text("结束时间不能大于当前日期");
					return false;
            	}
				
				if(_edate < _sdate){
					_mes.text("开始时间不能大于结束时间");
					return false;
				}
				_mes.text("");
				setCookie("local",location.toString());
            	getData();
            	//init(_sdate,_edate,_channel);
			});
           $("#add_id").on("click",
					function() {
        	   	var _sdate=$("#sdate_id").val();
           		var _edate=$("#edate_id").val();
           		var _channel=$("#channel_id").val();
           		add_data(_sdate,_edate,_channel);
			});
           $(".modify_id").on("click",
					function() {
       	   	var _sdate=$("#sdate_id").val();
          		var _edate=$("#edate_id").val();
          		var _channel=$("#channel_id").val();
          		modify_data(_sdate,_edate,_channel);
			});
         
           $(".audit_id").on("click",
					function() {
        	  
        		audit();
			});
           $(".schedule_id").on("click",
					function() {
        	   schedule();
			});
			
			docScroll();
        });

       //是否对活动状态更改有效
       function isOnlineState(){
    	   var td5,td6,td7,td8;
    	   $("tr").each(function(){
    		   td5 = $(this).find("td").eq(5);	//活动上线日期
    		   td6 = $(this).find("td").eq(6);	//上线类型
    		   td8 = $(this).find("td").eq(8);	//更改上线状态
    		   td7 = $(this).find("td").eq(7);
    		   
    		   //"已上线"; 
//    		    actStateArr[7] = "已结束";
//    		    actStateArr[8] = "准备结案基础数据";  才能显示上线类型以及活动上线时间
    		   if(td7.text() != actStateArr[6] && td7.text() != actStateArr[7] && td7.text() != actStateArr[8]){
    			   td5.text('--');
    			   td6.text('--');
    		   }
    		   
    	   });
       }//end isOnlineState()
       
	   //格式化日期 yyyy-mm-dd
	   function FormatDate (strTime) {
		   	if(strTime == '' || strTime == null){
			   return "--";
		   	}
		    var date = new Date(strTime);
		    var month = date.getMonth()+1;
		    var day = date.getDate();
		    return date.getFullYear()+"-"+(month < 10 ? "0"+ month :month)+"-"+(day < 10 ? "0"+ day:day);
		};
