/* 
 * 用户管理——管理修改其他用户信息
 * @author LQ
 * @date 2016-04-27
 */
jQuery(document).ready(function() {
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
		verify();//校验
		$("#username_id").val(getUrlParam('username'));
		var user_name= $("#username_id").val();
		//alert("user_name:"+user_name);
		var sdate='';
		var edate='';
		var role='';
		var uname='';
		getUserByUsername(sdate,edate,uname,role,user_name);
		
	});
	function getUserByUsername(sdate,edate,uname,role,user_name){//根据username获取user
		$.post("/amp/user/queryUser.do",{
			'sdate' : sdate,
			'edate' : edate,
			'uname'	:uname,
			'role':role,
			'user_name':user_name,
		},function (result, resultState) {
			var resultJson = eval(result);//
        	console.log(resultJson);
        	$.each(resultJson, function (i, item) { 
        		var role=item.roleId;
				var province=item.provinceId;
				//console.log("roleID:"+role+"  provinceID:"+province);
				layer.load(1, {
					  shade: [0.1,'#fff'] //0.1透明度的白色背景
					});
				roleCustomer(role,province);
				if (item!= "") {
					layer.closeAll();
					$("#user_id").val(item.userId);
					$("#username").val(item.userName);
					$('#username').attr("disabled",true).attr("disabled",true).css("color","rgba(153, 153, 153, 1)");
					$("#password").val(item.password);
					$('#password').attr("disabled",true).attr("disabled",true).css("color","rgba(153, 153, 153, 1)");
					$('#confirmpwd').attr("disabled",true).attr("disabled",true).css("color","rgba(153, 153, 153, 1)");
					$("#confirmpwd").val(item.password);
					$("#uname").val(item.realName);
					$("#tel").val(item.phoneNumber);
					$("#email").val(item.mailbox);
					$("#province").val(item.provinceId);
					$("#role").val(item.roleId);
					
					if(item.userState==0){ 	
						$("#user_state1").attr("checked","checked");
						$("#state").val("0");
					}else if(item.userState==1){
						$("#user_state2").attr("checked","checked");
						$("#state").val("1");
					}
					//console.log(item);
				}
			}); 
		});
		/*roleCustomer();*/
		
	}
	function roleCustomer(role,province){//roleCustomer
		$.post( "../../user/getRole.do", {
		},
		function(result, resultState) {
			if (resultState == "success") {
				var resultJson = eval(result);
				//console.log(resultJson);
				/*var role=$("#role").val();*/
				$.each(resultJson, function (i, item) { 
					if (item!= "") {
						if(item.roleId==role){
							$("#role_id").append("<option selected value="+ item.roleId+">"+ item.role_Name+"</option>");
						}else{
							$("#role_id").append("<option  value="+ item.roleId+">"+ item.role_Name+"</option>");
						}
					}
				}); 
			}
		});
		$.post( "../../user/getCustomer.do", {
		},
		function(result, resultState) {
			if (resultState == "success") {
				var resultJson = eval(result);
				//console.log(resultJson);
				/*var province=$("#province").val();*/
				$.each(resultJson, function (i, item) { 
					if (item!= "") {     
						
						if(province==item.customerId){
							$("#province_id").append("<option selected value="+ item.customerId+">"+ item.customerName+"</option>");
						}else{
							$("#province_id").append("<option value="+ item.customerId+">"+ item.customerName+"</option>");
						}
					}
				}); 
			}
		});
	}
	

	function getUrlParam(name){ //url 
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
		var r = window.location.search.substr(1).match(reg); 
		if (r != null) return unescape(r[2]); return null; 
	}
	function resetForm(){//清除
		 $('#myform')[0].reset();
		 $('#myform textarea,#myform select').val(' ');
	}
	function back(){
		window.location.href = "./queryUser.html";
	}
	
	function submit() {//提交
		//$('#username').blur();
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
		}//alert($("#user_state").val());
		$.post("../../user/updateUser.do", {
			'userid':$("#user_id").val(),
			'username' : $("#username").val(),
			'password' : $("#password").val(),
			'uname' : $("#uname").val(),
			'tel' : $("#tel").val(),
			'email' : $("#email").val(),
			'province' : $("#province_id").val(),
			 'role':$("#role_id").val(),
			 'user_state':$("#state").val()
			 
		}, function(result) {
			//alert(result);
			if(result == 0){
				parent.layer.msg('编辑失败');
				$("#submit").attr("disabled",false);
				
			}else if(result == 1){
				parent.layer.msg('提交成功');
					window.location.href = "./queryUser.html";
				
				$("#submit").attr("disabled",true);
				
			}
		});
	} 

	//校验
	function verify(){
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
				var isMobile=/^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
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
		
	}
	
	function state(tag){
		$("#state").val(tag);
	}