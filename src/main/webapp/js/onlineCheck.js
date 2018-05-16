var mic = "";
var url = "";
var state;
var or_mic_state;  
var pv; //曝光数
var click; //点击数
var exposure_avg; //曝光预估
var click_avg; //点击预估
var support_exposure; //是否支持曝光监测 
var support_click; //是否支持点击监测
var time;//作为上线状态变更时的检索条件
var unit ; //
var put_value;
var actCode1;
var st1; //判断是哪一个按钮被点击
var oldSavePath1; //以前存放的路径
var cusName;
var micOne;//第一个页mic查询的参数
var activity1;
var userName;
var checkValue;

var online_data = new Array();// 定义上线状态
	online_data[0] = '全部数据';	
	online_data[1] = '上线无数';
	online_data[2] = '取消上线';
	online_data[3] = '正常上线';
	online_data[4] = '推迟上线';
	online_data[5] = '曝光无数';
	online_data[6] = '点击无数';
	online_data[7] = '点击比曝光大';
	online_data[8] = '曝光预估不符';
	online_data[9] = '点击预估不符';
	online_data[10] = '不监测点击,但有数';
	online_data[11] = '不监测曝光,但有数';
	online_data[12] = '与环比差异过大';
	online_data[13] = '与同比差异过大';
	online_data[14] = '经验值差异过大';
var bg_color = new Array() // 定义改变状态时的背景颜色
	bg_color[0] = 'rgba(20, 93, 93, 0.6)';
	bg_color[1] = 'rgba(33, 11, 216, 0.6)';
	bg_color[2] = 'rgba(178, 178, 165, 0.4)';
	bg_color[3] = 'rgba(50, 203, 20, 0.6)';
	bg_color[4] = 'rgba(4, 144, 21, 0.6)';
	bg_color[5] = 'rgba(202, 53, 8, 0.6)';
	bg_color[6] = 'rgba(228, 97, 97, 0.6)';
	bg_color[7] = 'rgba(100, 163, 163, 0.6)';
	bg_color[8] = 'rgba(110, 173, 173, 0.6)';
	bg_color[9] = 'rgba(120, 183, 183, 0.6)';
	bg_color[10] = 'rgba(130, 193, 193, 0.6)';
	bg_color[11] = 'rgba(140, 203, 203, 0.6)';
	bg_color[12] = 'rgba(247, 92, 2, 0.6)';
	bg_color[13] = 'rgba(189, 63, 15, 0.6)';
	bg_color[14] = 'rgba(255, 0, 0, 0.6)';
		
var mic_state = new Array();
	mic_state[30] = '第一天核查-->后端核查';
	mic_state[31] = '第一天核查-->核查正常';
	mic_state[32] = '第一天核查-->前端核查';
	mic_state[33] = '前端核查 --> 核查正常';
	mic_state[34] = '后端核查 --> 核查正常';
	mic_state[35] = '前端核查 --> 后端核查';
//	
	
var trLen = 0;
function test1(mod) {
	// 传mic
	mic = $($(mod).parents("tr").find("td")[7]).text();
	if( $(mod).attr('expurl') != "undifined"){
		var fileName = $(mod).attr('expurl');
		if (fileName == null || fileName == '' || fileName == "null") {
		} else {
			$($(mod)).attr("data-target", "#myPic");
			//判断取出来的是图片还是压缩包
			if(fileName.substring(fileName.lastIndexOf('.') + 1) == 'zip' || fileName.substring(fileName.lastIndexOf('.') + 1) == 'rar'){
				//隐藏图片显示区域
				$(".sb").hide();
				//只显示下载的链接
				$(".downloadfile").attr("href","/amp/check/getFile.do?fileName=" + encodeURI(encodeURI(fileName)));
			}else{
				$(".downloadfile").hide();
				$("#show_pic").attr("src", "/amp/check/getFile.do?fileName=" + encodeURI(encodeURI(fileName)));
			}
		}
	}
	if( $(mod).attr('click') != "undifined"){
		var fileName = $(mod).attr('click');
		if (fileName == null || fileName == '' || fileName == "null") {
		} else {
			$($(mod)).attr("data-target", "#myPic");
			//判断取出来的是图片还是压缩包
			if(fileName.substring(fileName.lastIndexOf('.') + 1) == 'zip' || fileName.substring(fileName.lastIndexOf('.') + 1) == 'rar'){
				//隐藏图片显示区域
				$(".sb").hide();
				//只显示下载的链接
				$(".downloadfile").attr("href","/amp/check/getFile.do?fileName=" + encodeURI(encodeURI(fileName)));
			}else{
				$(".downloadfile").hide();
				$("#show_pic").attr("src", "/amp/check/getFile.do?fileName=" + encodeURI(encodeURI(fileName)));
			}
		}
	}
	if( $(mod).attr('after') != "undifined"){
		var fileName = $(mod).attr('after');
		if (fileName == null || fileName == '' || fileName == "null") {
		} else {
			$($(mod)).attr("data-target", "#myPic");
			//判断取出来的是图片还是压缩包
			if(fileName.substring(fileName.lastIndexOf('.') + 1) == 'zip' || fileName.substring(fileName.lastIndexOf('.') + 1) == 'rar'){
				//隐藏图片显示区域
				$(".sb").hide();
				//只显示下载的链接
				$(".downloadfile").attr("href","/amp/check/getFile.do?fileName=" + encodeURI(encodeURI(fileName)));
			}else{
				$(".downloadfile").hide();
				$("#show_pic").attr("src", "/amp/check/getFile.do?fileName=" + encodeURI(encodeURI(fileName)));
			}
		}
	}
}
jQuery(document).ready(function(){
	$(".headers").loadTemplate("../../common/header.html", {});
        new uploadPreview({ UpBtn: "filename", DivShow: "imgdiv", ImgShow: "imgShow" });
        
        if(location.toString().indexOf("back") == -1){ //判断上个页面是否为同一个模块，否则，清理查询条件Cookie
      	   delCookie("state");
      	   delCookie("micOne");
      	   delCookie("cusName");
      	   delCookie("activity1");
         }

        initData(false);
        
        
        var urlParam = window.location.href;
        if(urlParam.substr(urlParam.length-3) == "two"){
        	$("#dro_two").show();
        	$("#dro_one").hide();
        }
        
});

function initData(isSelect) {
	
	if(location.toString().indexOf("back") == -1){
		   location = "/amp/page/check/check.html?back";
	 }
	
	var time1 = new Date().Format("yyyy-MM-dd");
	$("#date1").text(time1 + "上线第一天");
	
	// 显示页面信息
	$.post(
			'/amp/check/list.do',
			{'state':state,'date' : time1,'mic' : micOne,'cusName':cusName,'isShowMy':checkValue},function(data,status) {
			if(status == "success"){
				$(".zxy_mask").remove();
			}
				if (data == null || data == '' || data.length == 0) {
					$('#tb').html('没有数据。');
				} else {
					$('#tb').html('');
				}
				for (var i = 0; i < data.length; i++) {// start
					if(data[i].online_state){   //取出是-1时，是还没有改变过状态，默认自动判断,当人工修改状态时，就不是-1了
						//页面取值判断
						if(data[i].pv == null){  //当前曝光
							data[i].pv = "";
						}
						if(data[i].click == null){ //当前点击
							data[i].click = "";
						}
						if(data[i].exposure_avg == null){ //曝光预估
							data[i].exposure_avg = "";
						}
						if(data[i].click_avg == null){ //点击预估
							data[i].click_avg = "";
						}
					}
					var cus = null;
					var span = '<span id="st" class="label label-primary" style="height: 30px; padding: .2em .7em .3em; font-size: 88%;">'
							+ data[i]["customer_name"]
							+ '</span>';
					var tb = '<table id="tab_1" class="table table-hover table-responsive"><tbody id="t_id">'
						 	+ '<tr id=' + data[i]["mic"] +' mergin="0" time='
						 	+ data[i]["put_date"] + '  click_avg="'+data[i]["click_avg"]+'"  exposure_avg="' + data[i]["exposure_avg"] + '"  unit= '+ data[i]["unit"] + ' put_value='+ data[i]["put_value"]
						 		+ ' sup_click='+data[i]["support_click"]+' sup_exp = '+ data[i]["support_exposure"] 
							+ ' state='
							+ data[i]["online_state"]
							+ ' color = "" actCode = '+data[i]["activity_code"]+'>'
							+ '<td colspan="2" style="width:128px;"><select name="select_state" class="select_state"></select></td>'
							+ '<td class="t_td" style="width:66px;">'
							+ data[i]["customer_name"]
							+ '</td><td style="width:95px;">'
							+ data[i]["activity_code"]
							+ '</td><td style="width:150px;">'
							+ data[i]["activity_name"]
							+ '</td><td style="width:75px;">'
							+ data[i]["media_name"]
							+ '</td><td style="width:200px;">'
							+ data[i]["point_location"]
							+ '</td><td style="width:170px;">'
							+ data[i]["put_function"]
							+ '</td><td style="width:190px;">'
							+ data[i]["mic"]
							+ '</td><td style="width:70px;">'
							+ (data[i]["unit"] == "CPM" && data[i]["click_avg"] != null && data[i]["click_avg"] != ''
								? parseFloat(data[i]["click_avg"]) *  parseFloat(data[i]["put_value"]) : data[i]["click_avg"] )
							+ '</td><td style="width:70px;">'
							+ (data[i]["unit"] == "CPM" && data[i]["exposure_avg"] != null && data[i]["exposure_avg"] != ''
								? 1000 *  parseFloat(data[i]["put_value"]) : data[i]["exposure_avg"] )
							+ '</td><td style="width:70px;">'
							+ data[i]["click"]
							+ '</td><td style="width:70px;">'
							+ data[i]["pv"]
							+ '</td><td id="btn_td" style="width:230px;"><button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#myMod" id="baoguang" onclick="test1(this)" expurl='
							+ data[i].exposure_file
							+ '>曝光核查</button><button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#myMod" id="dianji" onclick="test1(this)" click='
							+ data[i].click_file
							+ '>点击核查</button><button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#myMod" id="houduan" onclick="test1(this)" after='
							+ data[i].after_file
							+ '>后端核查</button></td><td class="mic_state" style="width:185px;"><select name="mic_state" micstate='+data[i]["now_state_id"]+'  class="mic_state"></select></td><td style="display:none;"><button style="height:28px;" type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal" onclick="local()">备注</button>'
							+ '</td><td style="width:70px;">'
//							+ '<button type="button" class="btn btn-primary btn-sm" onclick="saveMyFavorites(this)">收藏</button>'
							+ '<img alt="" src="/amp/images/star1.png" width="25px;" height="25px;" onclick="saveMyFavorites(this,date1)"> '
							+ '</td><td style="width:70px;">'
							+ '<button type="button" class="btn btn-danger btn-sm" onclick="sendMail(this)">反馈</button></td>'
							+ '<td style="width:100px;">'
							+ '<input type="checkbox" name="checkMic" isshow='+data[i]["collect_user"]+' value='+data[i]["mic"]+' style="margin-left: 47%;"/></td></tr></tbody></table>';
					if ($("#cus" + data[i]['customer_name']).length == 0) {
						cus = $("<div id = 'cus" + data[i]['customer_name']+ "' pid='"+data[i]['customer_name']+"' ></div>");
						$("#tb").append(cus);
					}
					$("#cus" + data[i]['customer_name']).append(span + tb);
					if ($("#cus" + data[i]['customer_name']).find("span").length > 1){
						
						//1. 删除span(第二个span开始删除)
						$($("#cus" + data[i]['customer_name']).children("span:gt(0)")).remove();
						
						//2. 找到行
						var old = $("#cus" + data[i]['customer_name']).children("table").eq(0).children("tbody");
						var tr =  $("#cus" + data[i]['customer_name']).children("table").eq(1).children("tbody").children("tr");
						$( $("#cus" + data[i]['customer_name']).children("table:gt(0)") ).remove() ;
						//3. 追加到一个table中
						old.append(tr);
					}
					
				} // end
				
				
				isshow();
				
				// 追加上线状态列表
				$("ul[name=selectOne]").html("");
				for (i = 0; i < online_data.length; i++) {
					$("ul[name=selectOne]").append("<li><a value="+ i + ">" + online_data[i]+ "</a></li>");
					$(".select_state").append("<option value=" + i + ">" + online_data[i]+ "</option>");
				}
				
				//系统判断的默认禁用：
				$(".select_state").each(function(){
					for(j = 5;j<12;j++){
						$(this).find("option").eq(1).attr("disabled","disabled");
						$(this).find("option").eq(j).attr("disabled","disabled");
					}
				});
				
				  // 筛选省份的上线状态
				$("ul[name=selectOne]").find("li a").click(function(){
					$("input[list=mic-search]").val('');
			        state = $(this).attr("value");
			        
			        var cusName =  $("select[name='cusName']").val();
			        //点击筛选状态后，状态更改为筛选时选择的状态
			        if(state == 0){
						$(".top").text('全部数据');
					} else if (state == 1) {
			            $(".top").text('上线无数');
			        } else if (state == 2) {
			            $(".top").text('取消上线');
			        } else if (state == 3) {
			            $(".top").text('正常上线');
			        } else if (state == 4) {
			            $(".top").text('推迟上线');
			        } else if (state == 5) {
			            $(".top").text('曝光无数');
			        } else if (state == 6) {
			            $(".top").text('点击无数');
			        } else if (state == 7) {
			            $(".top").text('点击比曝光大');
			        } else if (state == 8) {
			            $(".top").text('曝光预估不符');
			        } else if (state == 9) {
			            $(".top").text('点击预估不符');
			        } else if (state == 10) {
			            $(".top").text('不监测点击，但有数');
			        } else if (state == 11) {
			            $(".top").text('不监测曝光，但有数');
			        } else if (state == 12) {
			            $(".top").text('与环比差异过大');
			        } else if (state == 13) {
			            $(".top").text('与同比差异过大');
			        } else if (state == 14) {
			            $(".top").text('经验值差异过大');
			        }
			        //页面条件筛选查询
					trLen = 0;
					actName = $("select[name=cusName]").find("option:selected").val();
					
					trLen = checkTr(state,actName,micOne,activity1);
			        $("#sumcus").text("点位数："+trLen+"件");
			        
			        setCookie("state",state);
			    });
				
				// 选中默认查询出来的上线状态
				$("tr[state]").each(function() {
					
					//判断是否支持曝光，点击，不支持，在对应的预估显示“不支持”
					setSupport(this);
					
					var index = $(this).index();
					var orState  = $(this).attr("state");  
				    pv = parseInt($($(this).parents().children("#tab_1 tr").eq(index).find("td")[11]).text());
				    click = parseInt($($(this).parents().children("#tab_1 tr").eq(index).find("td")[10]).text());
				    support_exposure = $(this).attr("sup_exp") == "null" ? 0 : parseInt($(this).attr("sup_exp"));
			        support_click = $(this).attr("sup_click") == "null" ? 0 : parseInt($(this).attr("sup_click"));
			        unit = $(this).attr("unit");
			        put_value =  $(this).attr("put_value");
			        var now_mic = $($(this).parents().children("#tab_1 tr").eq(index).find("td")[7]).text();
			        var time = $(this).attr("time");
			        
			        exposure_avg = "";
			        click_avg = "";
			        
			        if("CPM" != unit){
			        	 exposure_avg = parseInt($(this).attr("exposure_avg"));
			        	 click_avg = parseInt($(this).attr("click_avg"));
			        }else{
			        	if($(this).attr("exposure_avg") != '' && put_value != ""){
			        		exposure_avg = parseInt(put_value) * 1000;
			        	}
			        	
			        	if($(this).attr("click_avg") != '' && put_value != ""){
			        		click_avg = parseInt($(this).attr("click_avg")) * parseInt(put_value);
			        	}
			        }
				    /***************************************系统自动判断状态******************************************/
				    //未做状态修改的，没有人为操作的,-1的状态才会去修改状态，否则就不会
				    if(orState == -1){
				    	stateT = 0;
				    	//支持曝光以及点击
				    	if(support_exposure == 1 && support_click == 1){//上线无数

				    		if(click == "" && pv == "" ){
				    			stateT = 1;
				    		}
				    		//点击比曝光大
				    		else if(click != "" && pv != "" && click > pv){
				    			stateT = 7;
				    		}
				    		//曝光预估不符
				    		else if(exposure_avg != "" && (pv < exposure_avg * 0.3 || pv > exposure_avg * 10)){
				    			stateT = 8;
				    		}
				    		//点击预估不符
				    		else if(click_avg != "" && (click < click_avg * 0.3 || click > click_avg * 10)){
				    			stateT = 9;
				    		}else{
				    			stateT = 3;
				    		}
				    	}
				    	//点击无数
				    	else if(support_click == 1 && (click == "" || click == 0)){
				    			stateT = 6;
				    	}
				    	//曝光无数
			    		else if(support_exposure== 1 && (pv == "" || pv == 0)){
			    			stateT = 5;
			    		}
				    	//end 支持曝光以及点击判断
				    	//不监测点击,但有数
				    	else if(support_click==0 && click != "" && click != 0){
				    		stateT = 10;
				    	}
				    	//不监测曝光,但有数
				    	else if(support_exposure==0 && pv != "" && pv != 0){
				    		stateT = 11;
				    	}else{
				    		stateT = 3;
				    	}
				    	
				    	$(this).find("select").val(stateT); //系统赋初始状态  
				    	$(this).attr("state",stateT) //改变行中state_t属性值，方便查询用
				    	
				    }else{
				    	
				    	//显示的这个当前状态是人为操作的，可改变上线状态
				    	if ( $(this).find("select").val(orState) ){
							 $(this).children("td").find("option:selected").attr({"selected":"selected"});
						 }
				    }
				});
			
				// 用户更改后的颜色与点击更改的颜色一致
				$("tr[color]").each(function() {
					var index = $(this).index();
					var val = $(this).parents().children("#tab_1 tr").eq(index).find("td:eq(0)").find("option:selected").text();
						if (val == "上线无数") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[1]);
						} else if (val == "取消上线") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[2]);
						} else if (val == "正常上线") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[3]);
						} else if (val == "推迟上线") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[4]);
						} else if (val == "曝光无数") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[5]);
						} else if (val == "点击无数") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[6]);
						} else if (val == "点击比曝光大") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[7]);
						} else if (val == "曝光预估不符") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[8]);
						}else if (val == "点击预估不符") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[9]);
						}  else if (val == "不监测点击,但有数") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[10]);
						} else if (val == "不监测曝光,但有数") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[11]);
						} else if (val == "与环比差异过大") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[12]);
						} else if (val == "与同比差异过大") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[13]);
						} else if (val == "经验值差异过大") {
							$(this).parents().children("#tab_1 tr").eq(index).css("background-color",bg_color[14]);
						} 
					});
				// 更改上线状态背景色
				$("tr[state]").change(function() {
					time = $(this).attr("time");
					var index = $(this).index();
					var cn_state = $(this).find("option:selected").val();  //获取选中以后的状态
					var cn_mic = $($(this).parents().children("#tab_1 tr").eq(index).find("td")[7]).text();
					var val = $(this).parents().children("#tab_1 tr").eq(index).find("td:eq(0) option:selected").val();
					if(val=="0"){
						 alert("点我没用");
					 }else if (val == "1") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[1]);
					} else if (val == "2") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[2]);
					} else if (val == "3") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[3]);
					} else if (val == "4") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[4]);
					} else if (val == "5") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[5]);
					} else if (val == "6") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[6]);
					} else if (val == "7") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[7]);
					} else if (val == "8") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[8]);
					} else if (val == "9") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[9]);
					} else if (val == "10") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[10]);
					} else if (val == "11") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[11]);
					} else if (val == "12") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[12]);
					} else if (val == "13") {
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[13]);
					} else{
						$(this).parents().children("#tab_1 tr").eq(index).css("background",bg_color[14]);
					} 
				});
				//删除点位的---全部数据option
				$("select[name=select_state]").each(function(){
					$(this).find("option:first").remove();  //列表的第一个option remove掉
				})
				//备注页面跳转
				function local() {
					location.href = '../monitorPlan/memoMonitorPlan.html';
				}

				function clear(){
					var file = $("#filename") ;
					file.after(file.clone().val(""));      
					file.remove();
				}
				
				function showPic(){
					new uploadPreview({ UpBtn: "filename", DivShow: "imgdiv", ImgShow: "imgShow" });
				}
				$(".close").click(function(){
					clear();
					$("#imgdiv").remove();
					showPic();
					$('.col-sm-6').append('<div id="imgdiv" style="height:310px;"><img id="imgShow" /></div>');
					$(".msg").css({ "display": "none"});
				});
				// 1.判断鼠标点击了哪个按钮，然后相应的改变图片上传框的文字信息
				$("[id=baoguang]").each(function() {
					$(this).click(function() {
						st1 = 0;
						oldSavePath1 = $(this).attr('expurl');
						mic = $($(this).parents("tr").find("td")[7]).text();
						actCode1 = $(this).parents().children("tr").attr("actcode");
						$("#myModalLabel_text").text("曝光核查");
						url = '/amp/check/photoUpload.do?mic='+ mic+ '&'+ 'actCode='+actCode1+ '&'+ 'st=0'+'&reupload=false';
						});
				});
				$("[id=dianji]").each(function(){
					$(this).click(function() {
						st1 = 1;
						oldSavePath1 = $(this).attr('click');
						mic = $($(this).parents("tr").find("td")[7]).text();
						actCode1 = $(this).parents().children("tr").attr("actcode");
						$("#myModalLabel_text").text("点击核查");
						url = '/amp/check/photoUpload.do?mic='+ mic+ '&'+ 'actCode='+actCode1+ '&'+ 'st=1'+'&reupload=false';
						});
				});
				$("[id=houduan]").each(function(){
					$(this).click(function() {
						st1 = 2;
						oldSavePath1 = $(this).attr('after');
						mic = $($(this).parents("tr").find("td")[7]).text();
						var actCode = $(this).parents().children("tr").attr("actcode");
						$("#myModalLabel_text").text("后端核查");
						url = '/amp/check/photoUpload.do?mic='+ mic+ '&'+ 'actCode='+actCode+ '&'+ 'st=2'+'&reupload=false';
					});
				});
				
				//用户已经点击的按钮(曝光、点击、后端)，查看上传了的图片
				$("button[expurl]").each(function() {
					var expurl = $(this).attr("expurl");
					    if (expurl != null&& expurl != "null"&& expurl != "") {
							$(this).removeClass("btn btn-danger btn-sm").addClass("btn btn-success btn-sm aaabbb").text("曝光已查");
						}
				    });
				$("button[click]").each(function() {
					var click = $(this).attr("click");
					    if (click != null&& click != "null"&& click != "") {
							$(this).removeClass("btn btn-danger btn-sm").addClass("btn btn-success btn-sm aaabbb").text("点击已查");
						}
				    });
				$("button[after]").each(function() {
					var after = $(this).attr("after");
					    if (after != null&& after != "null"&& after != "") {
							$(this).removeClass("btn btn-danger btn-sm").addClass("btn btn-success btn-sm aaabbb").text("后端已查");
						}
				    });
				
				/*************************************************新增加重新上传************************************************************/
			    $(".reupload").click(function(){
					$(".hideModal").modal('hide'); //已查框隐藏
					//获取参数
					url = '/amp/check/photoUpload.do?mic='+ mic+ '&'+ 'actCode='+actCode1+ '&'+ 'st='+st1+'&oldSavePath='+oldSavePath1+'&reupload=true';
				});
				
			    /****************span*******************/
			    $("#tb").find("span").each(function(){ //遍历所有span元素的值
	    			$(this).remove();
			    });
			    
			   $("#tb").find("thead").each(function(){
				   $(this).remove();
			   });
			   
			    /************************************得到下拉列表中的客户参数***********************************/
			    
			    if(!isSelect){
				    $("#tb").find("div").each(function(){
				    	var cusid = $(this).attr('pid');
				    	$("select[name=cusName]").append("<option value='" + cusid+ "'> "+ cusid + "</option>");
				    });
			    }
			    
			    state = getCookie("state");
				cusName = getCookie("cusName");
				micOne = getCookie("micOne");
				activity1 = getCookie("activity1");
			    
			  //查找cookie中当前选中的值(上线状态的)
				$("ul[name=selectOne]").find("li").each(function(){
					var a = $(this).find('a');
					if(a.attr('value') == getCookie("state")){
						$(".top").text(a.text());
					}		
				});
				
				//查找cookie中当前选中的值(筛选客户下拉列表的)
				$("select[name=cusName]").find("option").each(function(){
					if( cusName ==null ){
						cusName = "all";
					}else if($(this).val() == getCookie("cusName")){
						$(this).attr("selected","selected");
					}		
				});
				
				//设置短代码的
				$("input[list=mic-search]").val(micOne);
				//设置活动编号的
				$("input[list=activity1-search]").val(activity1);
				
				console.log("第一个页  "+state)
				console.log("第一个页  "+cusName)
				console.log("第一个页  "+micOne)
				console.log("第一个页  "+activity1)
				
				if(state == undefined || state == "0" || state == null){
	    			 state = "0";
	    		}
				
				trLen = checkTr(state, cusName, micOne, activity1);
			    
			    $("#sumcus").text("点位数："+ trLen +"件");
			    
			   $("select[name=cusName]").find("option:selected").attr("selected","selected");
			   
		    	//3.获取mic输入框的值去查询
		    	 $("#activity01").click(function(){ //输入框失去焦点就去查找对应的mic
		    		//得到输入框的值
		    		 micOne = $("input[list=mic-search]").val();
		    		 
		    		 setCookie("micOne",micOne);
		    		 
		    		 //得到输入框的值
		    		 activity1 = $("input[list=activity1-search]").val();
		    		 
		    		 setCookie("activity1",activity1);
		    		 
		    		 if(state == undefined || state == "0" || state == null){
		    			 state = "0";
		    		 }
		    		 var cusName = $("select[name=cusName]").find("option:selected").val();
		    		 
		    		 setCookie("cusName",cusName);
		    		 
		    		 checkTr(state,cusName,micOne,activity1)
		    		 
		    		 trLen = checkTr(state,cusName,micOne,activity1)
		    		 $("#sumcus").text("点位数："+trLen+"件");
		    		 
		    		console.log("点击时触发的  "+state)
					console.log("点击时触发的  "+cusName)
					console.log("点击时触发的  "+micOne)
					console.log("点击时触发的  "+activity1)
		    		 
		    	 });
		    	 
				//追加点位状态列表
				for(var j = 30;j< mic_state.length; j++){
					var option = '<option value='+j+'>'+mic_state[j]+'</option>'
					$("[name=mic_state]").append(option);
				}
				
				//改变点位状态
				$("select[name=mic_state]").click(function(){
					 or_mic_state = $(this).val();     //原始状态
				});
				$("select[name=mic_state]").change(function(){
					var index = $(this).parents().parents().index();
					var new_mic_state = $(this).val();    //改变时状态
					var st_mic = $($(this).parents().children("#tab_1 tr").eq(index).find("td")[7]).text();
					
					$.post('/amp/check/micState.do',{'st_mic':st_mic,'new_mic_state':new_mic_state});
				});
				// 选中点位状态更新后的状态
				$("select[name=mic_state]").each(function(){
					var mic_state = $(this).attr("micstate");	
					$(this).val(mic_state);
				});
				
				//绑定实时数据
				$("#tab_1 tr").each(function(){
					$(this).find("td").eq(7).dblclick(function(){
						var state = $(this).parents("tr").attr("state");
						var act = $(this).parents("tr").find("td").eq(2).text();
						var media = $(this).parents("tr").find("td").eq(3).text();
						var point = $(this).parents("tr").find("td").eq(4).text();
						var tab_url = "tab_url";
						
						realData($(this).text(),state,act,media,point,tab_url);
					});
				});
				
			}//end function(data)
	);
	$("dropdown-menu li").remove(); // 去重状态显示列表
	
}

$('#myMod').each(function(){
	$(this).modal({backdrop: 'static', keyboard: false});
});

/**
 * 客户筛选查询
 * @param mod
 */
function getCusName(mod){
	$("input[list=mic-search]").val('');
	
	$("input[list=activity1-search]").val('');
	
	//下拉客户的值
	cusName = $(mod).find("option:selected").val();
	
	setCookie("cusName",cusName);
	
	$(this).find("option:selected").attr("selected","selected");
	
	//下拉状态的值
	ulstate = $("#dropdownMenu1").text();
	var i=0;
	
	if(ulstate.indexOf("问题数据筛选") < 0 && ulstate.indexOf("全部数据") < 0){
		for(;online_data.length > i;i++){
			if(ulstate.indexOf(online_data[i]) >= 0){
				break;
			}
		}
	}
//	console.log(i + "==" + cusName);
	
	checkTr(i,cusName);
	
	trLen = checkTr(i,cusName);
    $("#sumcus").text("点位数："+trLen+"件");
}

function ajaxFileUpload(){
	var value = $("#filename").val();
	if(value != null && value != ""){
		// 执行图片异步上传操作的函数
	    $.ajaxFileUpload({
	        //  处理文件上传操作的服务器端地址
	        url: url,
	        secureuri: false,
	        //是否启用安全提交,默认为false 
	        fileElementId: 'filename',
	        //文件选择框的id属性
	        dataType: 'text',
	        //服务器返回的格式,可以是json或xml等
	        success: function(data, status) { //服务器响应成功时的处理函数
	            if (status == "success") { //0表示上传成功(后跟上传后的文件路径),1表示失败(后跟失败描述)
	            	  initData(true); //执行完上传操作后刷新实时页面
	                  initData2(true); //执行完上传操作后刷新实时页面
	                //返回信息提示
	                $(".msg").text("上传成功!").css({ "display": "inline"});
	            } else {
	                $(".msg").text("图片上传失败!").css({"display": "inline"});
	            }
	        },
	        error: function(data, status, e) { //服务器响应失败时的处理函数
	            $(".msg").text("服务器端无响应!").css({"display": "inline"});
	        }
	    });
	    return false;
	}else{
		$(".msg").text("请选择文件在上传！").css({"display": "inline"});
	}
}

function addTodoInfo(activityCode,st_mic,new_mic_state,or_mic_state){
	
	var planformPer = 'activityCode='+activityCode+'&nowState='+or_mic_state+'&nextState='+new_mic_state+'&todoType=1&ofTaskType=1&mic='+st_mic;
		// 活动编号 当前状态 下一个状态 任务类别(0活动任务，1 点位)
//	console.log(planformPer);
	$.post("/amp/amp/addTodoTask.do", planformPer, function(msg) {
		if (msg.message != "1") {
			layer.msg(msg.message, function() {
			});
		} else {
			location.reload();
		}
	});
}

/**
 * 查询实时数据。
 */
function realData(mic,state,act,media,point,tab_url){
//	alert(mic);
	act = encodeURI(act);
	media = encodeURI(media);
	point =  encodeURI(point);
	tab_url= encodeURI(tab_url);  //判断tab点击路径
	
	var dateStr = $("input[placeholder='选择日期']").val();
	
	if($("#home").attr("aria-expanded") == 'false'){
		dateStr = $("#date1").text();
	}
	
	dateStr = dateStr.substring(0,12);
	location = "/amp/page/check/realData.html?mic=" + mic + "&date=" +  dateStr + 
	"&state=" + state + "&act=" + act + "&media=" + media + "&point=" + point + 
	"&sid="+new Date().getTime()+"&tab_url="+tab_url;
}


function checkTr(state,actName,micOne,activity1){
	var trLen = 0;
	var hideN = 0;	//用于记录DIV隐藏的TR数量
	var currentDiv = null;
	$("#tb").find("tr").each(function(){
		var  name = $(this).find("td").eq(1).text();
		var  mic = $(this).find("td").eq(7).text();
		var  activity = $(this).find("td").eq(2).text();
		
		if(currentDiv == null || currentDiv.attr("id") != $(this).parent().parent().parent().attr("id")){
    		hideN = 0;
    		currentDiv = $(this).parent().parent().parent();
    		currentDiv.show();
    	}
		
		if( $(this).attr("mergin") == 0){
			strState =  $(this).attr("state");
			if( state == strState && (name == actName || actName == "all") && ( mic == micOne || micOne == null || micOne == "")
					&& ( activity1 == activity || activity1 == null || activity1 == "") ){
			   $(this).show();	//相等时显示
			   trLen++;
			}else if( state == 0 && (name == actName || actName == 'all') && (mic == micOne || micOne == null || micOne == "")
					&& (activity1 == activity || activity1 == null || activity1 == "" )){
				   $(this).show();	//相等时显示
				   trLen++;
			}else{
				$(this).hide(); //隐藏tr
				++hideN;
				
				if(currentDiv.find("tbody tr").length == hideN){
        			currentDiv.hide();
        		}
			}
		}
		else{
		}
	});

	return trLen;
}


	
	$.post("/amp/weekExport/getUserInfoSeesion.do", {},function(data) {
		var res = eval('(' + data + ')');
		
		if(res.userRoleId == "6"){ //角色是客户
			$("select[name=cusName]").hide();
			$("#sumcus").hide();
			
			$("select[name=cusName2]").hide();
			$("#sumcus2").hide();
		}
		
		$('#mailContent').find("input").eq(1).val(res.userName);
	});

//邮件反馈
function sendMail(mic){
	
	//初始化input输入框内容
	$('#tofrom').val('');
	$('#requireDate').val('');
	$('#content').val('');
	
	$('#mailContent').css("display","show");
	
	//得到相关参数
	var mic = $($(mic).parents("tr").find("td")[7]).text();
	$('#mailContent').find("input").eq(0).val(mic);
	
	var from = $('#mailContent').find("input").eq(1).val();
	
	//页面层
	layer.open({
	  title:'邮件反馈', //标题
	  type: 1, //样式
	  skin: 'layer-ext-seaning', //加上边框
	  area: ['430px', '430px'], //宽高
	  content: $('#mailContent'),
	  btn: ['发送'], //按钮
	  yes: function(index, layero){
		    //按钮【按钮一】的回调
		  tofrom = $('#tofrom').val();
		  
		  date = $('#requireDate').val();
		  
		  content = $('#content').val();
		  
		  if(tofrom != '' && date != '' && content != ''){
			  $.post('/amp/check/mail.do',{'mic':mic,'userName':from,'toUserName':tofrom,'requireDate':date,'content':content},function(data){
				  if(data == "1"){
					  layer.msg('邮件反馈成功',{icon: 1});
				  }else{
					  layer.msg('邮件发送异常',{icon: 2});
				  }
			  });
		  }else{
			  layer.msg('页面填写不完整',{icon: 2});
		  }
		  
	  }
	});
	
}

//判断点击了哪个tab页
function getTab(tab){
	var tabStr;
	//tabStr = $(tab).attr('id');
	
	if(tab == 'date1'){
		tabStr = new Date().Format("yyyy-MM-dd");
	}else{
		tabStr = $("#date2").text().substr(0,10);
	}
	
	return tabStr;
}

//添加收藏
function saveMyFavorites(mic,tab){
	var mic = $($(mic).parents("tr").find("td")[7]).text();
	var user = $("#currentUser").text();
//	var date  = new Date().Format("yyyy-MM-dd"); //需要判断当前是是哪个tab页
	var date  =getTab(tab);
	
	$.post('/amp/check/save.do',{'mic':mic,'user':user,'date':date},function(data){
		if(data == "1"){
			layer.msg('点位：'+mic+' 收藏成功', {icon: 1});
			 $($(mic).parents().find("td").eq(15).find("img")).attr('src','/amp/images/star2.png');
			 initData(true);
			 initData2(true);
		}else if(data == "2"){
			//询问框
			layer.confirm('您已经收藏过了', {
			  btn: ['取消收藏','留着吧'] //按钮
			}, function(){
				$.post('/amp/check/delete.do',{'mic':mic,'user':user,'date':date},function(res){
					if(res == "1"){
						layer.msg('已经取消', {icon: 1});
						$($(mic).parents().find("td").eq(15).find("img")).attr('src','/amp/images/star1.png');
						initData(true);
						initData2(true);
					}else{
						layer.msg('取消失败', {icon: 2});
					}
				});
			}, function(){
			}); 
		}else{
			layer.msg('点位：'+mic+' 收藏失败', {icon: 2});
		}
	});
}

/**
 * 批量收藏点位
 * @returns
 */
function checkMic(){
	var str = ''; //初始化选项
	//获取选中的value
	$(":checkbox[name='checkMic']:checked").each(function(){  
		 str += $(this).val()+",";    
    });  
	
	var user = $("#currentUser").text();
	var date  = $("#date1").text().substr(0,10); //需要判断当前是是哪个tab页
	if(str == ''){
		layer.msg('至少勾选一个点位吧', {icon: 2});
		return false;
	}
	if(user == ''){
		layer.msg('登录超时，请重新登录', {icon: 2});
		return false;
	}
	$.post('/amp/check/saveMany.do',{'mic':str,'user':user,'date':date},function(data){
		if(data == "1"){
			layer.msg('点位: '+str+' 收藏成功', {icon: 1});
		}else{
			layer.msg('点位收藏失败', {icon: 2});
		}
	});
}

/**
 * 批量取消收藏点位
 * @returns
 */
function deleteCheckMic(){
	var str = ''; //初始化选项
	//获取选中的value
	$(":checkbox[name='checkMic']:checked").each(function(){  
		 str += $(this).val()+",";    
    });  
	
	var user = $("#currentUser").text();
	var date  = $("#date1").text().substr(0,10); //需要判断当前是是哪个tab页
	if(str == ''){
		layer.msg('至少勾选一个点位吧', {icon: 2});
		return false;
	}
	if(user == ''){
		layer.msg('登录超时，请重新登录', {icon: 2});
		return false;
	}
	layer.confirm('确定取消收藏吗?', {
		  btn: ['取消收藏','留着吧'] //按钮
		}, function(){
			$.post('/amp/check/deleteMany.do',{'mic':str,'user':user,'date':date},function(data){
				if(data == "1"){
					layer.msg('点位: '+str+' 取消收藏成功', {icon: 1});
					initData(true);
					initData2(true);
				}else{
					layer.msg('点位收藏失败', {icon: 2});
				}
			});
		}, function(){
		});
}
/**
 * 选择我的收藏
 * @param select
 * @returns
 */
function isShowMy(select){
	checkValue = $(select).find("option:selected").val();
	initData(true);
}
function isshow(){
	$("#tb").find("tr").each(function(){
		var isshow = $($(this).find("td").eq(17).find("input")).attr('isshow');
//		console.log(isshow == $('#currentUser').text())
		if(isshow != "null" && isshow == $('#currentUser').text()){
//			$($(this).find("td").eq(17).find("input")).attr('checked','checked');
			$($(this).find("td").eq(15).find("img")).attr('src','/amp/images/star2.png');
		}
	});
}
