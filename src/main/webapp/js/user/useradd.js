/* 
 * 用户管理——注册用户
 * @author LQ
 * @date 2016-04-27
 */jQuery(document).ready(function() {
		$("#template-nav").loadTemplate("../../common/navbar.html", {});
		$(".headers").loadTemplate("../../common/header.html", {});
		var date = new Date();
		//add by LiuJie
		$('[data-toggle="popover"]').popover({
			'animation' : true,
			'trigger' : 'hover',
		});
		$.post("../../campaign/getUserSession.do", {
		}, function(data){
			if(data=="error"){
				layer.alert('未登录，请登录用户！', {
					  skin: 'layui-layer-molv' //样式类名
					  ,closeBtn: 0
					}, function(){
				window.location.href = "../login/login.html";
				});
			}
		});
		getsession();
		$('#username').blur(function() {
			var username = $('#username').val();
			var req = /^[a-zA-Z0-9]+$/;
			if (username != ''&&username != null) {
				if(!req.test(username)){
					$('#name_span').html('用户名只能为字母和数字！');
				}else{
					//$('#name_span').text('');
					$.post("../../user/queryUsername.do", {
						'username' : $("#username").val()
					}, function(result, resultState) {
						//alert(result);
						if(result == 0){
							$('#name_span').text('用户名可用').hide();
						}else if(result == 1){
							$('#name_span').text('用户名已被占用！')
						}
					});
				}
				
			} else {
				$('#name_span').text('用户名不能为空！');
			}
		});
		$('#password').blur(function() {
			var password = $('#password').val();
			if (password == '' || password == null ) {
				$('#pwd_span').html('密码不能为空！');
			} else if(password.length<6){
				$('#pwd_span').text('密码不能小于6位！');
			}else if(password.length>20){
				$('#pwd_span').text('密码过长，不能超过20位！');
			}else{
				$('#pwd_span').html('');
			}
			
		});
		$('#confirmpwd').blur(function() {
			$('#pswd_span').text('');
			var confirmpwd = $('#confirmpwd').val();
			var password = $('#password').val();
			if (confirmpwd == '' || confirmpwd == null ){
				$('#pswd_span').text('确认密码不能为空！');
			}else if (!(password ==confirmpwd)){
				$('#pswd_span').text('确认密码与密码不一致！');
			}
		});
		$('#uname').blur(function() {
			var uname = $('#uname').val();
			if (uname != '' && uname != null) {
				if(uname.length >20){
					$('#uname_span').text('姓名输入过长！');
				}
				$('#uname_span').text('').hide();
			} else {
				$('#uname_span').text('姓名不能为空！');
			}
		});
		$('#tel').blur(function() {
			$('#tel_span').html('');
			var tel = $('#tel').val();
			if (tel != '' && tel != null) {
				var isMobile=/^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
				var isPhone=/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
				 if(!isMobile.test(tel) && !isPhone.test(tel)){
					 $('#tel_span').html("请正确填写电话号码，例如:13415764179或0321-4816048");
				            return false;
				        }
			} else {
				$('#tel_span').html('电话不能为空！')
			}
		});
		$('#email').blur(function() {
			var email = $('#email').val();
			if (email != '' && email != null) {
				$('#email_span').html('');
				var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			    if (!reg.test(email)) {
			    	$('#email_span').html('邮箱格式不正确，请重新填写');
			       return false;
			    }
			} else {
				$('#email_span').html('邮箱不能为空！')
				$("#submit").attr("disabled",true); 
			}
		});
		init();

	});

		function resetForm(){
			 $('#myform')[0].reset();
		}
		
		function submitForm() {
			$('#username').blur();
			$('#password').blur();
			$('#confirmpwd').blur();
			$('#uname').blur();
			$('#tel').blur();
			$('#email').blur(); 
			var pro=$("#province_id  option:selected").text();
			//alert(pro);
			if(pro =='--请选择--'){
				$('#province_span').html('请选择客户！');
			}else {
				$('#province_span').html('');
			}
			if($("#role_id  option:selected").text()=='--请选择--'){
				$('#role_span').html('请选择角色！');
			}else{
				$('#role_span').html('');
			}
			
			$.post("../../user/createUser.do", {
				'username' : $("#username").val(),
				'password' : $("#password").val(),
				'uname' : $("#uname").val(),
				'tel' : $("#tel").val(),
				'email' : $("#email").val(),
				'province' : $("#province_id").val(),
				 'role':$("#role_id").val() 
			}, function(result) {
				//alert(result);
				if(result == 0){
					parent.layer.msg('注册失败');
					$("#submit").attr("disabled",false);
				}else if(result == 1){
					parent.layer.msg('注册成功');
					$("#submit").attr("disabled",true); 
				}
			});
		}
		
		function init(){

			$.post( "../../user/getRole.do", {
			},
			function(result, resultState) {
				if (resultState == "success") {
					var resultJson = eval(result);
					//console.log(resultJson);
					$.each(resultJson, function (i, item) { 
						if (item!= "") {
							$("#role_id").append("<option value="+ item.roleId+">"+ item.role_Name+"</option>");
						}
					}); 
				}
			});
			$.post( "../../user/getCustomer.do", {
			},
			function(result, resultState) {
				if (resultState == "success") {
					var resultJson = eval(result);
					console.log(resultJson);
					$.each(resultJson, function (i, item) { 
						if (item!= "") {
							$("#province_id").append("<option value="+ item.customerId+">"+ item.customerName+"</option>");
						}
					}); 
				}
			});
		}
		function getsession(){
			
			$.post( "../../user/getUser.do", {
				
			},
			function(result, resultState) {
				if (resultState == "success") {
					var resultJson = eval(result);
					//console.log(resultJson.role_NAME);
					if (resultJson != ""||resultJson !=null) {
						/*alert(resultJson.role_NAME);*/
						if(resultJson.role_NAME!='管理员'&&resultJson.role_NAME!='超级管理员'){
							layer.alert('您不是管理员，没有权限！', {
							  skin: 'layui-layer-molv' //样式类名
							  ,closeBtn: 0
							}, function(){
								/*window.location.href = "../user/mytodo.html";*/
								if(resultJson.role_NAME!='客户'){
									window.location.href = "../index/index.html";
								}else{
									window.location.href = "../schedule/exportReport.html";
								}
							});
						}
					} 
				}
			});
			
		}