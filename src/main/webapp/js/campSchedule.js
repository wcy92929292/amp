var a = new Array();
var actCode;
var path;
var start_date;
var minDate;   //一个排期中的最小日期
var day; //第一天
var month;
var sDate;
var eDate;
var e_month;
var e_day; //结束日期
var put_date;
var put_value;
var month_td;
var put = new Array();

function init() {
		$("#mb").css("display", "block");
		// 加载等待效果
		layer.load(2);
	}
	function downSch() {
		window.location.href = "./campSchedule.html";
	}
	//排期下载
	function to_path() {
		if (path == null || path == '') {
			$(".msg").attr("style", "").text("此活动没有排期文件!");
			return;
		} else {
		$.post('/amp/campaign/downloadSchePath.do', 
			   {'path': path},
			function(data) {
				var data = eval('(' + data + ')'); //json字符串解析json数据格式
				$(".msg").attr("style", "").text(data.msg);
			});
		}
	}
	//查询数据
	function getData() {
		var loca = location.toString();
		actCode = loca.substring(loca.indexOf("actCode=")+8,loca.length);
		
		$.post('/amp/campaign/campSchedule.do', {"actCode" : actCode}, function(data) {
			//未找到数据
        	$("#div_table").find("div").remove();
			if(data == null || data == ''){
	        	$("#div_table").append("<b style='color:#efefef;'>未找到相关数据</b>");
			}	
			a = new Array();
			
			for(var i=0;i<data.length;i++){
        		a[i] = {media_type:data[i].media.mediaType,
        				media_name:data[i].media.mediaName,
        				resource_type:data[i].extenInfo.resourceType,
        				point_location:data[i].extenInfo.pointLocation,
        				put_function:data[i].extenInfo.putFunction,
        				url_pc:data[i].urlPc,
        				material_require:data[i].extenInfo.materialRequire,
        				unit:data[i].unit,
        				resource_position:data[i].resource_position,
        				put_frequency:data[i].put_frequency,
        				click_avg:data[i].clickAvg,
        		        exposure_avg:data[i].exposureAvg,
        		        support_exposure:data[i].extenInfo.supportExposure,
        		        support_click:data[i].extenInfo.supportClick,
        		        exposure_meterial:data[i].extenInfo.exposureMeterial, //曝光物料
        		        click_meterial:data[i].extenInfo.clickMeterial, //点击物料
        		        mic:data[i].mic,
        		        imp_data_caliber:data[i].impDataCaliber,
        		        clk_data_caliber:data[i].clkDataCaliber,
//        		        put_value:data[i].calendarInfo.putValue,
//        		        put_date:data[i].calendarInfo.putDate,
        		        click_url:data[i].extenInfo.clickUrl,
        		        exposure_url:data[i].extenInfo.exposureUrl,
        		        plan_name:data[i].extenInfo.planName,
        		        unit_name:data[i].extenInfo.unitName,
        		        key_name:data[i].extenInfo.keyName,
        		        reality_start_date:data[i].activityInfo.realityStartDate,
        		        activity_end_date:data[i].activityInfo.activityEndDate,
        		        schedule_path:data[i].activityInfo.schedulePath
        		       };
        			//alert(a[i].unit_name);
	        		//判断是否是百度关键词排期
	        		if(a[i].plan_name!=null || a[i].unit_name!=null){
	        		 window.location.href = './campSchedule_special.html?actCode='+actCode;	
	        		}
	        		sDate = new Date(a[i].reality_start_date);
	        		eDate = new Date(a[i].activity_end_date);
	        		//判断是集团排期还是普通排期  --资源类型
	        		if(a[i].resource_type == null || a[i].resource_type == "null" ||  a[i].resource_type == ""){
	        			a[i].resource_type = "--";
	        		}
	        		
	        		//判断若是不监测曝光的
	        		if(a[i].exposure_url==null){
	        			a[i].exposure_url = "不监测曝光";
	        		}
	        		
	        		//判断若是不监测点击的
	        		if(a[i].click_url==null){
	        			a[i].click_url = "不监测点击";
	        		}
	        		
	        		//是否支持添加第三方曝光监测和点击监测判断 0:否,1:是
	        		 if(a[i].support_exposure == 1){
	        			 a[i].support_exposure = "是";
					}else{
						a[i].support_exposure = "否";
					}

	        		 if(a[i].support_click == 1){
	        			 a[i].support_click = "是";
	        		 }else{
	        			 a[i].support_click = "否";
	        		 }
	        		//可添加曝光代码嵌入物料
	        		if(a[i].exposure_meterial == 1){
	        			a[i].exposure_meterial = "是";
	        		}else{
	        			a[i].exposure_meterial = "否";
	        		}
	        		
	        		if(a[i].click_meterial == 1 ){
	        			a[i].click_meterial = "是";
	        		}else{
	        			a[i].click_meterial = "否";
	        		}
	        	    // 曝光去重口径
	        		if (a[i].imp_data_caliber == "0") {
	        			a[i].imp_data_caliber = "标准口径";
	        		} else if (a[i].imp_data_caliber == "1") {
	        			a[i].imp_data_caliber = "去重口径";
	        		} else if (a[i].imp_data_caliber == "2") {
	        			a[i].imp_data_caliber = "冷却口径";
	        		} else {
	        			a[i].imp_data_caliber = "";
	        		}
	        		// 点击去重口径
	        		if (a[i].clk_data_caliber == "0") {
	        			a[i].clk_data_caliber = "标准口径";
	        		} else if (a[i].clk_data_caliber == "1") {
	        			a[i].clk_data_caliber = "去重口径";
	        		} else if (a[i].clk_data_caliber == "2") {
	        			a[i].clk_data_caliber = "冷却口径";
	        		} else {
	        			a[i].clk_data_caliber = "";
	        		}
	        		//投放频次
	        		if(a[i].resource_position == null || a[i].resource_position == "null" ||  a[i].resource_position == ""){
	        			a[i].resource_position = "--";
	        			a[i].resource_position = "--";
	        		}
	        		//投放频次
	        		if(a[i].put_frequency == null || a[i].put_frequency == "null" ||  a[i].put_frequency == ""){
	        			a[i].put_frequency = "--";
	        			a[i].put_frequency = "--";
	        		}
	        		//获取排期路径
	        		path = a[i].schedule_path; 
	        		
	        		//投放日期和投放量
	    			put[i]=data[i].calendarInfo;
				}//for
				while(sDate <= eDate){
	   			 month = sDate.getMonth()+1;
	   			 year = sDate.getFullYear();
	   			 e_month = eDate.getMonth()+1;
	   			 e_year = eDate.getFullYear();
	   			 day = sDate.getDate();
	   			 e_day = eDate.getDate();
	   			 var td = "<td class='len' id='row_td_id' date='"+FormatDate(sDate.getTime())+"'>"+day+"</td>";
	   			 sDate =  new Date(sDate.getTime() + 1000 * 60 * 60 * 24 );
	   			 $(".row_td").append(td);
	   			 
	        	 month_td = '<td id="row_td_id" colspan="" class="month"><div style="width:44px;">'+month+'月</div></td>';
	   			 $("#row_one").append(month_td);
		         var firstDay= $($(".row_td").find("td:first")).text();//取得开始时间第一天
		         var lastDay= $($(".row_td").find("td:last")).text();
		         var tdlen = getLastDay(year,month)-firstDay+1;
		        //设置td的宽度
		         //$(".month").attr("style","width:3.5em");
	   			}
				
				 $("thead_1 tr").each(function(){
					alert($($(this).find("td")[0].text())) 
				 });
				
				 //获得某月的最后一天  start
		        function getLastDay(year,month) {         
		             var new_year = year;    //取当前的年份          
		             var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）          
		             if(month>12) {         
		              new_month -=12;        //月份减          
		              new_year++;            //年份增          
		             }         
		             var new_date = new Date(new_year,new_month,1);                  //取当年当月中的第一天          
		             return (new Date(new_date.getTime()-1000*60*60*24)).getDate();  //获取当月最后一天日期          
		    } //end
		        
		    //计算两个日期之间相差多少天
	        function GetDateDiff(startDate,endDate)  
	        {  
	            var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();     
	            var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();     
	            var dates = Math.abs((startTime - endTime))/(1000*60*60*24);     
	            return  dates;    
	        }
	        
			var resultJson = eval(a);
			$("#tbody_id").html($("#template").tmpl(resultJson));
			$("#mb").css("display", "none"); 
			//获取日期天数单元格长度
			var len = $(".row_td td").length;	
			//追加td
			var trline = $(".trline");
			for(var j =0;j<trline.length;j++){
				for(var i=0;i<len;i++){
					trl = $('<td><div style="width:50px;"></div></td>');
					$(".trline").eq(j).append(trl);
				}
			}
			//追加投放量
			var calanList;
			for(var j=0;j<put.length;j++){
			
			for(var i=0;i<len;i++){
				var date_ = $(".row_td").children('td').eq(i).attr("date"); //得到每一行的date属性值
				
					 calanList = put[j];
					 for(var k=0;k<calanList.length;k++){
						put_date =  FormatDate(calanList[k].putDate);
						put_value =  calanList[k].putValue;
					if(date_ == put_date){
						$(".trline").eq(j).children('td').eq(19+i).find("div").text(put_value);
					}
				 }
			  }
			}
			
			//关闭等待效果
			layer.closeAll();
			timeOutLayer();
			//动态
			//setCalendarWidth();
		});
	}
	
	
	/**
	 * 
	 * @returns
	 *//*
	function setCalendarWidth(){
		$("tr").find("td:gt(18)").css("width","19px");
		$("#tbody_id tr").find("td:gt(18)").css("width","19px");
		
	}*/
	
	
	
	jQuery(document).ready(function() {
		$(".headers").loadTemplate("../../common/header.html", {});
		init();
		getData(); //页面加载完查询数据
		$("#query_id").on("click", function() {
			to_path();
		});
		
		/*$("#cancal_id").on("click", function() {
			window.location.href = "./campaign.html";
		});*/
		//判断是否登录
		$.post("../../campaign/getUserSession.do", {}, function(data){
			if(data=="error"){
				location.href = "../login/login.html";
		}});
	});

	
	
	/**
	 * 点击预估修改
	 */
	function changeClick(){
		//打开窗口
		$(".click").dblclick(function(event){
			//弹出一个层
            event.preventDefault();
            $('.cd-popup3').addClass('is-visible3');
            //得到该点位的短代码数据和原始点击预估数据
            $("#orgClick").val($(this).text());
            $("#orgMic").val($($(this).parent().children()[16]).text());
			
		});
		//关闭窗口
		$('.cd-popup3').on('click', function(event){
            if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup3') ) {
                event.preventDefault();
                $(this).removeClass('is-visible3');
            }
        });
		
		//保存修改的点击预估和曝光预估
		$("#updateBtn").click(function(){
			$("#orgMic").removeAttr("disabled");
			//得到新的点击预估值
			var newAvg = $("#newClick").val();
			var mic = $("#orgMic").val();
			//修改的请求
			$.post('/amp/campaign/updateClickAvg.do',{'clickAvg':newAvg,'mic':mic},function(data){
				    var res = eval('('+data+')');
					$("#updateShow").css('display','block');
					$("#updateShow").text(res.message);
			});
		});

		
		
		//打开窗口
		$(".exposure").dblclick(function(event){
			//弹出一个层
            event.preventDefault();
            $('.cd-popup2').addClass('is-visible2');
            //得到该点位的短代码数据和原始点击预估数据
            $("#orgExposure").val($(this).text());
            $("#orgMic2").val($($(this).parent().children()[16]).text());
			
		});
		//关闭窗口
		$('.cd-popup2').on('click', function(event){
            if( $(event.target).is('.cd-popup-close') || $(event.target).is('.cd-popup2') ) {
                event.preventDefault();
                $(this).removeClass('is-visible2');
            }
        });
		
		//保存修改的点击预估和曝光预估
		$("#updateBtn2").click(function(){
			$("#orgMic2").removeAttr("disabled");
			//得到新的点击预估值
			var newAvg2 = $("#newExposure").val();
			var mic2 = $("#orgMic2").val();
			//修改的请求
			$.post('/amp/campaign/updateExposureAvg.do',{'exposureAvg':newAvg2,'mic':mic2},function(data){
					var res = eval('('+data+')');
					$("#updateShow2").css('display','block');
					$("#updateShow2").text(res.message);
			});
		});
	}
	
