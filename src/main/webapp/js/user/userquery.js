 /* 
 * 用户管理——查看用户
 * @author LQ
 * @date 2016-04-25
 */
	jQuery(document).ready(function() {
           $(".headers").loadTemplate("../../common/header.html", {});
	       $.post("../../campaign/getUserSession.do", {
	   		}, function(data){
	   			if(data=="error"){
	   				layer.alert('未登录，请登录用户！', {
	   					  skin: 'layui-layer-molv' //样式类名
	   					  ,closeBtn: 0
	   					}, function(){
	   				window.location.href = "../login/login.html";
	   				});
	   			}else if(data=="管理员"||data=="超级管理员" ){
	   				//console.log(data);
	   				//getsession();
	   			   var date = new Date();
	   	           var sdate=  cal_date_s(date,4);
	   	           
	   	           var edate=cal_date_e(date,1);
	   	           //alert(edate);
	   	           //var edate=  cal_date_e(date,1);
	   	           var uname= $('#uname').val();
	               var role=$('#role_id').val();
	               var user_name='';
	               //console.log($("#sdate_id").val(sdate)+$("#sdate_id").val(edate))
	   	           init(sdate,edate,uname,role,user_name);
		   	        $("#sdate_id").val(sdate);
		            $("#edate_id").val(edate);
		            $("#uname").val(uname);
		            $("#role_id").val(role);
		            console.log($("#sdate_id").val()+$("#sdate_id").val());
		            $('#uname').blur(function(){
	   	      			var uname=$("#uname").val();//alert(marketingCode);
	   	      			if(uname!=""&&uname!=null){
	   	      				if(uname.length>20){
	   	      					layer.tips('姓名不能超过20位', '#uname',{
	   	      						 tips: [2,'#8F8888']
	   	      					});
	   	      					$("#uname").val("");	
	   	      				}else if(!req.test(uname)){
	   	      					layer.tips('请输入数字和英文组合', '#uname',{
	   	      						 tips: [2,'#8F8888']
	   	      					});
	   	      					$("#uname").val("");
	   	      				}
	   	      			}
	   	      		});
	   	          	$('#role_id').blur(function(){
	   	      			var role = $("#role_id").val();//alert(mediaName);
	   	      			if(role!=""&&role!=null){
	   	      				if(role.length>20){
	   	      					layer.tips('角色长度不能超过20', '#role_id',{
	   	      						 tips: [2,'#8F8888']
	   	      					});
	   	      					$("#role_id").val("");
	   	      				}
	   	      			}
	   	      		});
	   	           $("#query_id").on("click",//查询按钮事件
	   						function() {
		   	        	var sdate=$("#sdate_id").val();
		            	var edate=$("#edate_id").val();
	   	            	var uname= $('#uname').val();
	   	                var role=$('#role_id').val();
	   	                var user_name='';
	   	                var nowDate = FormatDate(new Date());
	   	            	if(edate!=""&&edate!=null){
	   	            	 //console.log("sdate_id:"+$("#sdate_id").val()+"sdate_id:"+$("#edate_id").val())
	   	            		//alert("_sdate:"+_sdate+"_edate:"+_edate+"mediaName:"+mediaName+"marketingCode"+marketingCode);
	   	        			if(edate < sdate){
	   	        				layer.tips('开始时间不能大于结束时间', '#sdate_id',{
	   	        					tips: [1,'#8F8888']
	   	        				});
	   	        				return false;
	   	        			}else
			   	            	if(sdate > nowDate){
			   	            		layer.tips('开始时间不能当前时间', '#sdate_id',{
		   	        					tips: [1,'#8F8888']
		   	        				});
			   	            		return false;
			   	            	}else
				   					if(edate > nowDate){
				   						layer.tips('开始时间不能大于当前时间', '#edate_id',{
			   	        					tips: [1,'#8F8888']
			   	        				});
				   	            		return false;
				   	            	}
	   	        			init(sdate,edate,uname,role,user_name);
	   	        		}else{
	   	        			//墨绿深蓝风
	   	        			layer.alert('请选择查询时间', {
	   	        			  skin: 'layui-layer-molv' //样式类名
	   	        			  ,closeBtn: 0
	   	        			});
	   	        		}
	   				});
	   			}
	   			else if(data == "客户"){
	   				layer.alert('您不是管理员，没有权限！', {
					    skin: 'layui-layer-molv' //样式类名
					  ,closeBtn: 0
					},function(){
						window.location.href = "../schedule/exportReport.html";
					});
	   				
	   			}
	   			else if(data.indexOf("接口人") >= 0 ||  data.indexOf("支撑") >=0 || data.indexOf("监测") >=0){
	   				layer.alert('您不是管理员，没有权限！', {
					    skin: 'layui-layer-molv' //样式类名
					  ,closeBtn: 0
					},function(){
						window.location.href = "../index/index.html";
					});
	   				
	   			}
	   			else{
	   				/*alert(resultJson);*/
	   				layer.alert('您不是管理员，没有权限！', {
					    skin: 'layui-layer-molv' //样式类名
					  ,closeBtn: 0
					}, function(){
						/*window.location.href = "../user/mytodo.html";*/
					});
	   				
	   			}
	   			
	   			
	   		});
           //2222222222
	       //22222222
        });
	function FormatDate(strTime){
		var d = new Date(strTime);
		var year=d.getFullYear();
		var day=d.getDate();
		var month=+d.getMonth()+1;
		var hour=d.getHours();
		var minute=d.getMinutes();
		var second=d.getSeconds();
		var misec=d.getMilliseconds();
		var f=year+"-"+formate(month)+"-"+formate(day);
		return f;
	 }
	 function formate(d){
			 return d>9?d:'0'+d;
		 }
	
 	function init(sdate,edate,uname,role,user_name){//初始化
			$("#mb").css("display", "block");
			 layer.load(2);// 加载等待效果
			$.post("../../user/queryUser.do", {
    			'sdate' : sdate,
    			'edate' : edate,
    			'uname'	:uname,
    			'role':role,
    			'user_name':user_name,
    		}, function (result, resultState) {
	        		layer.closeAll();
	            	var resultJson = eval(result);//
	            	//console.log(resultJson);
	              	if(resultJson!=""&&resultJson!=null){
	              		$.each(resultJson, function (i, item) { 
							if (item!= "") {
								item.createDate=FormatDate(item.createDate);
								if(item.userState==0){ 	
									item.userState="在职";
								}else if(item.userState==1){
									item.userState="离职";
									
								}
							}
						}); 
	            		$("#tbody_id").loadTemplate($("#template"),resultJson);
	            		
	            		$("tr").each(function(){
	            			var ss = $(this).find("td").eq(7).text();
	            			if(ss=="离职"){
	            				$(this).css({color:"#B0AEAE"});
	            				/*$(this).css({background:"#B5D9E4" });*/
	            			}
	            		});
	            	}else{
	            	  $("#tbody_id").html("<td colspan=9 id='sumtd'><span>未查询到相关数据!</span></td>");
	            	}
	        });
		}
 	function del(obj){
 		var userName= $(obj).parents("tr").children("td").eq(1).text();
 		//alert(userName);
 		layer.confirm('您确定要删除此用户？', {
			  btn: ['确定','取消'] //按钮
			}, function(){
				$.post("../../user/deleteUserByusername.do", {
					'userName' : userName
				}, function(result) {
					//alert(result);
					if(result == 0){
						parent.layer.msg('删除失败', {icon: 2});
					}else if(result == 1){
						layer.msg('删除成功', {icon: 1});
						$(obj).parents("tr").remove();
					}
		        });
			});
 	}
		
 	function update(obj){
 		var username= $(obj).parents("tr").children("td").eq(1).text();
 		window.location.href="./updateUser.html?userName="+username;
 	}
 	
 	
 	
