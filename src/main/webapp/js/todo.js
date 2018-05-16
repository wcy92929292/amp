/**
 * Todo JS 
 * @author lily
 */

/*******************************************************************************
 * 根据用户编号查询活动
 * 
 * @author lily
 */
function queryActivityByUser() {
	 
	
	var userId = getCookie('userId');
	$.get('/amp/amp/queryActivityByUser.do', {
		'userId' : userId
	}, function(data) {
		if (data.length > 0) {
			// data.length的长度就是当前tab页的个数，所以在此for循环中，处理tab以及tab对应的content的DIV
			for (var i = 0; i < data.length; i++) {
				var li, todoDiv;
				if (i == 0) {
					li = "<li class='active'><a href=#con" + i
							+ " data-toggle='tab'>" + data[i]['activityName']
							+ "</a></li>";
					todoDiv = "<div class='tab-pane fade active in' id='con"
							+ i + "'><ul  class='topicsList'></ul></div>";
				} else {
					li = "<li><a href=#con" + i + " data-toggle='tab'>"
							+ data[i]['activityName'] + "</a></li>";
					todoDiv = "<div class='tab-pane fade ' id='con" + i
							+ "'><ul class='topicsList'></ul></div>";
				}
				$("#myTab").append(li);
				$("#myTabContent").append(todoDiv);
				queryTodoInfo(data[i]['activityCode'], i);

			}
		}
		//网页加载全部的li之后要执行的这个函数
		getLi();
	});
}

/*******************************************************************************
 * 查询活动下的Todo
 * 
 * @author lily
 */
function queryTodoInfo(actCode, top) {
	var userId = getCookie('userId');
	$.get('/amp/amp/queryTodoInfo.do', {
		'actCode' : actCode,
		'sdate' : '',
		'edate' : '',
		'type' : '100',
		'userId' : userId
	}, function(data) {
		if (data.length > 0) {

			var li = "<li class='todo-li'></li>";

			for (var i = 0; i < data.length; i++) {
				// 用户、状态、 时间
				var userBox = "<div class='userBox'>"
						+ "<div class='user-name'>"
						+ data[i]['realName'] + "</div>"
						+ "<span class='msg-time'>"
						+ data[i]['createDate'] + "</span>"
						+ "</div>";
				// 内容1 (含有checkbook)
				var msgCont = "<div class='msg-cont'>"
						+ "<div class='fl' id='confl"+data[i]['todoCode']+"'>"
						+ data[i]['todoContent']
						+ "</div>"
						+ "<div class='fr msg-state'>"
						+ "<span class='scolor color1'>"
						+ data[i]['todoState']
						+ "</span> <input type='checkbox' class='m-check' id='check' value='"
						+ data[i]['todoCode']
						+ "' onclick='checkClickFun(this)'>"
						+ "</div>";
				// 内容2 (没有checkbox)
				var msgCont2 = "<div class='msg-cont' id='confl"+data[i]['todoCode']+"'>"
						+ "<div class='fl'>"
						+ data[i]['todoContent'] + "</div>"
						+ "<div class='fr msg-state'>"
						+ "<span class='scolor color1'>"
						+ data[i]['todoState'] + "</span>"
						+ "</div>";
				// 类型
				var msgType = "<div class='msg-type'>"
						+ data[i]['todoType'] + "</div>"
				//短代码
				var ahref="<br><a  href='/amp/page/check/check.html?mic="+data[i]['mic']+"'>短代码为："+data[i]['mic']+"</a>"
				//对tab页下的div添加li
				$("#con" + top).find("ul").append(li);
				//处理li添加那些div ;'已完成'状态不需要checkbook展示，否则需要
				if (data[i]['todoState'] == '已完成') {
					$("#con" + top).find("ul").find("li").eq(i)
							.append(userBox, msgCont2, msgType);
				} else {
					$("#con" + top).find("ul").find("li").eq(i)
							.append(userBox, msgCont, msgType);
				}
				if(data[i]['todoType'] == '点位任务'){
					$("#confl" + data[i]['todoCode'])
					.append(ahref);
				}

			}
		}
	});
}

/*******************************************************************************
 * 更新Todo状态
 * 
 * @param check
 */
function checkClickFun(check) {
	// checkbook 被选中
	if (check.checked == true) {
		alert(check.value);
		// layer 提示框
		layer.confirm('确定修改当前Todo状态？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			// 确定操作，更新Todo状态
			$.get('/amp/amp/updateTodoState.do', {
				'todoId' : check.value
			}, function(data) {
				if (data.message != "1") {
					layer.msg(msg.message, function() {
					});
				} else {
					// layer.msg('Todo状态更新成功', function() {});
					location.reload();

				}
			});
		}, function() {
			// 取消操作
		});
	}
}

function addTodoInfo2() {
	var actCode = getUrlParam('actCode');// 活动编号 ，只有添加子活动存在活动编号
	var planformPer = 'activityCode=JSM16051201&nowState=1&nextState=2&todoType=0&ofTaskType=0'
	// 活动编号 当前状态 下一个状态 任务类别(0活动任务，1 点位)

	console.log(planformPer);
	$.post("/amp/amp/addTodoTask.do", planformPer, function(msg) {
		if (msg.message != "1") {
			layer.msg(msg.message, function() {
			});
		} else {
			location.reload();
			// window.location.href = "./monitorPlan.html";
			// location.href="memoMonitorPlan.html#two";
		}
	});

}