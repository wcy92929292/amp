/**
 * 监测方案-增加、修改、查询、备注 JS
 * 
 */

/*******************************************************************************
 * 添加监测方案或子方案
 * 
 * @author lily
 */
var parent_idf;
var parent_actCode;
function addMonitorPlan() {
	var demo = formCheck();// 表单验证
	if (demo.check() == true) {
		$("#customer_id").removeAttr("disabled");
		if($(".m_s_title").text()=="添加活动方案子方案"){
			//子活动添加
			parent_idf = 1;
			parent_actCode=getUrlParam('actCode');
			
		}else{
			parent_idf = 0;
			parent_actCode=null;
		}
		
		//当前方案的状态信息
		if($('.addScheme input').prop('checked') == true){
			var res = save(); // 下部分的参数
			if(res == undefined || res == "undefined"){
				return false;
			}
		}else{
			res = '';
		}
		var planformPer = $('#formId').serialize(); // 上部分参数
//		alert(planformPer);
//		planformPer = decodeURIComponent(planformPer, true);
//		alert(planformPer);
//		if(planformPer.indexOf('%') != -1 || planformPer.indexOf('#') != -1 || planformPer.indexOf('$') != -1
//			|| planformPer.indexOf('^') != -1 || planformPer.indexOf('*') != -1){
//			layer.msg('活动名称不能有特殊符号');
//			return false;
//		}
		//*****************发Todo**********************//*
		// 活动编号 当前状态 下一个状态 任务类别(0活动任务，1 点位)
		activityCode = getUrlParam('actCode');
		nowState = '0';
		nextState = '1';
		ofTaskType = '0';
//		$.post("/amp/amp/addTodoTask.do", {'activityCode':activityCode,'nowState':nowState,'nextState':nextState,'ofTaskType':ofTaskType}, function(msg) {
//			if (msg.message != "1") {
//				layer.msg(msg.message, function() {
//				});
//			} 
//			else {
//				location.reload();
//				// window.location.href = "./monitorPlan.html";
//				// location.href="memoMonitorPlan.html#two";
//			}
//		});
//		alert(res)
		//活动增加请求
		$.post('/amp/amp/saveActivity.do', 'param=' + res + '&' + planformPer +"&parent_idf="+parent_idf+
				'&'+'parent_actCode='+parent_actCode,
				 function(msg){
			if (msg.message != "1") {
				layer.msg(msg.message, function() {
				});
			} else {
				layer.msg("添加成功！准备跳转至监测方案页面");
					window.location.href = "./monitorPlan.html";
			}
		});
		
	} else {
		layer.msg('页面存在未填写内容，请补充完整！', function() {
		});
	}
}


/**
 * 保存方案信息
 */
function save(){
	
	var tr = $('.trlen2');
	var len = tr.length;
	var error;
	var res = '[';
	var flag = true;
	
	tr.each(function(j){
		var td = $(this).children();// 获取每一个tr
		
		var key = td.eq(0).find('select').val(); // key
		var value = td.eq(1).find('select').val(); // value
		var op = td.eq(2).find('input').val(); // op
		
		var dcsid_s = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(0).val();
		var btn_type = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('select').eq(0).val();
		var btn_name = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(1).val();
		var btn_id = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(2).val();
		var page_url = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(3).val();
		var match_url = td.parent().parent().parent().parent().parent().find('.tab1 tr').find('input').eq(4).val();
		
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
		
		key = '';
		value = '';
		op = '';
		dcsid_s = '';
		btn_id = '';
		page_url = '';
		match_url = '';
		
		if(j != len -1){
			res += '{"page_name":"' + page_name 
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
			res += '{"page_name":"' + page_name 
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
	}
}


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
																'<option value="=">=</option>' +
																'<option value="~">~</option>' +
															'</select>' +
														'</td>' +
														'<td>' +
															'<button type="button" style="display: none;" class="button_style add2" value="新增规则">新增规则</button>' +
															'<button type="button" style="display: none;" class="button_style bg remove2" value="删除">删除</button>' +
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
											'<option style="display: none;" value="" selected disabled="disabled">接口人填写</option>' +
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
											'<button type="button" style="display: none;" class="button_style add2" value="新增规则">新增规则</button>' +
											'<button type="button" style="display: none;" class="button_style bg remove2" value="删除">删除</button>' +
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
						'<button type="button" style="display: none;" class="button_style add2" value="新增规则">新增规则</button>' +
						'<button type="button" style="display: none;" class="button_style bg remove2" value="删除">删除</button>' +
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
			
			This.parents("tr.trlen2").remove();//先做删除操作
			layer.closeAll();//关闭所有弹窗
		}, function(){//取消操作执行
			
		});
	});
});


/*******************************************************************************
 * 获取监测方案信息
 * 
 * @returns {String}
 */
function getMonitor() {
	// 拼接元素
	var res = '[';
	var len = $('#table_id tbody').find('tr').length;
	$("#table_id tbody").find("tr").each(
			function(i) {
				var tdArr = $(this).children();// 获取每一列
				var page_name = tdArr.eq(0).find("input").val();// 页面名称
				var but_name = tdArr.eq(2).find("input").val();// 按钮名称
				var but_event = tdArr.eq(4).find("input").val();// 按钮事件
				var but_invo_index = tdArr.eq(5).find("select").val();// 统计类别
				var but_type = tdArr.eq(6).find("select").eq(parseInt(but_invo_index) - 1).val();// 统计类别
				
				if (i != len - 1) {
					res += '{"page_name":"' + page_name + '","button_name":"'
							+ but_name + '","button_event":"' + but_event
							+ '","involve_index":"' + but_invo_index
							+ '","button_type":"' + but_type + '"},';
				} else {
					res += '{"page_name":"' + page_name + '","button_name":"'
							+ but_name + '","button_event":"' + but_event
							+ '","involve_index":"' + but_invo_index
							+ '","button_type":"' + but_type + '"}';
				}

			});
	res += ']';
    
	return res;

}

/*******************************************************************************
 * 查询系统用户并区分角色
 */
function queryUser() {
	$.post("/amp/user/queryUserById.do", {
		'roleId' : '0'
	}, function(data) {
		for (var i = 0; i < data.length; i++) {
			if (data[i]["role_id"] == "2" || data[i]["role_id"] == "8" || data[i]["role_id"] == "10" || data[i]["role_id"] == "11") {
				// 监测中心人员
				$("#monitor_peopleID").append(
						"<option value=" + data[i]['user_id'] + ">"
								+ data[i]['real_name'] + "</option>");
			}
			if (data[i]["role_id"] == "4") {
				// 前端支撑人员
				$("#font_support_peopleID").append(
						"<option value=" + data[i]['user_id'] + ">"
								+ data[i]['real_name'] + "</option>");
			} 
			if (data[i]["role_id"] == "1" || data[i]["role_id"] == "9" || data[i]["role_id"] == "10" || data[i]["role_id"] == "11") {
				// 后端支撑人员
				$("#after_support_peopleID").append(
						"<option value=" + data[i]['user_id'] + ">"
								+ data[i]['real_name'] + "</option>");
			}
		}
	});
}

/*******************************************************************************
 * 查询投放单位
 */
function queryCustomer() {
	var actCode = getUrlParam('actCode');// 活动编号 ，只有添加子活动存在活动编号
	var cus = $("#customer_id").find("option:selected").text();
	$.post('/amp/amp/queryCustomer.do', {
		"customer_id" : cus
	}, function(data) {
		for (var i = 0; i < data.length; i++) {
			$("#customer_id").append(
					"<option value=" + data[i]['customer_id'] + ">"
							+ data[i]['customer_name'] + "</option>");
			$("#customer_id").find("option").eq(i + 1).attr("code",
					data[i]['customer_code']);
		}
		if (actCode != null) {
			queryActNameAndCus();
		}
	});
}
/*******************************************************************************
 * 添加子方案需要处理的内容
 */
function queryActNameAndCus() {
	var actCode = getUrlParam('actCode');// 活动编号 ，只有添加子活动存在活动编号
	$.post('/amp/amp/queryActNameAndCus.do', {
		'actCode' : actCode
	}, function(data) {
		// 添加父级活动名称，为disabled
		$("#f_activity_name").val(data[0]["activity_name"]);
		// 选中父级的活动客户名称
		$("[name=customer_id]").find("option").each(function() {
			if (this.innerText == data[0]["customer_name"]) {
				$(this).attr("selected", "selected");
			}
		});
		$("#customer_id").attr("disabled", "disabled");
		$("#s_code").val(data[0]["customer_code"]);
		
	});
}
/*******************************************************************************
 * 判断是否存在活动编号
 * 
 * @author lily
 */
function checkActCode() {
	var actCode = getUrlParam('actCode');// 活动编号 ，只有添加子活动存在活动编号

	if (actCode != null) {
		// 活动编号不为空表示添加子级活动，将父级活动编号的li标签显示
		$(".parent_idf").css('display', 'block');
		$("#parent_activity_code").val(actCode);
		$(".m_s_title").text("添加活动方案子方案");
	}
}
/*******************************************************************************
 * table 增加行数
 */
/*function addLine() {
	
	var line = '<tr>'
		+ '<td > <input type="text" placeholder="" class="text-input text-input-1 plan-table-input"/></td>'
		+ '<td > <input type="text" placeholder="此处由后端支撑填写" class="text-input text-input-1 plan-table-input"  disabled="disabled"/></td>'
		+ '<td > <input type="text" placeholder="" class="text-input text-input-1 plan-table-input"/></td>'
		+ '<td > <input type="text" placeholder="此处由后端支撑填写" class="text-input text-input-1 plan-table-input" disabled="disabled" /></td>'
		+ '<td > <input type="text" placeholder="" class="text-input text-input-1 plan-table-input"/></td>'
		+ '<td > <select name="involve_index"><option value="1">有效行为转化率</option><option value="2">业务成功转化率</option></select></td>'
		+ '<td > <select class="button_type">'
		+ '<option value="11">终端</option><option value="12">业务</option><option value="13">充值</option><option value="14">推广</option><option value="15">配件</option><option value="16">终端和配件</option></select>'
		+ '<select class="button_type" style="width: 8em; padding-top: .3em; height: 26px; margin: 0; display:none;"><option value="21">买手机</option><option value="22">办业务</option><option value="23">办套餐</option><option value="24">挑配件</option></select></td>'
		+ '<td style="text-align:center;"><input type="button" onclick="deleteLine(this);" value="删除" class="button_style"/></td>'
		+ '</tr>';
	
	$("#table_id tbody").append(line);
	
	//页面初始化的时候增加二级联动
	var _tr = $("#table_id tbody").find("tr");
	var _option;
	_tr.eq(_tr.length-1).find("select[name='involve_index']").change(function(){
		_option = $(this).find("option");
		for(i = 0;i<_option.length;i++){
			if(_option[i].selected){
				$(this).parents("tr").find(".button_type").eq(i).show();
			}else{
				$(this).parents("tr").find(".button_type").eq(i).hide();
			}
		}
	});
		//$(this).change();]
		
		
	
}*/
/*******************************************************************************
 * table 删除行数
 * 
 * @param obj
 */
/*function deleteLine(obj) {
	$(obj).parent().parent().remove();
}*/

/*******************************************************************************
 * 添加活动备注
 * 
 * @author lily
 */
function addTodoInfo() {
	var demo = formCheck();// 表单验证
	if (demo.check() == true) {
		var planformPer = $('#formId').serialize();
		console.log(planformPer);
		$.post("/amp/amp/addTodoTask.do", planformPer, function(msg) {
			if (msg.message != "1") {
				layer.msg(msg.message, function() {
				});
			} 
//			else {
//				location.reload();
//				// window.location.href = "./monitorPlan.html";
//				// location.href="memoMonitorPlan.html#two";
//			}
		});
	} else {
		layer.msg('页面存在未填写内容，请补充完整！', function() {
		});
	}
}

/*******************************************************************************
 * 查询活动备注
 * 
 * @author lily
 */
function queryTodoInfo() {
	var actCode = getUrlParam('actCode');// 活动编号 ，只有添加子活动存在活动编号
	$.post('/amp/amp/queryTodoInfo.do', {
		'actCode' : actCode,
		'sdate' : $("#sdate_id").val(),
		'edate' : $("#edate_id").val(),
		'type' : $("#type_id").val(),
		'userId':''
	}, function(data) {
		$("#quoteId").empty();
		if (data.length > 0) {
			for (var i = 0; i < data.length; i++) {
				var li = "<li class='todo-li'></li>";
				var userBox = "<div class='userBox'>"
						+ "<div class='user-name'>" + "<strong>"
						+ data[i]['realName'] + "</<strong>" + "</div>"
						+ "<div class='msg-state'>"
						+ "<span class='scolor color1'>" + data[i]['todoState']
						+ "</span>" + "<span class='msg-time'>"
						+ data[i]['createDate'] + "</span>" + "</div>";
				var msgCont = "<div class='msg-cont'>" + data[i]['todoContent']
						+ "</div>";
				var msgState = "<div class='msg-type'>" + data[i]['todoType']
						+ "</div>"

				$("#quoteId").append(li);
				$("#quoteId li").eq(i).append(userBox,msgCont,msgState);

			}
		}
	});
}
/*******************************************************************************
 * 添加备注 根据角色编号查询系统用户
 */
function queryUserChange(obj) {
	console.log($(obj).children('option:selected').val());
	var js_roleId = $(obj).children('option:selected').val()
	if (js_roleId != '') {
		// 查询系统用户并区分角色
		$.post("/amp/user/queryUserById.do", {
			'roleId' : js_roleId
		}, function(data) {
			if (data.length > 0) {
				$("#peopleImpId").empty();
				for (var i = 0; i < data.length; i++) {
					$("#peopleImpId").append(
							"<option value=" + data[i]['user_id'] + ">"
									+ data[i]['real_name'] + "</option>");
				}
			}
		});
	} else {
		$("#peopleImpId").empty();
		$("#peopleImpId").append("<option value=''>--请选择--</option>");
	}
}
/*******************************************************************************
 * 活动备注上传图片
 * 
 * @author lily
 */
function uploadImg() {
	/*
	 * $('#test').uploadify({ 'swf' : '../js/com/uploadify.swf', 'uploader' :
	 * '/amp/upload/uploadImg.do', 'formData': {'projectid': '${project.id}'},
	 * 'fileObjName' : 'file', 'fileSizeLimit' : '0', 'onQueueComplete' :
	 * function(queueData) { alert(queueData.uploadsSuccessful + ' files were
	 * successfully uploaded.'); }
	 */
	$('#test').diyUpload({
		url : '/amp/upload',
		success : function(data) {
			console.info(data);
		},
		error : function(err) {
			console.info(err);
		},
		msg : '点击选择图片',
		accept : {
			title : "Images",
			extensions : "gif,jpg,jpeg,bmp,png",
			mimeTypes : "image/*"
		}
	});
}


jQuery(document).ready(function() {
	//判断是否登录
	$.post("../../campaign/getUserSession.do", {}, function(data){
		if(data=="error"){
		location.href = "../login/login.html";
	}});
	
	checkMoni();
});

function checkMoni(){
	$.post('../../user/getUser.do',{},function(res){
		
		if(res.role_ID  != 1){
			
			$('.addRmove-container').each(function(){
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
			});
		}
	});
}

//是否勾选了添加方案信息
$(".addScheme input").change(function(){ //周报的
	if($('.addScheme input').prop('checked') == true){
		$('.addRmove-container').css('display','block');
		check();
	}else{
		$('.addRmove-container').css('display','none');
	}
});

function check(){
	$(document).on("blur", "input", function(){
		if($(this).val() == '' || $(this).val() == null){
			if($(this).attr('title') == "undefined" || $(this).attr('title') == undefined){
				return false;
			}
			layer.msg($(this).attr('title')+"不能为空");
		}
	});
}
