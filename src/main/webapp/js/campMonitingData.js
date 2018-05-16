 /* 
 * 营销活动——活动监测数据
 * @author LQ
 * @param _sdate
 * @param _edate
 * @return list
 * @date 2016-04-19
 */
	function getUrlParam(name){ //url 
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
			var r = window.location.search.substr(1).match(reg); 
			if (r != null) return unescape(r[2]); return null; 
			}
	jQuery(document).ready(function() {
           $(".headers").loadTemplate("../../common/header.html", {});
           var xx = getUrlParam('activityCode');//alert(xx);
	       $.post("../../campaign/getUserSession.do", {
	   		}, function(data){
	   			if(data=="error"){
	   				layer.alert('未登录，请登录用户！', {
	   					  skin: 'layui-layer-molv' //样式类名
	   					  ,closeBtn: 0
	   					}, function(){
	   				window.location.href = "../login/login.html";
	   				});
	   			}else{
	   				var activityCode=$("#activityCode_id").val(getUrlParam('activityCode'));
	   				var sdate=$("#realityStartDate_id").val(getUrlParam('realityStartDate'));
	   				
	   	          // var req = /^[a-zA-Z0-9]+$/;
	   	           /*var s_date = new Date();
	   	           var e_date = new Date();*/
	   	           var  date = new Date();
	   	           /*var sdate=getUrlParam('realityStartDate');*/
	   	           var sdate=cal_date_s(date, 4);
	   	           var edate=cal_date_e(date, 4);
	   	           init(sdate,edate,activityCode);
		   	        $("#sdate_id").val(sdate);
		   	        $("#edate_id").val(edate);
	   	           	$('#marketingCode_id').blur(function(){
	   	      			var marketingCode=$("#marketingCode_id").val();//alert(marketingCode);
	   	      			if(marketingCode!=""&&marketingCode!=null){
	   	      				if(marketingCode.length>50){
	   	      					layer.tips('媒体名称不能超过50位', '#marketingCode_id',{
	   	      						 tips: [2,'#8F8888']
	   	      					});
	   	      					$("#marketingCode_id").val("");	
	   	      				}/*else if(!req.test(marketingCode)){
	   	      					layer.tips('请输入数字和英文组合', '#marketingCode_id',{
	   	      						 tips: [2,'#8F8888']
	   	      					});
	   	      					$("#marketingCode_id").val("");
	   	      				}*/
	   	      			}
	   	      		});
	   	          	$('#mediaName_id').blur(function(){
	   	      			var mediaName = $("#mediaName_id").val();//alert(mediaName);
	   	      			if(mediaName!=""&&mediaName!=null){
	   	      				if(mediaName.length>30){
	   	      					layer.tips('营销码长度不能超过30', '#mediaName_id',{
	   	      						 tips: [2,'#8F8888']
	   	      					});
	   	      					$("#mediaName_id").val("");
	   	      				}
	   	      			}
	   	      		});
	   	          	/*$('#activityCode_id').blur(function(){
		   	          	var activitycode = $("#activityCode_id").val();
		   	          	if(activitycode.length>"20"){
							layer.tips('活动编号不能超过20位', '#activityCode_id',{
								 tips: [2,'#8F8888']
							});
							$("#activityCode_id").val("");
						}
	   	          	});*/
	   	         
	           $("input[name='_date']").change( function(){	
	            if($("#check1").is(":checked")){
	            	$("#sdate_id").val("");
	            	$("#edate_id").val("");
	  			$("#sdate_id").attr("disabled", true); 
	  			$("#edate_id").attr("disabled", true); 
	  			
	  		}else if($("#check2").is(":checked")){
	  			
	  			$("#sdate_id").val("");
            	$("#edate_id").val("");
            	$("#sdate_id").attr("disabled", false); 
	  			$("#edate_id").attr("disabled", false); 	
  			$("#sdate_id").attr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})"); 
  			$("#edate_id").attr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})"); 
	  		}else{
	  			$("#sdate_id").attr("disabled", false); 
	  			$("#edate_id").attr("disabled", false); 
	   	  		$("#sdate_id").attr("onfocus","WdatePicker({skin:'twoer'})");
	   	  		$("#edate_id").attr("onfocus","WdatePicker({skin:'twoer'})");
	   	  		}
	        });
//	           
	   	           $("#query_id").on("click",//查询按钮事件
	   						function() {
	   	           		var _sdate=$("#sdate_id").val();
	   	            	var _edate=$("#edate_id").val();
	   	            	var mediaName= $('#mediaName_id').val();
	   	                var marketingCode=$('#marketingCode_id').val();
	   	                var nowDate = FormatDate(new Date());
	   	                var nowTime = nowDate+" "+FormatTime(new Date());
	   	            	if(_sdate!=""&&_sdate!=null){
	   	            		if($("#check0").is(":checked")){
	   	            			if(_edate==""||_edate==null){
		   	            			_edate=nowDate;
		   	            		}
	   	            		//alert("_sdate:"+_sdate+"_edate:"+_edate+"mediaName:"+mediaName+"marketingCode"+marketingCode);
	   	        			if(_edate < _sdate){
	   	        				layer.tips('开始时间不能大于结束时间', '#sdate_id',{
	   	        					tips: [1,'#8F8888']
	   	        				});
	   	        				return false;
	   	        			}else
			   	            	if(_sdate > nowDate){
			   	            		layer.tips('开始时间不能当前时间', '#sdate_id',{
		   	        					tips: [1,'#8F8888']
		   	        				});
			   	            		return false;
			   	            	}else
				   					if(_edate > nowDate){
				   						layer.tips('开始时间不能大于当前时间', '#edate_id',{
			   	        					tips: [1,'#8F8888']
			   	        				});
				   	            		return false;
				   	            	}
	   	        				Search(_sdate,_edate,mediaName,marketingCode,activityCode);
	   	        			}else if($("#check2").is(":checked")){
	   	        				if(_edate==""||_edate==null){
		   	            			_edate=nowDate+" "+FormatTime2(new Date());
		   	            		}
	   	        				if(_edate < _sdate){
		   	        				layer.tips('开始时间不能大于结束时间', '#sdate_id',{
		   	        					tips: [1,'#8F8888']
		   	        				});
		   	        				return false;
		   	        			}else
				   	            	if(_sdate > nowTime){
				   	            		layer.tips('开始时间不能当前时间', '#sdate_id',{
			   	        					tips: [1,'#8F8888']
			   	        				});
				   	            		return false;
				   	            	}else
					   					if(_edate > nowTime){
					   						layer.tips('开始时间不能大于当前时间', '#edate_id',{
				   	        					tips: [1,'#8F8888']
				   	        				});
					   	            		return false;
					   	            	}
	   	        			
	   	        				Search2(_sdate,_edate,mediaName,marketingCode,activityCode);
	   	        			}	
	   	        		}else{
	   	        			//墨绿深蓝风
	   	        			if($("#check1").is(":checked")){
	   	        				Search1(nowDate,mediaName,marketingCode,activityCode);
	   	        			}else{
	   	        			layer.alert('请选择开始时间', {
	   	        			  skin: 'layui-layer-molv' //样式类名
	   	        			  ,closeBtn: 0
	   	        			});
	   	        			}
	   	        		}
	   				});
	   				
	   			}
	   		});
           //2222222222
	       //22222222
        });
	function FormatDate(strTime){//YYYY-MM-DD 
		var d = new Date(strTime);
		var year=d.getFullYear();
		var day=d.getDate();
		var month=+d.getMonth()+1;
		var hour=d.getHours();
		var minute=d.getMinutes();
		var second=d.getSeconds();
		var misec=d.getMilliseconds();
		var f=year+"-"+formate(month)+"-"+formate(day)/*+" "+formate(hour)+":"+formate(minute)+":"+formate(second)+":"+formate(misec)*/;
		return f;
	 }
	function FormatTime(strTime){//YYYY-MM-DD 
		var d = new Date(strTime);
		var year=d.getFullYear();
		var day=d.getDate();
		var month=+d.getMonth()+1;
		var hour=d.getHours();
		var minute=d.getMinutes();
		var second=d.getSeconds();
		var misec=d.getMilliseconds();
		var f=formate(hour)+":"+formate(minute)+":"+formate(second);
		return f;
	 }
	function FormatTime2(strTime){//YYYY-MM-DD 
		var d = new Date(strTime);
		var year=d.getFullYear();
		var day=d.getDate();
		var month=+d.getMonth()+1;
		var hour=d.getHours();
		var minute=d.getMinutes();
		var second=d.getSeconds();
		var misec=d.getMilliseconds();
		var f=formate(hour)+":00:00";
		return f;
	 }
		 function formate(d){
			 return d>9?d:'0'+d;
		 }
	 function formatDate(strTime) {//YYYY-MM-DD
			var date = new Date(strTime);
			return date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
					+ date.getDate();
		}
 	function init(sdate,edate,activityCode){//初始化
			$("#mb").css("display", "block");
			 layer.load(2);// 加载等待效果
			$.post("../../campaign/queryMonitingData.do", {
    			'sdate' : sdate,
    			'edate' : edate,
    			'activityCode':$('#activityCode_id').val(),
    		}, function (result, resultState) {
	        		layer.closeAll();
	            	var resultJson = eval(result);//console.log(resultJson);
	              	if(resultJson!=""&&resultJson!=null){
	              		$.each(resultJson, function (i, item) { 
							if (item!= "") {
								
								item.createDate=FormatDate(item.createDate);
								item.realityStartDate=formatDate(item.realityStartDate);
								item.activityEndDate=formatDate(item.activityEndDate);
								$.each(item,function(j,value){
									 if(value==null){
										item[j]="--";
									 }
								})
							}
						}); 
	            		$("#tbody_id").loadTemplate($("#template"),resultJson);
	            	}else{
	            	  $("#tbody_id").html("<td style='width:4060px;' colspan=9 id='sumtd'><span>未查询到相关数据!</span></td>");
	            	}
	        });
		}
 	

 		//查询某段时间
		function Search(sdate,edate,mediaName,marketingCode,activityCode){//条件查询
			$("#mb").css("display", "block");
			 layer.load(2);// 加载等待效果
			$.post("../../campaign/searchMonitingData.do", {
    			'sdate' : sdate,
    			'edate' : edate,
    			'mediaName':$('#mediaName_id').val(),
    			'marketingCode':$('#marketingCode_id').val(),
    			'activityCode':$('#activityCode_id').val(),
    			'radio1':$('#radio1').val(),
    			'radio2':$('#radio2').val(),
    		}, function (result, resultState) {
	        		layer.closeAll();//关闭加载等待效果
	            	var resultJson = eval(result);//console.log(resultJson);
	              	if(resultJson!=""&&resultJson!=null){
	              		
	              		$.each(resultJson, function (i, item) { 
							if (item!= "") {
							
								$.each(item,function(j,value){
									 if(value==null){
										item[j]="--";
									 }
								})
								item.createDate=FormatDate(item.createDate);
							}
						});
	              		
	            		$("#tbody_id").loadTemplate($("#template"),resultJson);
	            	}else{
	            	  $("#tbody_id").html("<td colspan=9 id='sumtd'><span>未查询到相关数据!</span></td>");
	            	  setPositionSta();
	            	}
	        });
		}
		//today数据
		function Search1(nowDate,mediaName,marketingCode,activityCode){//条件查询
			$("#mb").css("display", "block");
			 layer.load(2);// 加载等待效果
			$.post("../../campaign/searchTodayMonitingData.do", {
    			
    			'nowDate' : nowDate,
    			'mediaName':$('#mediaName_id').val(),
    			'marketingCode':$('#marketingCode_id').val(),
    			'activityCode':$('#activityCode_id').val(),
    		}, function (result, resultState) {
	        		layer.closeAll();//关闭加载等待效果
	            	var resultJson = eval(result);//console.log(resultJson);
	              	if(resultJson!=""&&resultJson!=null){
	              		
	              		$.each(resultJson, function (i, item) { 
							if (item!= "") {
								
								item.createDate=FormatDate(item.createDate);
								item.realityStartDate=formatDate(item.realityStartDate);
								item.activityEndDate=formatDate(item.activityEndDate);
								$.each(item,function(j,value){
									 if(value==null){
										item[j]="--";
									 }
								})
							}
						}); 
	            		$("#tbody_id").loadTemplate($("#template"),resultJson);
	            	}else{
	            	  $("#tbody_id").html("<td colspan=9 id='sumtd'><span>未查询到相关数据!</span></td>");
	            	  setPositionSta();
	            	}
	        });
		}
		//分小时数据
		function Search2(_sdate,_edate,mediaName,marketingCode,activityCode){//条件查询
			$("#mb").css("display", "block");
			 layer.load(2);// 加载等待效果
			$.post("../../campaign/searchHourMonitingData.do", {
				'_sdate' : _sdate,
    			'_edate' : _edate,
    			'mediaName':$('#mediaName_id').val(),
    			'marketingCode':$('#marketingCode_id').val(),
    			'activityCode':$('#activityCode_id').val(),
    		}, function (result, resultState) {
	        		layer.closeAll();//关闭加载等待效果
	            	var resultJson = eval(result);//console.log(resultJson);
	              	if(resultJson!=""&&resultJson!=null){
	              		
	              		$.each(resultJson, function (i, item) { 
							if (item!= "") {
								item.createDate=FormatDate(item.createDate)+" "+FormatTime(item.createDate);
								item.realityStartDate=formatDate(item.realityStartDate);
								item.activityEndDate=formatDate(item.activityEndDate);
								$.each(item,function(j,value){
									 if(value==null){
										item[j]="--";
									 }
								})
							}
						}); 
	            		$("#tbody_id").loadTemplate($("#template"),resultJson);
	            	}else{
	            	  $("#tbody_id").html("<td colspan=9 id='sumtd'><span>未查询到相关数据!</span></td>");
	            	  setPositionSta();
	            	}
	        });
		}
		
		
