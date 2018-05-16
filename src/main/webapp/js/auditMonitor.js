/****
 * 监测活动审查JS
 * by lp	2016-5-13
 * 
 * 
 * 
 * 
 */


var list = new Array();
jQuery(document)
		.ready(
				function() {
					/* $("#template-nav").loadTemplate("navbar.html", {}); */
					// $(".footers").loadTemplate("../footer.html", {});
					$(".headers").loadTemplate("../../common/header.html", {});

					var loca = location.toString();
					actCode = loca.substring(loca.indexOf("actCode=") + 8,
							loca.length);
					// 判断用户是否登录
					$.post("../../campaign/getUserSession.do", {}, function(
							data) {
						if (data == "error") {
							window.location.href = "../login/login.html";
						}
					});
					layer.load(2);
					
					
					$.post("/amp/monitorPlan/findByCode.do",{actCode:actCode},function (data){
						$("#activity_code").val(data.activityCode);
						$("#activity_name").val(data.activityName);
						$("#customer_id").val(data.customer.customerName);
						$("#sdate_id").val(FormatDate(data.predictStartDate));
						$("#province_id_0").val(data.monitorPeople.realName);
						$("#province_id_1").val(data.frontSupportPeople.realName);
						$("#province_id_2").val(data.afterSupportPeople.realName);
						$('textarea').val(data.memo);
					});
					
					// 客户信息列表
//					var cus = $("#customer_id").find("option:selected").text();
//					$.post('/amp/amp/queryCustomer.do', {
//						"customer_id" : cus
//					}, function(data) {
//						for (var i = 0; i < data.length; i++) {
//							$("#customer_id").val(data[i]['customer_name']);
//						}
//
//					});

//					// 获取监测中心、前端监测、后端监测人员下拉信息
//					$.post('/amp/amp/showMonitorPlan.do', {
//						actCode : actCode
//					}, function(data) {
//						// 活动为父级标识为0时，父级活动编号隐藏，子级标识为1，显示父级活动
//					
//						if (data[0]["parent_idf"] == 1) {
//							$("#activity_code").val(data[0]["activity_code"]);
//							$("#parent_activity_code").val(
//									data[0]["parent_activity_code"]);
//							$(".m_s_title").text("修改子活动方案");
//						} else {
//							$(".parent_idf").hide();
//							$("#activity_code").val(data[0]["activity_code"]);
//						}
//
//						// 开始
//						for (var i = 1; i < data.length; i++) {
//							alert(data[i]['real_name']);
//							
//							if (data[i]["role_id"] == "2" || data[i]["role_id"] == "8" || data[i]["role_id"] == "10" || data[i]["role_id"] == "11") { 
//								// 追加监测中心的option的值
//								$("#province_id_0").val(data[i]['real_name']);
//							}  
//							if (data[i]["role_id"] == "4") {
//								// 追加前端监测option的值
//								// alert(data[i]["real_name"]);
//								$("#province_id_1").val(data[i]["real_name"]);
//
//							} 
//							if (data[i]["role_id"] == "1" || data[i]["role_id"] == "9" || data[i]["role_id"] == "10" || data[i]["role_id"] == "11") {
//								// 追加后端监测option的值
//								$("#province_id_2").val(data[i]['real_name']);
//							}
//						}// 结束
//
//						// 页面追加val
//						$("#sdate_id").val(
//								FormatDate(data[0]["predict_start_date"]));
//						$("#activity_name").val(data[0]["activity_name"]);
//						$("textarea").val(data[0]["memo"]);
//					});
					
					$('[data-toggle="popover"]').popover({
						'animation' : true,
						'trigger' : 'hover',
					});

					disableOcx();// 当页面加载的时候执行此函数
					$.post('../../auditMonitor/queryMonitor.do',
									{
										actCode : actCode
									},
									function(data) {
										if (data != "" && data != null) {
											for (i = 0; i < data.length; i++) {
												list[i] = {
													id : data[i]["scheme_id"],
													pageName : data[i]["page_name"],
													buttonName : data[i]["button_name"],
													buttonEvent : data[i]["button_event"],
													involveIndex : to_String(data[i]["involve_index"]),
													buttonType : data[i]["button_type"]
												};
											}
											$("#tbody_id").loadTemplate(
													$("#template"), eval(list));
										} else {
											$("#tbody_id")
													.html(
															"<td colspan=7 id='sumtd'><span>未查询到相关数据!</span></td>");
											$("#exit_id").show();
											$("#comfirm_id").show();
											$("#cancal_id").show();
//											$("#comfirm_id").hide();
//											$("#cancal_id").hide();
										}
//										titleDiv();
									});
					layer.closeAll();
//					$("#comfirm_id").attr("disabled", true);
					$("#comfirm_id").css("background-color", "#B7B7B7");
					$("#exit_id").on("click", function() {
						document.getElementById("back").click();
					});
//						$(".container-children").each(function() {
//							$(this).find('.tab2').each(function(k){
//								var tr = $(this).find('tbody tr');
//								
//								var dcsid_s = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(0).find('input').val();
//								var btn_id = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(3).find('input').val();
//								var page_url = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(4).find('input').val();
//								var match_url = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(5).find('input').val();
//								
//								var keys = tr.find('td').eq(0).find('select').val();
//								var value = tr.find('td').eq(1).find('select').val();
//								var op = tr.find('td').eq(2).find('input').eq(0).val();
//								var scheme_id = tr.find('td').eq(2).find('input').eq(1).val();
//								
//								if(dcsid_s == '' || dcsid_s ==null){
//									layer.msg('dcsid_s不能为空');
//									return flag = false;
//								}else if(btn_id == '' || btn_id ==null){
//									layer.msg('按钮ID不能为空');
//									return flag = false;
//								}else if(page_url == '' || page_url ==null){
//									layer.msg('页面URL不能为空');
//									return flag = false;
//								}else if(keys == '' || keys == null){
//									layer.msg('KEY不能为空');
//									return flag = false;
//								}else if(value == '' || value == null){
//									layer.msg('VALUE不能为空');
//									return flag = false;
//								}else if(op == '' || op == null){
//									layer.msg('OPERATION不能为空');
//									return flag = false;
//								}else{
//									flag = true;
//									if(flag){
//											$.post('../../auditMonitor/updateMonitor.do', {
//												actCode : actCode,
//												'dcsid_s' : dcsid_s,
//												'btn_id' : btn_id,
//												'page_url' : page_url,
//												'match_url' : match_url,
//												'key' : keys,
//												'value' : value,
//												'op' : op,
//												'scheme_id' : scheme_id,
//												ACTIVITY_STATE : '1'
//											}, function(data) {
//												if (data == "success") {
//													if( k == 0){
//														layer.msg('修改成功');
//														window.location.href = "../monitorPlan/monitorPlan.html?back";
//													}
//												}else if(data == "error"){
//													layer.msg('修改失败');
//												}
//										});
//									}
//								}
////								
//							});
//							
//						});
						
//						if($("#tbody_id tr").length == 0){
//							$.post('../../auditMonitor/updateMonitor.do', {
//								actCode : actCode,
//								id : {},
//								pageUrl : {},
//								buttonId : {},
//								ACTIVITY_STATE : '1'
//							}, function(data) {
//								if (data == "success") {
//									layer.msg('修改成功');
//									window.location.href = "../monitorPlan/monitorPlan.html?back";
//								}else if(data == "error"){
//									layer.msg('修改失败');
//								}
//							});
//						}


//					$("#cancal_id").on("click", function() {
//						$(".container-children").each(function() {
//							$(this).find('.tab2').each(function(){
//								var tr = $(this).find('tbody tr');
//								
//								var dcsid_s = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(0).find('input').val();
//								var btn_id = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(3).find('input').val();
//								var page_url = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(4).find('input').val();
//								var match_url = tr.parent().parent().parent().find('.tab1 tbody').find('td').eq(5).find('input').val();
//
//								var keys = tr.find('td').eq(0).find('select').val();
//								var value = tr.find('td').eq(1).find('select').val();
//								var op = tr.find('td').eq(2).find('input').eq(0).val();
//								var scheme_id = tr.find('td').eq(2).find('input').eq(1).val();
//								
//								if(dcsid_s == '' || dcsid_s ==null){
//									layer.msg('dcsid_s不能为空');
//									return flag = false;
//								}if(btn_id == '' || btn_id ==null){
//									layer.msg('按钮ID不能为空');
//									return flag = false;
//								}if(page_url == '' || page_url ==null){
//									layer.msg('页面URL不能为空');
//									return flag = false;
//								}
//								if(keys == '' || keys == null){
//									layer.msg('KEY不能为空');
//									return flag = false;
//								}
//								if(value == '' || value == null){
//									layer.msg('VALUE不能为空');
//									return flag = false;
//								}
//								if(op == '' || op == null){
//									layer.msg('OPERATION不能为空');
//									return flag = false;
//								}
//								
//								flag = true;
//								if(flag){
//									$.post('../../auditMonitor/updateMonitor.do', {
//										actCode : actCode,
//										'dcsid_s' : dcsid_s,
//										'btn_id' : btn_id,
//										'page_url' : page_url,
//										'match_url' : match_url,
//										'key' : keys,
//										'value' : value,
//										'op' : op,
//										'scheme_id' : scheme_id,
//										ACTIVITY_STATE : '2'
//									}, function(data) {
//										if (data == "success") {
//											layer.msg('修改成功');
//											window.location.href = "../monitorPlan/monitorPlan.html?back";
//										}else if(data == "error"){
//											layer.msg('修改失败');
//										}
//									});
//								}
//								
//							});
//							
//						});
//
//						});
////						if($("#tbody_id tr").length == 0){
////							if($("#tbody_id tr").length == 0){
////								$.post('../../auditMonitor/updateMonitor.do', {
////									actCode : actCode,
////									id : {},
////									pageUrl : {},
////									buttonId : {},
////									ACTIVITY_STATE : '2'
////								}, function(data) {
////									if (data == "success") {
////										layer.msg('修改成功');
////										window.location.href = "../monitorPlan/monitorPlan.html?back";
////									}else if(data == "error"){
////										layer.msg('修改失败');
////									}
////								});
////							};
////						}
//					});

					docScroll();
					
					
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
									'<input type="text" placeholder="请填写页面url" />' +
									'<span>页面序号</span>'+
									'<input type="text" placeholder="请填写页面序号" />'+
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
												'<td><input type="text"  title="dcsid_s" /></td>' +
												'<td>' +
													'<select>' +
													'<option value="PV">PV</option>' +
													'<option value="UV">UV</option>' +
													'<option value="VV">VV</option>' +
													'</select>' +
												'</td>' +
												'<td><input type="text" title="按钮名称" /></td>' +
												'<td><input type="text" title="按钮ID" /></td>' +
												'<td><input type="text" title="页面URL" /></td>' +
												'<td><input type="text" title="匹配规则" /></td>' +
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
												'<td><input type="text" title="OPERATION" /></td>' +
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
											'<td><input type="text" title="dcsid_s"/></td>' +
											'<td>' +
											'<select>' +
											'<option value="PV">PV</option>' +
											'<option value="UV">UV</option>' +
											'<option value="VV">VV</option>' +
											'</select>' +
										'</td>' +
											'<td><input type="text" title="按钮名称" /></td>' +
											'<td><input type="text" title="按钮ID" /></td>' +
											'<td><input type="text" title="页面URL" /></td>' +
											'<td><input type="text" title="匹配规则" /></td>' +
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
//										'<tr class="trlen2">' +
//											'<td>' +
//												'<select>' +
//													'<option value="WT.EVENT">WT.EVENT</option>' +
//												'</select>' +
//											'</td>' +
//											'<td>' +
//												'<select>' +
//													'<option value="=">=</option>' +
//													'<option value="~">~</option>' +
//												'</select>' +
//											'</td>' +
//											'<td><input type="text" /></td>' +
//											'<td>' +
//												'<button type="button" class="button_style add2" value="新增规则">新增规则</button>' +
//												'<button type="button" class="button_style bg remove2" value="删除">删除</button>' +
//											'</td>' +
//										'</tr>' +
									'</tbody>' +
								'</table>' +
							'</div>' +
						'</div>';
						
							$('#div'+j+'').append(bottom);  //中层模型填充
							
							//中层填充数据
							$('#div'+j+'').find('.tab1').eq(i).find('input').eq(0).val(res[0].planInfo[j].btn_INFO[i].dcsid_S);
//							$('#div'+j+'').find('.tab1').eq(i).find('input').eq(1).val(res[0].planInfo[j].btn_INFO[i].btn_TYPE);
							$('#div'+j+'').find('.tab1').eq(i).find('select option').each(function(){
								if(trimStr($(this).val()) == trimStr(res[0].planInfo[j].btn_INFO[i].btn_TYPE)){
									$(this).attr('selected',true);
								}
							});
							
							$('#div'+j+'').find('.tab1').eq(i).find('input').eq(1).val(res[0].planInfo[j].btn_INFO[i].btn_NAME);
							$('#div'+j+'').find('.tab1').eq(i).find('input').eq(2).val(res[0].planInfo[j].btn_INFO[i].btn_ID);
							$('#div'+j+'').find('.tab1').eq(i).find('input').eq(3).val(res[0].planInfo[j].btn_INFO[i].page_URL);
							$('#div'+j+'').find('.tab1').eq(i).find('input').eq(4).val(res[0].planInfo[j].btn_INFO[i].match_RULE);
							
							$('#div'+j+'').find('.tab1').each(function(){
								if( trimStr($(this).find('tr td').eq(0).find('input').val()) == ''){
									$(this).find('tr td').eq(0).find('input').val('');
								}else{
									$(this).find('tr td').eq(0).find('input').val(trimStr($(this).find('tr td').eq(0).find('input').val()));
								}
								
								
								$(this).find('tr td').eq(1).find('select').attr('disabled',true);
								$(this).find('tr td').eq(2).find('input').attr('disabled',true);
								$(this).find('tr td').eq(0).find('input').attr('placeholder','后端输入')
								$(this).find('tr td').eq(3).find('input').attr('placeholder','后端输入')
								$(this).find('tr td').eq(4).find('input').attr('placeholder','后端输入')
								$(this).find('tr td').eq(5).find('input').attr('placeholder','后端输入')
							}) ;
							
							$('#div'+j+'').find('.bottom').each(function(){
								$(this).find('button').eq(0).hide();
								$(this).find('button').eq(1).hide();
							})
							
//							$('#div'+j+'').find('.trlen2').remove(); //WT.EVENT模型初始化情况
							
							//循环WT.EVENT模型，并填充数据
							for(var p=0;p<res[0].planInfo[j].btn_INFO[i].key_VALUE.length;p++){

								var trlen = '<tr class="trlen2">' +
								'<td>' +
									'<select>' +
										'<option value="WT.EVENT">WT.EVENT</option>' +
									'</select>' +
								'</td>' +
								'<td><input type="text" title="OPERATION"/><input type="hidden"/></td>' +
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
								
								//后端对应的按钮
//								$('#div'+j+'').find('.trlen2').each(function(){
//									$(this).find('td').eq(3).find('button').hide()
//								})
								
								var key = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].key;
								var value = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].value;
								var op = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].operation;
								
								var scheme_id = res[0].planInfo[j].btn_INFO[i].key_VALUE[p].scheme_ID;
								
								//设置op
								$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(2).find('input').eq(0).val(op);
								$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('input').eq(0).attr('placeholder','后端输入')
								
								$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('input').eq(1).val(scheme_id);
								

								//回显key-value
								$('#div'+j+'').find('.tab2').eq(i).find('tbody').find('tr').eq(p).find('td').eq(1).find('select option').each(function(){
									 if($(this).val() == value ){
										 $(this).attr('selected',true);
									 }
								});
								
							}
							
						}
						
						}
						
					});
					
				});

// 返回String字符
function to_String(str) {
	if (str == "1") {
		return "有效行为转换率";
	} else if (str == "2") {
		return "业务成功转换率";
	}
}


/*新增单条 add2*/
$(function(){
	$(".addRmove-container").on("click", ".add2", function(){
		
		var tr = '<tr class="trlen2">' +
					'<td>' +
						'<select>' +
							'<option value="WT.EVENT">WT.EVENT</option>' +
						'</select>' +
					'</td>' +
					'<td><input type="text" title="OPERATION" /></td>' +
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
			btn: ['确定', '删除']
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

// 判断表格中是否均与赋值,给予按钮属性
//function upperCase() {
//	$("#tbody_id tr").each(function() {
//		var a = $(this).find("#pageName").val();
//		var b = $(this).find("#buttonName").val();

//		if (a != "" && b != "") {
//			$("#comfirm_id").attr("disabled",false);
//			$("#comfirm_id").css("background-color", "#3F97DD");
//			$("#cancal_id").attr("disabled",true);
//			$("#cancal_id").css("background-color", "#B7B7B7");
//		}else{
//			$("#comfirm_id").attr("disabled",true);
//			$("#comfirm_id")
//			.css("background-color", "#B7B7B7");
//			$("#cancal_id").attr("disabled",false);
//			$("#cancal_id").css("background-color", "#3F97DD");
//		}
//	});
//}
/* 	//判断表格中同一列是否有重复
 function hasRepeat(objId, columnIndex) {
 var arr = [];
 $("#" + objId + " tbody tr").each(function() {
 arr.push($("td:eq(" + columnIndex + ")", this).text());
 });
 if (arr.length == $.unique(arr).length) {
 return false;
 } else {
 return true;
 }
 } */

//使表单不可编辑
function disableOcx() {
	var form = document.forms[0];
	for (var i = 0; i < form.length; i++) {
		var element = form.elements[i];
		//部分元素可以编号 element.name 是元素自定义 name 
		if (element.name != "" && element.name != null) {
			element.disabled = "true";
		}
	}
};

//使编辑页面的input框文字居右显示
//function titleDiv()
//{
//	var oTbody = document.getElementById("tbody_id");
//	var aIpt = oTbody.getElementsByTagName("input");
//	//alert(aIpt.length)
//	for(var i=0; i<aIpt.length; i++)
//	{
//		if(i == 1 || i == 7 || i == 13 || i == 19)
//		{
//			aIpt[i].title = aIpt[i].value;
//		}
//	}
//};

function trimStr(str){
	return str.replace(/(^\s*)|(\s*$)/g,"");
}

$(document).on("blur", "input", function(){
	if($(this).attr('title') == "undefined" || $(this).attr('title') == undefined){
		return false;
	}else if($(this).attr('title') == '匹配规则' ){
		return false;
	}else if($(this).attr('title') == 'dcsid_s'){
			if($(this).val() == '' || $(this).val() == null){
				layer.msg('dcsid_s不能为空');
				return false;
			}else if($(this).val().length !=4){
				layer.msg('dcsid_s长度只能是4位');
				return false;
			}
	}else if($(this).val() == '' || $(this).val() == null){
		layer.msg($(this).attr('title')+"不能为空");
	}else if($(this).val() !=''  && $(this).attr('title') == '页面URL'){
		var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
		if(!reg.test($(this).val())){
			layer.msg($(this).attr('title')+"输入不正确");
		}
	}
});


function save(){

	
	var tr = $('.trlen2');
	var len = tr.length;
	var error;
	var res = '[';
	var flag = true;
	
	tr.each(function(j){
		var td = $(this).children();// 获取每一个tr
		
//		var keys = td.eq(0).find('select').val(); // key
//		var value = td.eq(1).find('select').val(); // value
//		var op = td.eq(2).find('input').val(); // op
//		
		var keys = td.eq(0).find('select').val(); // key
   		var value = td.eq(2).find('select').val(); // value
   		var op = td.eq(1).find('input').eq(0).val(); // op
		
//		var scheme_id = td.eq(2).find('input').eq(1).val(); //scheme_id
		var scheme_id = td.eq(1).find('input').eq(1).val(); //scheme_id
		
		var dcsid_s = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(0).val();
		var btn_type = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('select').eq(0).val();
		var btn_name = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(1).val();
		var btn_id = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(2).val();
		var page_url = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(3).val();
		var match_url = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(4).val();
		
		var page_name = td.parent().parent().parent().parent().parent().parent().find('.title input').eq(0).val();
		var page_id = td.parent().parent().parent().parent().parent().parent().find('.title input').eq(1).val();
		
		var page_name = td.parent().parent().parent().parent().parent().parent().find('.title input').eq(0).val();
		var page_id = td.parent().parent().parent().parent().parent().parent().find('.title input').eq(1).val();
		
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
		
		if(scheme_id == '' || scheme_id == 'null' || scheme_id =="undefined" || scheme_id ==undefined){
   			scheme_id = 0;
   		}
		
		if(dcsid_s == '' || dcsid_s ==null){
			layer.msg('dcsid_s不能为空');
			return flag = false;
		}else if(dcsid_s != '' && dcsid_s != null && dcsid_s.length !=4){
			layer.msg('dcsid_s长度只能是4位');
			return flag = false;
		}else if(btn_id == '' || btn_id ==null){
			layer.msg('按钮ID不能为空');
			return flag = false;
		}else if(page_url == '' || page_url ==null){
			layer.msg('页面URL不能为空');
			return flag = false;
		}else if(keys == '' || keys == null){
			layer.msg('KEY不能为空');
			return flag = false;
		}else if(op == '' || op == null){
			layer.msg('OPERATION不能为空');
			return flag = false;
		}else if(value == '' || value == null || value=='null'){
			layer.msg('VALUE不能为空');
			return flag = false;
		}
		
		if(j != len -1){
			res += '{"page_name":"' + page_name 
				+ '","page_id":"' + page_id 
				+ '","dcsid_s":"' + dcsid_s 
				+ '","scheme_id":"' + scheme_id
				+ '","btn_type":"' + btn_type
				+ '","btn_name":"' + btn_name
				+ '","btn_id":"' + btn_id
				+ '","page_url":"' + page_url
				+ '","match_url":"' + match_url
				+ '","key":"' + keys
				+ '","value":"' + value
				+ '","op":"' + op + '"},';
		}else{
			res += '{"page_name":"' + page_name 
				+ '","page_id":"' + page_id 
				+ '","dcsid_s":"' + dcsid_s
				+ '","btn_type":"' + btn_type
				+ '","btn_name":"' + btn_name
				+ '","scheme_id":"' + scheme_id
				+ '","btn_id":"' + btn_id
				+ '","page_url":"' + page_url
				+ '","match_url":"' + match_url
				+ '","key":"' + keys
				+ '","value":"' + value
			+ '","op":"' + op + '"}';
		}
		
	});
	
	
	res += ']';
	if(flag){
		return res;
	}

}

function detail(input){
	var res = save(); // 下部分的参数
	var loca = location.toString();
	actCode = loca.substring(loca.indexOf("actCode=") + 8,
			loca.length);
	if(res == undefined || res == "undefined"){
		return false;
	}
	
	if($(input).val() == "具备上线条件"){
		ACTIVITY_STATE = 1;
	}else{
		ACTIVITY_STATE = 2;
	}
	alert(res)
	var delId = getCookie('delId');
	
	$.post('../../auditMonitor/updateMonitor.do', 'param=' + res+"&ACTIVITY_STATE="+ACTIVITY_STATE+"&actCode="+actCode+'&delId='+delId, 
			function(data) {
				delCookie('delId');
				
				if (data == "success") {
					layer.msg('操作成功!');
					window.location.href = "../monitorPlan/monitorPlan.html?back";
				}else if(data == "error"){
					layer.msg('操作失败');
				}
	});
}