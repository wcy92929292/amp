var page_name;
var but_name;
var but_event;
var but_invo_index;
var but_type;
var object;
function init(sdate,edate,channel){
$("#mb").css("display", "block");
        var resultJson = eval(a);
        $("#tbody_id").loadTemplate($("#template"),resultJson);
        $("#mb").css("display","none");
       	//关闭等待效果
		layer.closeAll();
		timeOutLayer();
}
function add_data(sdate,edate,channel){
	//iframe层
	layer.open({
	    type: 2,
	    title: '添加活动监测方案',
	    shadeClose: true,
	    shade: 0.8,
	    area: ['893px', '600px'],
	    content: './addMonitorPlan.html'
	    
	}); 
}
//function addLine(){
//	var line = '<tr scheme_id = "0" isupdate="0"><td style="position:relative; overflow:hidden;"><input placeholder="" class="text-input text-input-1" type="text" id="pageName" style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td>'
//				+ '<td style="position:relative; overflow:hidden;"><input disabled="disabled"	placeholder="此处由后端支撑填写" class="text-input text-input-1" type="text" id="pageName" 	style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td>' 
//				+ '<td style="position:relative; overflow:hidden;"><input placeholder="" class="text-input text-input-1" type="text" id="pageName" style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td>'
//				+ '<td style="position:relative; overflow:hidden;" ><input disabled="disabled"	placeholder="此处由后端支撑填写" class="text-input text-input-1"	type="text" id="pageName"	style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td>'
//				+ '<td style="position:relative; overflow:hidden;" ><input	placeholder="" class="text-input text-input-1"	type="text" id="pageName" style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td>' 
//				+ '<td ><select name="involve_index" style="width: 7em; padding-top: .3em; height: 26px; margin: 0;">'
//				+ '<option value="1">有效行为转化率</option><option value="2">业务成功转化率</option></select></td>' 
//				+' <td>'
//				+ '<select class="button_type" style="width: 7em; padding-top: .3em; height: 26px; margin: 0;">'
//				+ '<option value="11">终端</option><option value="12">业务</option><option value="13">充值</option>'
//				+ '<option value="14">推广</option><option value="15">配件</option><option value="16">终端和配件</option></select>'
//				+ '<select class="button_type" style="width: 7em; padding-top: .3em; height: 26px; margin: 0;  display:none;">'
//				+ '<option value="21">买手机</option><option value="22">办业务</option><option value="23">办套餐</option>'
//				+ '<option value="24">挑配件</option></select>' 
//				+'</td> '
//				+ '<td><input style="margin:auto;" name="" type="button" onclick="deleteLine(this);" value="删除" id="delete" class="button_style class"/></td></tr>';
//				
//				$("#table_id").append(line);
//				//页面初始化的时候增加二级联动
//				var _tr = $("#table_id tbody").find("tr");
//				var _option;
//				_tr.eq(_tr.length-1).find("select[name='involve_index']").change(function(){
//					_option = $(this).find("option");
//					for(i = 0;i<_option.length;i++){
//						if(_option[i].selected){
//							$(this).parents("tr").find(".button_type").eq(i).show();
//						}else{
//							$(this).parents("tr").find(".button_type").eq(i).hide();
//						}
//					}
//				});
//}


var parent_idf;
jQuery(document).ready(function() {
	//判断是否登录
	$.post("../../campaign/getUserSession.do", {}, function(data){
		if(data=="error"){
		location.href = "../login/login.html";
	}});
	var loca = location.toString();
	actCode = loca.substring(loca.indexOf("actCode=")+8,loca.length);
	
	//客户信息列表
	var cus = $("#customer_id").find("option:selected").text();
	$.post('/amp/amp/queryCustomer.do',{"customer_id":cus} ,function(data){
		for(var i = 0; i<data.length;i++){
			$("#customer_id").append("<option value="+data[i]['customer_id']+">"+data[i]['customer_name']+"</option>");
		  }
		//显示当前活动的所属客户名称
		if($(".m_s_title").text()=="修改子活动方案"){
			//子活动添加
			parent_idf = 1;
		}else{
			parent_idf = 0;
		}
		$.post('/amp/amp/queryActAndCus.do',{actCode:actCode,parent_idf:parent_idf} ,function(data){
			 $("#customer_id").find("option").each(function(){
				if(this.innerText == data[0]["customer_name"]){
					$(this).attr("selected","selected");
				}			
			});
		});	
		}
	);	
//获取监测中心、前端监测、后端监测人员下拉信息
$.post('/amp/amp/showMonitorPlan.do',{actCode:actCode},function(data){
	//活动为父级标识为0时，父级活动编号隐藏，子级标识为1，显示父级活动
	if(data[0]["parent_idf"] == 1 ){
		$("#activity_code").val(data[0]["activity_code"]);
		$("#parent_activity_code").val(data[0]["parent_activity_code"]);
		$(".m_s_title").text("修改子活动方案");
	}else{
		$(".parent_idf").hide();
		$("#activity_code").val(data[0]["activity_code"]);
	}
	
	
	$('select[name=stat_mark] option').each(function(){
		if($(this).attr('value') == data[0]["stat_mark"]){
			$(this).attr('selected','selected');
		} 
	});
	
	
	//返回的监测方案数据动态生成 table tr，填充数据
	for(var i =0;i<data.length;i++){
		//一次性生成的数据，其中没有用到的，不做显示,设定取得第一个值为undefined时，不再动态生成行
		if(data[i]["page_name"]==undefined){
			break;
		}
		if(data[i]["page_url"] == null ||data[i]["page_url"] ==""){
			data[i]["page_url"] = "未填写";
		}
		
		if(data[i]["button_id"] == null ||data[i]["button_id"] ==""){
			data[i]["button_id"] = "未填写";
		}
//		var line = '<tr isupdate = "-1" scheme_id = '+data[i]["scheme_id"]+' involve_index='+data[i]["involve_index"]+' button_type='+data[i]["button_type"]+'><td><input value= "'+data[i]["page_name"]+'"	placeholder="" class="text-input text-input-1" 	type="text" id="page_name"/></td> ' 
//				   + '<td style="position:relative; overflow:hidden; width:179px;"><input disabled="disabled" value='+data[i]["page_url"]+'	placeholder="此处由后端支撑填写"  class="text-input text-input-1" type="text" id="page_url" style="min-width:179px; position: absolute;right: 0;bottom: 8px; border:none; background:none; box-shadow:none; -webkit-box-shadow:none; -moz-box-shadow:none;"/></td>'
//				   + '<td><input value= "'+data[i]["button_name"]+'" placeholder="" class="text-input text-input-1" 	type="text" id="button_name"/></td> '
//				   + '<td><input disabled="disabled" value='+data[i]["button_id"]+'	placeholder="此处由后端支撑填写" class="text-input text-input-1"	type="text" id="button_id"/></td> '
//				   + '<td><input value= "'+data[i]["button_event"]+'"	placeholder="" class="text-input text-input-1"	type="text" id="button_event"/></td>'
//				   + '<td><select name="involve_index" style="width:7em; padding-top: .3em; height: 26px; margin: 0;"><option value="1">有效行为转化率</option><option value="2">业务成功转化率</option></select></td>'
//				   + '<td>'
//				   + '<select class="button_type" style="width: 7em; padding-top: .3em; height: 26px; margin: 0;">'
//				   + '<option value="11">终端</option><option value="12">业务</option><option value="13">充值</option><option value="14">推广</option>'
//				   + '<option value="15">配件</option><option value="16">终端和配件</option></select>'
//				   + '<select class="button_type" style="width: 7em; padding-top: .3em; height: 26px; margin: 0;">'
//				   + '<option value="21">买手机</option><option value="22">办业务</option>'
//				   + '<option value="23">办套餐</option><option value="24">挑配件</option></select> '
//				   + '</td><td><input style="margin:auto;" name="" type="button" onclick="deleteLine(this);" value="删除" class="button_style modify"/></td></tr>';
//			
//			$("#table_id").append(line);
			
		//追加涉及指标的
//		var inx;
//		$("tr[involve_index]").each(function(){
//			inx = $(this).attr("involve_index");  
//			$(this).find("td").children("select").eq(0).val(inx);  //设置为指标为几的选中
//			
//			if(inx == 1){
//				$(this).find("select").eq(2).css("display","none");
//			}
//			if(inx == 2){
//				$(this).find("select").eq(1).css("display","none");
//			}
//		});
//		
//		var _tr = $("#table_id tbody").find("tr");
//		var _option;
//		_tr.eq(_tr.length-1).find("select[name='involve_index']").change(function(){
//			_option = $(this).find("option");
//			for(i = 0;i<_option.length;i++){
//				if(_option[i].selected){
//					$(this).parents("tr").find(".button_type").eq(i).show();
//				}else{
//					$(this).parents("tr").find(".button_type").eq(i).hide();
//				}
//			}
//		});
		
		//追加统计类别
//		$("tr[button_type]").each(function(){
//			var bty = $(this).attr("button_type");  
//			$(this).find("td").eq(6).children("select").val(bty);  //设置为统计类别的选中
//		});
	}
	
	//bang ding xiu gai tr
//	$("#tbody_id2 input").change(function(){
//		$(this).parents("tr").attr("isupdate","1");
//	});
//	
//	$("#tbody_id2 select").change(function(){
//		$(this).parents("tr").attr("isupdate","1");
//	});
//	
	
	//开始
	for(var i=1;i<data.length;i++){
		if( data[i]["role_id"] == "2" ||  data[i]["role_id"] == "8" ||  data[i]["role_id"] == "10" ||  data[i]["role_id"] == "11"){
			//追加监测中心的option的值
			$("#province_id_0").append("<option value="+data[i]['user_id']+">"+data[i]['real_name']+"</option>");
			//显示当前活动的所属监测人员
			 $.post('/amp/amp/queryMonitor.do',{actCode:actCode} ,function(data){
				 $("#province_id_0").find("option").each(function(){
					if(this.innerText == data[0]["real_name"]){
						$(this).attr("selected","selected");
					}		
				});
			});
//			   titleDiv();
		}
		if( data[i]["role_id"] == "4"){
			//追加前端监测option的值
			 $("#province_id_1").append("<option value="+data[i]['user_id']+">"+data[i]['real_name']+"</option>");
			//显示当前活动的所属前端监测人员
			 $.post('/amp/amp/queryFontSupport.do',{actCode:actCode} ,function(data){
				 $("#province_id_1").find("option").each(function(){
					if(this.innerText == data[0]["real_name"]){
						$(this).attr("selected","selected");
					}		
				});
			});	 
		}
		if( data[i]["role_id"] == "1" ||  data[i]["role_id"] == "9" ||  data[i]["role_id"] == "10" ||  data[i]["role_id"] == "11"){
			//追加后端监测option的值
			$("#province_id_2").append("<option value="+data[i]['user_id']+">"+data[i]['real_name']+"</option>");
			//显示当前活动的所属后端监测人员
			 $.post('/amp/amp/queryAfterSupport.do',{actCode:actCode} ,function(data){
				 $("#province_id_2").find("option").each(function(){
					if(this.innerText == data[0]["real_name"]){
						$(this).attr("selected","selected");
					}		
				});
			});	 
		}
		
		//获取活动的上线状态
		$.post('/amp/amp/activityState.do',{"actCode":actCode} ,function(data){
			var state = data[0].activity_state;
			setCookie("activityState",state);
			$('#activity_state').val(state);
			if(state == 0 ||state == 1 ||state == 2 ||state == 3||state == 4 || state == 5){
				//针对未上线的,除活动编号以外的可以修改
			}else if(state == 6){
				//针对已经上线的，只能修改角色人和活动名称
				$("#customer_id").attr("disabled","disabled");
				$("#sdate_id").attr("disabled","disabled");
				$("#activity_code").attr("disabled","disabled");
				$("#memo").attr("disabled","disabled");
				//新增按钮置灰
				$("#add_line").remove();
//				$("tfoot").remove();
//				$("#comfirm_id").removeClass("button_style class").addClass("disable_button_style comfirm_id").attr("disabled","disabled");
				
//				$("#table_id tbody").find("tr").each(function(i){
//			        var tdArr = $(this).find("td");//获取每一列
//			        tdArr.eq(7).find("input").removeClass("button_style class").addClass("disable_button_style comfirm_id").attr("disabled","disabled");
//			        if($(this).length == 0){
//			        	$("#table_id").append('<tr>qqq</tr>');
//			        }
//				});
//				$("#tbody_id2").find("input").attr("disabled","disabled");
//				$("#tbody_id2").find("select").attr("disabled","disabled");
					
			}else{
				//其他处理的
			}
		}
		);
}//结束
	
	$("#sdate_id").val(format(data[0]["predict_start_date"],'yyyy-MM-dd'));
	$("#activity_name").val(data[0]["activity_name"]);
	$("textarea").val(data[0]["memo"]);
	$("#activity_code").attr("disabled","disabled");
});

	
   $(".headers").loadTemplate("../../common/header.html", {});
   var date = new Date();
   var sdate=  cal_date_s(date,1);
   var edate=  cal_date_e(date,1);
   var channel="全部";
   
   init(sdate,edate,channel);
   $("#sdate_id").val(sdate);
   $("#edate_id").val(edate);
   $("#channel_id").val(channel);
   
   $("#add_line").on("click",
		function() {
       	   addLine();
		});
//   $("#delete_line").on("click",
//  			function() {
//	   alert(1);
//           deleteLine();
//  	});
 
   
   /**
    * 保存方案信息
    */
   function save(){
   	
   	var tr = $('.trlen2');
   	var len = tr.length;
   	var flag = true;
   	var res = '[';
   	
   	tr.each(function(j){
   		var td = $(this).children();// 获取每一个tr
   		
   		var key = td.eq(0).find('select').val(); // key
   		var value = td.eq(2).find('select').val(); // value
   		var op = td.eq(1).find('input').eq(0).val(); // op
   		
   		var scheme_id = td.eq(1).find('input').eq(1).val(); //scheme_id 
   		
   		var dcsid_s = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(0).val();
   		var btn_type = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('select').eq(0).val();
		var btn_name = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(1).val();
		var btn_id = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(2).val();
		var page_url = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(3).val();
		var match_url = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(4).val();;
   		
   		var page_name = td.parent().parent().parent().parent().parent().parent().find('.title input').eq(0).val();
   		var page_id = td.parent().parent().parent().parent().parent().parent().find('.title input').eq(1).val();
   		
   		if(scheme_id == '' || scheme_id == 'null' || scheme_id =="undefined" || scheme_id ==undefined){
   			scheme_id = 0;
   		}
   		
   		if(page_name == '' || page_name == null || page_name == 'null'){
			layer.msg('页面名称不能为空!');
			flag = false;
			return false;
		}
		
		if(page_id == '' || page_id == null || page_id == 'null'){
			layer.msg('页面序号不能为空!');
			flag = false;
			return false;
		}
		
		if(btn_type == '' || btn_type == null || btn_type == 'null'){
			layer.msg('按钮类型不能为空!');
			flag = false;
			return false;
		}
		
		if(btn_name == '' || btn_name == null || btn_name == 'null'){
			layer.msg('按钮名称为空!');
			flag = false;
			return false;
		}
		
   		
   		if(j != len -1){
   			res += '{"scheme_id":"' + scheme_id
   				+ '","page_name":"' + page_name 
   				+ '","page_id":"' + page_id 
   				+ '","dcsid_s":"' + dcsid_s 
   				+ '","btn_type":"' + btn_type
   				+ '","btn_name":"' + btn_name
   				+ '","btn_id":"' + btn_id
   				+ '","page_url":"' + page_url
   				+ '","match_url":"' + match_url
   				+ '","key":"' + key 
   				+ '","value":"' + value
   				+ '","op":"' + op + '"},';
   		}else{
   			res += '{"scheme_id":"' + scheme_id
   			+ '","page_name":"' + page_name
   			+ '","page_id":"' + page_id
   			+ '","dcsid_s":"' + dcsid_s 
   			+ '","btn_type":"' + btn_type
   			+ '","btn_name":"' + btn_name
   			+ '","btn_id":"' + btn_id
   			+ '","page_url":"' + page_url
   			+ '","match_url":"' + match_url
   			+ '","key":"' + key 
   			+ '","value":"' + value
   			+ '","op":"' + op + '"}';
   		}
   		
   	});
   	res += ']';

   	if(flag){
		return res;
	}else{
		return "undefined";
	}
   }

   
   //修改时向后台发送请求
   $("#comfirm_id").on("click",function() {
	   modifyMonitorPlan();
   });
   
  function modifyMonitorPlan(){
	   var demo = formCheck();// 表单验证
	   if (demo.check() == true) {
	   //修改时移除disabled属性
	    $("#customer_id").removeAttr("disabled");
		$("#sdate_id").removeAttr("disabled");
		$("#activity_code").removeAttr("disabled");
		$("#memo").removeAttr("disabled");
//	   var planformPer = decodeURIComponent($('#formId').serialize(),true);
		var planformPer = $('#formId').serialize();
	   
	   var res = save();
	   
	 //当前方案的状态信息
		if($('.addScheme input').prop('checked') == true){
			var res = save(); // 下部分的参数
			if(res == undefined || res == "undefined"){
				return false;
			}
		}else{
			res = '';
		}
	   var loca = location.toString();
	   actCode = loca.substring(loca.indexOf("actCode=")+8,loca.length);
	   var delId = getCookie('delId');

	   
//	   alert(res);
//	   
//	   alert(delId);
	   
	   var activityState = getCookie("activityState");
	   $.post('/amp/amp/updateMonitor.do','param='+res+'&'+planformPer+'&'+'activity_code='+actCode+'&delId='+delId+"&activityState="+activityState,
		   function(msg){
		   
		   		delCookie('delId')
				if (msg.message != "1") {
					layer.msg(msg.message, function() {
					});
				} else {
					layer.msg("操作成功！", function() {
						window.location.href = "./monitorPlan.html";
					});
				}
			});
//	   
	}else{
		layer.msg('页面存在未填写内容，请补充完整！', function() {
		});
	}
  } 
 
   $("#add_id").on("click",
		function() {
   	var _sdate=$("#sdate_id").val();
	var _edate=$("#edate_id").val();
	var _channel=$("#channel_id").val();
   		add_data(_sdate,_edate,_channel);
	});
	docScroll();
	
	/****************************************************************/
	/*2017年07月07日11:01:51*/
	$.post('/amp/amp/planBean.do',{'actCode':actCode},function(res){
		if(res[0].planInfo != '' ){ //包含指标的，先做情况操作。
			$('.container-children').remove();
			
			$('.addRmove-container').css('display','block');
			$('.addScheme input').prop('checked','checked');
		}
		
		for(var j=0;j<res[0].planInfo.length;j++){ //循环最外层
			var containerChildren = '<div class="container-children" id="div'+j+'">' +
			'<div class="top clearfix">' +
				'<div class="title fl">' +
					'<span>页面名称</span>' +
					'<input type="text" title="页面名称"  placeholder="请填写页面url" />' +
					'<span>页面序号</span>'+
					'<input type="text" title="页面序号"  placeholder="请填写页面序号" />'+
				'</div>' +
				'<div class="addRmove fr">' +
					'<input class="button_style add" type="button" value="新增页面" />' +
					'<input class="button_style bg remove" type="button" value="删除页面" />' +
				'</div>' +
			'</div>' +
			'<div class="bottom clearfix">' +
				'<button type="button" class="button_style fr bg remove1" value="删除按钮">删除按钮</button>' +
				'<button type="button" class="button_style fr mg add1" value="新增按钮">新增按钮</button>' +
				'<div class="tabParent">' +
					'<table class="tab1">' +
						'<thead>' +
							'<tr>' +
								'<th>dcsid_s</th>' +
								'<th>按钮类型</th>' +
								'<th>按钮名称</th>' +
								'<th>按钮ID</th>' +
								'<th>页面URL</th>' +
								'<th>匹配规则</th>' +
							'</tr>' +
						'</thead>' +
						'<tbody>' +
							'<tr>' +
								'<td><input type="text" /></td>' +
								'<td>' +
									'<select>' +
									'<option style="display: none;" selected value="" disabled="disabled">接口人填写</option>' +
									'<option value="PV">PV</option>' +
									'<option value="UV">UV</option>' +
									'<option value="VV">VV</option>' +
									'</select>' +
								'</td>' +
								'<td><input type="text" title="按钮名称"/></td>' +
								'<td><input type="text" /></td>' +
								'<td><input type="text" /></td>' +
								'<td><input type="text" /></td>' +
							'</tr>' +
						'</tbody>' +
					'</table>' +
					'<table class="tab2">' +
						'<thead>' +
							'<tr>' +
								'<th>KEY</th>' +
								'<th>OPERATION</th>' +
								'<th>VALUE</th>' +
								'<th>操作</th>' +
							'</tr>' +
						'</thead>' +
						'<tbody>' +
							'<tr class="trlen2">' +
								'<td>' +
									'<select>' +
										'<option value="WT.EVENT">WT.EVENT</option>' +
									'</select>' +
								'</td>' +
								'<td><input type="text" /></td>' +
								'<td>' +
									'<select>' +
										'<option style="display: none;" selected value="" disabled="disabled">后端填写</option>' +
										'<option value="=">=</option>' +
										'<option value="~">~</option>' +
									'</select>' +
								'</td>' +
								'<td>' +
									'<button type="button" class="button_style add2" value="新增规则">新增规则</button>' +
									'<button type="button" class="button_style bg remove2" value="删除">删除</button>' +
								'</td>' +
							'</tr>' +
						'</tbody>' +
					'</table>' +
				'</div>' +
			'</div>' +
		'</div>';
			
		
		$('.addRmove-container').append(containerChildren);
		
		$('#div'+j+'').find('.title input').eq(0).val(res[0].planInfo[j].page_NAME);
		$('#div'+j+'').find('.title input').eq(1).val(res[0].planInfo[j].page_ID);
		
		//清空原有的层
		$('#div'+j+'').find('.bottom').remove();
	  
		for(var i=0;i<res[0].planInfo[j].btn_INFO.length;i++){ //循环中层的结构
			
		var bottom = '<div class="bottom clearfix">' +
			'<button type="button" class="button_style fr bg remove1" value="删除按钮">删除按钮</button>' +
			'<button type="button" class="button_style fr mg add1" value="新增按钮">新增按钮</button>' +
			'<div class="tabParent">' +
				'<table class="tab1">' +
					'<thead>' +
						'<tr>' +
							'<th>dcsid_s</th>' +
							'<th>按钮类型</th>' +
							'<th>按钮名称</th>' +
							'<th>按钮ID</th>' +
							'<th>页面URL</th>' +
							'<th>匹配规则</th>' +
						'</tr>' +
					'</thead>' +
					'<tbody>' +
						'<tr>' +
							'<td><input type="text" /></td>' +
							'<td>' +
								'<select>' +
								'<option style="display: none;" selected value="" disabled="disabled">接口人填写</option>' +
								'<option value="PV">PV</option>' +
								'<option value="UV">UV</option>' +
								'<option value="VV">VV</option>' +
								'</select>' +
							'</td>' +
							'<td><input type="text" title="按钮名称"/></td>' +
							'<td><input type="text" /></td>' +
							'<td><input type="text" /></td>' +
							'<td><input type="text" /></td>' +
						'</tr>' +
					'</tbody>' +
				'</table>' +
				'<table class="tab2">' +
					'<thead>' +
						'<tr>' +
							'<th>KEY</th>' +
							'<th>OPERATION</th>' +
							'<th>VALUE</th>' +
							'<th>操作</th>' +
						'</tr>' +
					'</thead>' +
					'<tbody>' +
//						'<tr class="trlen2">' +
//							'<td>' +
//								'<select>' +
//									'<option value="WT.EVENT">WT.EVENT</option>' +
//								'</select>' +
//							'</td>' +
//							'<td>' +
//								'<select>' +
//									'<option value="=">=</option>' +
//									'<option value="~">~</option>' +
//								'</select>' +
//							'</td>' +
//							'<td><input type="text" /></td>' +
//							'<td>' +
//								'<button type="button" class="button_style add2" value="新增规则">新增规则</button>' +
//								'<button type="button" class="button_style bg remove2" value="删除">删除</button>' +
//							'</td>' +
//						'</tr>' +
					'</tbody>' +
				'</table>' +
			'</div>' +
		'</div>';
		
			$('#div'+j+'').append(bottom);  //中层模型填充
			checkMoni();
			
			//中层填充数据
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(0).val(res[0].planInfo[j].btn_INFO[i].dcsid_S);
//			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(1).val(res[0].planInfo[j].btn_INFO[i].btn_TYPE);
			
			if(trimStr($('#div'+j+'').find('.tab1').eq(i).find('tbody tr td').eq(0).find('input').val()) == ''){
				$('#div'+j+'').find('.tab1').eq(i).find('tbody tr td').eq(0).find('input').val('');
			}
			$('#div'+j+'').find('.tab1').eq(i).find('select option').each(function(){
				if(trimStr($(this).val()) == trimStr(res[0].planInfo[j].btn_INFO[i].btn_TYPE)){
					$(this).attr('selected',true);
				}
			});
			
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(1).val(res[0].planInfo[j].btn_INFO[i].btn_NAME);
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(2).val(res[0].planInfo[j].btn_INFO[i].btn_ID);
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(3).val(res[0].planInfo[j].btn_INFO[i].page_URL);
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(4).val(res[0].planInfo[j].btn_INFO[i].match_RULE);
			

//			$('#div'+j+'').find('.trlen2').remove(); //WT.EVENT模型初始化情况
			
			//循环WT.EVENT模型，并填充数据
			for(var p=0;p<res[0].planInfo[j].btn_INFO[i].key_VALUE.length;p++){

				var trlen = '<tr class="trlen2">' +
				'<td>' +
					'<select>' +
						'<option value="WT.EVENT">WT.EVENT</option>' +
					'</select>' +
				'</td>' +
				'<td><input type="text" /><input type="hidden" /></td>' +
				'<td>' +
					'<select>' +
						'<option style="display: none;" selected value="" disabled="disabled">后端填写</option>' +
						'<option value="=">=</option>' +
						'<option value="~">~</option>' +
					'</select>' +
				'</td>' +
				'<td>' +
					'<button type="button" class="button_style add2" value="新增规则">新增规则</button>' +
					'<button type="button" class="button_style bg remove2" value="删除">删除</button>' +
				'</td>' +
			'</tr>';
				
				$('#div'+j+'').find('.tab2').eq(i).find('tbody').append(trlen);
				checkMoni();
				
				var key = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].key;
				var value = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].value;
				var op = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].operation;
				
				var scheme_id = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].scheme_ID;
				
				//设置op
				$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('input').eq(0).val(op);
				
				$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('input').eq(1).val(scheme_id);

				//回显key-value
				$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('select option').each(function(){
					 if($(this).val() == value ){
						 $(this).attr('selected',true);
					 }
				})
				
			}
			
		}
		
		}
		
	});
});

var scheme_id;
var isupdate;
//function add(){
//	$("#activity_code").removeAttr("disabled");
//	//拼接元素
//	var res = '[';
//	var len = $('#table_id tbody').find('tr').length;
//	
//	$("#table_id tbody").find("tr").each(function(i){
//        var tdArr = $(this).find("td");//获取每一列
//        var page_name = tdArr.eq(0).find("input").val();//	页面名称
//        var but_name = tdArr.eq(2).find("input").val();//	按钮名称
//        var but_event = tdArr.eq(4).find("input").val();//	按钮事件
//        var but_invo_index = tdArr.eq(5).find("select").val();//  统计类别
//        var but_type = tdArr.eq(6).find("select").eq(parseInt(but_invo_index) - 1).val();//  统计类别
//        scheme_id = tdArr.parents().attr("scheme_id");  //方案ID修改监测方案信息
//        isupdate = tdArr.parents().attr("isupdate");  //方案ID修改监测方案信息
//		var loca = location.toString();
//		actCode = loca.substring(loca.indexOf("actCode=")+8,loca.length);
//
//		//添加操作
//		 if(i!=len-1){
//	        	res += '{"isupdate":"'+isupdate+'","scheme_id":"'+scheme_id+'","page_name":"'+page_name+'","button_name":"'+but_name+'","button_event":"'+but_event+'","involve_index":"'+but_invo_index+'","button_type":"'+but_type+'"},';
//	     }else{
//	        	res += '{"isupdate":"'+isupdate+'","scheme_id":"'+scheme_id+'","page_name":"'+page_name+'","button_name":"'+but_name+'","button_event":"'+but_event+'","involve_index":"'+but_invo_index+'","button_type":"'+but_type+'"}';
//	        }
//		
//    });
//	res += ']';
//	return res;
//}











/*******************************************************************************
 * 新增页面
 * */
$(function(){
	$(".addRmove-container").on("click", ".addRmove>.add", function(){
		
		var containerChildren = '<div class="container-children">' +
									'<div class="top clearfix">' +
										'<div class="title fl">' +
											'<span>页面名称</span>' +
											'<input type="text" placeholder="请填写页面url" />' +
											'<span>页面序号</span>'+
											'<input type="text"  placeholder="请填写页面序号" />'+
										'</div>' +
										'<div class="addRmove fr">' +
											'<input class="button_style add" type="button" value="新增页面" />' +
											'<input class="button_style bg remove" type="button" value="删除页面" />' +
										'</div>' +
									'</div>' +
									'<div class="bottom clearfix">' +
										'<button type="button" class="button_style fr bg remove1" value="删除按钮">删除按钮</button>' +
										'<button type="button" class="button_style fr mg add1" value="新增按钮">新增按钮</button>' +
										'<div class="tabParent">' +
											'<table class="tab1">' +
												'<thead>' +
													'<tr>' +
														'<th>dcsid_s</th>' +
														'<th>按钮类型</th>' +
														'<th>按钮名称</th>' +
														'<th>按钮ID</th>' +
														'<th>页面URL</th>' +
														'<th>匹配规则</th>' +
													'</tr>' +
												'</thead>' +
												'<tbody>' +
													'<tr>' +
														'<td><input type="text" /></td>' +
														'<td>' +
															'<select>' +
															'<option style="display: none;" selected value="" disabled="disabled">接口人填写</option>' +
															'<option value="PV">PV</option>' +
															'<option value="UV">UV</option>' +
															'<option value="VV">VV</option>' +
															'</select>' +
														'</td>' +
														'<td><input type="text" /></td>' +
														'<td><input type="text" /></td>' +
														'<td><input type="text" /></td>' +
														'<td><input type="text" /></td>' +
													'</tr>' +
												'</tbody>' +
											'</table>' +
											'<table class="tab2">' +
												'<thead>' +
													'<tr>' +
														'<th>KEY</th>' +
														'<th>OPERATION</th>' +
														'<th>VALUE</th>' +
														'<th>操作</th>' +
													'</tr>' +
												'</thead>' +
												'<tbody>' +
													'<tr class="trlen2">' +
														'<td>' +
															'<select>' +
																'<option value="WT.EVENT">WT.EVENT</option>' +
															'</select>' +
														'</td>' +
														'<td><input type="text" /></td>' +
														'<td>' +
															'<select>' +
																'<option style="display: none;" selected value="" disabled="disabled">后端填写</option>' +
																'<option value="=">=</option>' +
																'<option value="~">~</option>' +
															'</select>' +
														'</td>' +
														'<td>' +
															'<button type="button" class="button_style add2" value="新增规则">新增规则</button>' +
															'<button type="button" class="button_style bg remove2" value="删除">删除</button>' +
														'</td>' +
													'</tr>' +
												'</tbody>' +
											'</table>' +
										'</div>' +
									'</div>' +
								'</div>';
		$(".addRmove-container").append(containerChildren);
		checkMoni();
	});
});

/*删除页面*/
$(function(){
	$(".addRmove-container").on("click", ".addRmove>.remove", function(){
		
		var This = $(this);
		var len = $(".addRmove-container").children(".container-children").length;
		
		var str = '';
		console.log(This.find('tbody tr').html())
		This.parent().parent().parent().find('.tab2 tbody tr').each(function(){
				var delId = $(this).find('td').eq(2).find('input').eq(1).val();
				 str += delId+',';
			});
		
		
		/*删除页面之前先做判断是否小于1条了*/
		if(len-1 < 1){
			layer.tips('至少保留一条！', This, {
				tips: [1, '#3595CC'],
				time: 4000
			});
			return false;
		}
		
		layer.confirm('确定是否删除！', {
			btn: ['确定', '取消']
		}, function(){//确定删除执行
			
			
			if( getCookie('delId') == null){
				setCookie('delId',str);
			}else{
				var newStr = getCookie('delId') + ',' + str;
				setCookie('delId',newStr);
			}
			
			This.parents(".container-children").remove();//先做删除操作
			layer.closeAll();//关闭所有弹窗
		}, function(){//取消操作执行
			
		});
	});
});


/*新增按钮 add1*/
$(function(){
	$(".addRmove-container").on("click", ".add1", function(){
		
		var bottom = '<div class="bottom clearfix">' +
						'<button type="button" class="button_style fr bg remove1" value="删除按钮">删除按钮</button>' +
						'<button type="button" class="button_style fr mg add1" value="新增按钮">新增按钮</button>' +
						'<div class="tabParent">' +
							'<table class="tab1">' +
								'<thead>' +
									'<tr>' +
										'<th>dcsid_s</th>' +
										'<th>按钮类型</th>' +
										'<th>按钮名称</th>' +
										'<th>按钮ID</th>' +
										'<th>页面URL</th>' +
										'<th>匹配规则</th>' +
									'</tr>' +
								'</thead>' +
								'<tbody>' +
									'<tr>' +
										'<td><input type="text" /></td>' +
										'<td>' +
											'<select>' +
											'<option style="display: none;" selected value="" disabled="disabled">接口人填写</option>' +
											'<option value="PV">PV</option>' +
											'<option value="UV">UV</option>' +
											'<option value="VV">VV</option>' +
											'</select>' +
										'</td>' +
										'<td><input type="text" /></td>' +
										'<td><input type="text" /></td>' +
										'<td><input type="text" /></td>' +
										'<td><input type="text" /></td>' +
									'</tr>' +
								'</tbody>' +
							'</table>' +
							'<table class="tab2">' +
								'<thead>' +
									'<tr>' +
										'<th>KEY</th>' +
										'<th>OPERATION</th>' +
										'<th>VALUE</th>' +
										'<th>操作</th>' +
									'</tr>' +
								'</thead>' +
								'<tbody>' +
									'<tr class="trlen2">' +
										'<td>' +
											'<select>' +
												'<option value="WT.EVENT">WT.EVENT</option>' +
											'</select>' +
										'</td>' +
										'<td><input type="text" /></td>' +
										'<td>' +
											'<select>' +
												'<option style="display: none;" selected value="" disabled="disabled">后端填写</option>' +
												'<option value="=">=</option>' +
												'<option value="~">~</option>' +
											'</select>' +
										'</td>' +
										'<td>' +
											'<button type="button" class="button_style add2" value="新增规则">新增规则</button>' +
											'<button type="button" class="button_style bg remove2" value="删除">删除</button>' +
										'</td>' +
									'</tr>' +
								'</tbody>' +
							'</table>' +
						'</div>' +
					'</div>';
		$(this).parents(".container-children").append(bottom);
		checkMoni();
	});
});

/*删除按钮 remove1*/
$(function(){
	$(".addRmove-container").on("click", ".remove1", function(){
		
		var This = $(this);
		var len = This.parents(".container-children").children("div.bottom").length;
		var str = ''
		/**
		 *遍历该模板下所有的scheme_id
		 */
		This.parent().find('.tab2').find('tbody tr').each(function(){
			var delId = $(this).find('td').eq(2).find('input').eq(1).val();
			
			 str += delId+',';
		});
		
		
		//删除页面之前先做判断是否小于1条了
		if(len-1 < 1){
			layer.tips('至少保留一条！', This, {
				tips: [1, '#3595CC'],
				time: 4000
			});
			return false;
		}
		
		layer.confirm('确定是否删除！', {
			btn: ['确定', '取消']
		}, function(){//确定删除执行
			
			if( getCookie('delId') == null){
				setCookie('delId',str);
			}else{
				var newStr = getCookie('delId') + ',' + str;
				setCookie('delId',newStr);
			}
			
			
			This.parent(".bottom").remove();//先做删除操作
			layer.closeAll();//关闭所有弹窗
		}, function(){//取消操作执行
			
		});
	});
});

/*新增单条 add2*/
$(function(){
	$(".addRmove-container").on("click", ".add2", function(){
		
		var tr = '<tr class="trlen2">' +
					'<td>' +
						'<select>' +
							'<option value="">WT.EVENT</option>' +
						'</select>' +
					'</td>' +
					'<td><input type="text" /></td>' +
					'<td>' +
						'<select>' +
							'<option style="display: none;" selected value="" disabled="disabled">后端填写</option>' +
							'<option value="=">=</option>' +
							'<option value="~">~</option>' +
						'</select>' +
					'</td>' +
					'<td>' +
						'<button type="button" class="button_style add2" value="新增规则">新增规则</button>' +
						'<button type="button" class="button_style bg remove2" value="删除">删除</button>' +
					'</td>' +
				'</tr>';
		$(this).parents(".tab2").children("tbody").append(tr);
		checkMoni();
	});
});

/*删除 remove2*/
$(function(){
	$(".addRmove-container").on("click", ".remove2", function(){
		
		var This = $(this);
		var len = This.parents(".tab2").find("tr.trlen2").length;
		
		var delId = $(this).parent().parent().find('td').eq(2).find('input').eq(1).val();
		
		/*删除页面之前先做判断是否小于1条了*/
		if(len-1 < 1){
			layer.tips('至少保留一条！', This, {
				tips: [1, '#3595CC'],
				time: 4000
			});
			return false;
		}
		
		layer.confirm('确定是否删除！', {
			btn: ['确定', '取消']
		}, function(){//确定删除执行
			
			if( getCookie('delId') == null){
				setCookie('delId',delId);
			}else{
				var newStr = getCookie('delId') + ',' + delId;
				setCookie('delId',newStr);
			}
			This.parents("tr.trlen2").remove();//先做删除操作
			layer.closeAll();//关闭所有弹窗
		}, function(){//取消操作执行
			
		});
	});
});


/*使编辑页面的input框文字居右显示*/
//function titleDiv()
//{
//	var oTbody = document.getElementById("tbody_id2");
//	var aIpt = oTbody.getElementsByTagName("input");
//	//console.log(aIpt.length);
//	for(var i=0; i<aIpt.length; i++)
//	{
//		if(i == 1 || i == 7 || i == 13 || i == 19 || i == 25 || i == 31 || i == 37 || i == 43 || i == 49 || i == 55)
//		{
//			aIpt[i].title = aIpt[i].value;
//			aIpt[i].style.width = parseInt(aIpt[i].value.length*7) + "px";
//		}
//	}
//};

window.onload = function (){
	$.post('../../user/getUser.do',{},function(res){
		
		if(res.role_ID  != 1){
			
			$('.container-children').each(function(){
				$(this).find('.title').find('input').each(function(){
					$(this).attr('placeholder',"接口人填写");
				})
			})
			
			$('.tab1').find('tbody tr').each(function(){
					$(this).find('td').each(function(i){
						if(i != 2){
							$(this).find('input').attr('disabled',true);
							$(this).find('input').attr('placeholder',"后端填写");
						}else{
							$(this).find('input').attr('placeholder',"接口人填写");
						}
					});
			});
			
			$('.tab2').find('tbody tr').each(function(){
				$(this).find('td').each(function(){
						$(this).find('select').attr('disabled',true);
						$(this).find('input').attr('disabled',true);
						$(this).find('input').attr('placeholder',"后端填写");
				});
			})
		}
	});
}

function checkMoni(){
$.post('../../user/getUser.do',{},function(res){
		
		if(res.role_ID  != 1){
			
			$('.container-children').each(function(){
				$(this).find('.fl').find('input').each(function(){
					$(this).attr('placeholder',"接口人填写");
				})
			})
			
			$('.tab1').find('tbody tr').each(function(){
					$(this).find('td').each(function(i){
						if(i != 2){
							$(this).find('input').attr('disabled',true);
							$(this).find('input').attr('placeholder',"后端填写");
						}else{
							$(this).find('input').attr('placeholder',"接口人填写");
						}
					});
			});
			
			$('.tab2').find('tbody tr').each(function(){
				$(this).find('td').each(function(){
						$(this).find('select').attr('disabled',true);
						$(this).find('input').attr('disabled',true);
						$(this).find('input').attr('placeholder',"后端填写");
				});
			})
		}
	});
}


function trimStr(str){
	return str.replace(/(^\s*)|(\s*$)/g,"");
}

//是否勾选了添加方案信息
$(document).on("change", ".addScheme input", function(){
	if($('.addScheme input').prop('checked') == true){
		$('.addRmove-container').css('display','block');
		checkBox();
	}else{
		$('.addRmove-container').css('display','none');
	}
});

function checkBox(){
	$(document).on("blur", "input", function(){
		if($(this).val() == '' || $(this).val() == null){
			if($(this).attr('title') == "undefined" || $(this).attr('title') == undefined){
				return false;
			}
			layer.msg($(this).attr('title')+"不能为空");
		}
	});
}

$(document).on("blur", "input", function(){
	if($(this).val() == '' || $(this).val() == null){
		if($(this).attr('title') == "undefined" || $(this).attr('title') == undefined){
			return false;
		}
		layer.msg($(this).attr('title')+"不能为空");
	}
});