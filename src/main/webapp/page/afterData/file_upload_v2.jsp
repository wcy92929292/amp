<%@ page isELIgnored="true"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="java.util.*,java.text.*,com.udbac.model.*"%>
<%
	response.setContentType("text/html;charset=utf-8");
%>
<%
	String username = "";//用户名
	String province = "";//省份
	String pro_code = "";// 省份代码
	String edate = "";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	edate = sdf.format(new Date());
	
	
%>
<!DOCTYPE>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>移动集团可视化平台-文件上传</title>
		<link href="../../css/afterData/main.css" rel="stylesheet"></link>
		<script type="text/javascript" src="../../js/com/jquery-1.9.1.min.js"></script>
		<!-- 模板 -->
		<script type="text/javascript" src="../../js/com/jquery.tmpl.js"></script>
		<!-- 日期选择控件 -->
		<script type="text/javascript" src="../../js/my97DatePicker/WdatePicker.js"></script>
		<!-- 图层加载样式 -->
		<script src="../../js/layer/layer.min.js"></script>
		<script type="text/javascript" src="../../js/com/jquery.treetable.js"></script>
		<!-- 文件上传 -->
		<script type="text/javascript" src="../../js/ajaxfileupload.js"></script>
		<link href="../../css/afterData/styles.css" rel="stylesheet" type="text/css" />

		<script type="text/javascript">
			function cleanRate(id) {
				$("#processRateDiv" + id).css("display", "none");
				$("#processRateDiv5").css("display", "none");
			}

			//上传
			function upload(id) {
				$("#processRateDiv" + id).css("display", "block");

				$("#processRate51").text("");
				$("#processRate52").text("");
				$("#processRate53").text("");
				$("#processRate54").text("");

				if(id == '5') {
					var success1 = false;
					var success2 = false;

					var file1 = $("#file1").val();
					var file2 = $("#file2").val();
					var file3 = $("#file3").val();
					var file4 = $("#file4").val();
					if(file1 != '' && file2 != '' && file3 != '' && file4 != '') {
						$("#processRate51").text("文件1：1%");
						$("#processRate52").text("文件2：1%");
						$("#processRate53").text("文件3：1%");
						$("#processRate54").text("文件4：1%");
					} else {
						$("#processRate51").text("请选择四个文件！");
						return false;
					}
					if(file1 != '') {

						$.ajaxFileUpload({
							url: "/amp/admonitor/fileUploadv2.do?date="+$("#sdate_id").val()+'&file=1', //submit to UploadFileServlet  
							secureuri: false,
							fileElementId: 'file1',
							dataType: 'text', //or json xml whatever you like~  
							success: function(data, status) {
								if(data == 'success') {
									$("#processRate51").text("文件1：100%  上传成功！");

									if(file2 != '') {
										$.ajaxFileUpload({
											url: "/amp/admonitor/fileUploadv2.do?date="+$("#sdate_id").val()+'&file=2', //submit to UploadFileServlet  
											secureuri: false,
											fileElementId: 'file2',
											dataType: 'text', //or json xml whatever you like~  
											success: function(data, status) {
												if(data == 'success') {
													$("#processRate52").text("文件2：100%  上传成功！");

													if(file3 != '') {
														$.ajaxFileUpload({
															url: "/amp/admonitor/fileUploadv2.do?date="+$("#sdate_id").val()+'&file=3', //submit to UploadFileServlet  
															secureuri: false,
															fileElementId: 'file3',
															dataType: 'text', //or json xml whatever you like~  
															success: function(data, status) {
																if(data == 'success') {
																	$("#processRate53").text("文件3：100%  上传成功！");
																	if(file4 != '') {
																		$.ajaxFileUpload({
																			url: "/amp/admonitor/fileUploadv2.do?date="+$("#sdate_id").val()+'&file=4', //submit to UploadFileServlet  
																			secureuri: false,
																			fileElementId: 'file4',
																			dataType: 'text', //or json xml whatever you like~  
																			success: function(data, status) {
																				if(data == 'success') {
																					$("#processRate54").text("文件4：100%  上传成功！");
																				} else if(data == 'wrong_date') {
																					$("#processRate54").text("文件4：发生错误！ 选择的日期和文件日期不匹配！");
																				} else if(data == 'not_match') {
																					$("#processRate54").text("文件4：发生错误！ 两个文件内容不匹配！");
																				} else {
																					$("#processRate54").text("文件4：发生错误！");
																				}

																			},
																			error: function(data, status, e) {
																				$("#processRate54").text("文件4：发生错误！");
																			}
																		});

																	}

																} else if(data == 'wrong_date') {
																	$("#processRate53").text("文件3：发生错误！ 选择的日期和文件日期不匹配！");
																} else if(data == 'not_match') {
																	$("#processRate53").text("文件3：发生错误！ 两个文件内容不匹配！");
																} else {
																	$("#processRate53").text("文件3：发生错误！");
																}

															},
															error: function(data, status, e) {
																$("#processRate53").text("文件3：发生错误！");
															}
														});

													}

												} else if(data == 'wrong_date') {
													$("#processRate52").text("文件2：发生错误！ 选择的日期和文件日期不匹配！");
												} else if(data == 'not_match') {
													$("#processRate52").text("文件2：发生错误！ 两个文件内容不匹配！");
												} else {
													$("#processRate52").text("文件2：发生错误！");
												}

											},
											error: function(data, status, e) {
												$("#processRate52").text("文件2：发生错误！");
											}
										});

									}

								} else if(data == 'wrong_date') {
									$("#processRate51").text("文件1：发生错误！ 选择的日期和文件日期不匹配！");
								} else if(data == 'not_match') {
									$("#processRate51").text("文件1：发生错误！ 两个文件内容不匹配！");
								} else {
									$("#processRate51").text("文件1：发生错误！");
								}
							},
							error: function(data, status, e) {
								$("#processRate51").text("文件1：发生错误！");
							}
						});
					}

					return false;

				}

			}
		</script>
	</head>

	<body>
		<div>
			<div class="form_box">
				<div id="month" style="text-align:center;">日期:&emsp;
					<input class="text-input text-input-1 day-time" type="text" id="sdate_id" value="<%=edate%>" onfocus="WdatePicker({skin:'twoer'})" />&emsp;&emsp;
				</div>
			</div>

			<div class="form_box">
				<div id="month" style="text-align:center;">营销活动访问_浏览时间:&emsp;
					<input class="text-input" type="file" id="file1" name="file1" value="" onfocus="cleanRate('1');"/>&emsp;&emsp;

					<div id="processRateDiv1" style="display:none;">&emsp;&emsp;正在上传:
						<span id="processRate1"></span>
					</div>
				</div>

				<div style="height:20px;"></div>
				<div id="month" style="text-align:center;">营销活动访问_访客:&emsp;
					<input class="text-input" type="file" id="file2" name="file2" value="" style="margin-left:25px;" onfocus="cleanRate('2');" />&emsp;&emsp;
					<div id="processRateDiv2" style="display:none;">&emsp;&emsp;正在上传:
						<span id="processRate2"></span>
					</div>
				</div>

				<div style="height:20px;"></div>
				<div id="month" style="text-align:center;">营销活动访问_pv访次:&emsp;
					<input class="text-input" type="file" id="file3" name="file3" value="" style="margin-left:8px;" onfocus="cleanRate('3');" />&emsp;&emsp;

					<div id="processRateDiv3" style="display:none;">&emsp;&emsp;正在上传:
						<span id="processRate3"></span>
					</div>
				</div>

				<div style="height:20px;"></div>
				<div id="month" style="text-align:center;">营销活动跳出:&emsp;
					<input class="text-input" type="file" id="file4" name="file4" value="" style="margin-left:55px;" onfocus="cleanRate('4');" />&emsp;&emsp;

					<div id="processRateDiv4" style="display:none;">&emsp;&emsp;正在上传:
						<span id="processRate4"></span>
					</div>
				</div>

				<div style="height:20px;"></div>
				<div id="month">
					<div style="width:73px;margin-left:auto;margin-right:auto;">
						<input name="" type="button" id="id5" onclick="return upload('5');" value="上传全部"></input>
					</div>
					<div id="processRateDiv5" style="display:none;">&emsp;&emsp;正在上传:
						<span id="processRate51"></span>&emsp;&emsp;
						<span id="processRate52"></span>&emsp;&emsp;
						<span id="processRate53"></span>&emsp;&emsp;
						<span id="processRate54"></span>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>