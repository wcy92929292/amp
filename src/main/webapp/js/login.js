/****
 * 登錄JS
 * @user lp
 * @date 2016-04-07
 * 
 */

jQuery(document).ready(function() {
	var req = /^[a-zA-Z0-9]+$/;
	$("#username").on("blur", function() {
		if ($("#username").val() == "") {
			layer.tips('用户名不能为空', '#username', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
		} else if ($("#username").val().length > "20") {
			layer.tips('输入长度超过20位', '#username', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
			$("#username").val("");
		} else if (!req.test($("#username").val())) {
			layer.tips('请输入数字和字母', '#username', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
			$("#username").val("");
		} else {
			$.post("../../amp/QueryUserName.do", {
				'username' : $("#username").val(),
			}, function(data) {
				if (data == "0") {
					layer.tips('用户名不存在！', '#username', {
						tips : [ 2, '#FF0000' ],
						tipsMore : true
					});
				} else {
				}
			});
		}
	});
	$("#password").on("blur", function() {
		if ($("#password").val() == "") {
			layer.tips('密码不能为空！', '#password', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
		} else if ($("#password").val().length < "6") {
			layer.tips('密码不能少于6位！', '#password', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
		} else if ($("#password").val().length > "20") {
			layer.tips('密码长度超过20位！', '#password', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
			$("#password").val("");
		} else if (!req.test($("#password").val())) {
			layer.tips('密码输入格式有误！', '#password', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
			$("#password").val("");
		} else {
			$('#info').html('');
		}
	});
	$("#check_code").on("blur", function() {
		if ($("#check_code").val() == "") {
			layer.tips('验证码不能为空！', '#icodeli2', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
		} else if (!req.test($("#check_code").val())) {
			layer.tips('请输入正确的验证码！', '#icodeli2', {
				tips : [ 2, '#FF0000' ],
				tipsMore : true
			});
			$("#check_code").val("");
		} else {
			$('#info').html('');
		}
	});
	$("#comfirm_id").on("click", function() {
		var username = $("#username").val();
		var pwd = $("#password").val();
		var code = $("#check_code").val();
		if (username != "") {
			if (pwd != "") {
				if (code != "") {
					$.post(
							"../../amp/login.do",
							{
								"username" : $("#username").val(),
								"pwd" : $("#password").val(),
								"check_code" : $("#check_code").val()
							},
							function(data) {
							if (data.message == "验证码错误") {
								layer.tips('验证码错误！', '#icodeli2', {
									tips : [ 2, '#FF0000' ]
								});
							} else if (data.message == "index") {
								setCookie("roleName",data.roleName);
								parent.layer.msg('登录成功');
								window.location.href = "../index/index.html";
							} else if (data.message == "todo") {
								setCookie("roleName",data.roleName);
								parent.layer.msg('登录成功');
								window.location.href = "../index/index.html";
							} else if (data.message == "customer") {
								setCookie("roleName",data.roleName);
								parent.layer.msg('登录成功');
								window.location.href = "../schedule/exportReport.html";
							} else if(data.message == "用户名或密码错误"){
								layer.tips('用户名或密码错误！', '#username', {
									tips : [ 1, '#FF0000' ]
								});
								$(".text-input").val("");
							}
							if(data.userId!=""&&data.userId!=null){
								setCookie("userId",data.userId );
							}
							console.log(getCookie('userId'));
						});
				} else {
					layer.tips('请输入验证码！', '#icodeli2', {
						tips : [ 2, '#FF0000' ]
					});
				}
			} else {
				layer.tips('请输入密码！', '#password', {
					tips : [ 2, '#FF0000' ]
				});
			}
		} else {
			layer.tips('请输入用户名！', '#username', {
				tips : [ 2, '#FF0000' ]
			});
		}
		$("#icodeli2").click();
	});
	//回车绑定事件
	$("#check_code,#username,#password").bind('keypress', function(e) {
		if (e.keyCode == 13) {
			$("#comfirm_id").click();
			//$("#icodeli2").click();
			return false;
		}
	});
});