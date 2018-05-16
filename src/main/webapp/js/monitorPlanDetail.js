var page_name;
var but_name;
var but_event;
var but_invo_index;
var but_type;
var object;
//function init(){
//$("#mb").css("display", "block");
//        var resultJson = eval(a);
//        $("#tbody_id").loadTemplate($("#template"),resultJson);
//        $("#mb").css("display","none");
//       	//关闭等待效果
//		layer.closeAll();
//		timeOutLayer();
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
//	
	//获取监测中心、前端监测、后端监测人员下拉信息
	$.post('/amp/amp/monitorPlanDetail.do',{actCode:actCode},function(data){
		//活动为父级标识为0时，父级活动编号隐藏，子级标识为1，显示父级活动
		if(data[0]["parent_idf"] == 1 ){
			$("#activity_code").val(data[0]["activity_code"]);
			$("#parent_activity_code").val(data[0]["parent_activity_code"]);
			$("#customer_id").val(data[0]["customer_name"]);
			$("#province_id_0").val(data[0]["monitor_people_name"]);
			$("#province_id_1").val(data[0]["font_support_people_name"]);
			$("#province_id_2").val(data[0]["after_support_people_name"]);
			$('textarea').val(data[0]["memo"])
			$(".m_s_title").text("子活动方案信息");
		}else{
			$(".parent_idf").hide();
			$("#activity_code").val(data[0]["activity_code"]);
			$("#customer_id").val(data[0]["customer_name"]);
			$("#province_id_0").val(data[0]["monitor_people_name"]);
			$("#province_id_1").val(data[0]["font_support_people_name"]);
			$("#province_id_2").val(data[0]["after_support_people_name"]);
			$('textarea').val(data[0]["memo"])
		}
	//	
	//	//返回的监测方案数据动态生成 table tr，填充数据
	//	for(var i =0;i<data.length;i++){
	//		//一次性生成的数据，其中没有用到的，不做显示,设定取得第一个值为undefined时，不再动态生成行
	//		if(data[i]["page_name"]==undefined){
	//			break;
	//		}
	//		if(data[i]["page_url"] == null||data[i]["page_url"] == ""){
	//			data[i]["page_url"] = "未填写";
	//		}
	//		
	//		if(data[i]["button_id"] == null||data[i]["button_id"] == ""){
	//			data[i]["button_id"] = "未填写";
	//		}
	//		var line = '<tr isupdate = "-1" scheme_id = '+data[i]["scheme_id"]+' involve_index='+data[i]["involve_index"]+' button_type='+data[i]["button_type"]+'><td ><input disabled="disabled" value= "'+data[i]["page_name"]+'"	placeholder="" class="text-input text-input-1" 	type="text" id="page_name" 	style="width:100%;height:26px;padding-left:3px;border-radius:5px;" /></td> ' 
	//				   + '<td style="position:relative; overflow:hidden; width:213px;"><input disabled="disabled" value='+data[i]["page_url"]+'	placeholder="此处由后端支撑填写" class="text-input text-input-1" type="text" id="page_url" style="min-width:213px; position:absolute; right:0; bottom:8px;border:none; background:none; box-shadow:none; -webkit-box-shadow:none; -moz-box-shadow:none;"/></td>'
	//				   + '<td><input disabled="disabled" value= "'+data[i]["button_name"]+'" placeholder="" class="text-input text-input-1" 	type="text" id="button_name" style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td> '
	//				   + '<td><input disabled="disabled" value= '+data[i]["button_id"]+'	placeholder="此处由后端支撑填写" class="text-input text-input-1"	type="text" id="button_id"	style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td> '
	//				   + '<td><input disabled="disabled" value= "'+data[i]["button_event"]+'"	placeholder="" class="text-input text-input-1"	type="text" id="button_event" style="width:100%;height:26px;padding-left:3px;border-radius:5px;"/></td>'
	//				   + '<td><input disabled="disabled" value= "'+data[i]["involve_index"]+'" style="width:10em; padding-top: .3em; height: 26px; margin: 0;">'
	//				   + '<td>'
	//				   + '<input disabled="disabled"value= "'+data[i]["button_type"]+'" style="width: 10em; padding-top: .3em; height: 26px; margin: 0;">'
	//				   + '</td>	</tr>';
	//			
	//			$("#table_id").append(line);
	//	}
	
	//	//开始
		$("#sdate_id").val(format(data[0]["predict_start_date"],'yyyy-MM-dd'));
		$("#activity_name").val(data[0]["activity_name"]);
		$("#memo").val(data[0]["memo"]);
		$("#activity_code").attr("disabled","disabled");
		
	//	titleDiv();
		
	});	

   $(".headers").loadTemplate("../../common/header.html", {});   
//   init();

   /****************************************************************/
	/*2017年07月07日11:01:51*/
	$.post('/amp/amp/planBean.do',{'actCode':actCode},function(res){
		
		if(res[0].planInfo != '' ){ //包含指标的，先做情况操作。
			$('.container-children').remove();
		}
		
		for(var j=0;j<res[0].planInfo.length;j++){ //循环最外层
			var containerChildren = '<div class="container-children" id="div'+j+'">' +
			'<div class="top clearfix">' +
				'<div class="title fl">' +
					'<span>页面名称</span>' +
					'<input type="text"  placeholder="请填写页面url" />' +
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
		
		$('#div'+j+'').find('.title input').attr('disabled',true);
		
		$('#div'+j+'').find('.fr').hide();
		

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
			
			//中层填充数据
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(0).val(res[0].planInfo[j].btn_INFO[i].dcsid_S);
//			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(1).val(res[0].planInfo[j].btn_INFO[i].btn_TYPE);
			$('#div'+j+'').find('.tab1').eq(i).find('select option').each(function(){
				if(trimStr($(this).val()) == trimStr(res[0].planInfo[j].btn_INFO[i].btn_TYPE)){
					$(this).attr('selected',true);
				}
			});
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(1).val(res[0].planInfo[j].btn_INFO[i].btn_NAME);
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(2).val(res[0].planInfo[j].btn_INFO[i].btn_ID);
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(3).val(res[0].planInfo[j].btn_INFO[i].page_URL);
			$('#div'+j+'').find('.tab1').eq(i).find('input').eq(4).val(res[0].planInfo[j].btn_INFO[i].match_RULE);
			
			
			$('#div'+j+'').find('.tab1 input').attr('disabled',true);
			$('#div'+j+'').find('.tab1 select').attr('disabled',true);
			
			$('#div'+j+'').find('.bottom button').hide();

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
				
				$('#div'+j+'').find('.tab2 input').attr('disabled',true);
				$('#div'+j+'').find('.tab2 select').attr('disabled',true);
				
				$('#div'+j+'').find('.trlen2').each(function(){
					$(this).find('td').eq(3).find('button').hide()
				})
				
				var key = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].key;
				var value = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].value;
				var op = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].operation;
				
				var scheme_id = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].scheme_ID;
				console.log(key + op + value)
				//设置op
				$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('input').eq(0).val(op);
				
				$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('input').eq(1).val(scheme_id);

				//回显key-value
				$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(2).find('select option').each(function(){
					 if($(this).val() == value ){
						 $(this).attr('selected',true);
					 }
				})
				
			}
			
		}
		
		}
		
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
//		if(i == 1 || i == 8 || i == 15 || i == 22 || i == 29 || i == 36 || i == 43 || i == 50 || i == 57 || i == 64)
//		{
//			aIpt[i].title = aIpt[i].value;
//			aIpt[i].style.width = parseInt(aIpt[i].value.length*7) + "px";
//		}
//	}
//};

function trimStr(str){
	return str.replace(/(^\s*)|(\s*$)/g,"");
}