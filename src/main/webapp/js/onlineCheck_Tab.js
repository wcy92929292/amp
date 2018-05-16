var mic2 = "";
var url = "";
var or_state;  //开始状态
var cx_state;  //选中查询时的状态
var select_date;
var or_mic_state; //之前的点位状态
var pv; //当前曝光数
var click; //当前点击数
var exposure_avg; //曝光预估
var click_avg; //点击预估
var support_exposure; //是否支持曝光监测 
var support_click; //是否支持点击监测
var unit ; //
var put_value;
var actCode;
var st; //判断是哪一个按钮被点击
var oldSavePath; //以前存放的路径
var cusName2; //第二个tab页的客户查询参数
var micTab; //第二个tab页mic查询的参数
var trLen2;
var activity2;
var checkValueTab;

/***********截取URL参数***************/
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

var online_data_two = new Array();  //定义上线状态
	online_data_two[0] = '全部数据';
	online_data_two[1] = '上线无数';
	online_data_two[2] = '取消上线';
	online_data_two[3] = '正常上线';
	online_data_two[4] = '推迟上线';
	online_data_two[5] = '曝光无数';
	online_data_two[6] = '点击无数';
	online_data_two[7] = '点击比曝光大';
	online_data_two[8] = '曝光预估不符';
	online_data_two[9] = '点击预估不符';
	online_data_two[10] = '不监测点击,但有数';
	online_data_two[11] = '不监测曝光,但有数';
	online_data_two[12] = '与环比差异过大';
	online_data_two[13] = '与同比差异过大';
	online_data_two[14] = '经验值差异过大';
	
var bg_color_two = new Array();  //定义改变状态时的背景颜色
	bg_color_two[0] = 'rgba(20, 93, 93, 0.6)';
	bg_color_two[1] = 'rgba(33, 11, 216, 0.6)';
	bg_color_two[2] = 'rgba(178, 178, 165, 0.4)';
	bg_color_two[3] = 'rgba(50, 203, 20, 0.6)';
	bg_color_two[4] = 'rgba(4, 144, 21, 0.6)';
	bg_color_two[5] = 'rgba(202, 53, 8, 0.6)';
	bg_color_two[6] = 'rgba(228, 97, 97, 0.6)';
	bg_color_two[7] = 'rgba(100, 163, 163, 0.6)';
	bg_color_two[8] = 'rgba(110, 173, 173, 0.6)';
	bg_color_two[9] = 'rgba(120, 183, 183, 0.6)';
	bg_color_two[10] = 'rgba(130, 193, 193, 0.6)';
	bg_color_two[11] = 'rgba(140, 203, 203, 0.6)';
	bg_color_two[12] = 'rgba(247, 92, 2, 0.6)';
	bg_color_two[13] = 'rgba(189, 63, 15, 0.6)';
	bg_color_two[14] = 'rgba(255, 0, 0, 0.6)';
	
var mic_state_two = new Array();
	mic_state_two[30] = '每日核查 -->后端核查';
	mic_state_two[31] = '每日核查 -->核查正常';
	mic_state_two[32] = '每日核查 -->前端核查';
function test12(mod){
	//传mic
	mic2 = $($(mod).parents("tr").find("td")[7]).text();
	if( $(mod).attr('expurl2') != "undifined"){
		var fileName = $(mod).attr('expurl2');
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
	if( $(mod).attr('click2') != "undifined"){
		var fileName = $(mod).attr('click2');
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
	if( $(mod).attr('after2') != "undifined"){
		var fileName = $(mod).attr('after2');
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

jQuery(document).ready(function() {
	initData2(false);
	
	 var urlParam = window.location.href;
     if(urlParam.substr(urlParam.length-3) == "two"){
     	$("#dro_two").show();
     	$("#dro_one").hide();
     }
	
	/* center modal */
	function centerModals(){
	    $('.modal').each(function(i){
	        var $clone = $(this).clone().css('display', 'block').appendTo('body');    var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
	        top = top > 0 ? top : 0;
	        $clone.remove();
	        $(this).find('.modal-content').css("margin-top", top);
	    });
	}
	$('.modal').on('show.bs.modal', centerModals);
	$(window).on('resize', centerModals);
	
	time1 = new Date().Format("yyyy-MM-dd"); 
	
	select_date = getCookie("select_date");
	if(select_date == null || select_date == ""){
		select_date = time1;
		$(".calendar input").val(time1);
		$("#date2").text(select_date+"上线");
	}else{
		select_date = getCookie("select_date");
		console.log("cookie中取到的：" +select_date);
		$(".calendar input").val(getCookie("select_date"));
		$("#date2").text(getCookie('select_date')+"上线");
	}
	
	if(location.toString().indexOf("back") == -1){ //判断上个页面是否为同一个模块，否则，清理查询条件Cookie
   	   delCookie("cx_state");
   	   delCookie("micTab");
   	   delCookie("cusName2");
   	   delCookie("activity2");
   	   delCookie("select_date");
     }
	
});	
var time1;
var put_date;
var s_mic;
function initData2(isSelect){

	//显示页面数据
	$.post(
		'/amp/onceCheck/list.do',
		{'state':cx_state,'select_date':getCookie('select_date'),'cusName':cusName2,'mic':micTab,'isShowMyTab':checkValueTab},
		function(data,status){
			
				if (data == null || data == '' || data.length == 0) {
					$('#tb2').html('没有数据。');
				}else {
					$('#tb2').html('');
				}
			
			for(var i=0;i<data.length;i++){//start
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
				var cus2 = null;
				var span2 = '<span id="st2" class="label label-primary" style="height: 30px; padding: .2em .7em .3em; font-size: 88%;">'+data[i]["customer_name"]+'</span>';
			 	var tb2 = '<table id="tab_2" class="table table-hover table-responsive">'//最后一个td暂时隐藏掉勿删备注功能暂时不用
			 		+ '<tbody id="t_id">'
			 		+ '<tr mergin2="0" click_avg="'+data[i]["click_avg"]+'" exposure_avg="' + data[i]["exposure_avg"] + '" unit= '+ data[i]["unit"] + ' put_value='+ data[i]["put_value"]
			 		+ ' sup_click='+data[i]["support_click"]+' sup_exp = '+ data[i]["support_exposure"] +'  time2 =' + data[i]["put_date"] +' state_t='
			 		+ data[i]["online_state"]+' colors ="" actCode='+data[i]["activity_code"]+'>'
			 		+ '<td style="width:128px;"><select name="select_state_two" class="select_state_two"></select>'
			 		+ '</td><td class="t_td" style="width:66px;">'+data[i]["customer_name"]+'</td><td class="t_td" style="width:95px;">'+data[i]["activity_code"]+'</td><td class="t_td" style="width:150px;">'+data[i]["activity_name"]+'</td><td style="width:75px;">'+data[i]["media_name"]+'</td><td style="width:200px;">'+data[i]["point_location"]+'</td>'
			 		+ '<td style="width:170px;">'+data[i]["put_function"]+'</td><td style="width:190px;">'+data[i]["mic"]+'</td><td style="width:70px;">'
			 		+ (data[i]["unit"] == "CPM" && data[i]["click_avg"] != null && data[i]["click_avg"] != ''
			 			? parseFloat(data[i]["click_avg"]) *  parseFloat(data[i]["put_value"]) : data[i]["click_avg"] )
			 		+'</td><td style="width:70px;">'
			 		+ (data[i]["unit"] == "CPM" && data[i]["exposure_avg"] != null && data[i]["exposure_avg"] != '' 
			 			? 1000 *  parseFloat(data[i]["put_value"]) : data[i]["exposure_avg"] )
			 		+'</td>'
			 		+ '<td style="width:70px;">'+data[i]["click"]+'</td><td style="width:70px;">'+data[i]["pv"]+'</td><td id="btn_td" style="width:230px;">'
			 		+ '<button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#myMod" id="baoguang2" onclick="test12(this)" expurl2='+data[i].exposure_file+'>曝光核查</button> '
			 		+ '<button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#myMod" id="dianji2" onclick="test12(this)" click2='+data[i].click_file+'>点击核查</button>'+'&nbsp;'
			 		+ '<button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#myMod" id="houduan2" onclick="test12(this)" after2='+data[i].after_file+'>后端核查</button></td>'
			 		+ '<td style="width:185px;"><select class="mic_state_two" name="mic_state_two" now_state_id='+data[i]["now_state_id"]+'></select></td><td style="display:none;">' 
			 		+ '<button style="height:28px;" type="button" class="btn btn-default btn-sm" data-toggle="modal" data-target="#myModal" onclick="local()">备注</button></td> '//最后一个td暂时隐藏掉勿删备注功能暂时不用
			 		+ '<td style="width:70px;">'
			 		+ '<img alt="" src="/amp/images/star1.png" width="25px;" height="25px;" onclick="saveMyFavorites(this,date2)">'
					+ '</td><td style="width:70px;">'
					+ '<button type="button" class="btn btn-danger btn-sm" onclick="sendMail(this)">反馈</button></td>'
					+ '<td style="width:100px;">'
					+ '<input type="checkbox" name="checkMic" isshow='+data[i]["collect_user"]+' value='+data[i]["mic"]+' style="margin-left: 47%;"/></td>'
			 		+ '</tr></tbody></table>';
			 	
			 		if($("#cus2"+data[i]['customer_name']).length == 0){
						cus2 = $("<div id = 'cus2"+data[i]['customer_name']+"' pid2='"+data[i]['customer_name']+"'></div>");
						$("#tb2").append(cus2);
					}
					$("#cus2"+data[i]['customer_name']).append(span2+tb2);

					if ($("#cus2" + data[i]['customer_name']).find("span").length > 1){
						//1. 删除span(第二个span开始删除)
						$($("#cus2" + data[i]['customer_name']).children("span:gt(0)")).remove();
						//2. 找到行
						var old2 = $("#cus2" + data[i]['customer_name']).children("table").eq(0).children("tbody");
						var tr2 =  $("#cus2" + data[i]['customer_name']).children("table").eq(1).children("tbody").children("tr");
						$( $("#cus2" + data[i]['customer_name']).children("table:gt(0)") ).remove() ;
						$( $("#cus2" + data[i]['customer_name']).children("table").eq(1).children("thead")).remove() ;
						//3. 追加到一个table中
						old2.append(tr2);
					}
			} //end
			
			isshowTab();
			
			// 1.判断鼠标点击了哪个按钮，然后相应的改变图片上传框的文字信息
			$("[id=baoguang2]").each(function() {
				$(this).click(function() {
					st = 0;
					oldSavePath = $(this).attr('expurl2');
					mic2 = $($(this).parents("tr").find("td")[7]).text();
					actCode = $(this).parents().children("tr").attr("actcode");
					$("#myModalLabel_text").text("曝光核查");
					url = '/amp/check/photoUpload.do?mic='+ mic2+ '&'+ 'actCode='+actCode+ '&'+ 'st=0'+'&reupload=false';
					});
			});
			$("[id=dianji2]").each(function() {
				$(this).click(function() {
					st = 1;
					oldSavePath = $(this).attr('click2');
					mic2 = $($(this).parents("tr").find("td")[7]).text();
					actCode = $(this).parents().children("tr").attr("actcode");
					$("#myModalLabel_text").text("点击核查");
					url = '/amp/check/photoUpload.do?mic='+ mic2+ '&'+ 'actCode='+actCode+ '&'+ 'st=1'+'&reupload=false';
					});
				});
			$("[id=houduan2]").each(function() {
				$(this).click(function() {
					st = 2;
					oldSavePath = $(this).attr('after2');
					mic2 = $($(this).parents("tr").find("td")[7]).text();
					actCode = $(this).parents().children("tr").attr("actcode");
					$("#myModalLabel_text").text("后端核查");
					url = '/amp/check/photoUpload.do?mic='+ mic2+ '&'+ 'actCode='+actCode+ '&'+ 'st=2'+'&reupload=false';
				});
		       });// 
			//追加上线状态列表
			$("ul[name=tab_li]").html("");
			for (i=0;i<online_data_two.length;i++){				
				$("ul[name=tab_li]").append("<li><a value="+i+">"+online_data_two[i]+"</a></li>");
				$(".select_state_two").append("<option value="+i+">"+online_data_two[i]+"</option>");
				
			}	
			//对select元素所有指定系统判断的地方禁用
			$(".select_state_two").each(function(){
				for(j = 5;j<12;j++){
					$(this).find("option").eq(1).attr("disabled","disabled");
					$(this).find("option").eq(j).attr("disabled","disabled");
				}
			});
			//下拉状态筛选时状态信息显示,内存中操作查询！
			$("ul[name=tab_li]").find("li a").click(function() {
				$("input[list=mic-search2]").val('');
				
				cx_state = $(this).attr("value");
				if(cx_state==0){
					$(".t_top").text('全部数据');
				}else if(cx_state==1){
					$(".t_top").text('上线无数');
				}else if(cx_state==2){
					$(".t_top").text('取消上线');
				}else if(cx_state==3){
					$(".t_top").text('正常上线');
				}else if(cx_state==4){
					$(".t_top").text('推迟上线');
				}else if(cx_state==5){
					$(".t_top").text('曝光无数');
				}else if(cx_state==6){
					$(".t_top").text('点击无数');
				}else if(cx_state==7){
					$(".t_top").text('点击比曝光大');
				}else if(cx_state==8){
					$(".t_top").text('曝光预估不符');
				}else if(cx_state==9){
					$(".t_top").text('点击预估不符');
				}else if(cx_state==10){
					$(".t_top").text('不监测点击，但有数');
				}else if(cx_state==11){
					$(".t_top").text('不监测曝光，但有数');
				}else if(cx_state==12){
					$(".t_top").text('与环比差异过大');
				}else if(cx_state==13){
					$(".t_top").text('与同比差异过大');
				}else{
					$(".t_top").text('经验值差异过大');
				}
				 //页面条件筛选查询
				//页面条件筛选查询
				trLen2 = 0;
				actName2 = $("select[name=cusName2]").find("option:selected").val();
				
				console.log(cx_state+"--------"+actName2);
				trLen2 = checkTrTab(cx_state,actName2,micTab,activity2);
		        $("#sumcus2").text("点位数："+trLen2+"件");
		        
		        setCookie("cx_state",cx_state);
			});
			
			//删除点位的全部数据option
			$("select[name=select_state_two]").each(function(){
				$(this).find("option:first").remove();  //列表的第一个option remove掉
			})
			
			//选中默认查询出来的上线状态
			$("tr[state_t]").each(function(i){
				
				//判断是否支持曝光，点击，不支持，在对应的预估显示“不支持”
				setSupport(this);
				
				var index = $(this).index();
				var state_t = $(this).attr("state_t");	//页面初始化以后的状态
				pv =parseInt( $($(this).parents().children("#tab_2 tr").eq(index).find("td")[11]).text() );
		        click = parseInt($($(this).parents().children("#tab_2 tr").eq(index).find("td")[10]).text());
		        support_exposure = parseInt($(this).attr("sup_exp") == "null" ? 0 : parseInt($(this).attr("sup_exp")));
		        support_click =parseInt( $(this).attr("sup_click") == "null" ? 0 : parseInt($(this).attr("sup_click")));
		        unit = $(this).attr("unit");
		        put_value =  $(this).attr("put_value");
		        var time =  $(this).parents().children("#tab_2 tr").eq(index).attr("time2"); 
		        var mic = $($(this).parents().children("#tab_2 tr").eq(index).find("td")[7]).text();  //获取MIC
		        
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
		    //未做状态修改的，没有人为操作的
		    if(state_t == -1){
		    	
		    	stateT = 0;
		    	//支持曝光以及点击
		    	if(support_exposure == 1 && support_click == 1){
		    		//上线无数
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
		    	$(this).attr("state_t",stateT) //改变行中state_t属性值，方便查询用
		    	
		    	//改变上线状态  通过投放时间和短代码更新上线状态
//				$.post(
//					'/amp/onceCheck/changeState.do',
//					{'state':stateT,'time':time,'mic':mic}
//				); 
		    }else{
		    	//显示的这个当前状态是人为操作的，可改变上线状态
		    	if ( $(this).find("select").val(state_t) ){
					 $(this).children("td").find("option:selected").attr({"selected":"selected"});
				 }
		    }
				   
		});
			//用户更改后的颜色与点击更改的颜色一致
			$("tr[colors]").each(function(){
				var index = $(this).index();
				var val = $(this).parents().children("#tab_2 tr").eq(index).find("td:eq(0)").find("option:selected").text();
				//col = $(this).attr("colors");
				
				if(val=="上线无数"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[1]);
				}else if(val=="取消上线"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[2]);
				}else if(val=="正常上线"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[3]);
				}else if(val=="推迟上线"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[4]);
				}else if(val=="曝光无数"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[5]);
				}else if(val=="点击无数"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[6]);
				}else if(val=="点击比曝光大"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[7]);
				}else if(val=="曝光预估不符"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[8]);
				}else if(val=="点击预估不符"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[9]);
				}else if(val=="不监测点击但有数"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[10]);
				}else if(val=="不监测曝光但有数"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[11]);
				}else if(val=="与环比差异过大"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[12]);
				}else if(val=="与同比差异过大"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[13]);
				}else if(val=="经验值差异过大"){
					$(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[14]);
				}
			});
			
			//获取之前默认选中的状态(原始状态)
			$(".select_state_two").click(function(){
				 or_state = $(this).val();
			});
			//更改上线状态背景色
			$("tr[state_t]").change(function(){
				var index = $(this).index();
				var cn_state = $(this).find("option:selected").val();  //获取选中以后的状态
			    var cn_mic = $($(this).parents().children("#tab_2 tr").eq(index).find("td")[7]).text();  //获取MIC
			    if(cn_state=="0"){
					 alert("点我没用");
				 }else if(cn_state=="1"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[1]);
				 }else if(cn_state=="2"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[2]);
				 }else if(cn_state=="3"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[3]);
				 }else if(cn_state=="4"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[4]);
				 }else if(cn_state=="5"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[5]);
				 }else if(cn_state=="6"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[6]);
				 }else if(cn_state=="7"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[7]);
				 }else if(cn_state=="8"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[8]);
				 }else if(cn_state=="9"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[9]);
				 }else if(cn_state=="10"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[10]);
				 }else if(cn_state=="11"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[11]);
				 }else if(cn_state=="12"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[12]);
				 }else if(cn_state=="13"){
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[13]);
				 }else{
					 $(this).parents().children("#tab_2 tr").eq(index).css("background",bg_color_two[14]);
				 }
			   $(this).attr("state_t",cn_state);
			   
			   var time =  $(this).parents().children("#tab_2 tr").eq(index).attr("time2");
//			    改变上线状态  通过投放时间和短代码更新上线状态
//				$.post(
//					'/amp/onceCheck/changeState.do',
//					{'state':cn_state,'time':time,'mic':cn_mic},
//					function(data){
//					}
//				); 
			});
			
			function clearTwo(){
				var file = $("#filename") ;
				file.after(file.clone().val(""));      
				file.remove();
			}
			
			function showPicTwo(){
				new uploadPreview({ UpBtn: "filename", DivShow: "imgdiv", ImgShow: "imgShow" });
			}
			
			$(".close").click(function(){
				clearTwo();
				$("#imgdiv").remove();
				showPicTwo();
				$('.col-sm-6').append('<div id="imgdiv" style="height:310px;"><img id="imgShow" /></div>');
				$(".msg").css({ "display": "none"});
			});
			
			//用户已经点击的按钮(曝光、点击、后端)，查看上传了的图片
			$("button[expurl2]").each(function() {
				var expurl2 = $(this).attr("expurl2");
				    if (expurl2 != null&& expurl2 != "null"&& expurl2 != "") {
						$(this).removeClass("btn btn-danger btn-sm").addClass("btn btn-success btn-sm aaabbb").text("曝光已查");
					}
			    });
			$("button[click2]").each(function() {
				var click2 = $(this).attr("click2");
				    if (click2 != null&& click2 != "null"&& click2 != "") {
						$(this).removeClass("btn btn-danger btn-sm").addClass("btn btn-success btn-sm aaabbb").text("点击已查");
					}
			    });
			$("button[after2]").each(function() {
				var after2 = $(this).attr("after2");
				    if (after2 != null&& after2 != "null"&& after2 != "") {
						$(this).removeClass("btn btn-danger btn-sm").addClass("btn btn-success btn-sm aaabbb").text("后端已查");
					}
			    });
			
			/*************************************************新增加重新上传************************************************************/
		    $(".reupload").click(function(){
				$(".hideModal").modal('hide'); //已查框隐藏
				//获取参数
				url = '/amp/check/photoUpload.do?mic='+ mic2+ '&'+ 'actCode='+actCode+ '&'+ 'st='+st+'&oldSavePath='+oldSavePath+'&reupload=true';
			});
			
		    /***********************************/
		    $("#tb2").find("span").each(function(){ //遍历所有span元素的值
    			$(this).remove();
		    });
		    
		    /*************************************************得到该页面上的所有客户************************************************************/
		    if(!isSelect){
		    	$("#tb2").find("div").each(function(){
		    	
			    	var cusid = $(this).attr('pid2'); //1.得到下拉列表中的客户参数
			    	
			    	$("select[name=cusName2]").append("<option value='" + cusid+ "'> "+ cusid + "</option>");
		    	});
		    }
		    
		    
		    cx_state = getCookie("cx_state");
			cusName2 = getCookie("cusName2");
			micTab = getCookie("micTab");
			activity2 = getCookie("activity2");
		    
		  //查找cookie中当前选中的值(上线状态的)
			$("ul[name=tab_li]").find("li").each(function(){
				var a = $(this).find('a');
				if(a.attr('value') == getCookie("cx_state")){
					$(".t_top").text(a.text());
				}		
			});
			
			//查找cookie中当前选中的值(筛选客户下拉列表的)
			$("select[name=cusName2]").find("option").each(function(){
				if(cusName2 == null ){
					cusName2 = "all";
				}else if($(this).val() == getCookie("cusName2")){
					$(this).attr("selected","selected");
				}		
			});
			
			//设置短代码的
			$("input[list=mic-search2]").val(micTab);
			//设置活动编号的
			$("input[list=activity2-search]").val(activity2);
			
			console.log("第二个页  "+cx_state)
			console.log("第二个页  "+cusName2)
			console.log("第二个页  "+micTab)
			console.log("第二个页  "+activity2)
			
			trLen2 = checkTrTab(cx_state, cusName2, micTab, activity2);

		    $("#sumcus2").text("点位数："+ trLen2 +"件");
		    
		    //3.获取mic输入框的值去查询
	    	 $("#activity02").click(function(){ //输入框失去焦点就去查找对应的mic
	    		 //得到输入框的值
	    		 micTab = $("input[list=mic-search2]").val();
	    		 
	    		 setCookie("micTab",micTab);
	    		
	    		 if(cx_state == undefined || cx_state == "0"){
	    			 cx_state = "0";
	    		 }
	    		 
	    		 var cusName2 = $("select[name=cusName2]").find("option:selected").val();
	    		 
	    		 console.log("cx_state: "+cx_state );
	    		 console.log("cusName2: "+cusName2 );
	    		 console.log("micTab: "+micTab );
	    		 
	    		 //得到输入框的值
	    		 activity2 = $("input[list=activity2-search]").val();
	    		 
	    		 setCookie("activity2",activity2);
	    		 
	    		 if(cx_state == undefined || cx_state == "0"){
	    			 cx_state = "0";
	    		 }
	    		 var cusName2 = $("select[name=cusName2]").find("option:selected").val();
	    		 
	    		 setCookie("cusName2",cusName2);

	    		 console.log("cx_state: "+cx_state );
	    		 console.log("cusName2: "+cusName2 );
	    		 console.log("micTab: "+micTab );
	    		 console.log("activity02: "+activity2);

	    		 trLen2 = checkTrTab(cx_state, cusName2,micTab,activity2)
	    		 $("#sumcus2").text("点位数："+trLen2+"件");
	    		 
	    	 });
	    	 
	    	
			//追加点位状态列表
			for(j = 30;j< mic_state_two.length; j++){
				var option = '<option value='+j+'>'+mic_state_two[j]+'</option>'
				$("[name=mic_state_two]").append(option);
			}
			
			//改变点位状态
			$("select[name=mic_state_two]").click(function(){
				or_mic_state = $(this).val();     //原始状态
			});
			
			$("select[name=mic_state_two]").change(function(){
				var index = $(this).parents().parents().index();
				var new_mic_state = $(this).val();    //改变时状态
				var st_mic = $($(this).parents().children("#tab_2 tr").eq(index).find("td")[5]).text();
				$.post(
						'/amp/onceCheck/micState.do',{'st_mic':st_mic,'new_mic_state':new_mic_state}
				);
			});
			
			// 选中点位状态更新后的状态
			$("select[name=mic_state_two]").each(function() {
				var index = $(this).index();
				var mic_state_two = $(this).attr("now_state_id");	
				$(this).val(mic_state_two);
			});
			
			//绑定实时数据click时间
			$("#tab_2 tr").each(function(){
				$(this).find("td").eq(7).dblclick(function(){
					var state = $(this).parents("tr").attr("state_t");
					var act = $(this).parents("tr").find("td").eq(2).text();
					var media = $(this).parents("tr").find("td").eq(3).text();
					var point = $(this).parents("tr").find("td").eq(4).text();
					
					realData($(this).text(),state,act,media,point);
				});
			});
			
		}//end function(data)
	);
	$(".t_menu li").remove(); // 去重状态显示列表
	
}

/**
 * 第二个tab页面的客户筛选
 * @param mod
 */
function getCusNameTab(mod){
	
	$("input[list=mic-search2]").val('');
	
	//下拉客户的值
	cusName2 = $(mod).find("option:selected").val();
	
	setCookie("cusName2",cusName2);
	
	//下拉状态的值
	ulstate2 = $("#dropdownMenu2").text();
	var i=0;
	
	if(ulstate2.indexOf("上线状态查看") < 0 && ulstate2.indexOf("全部数据") < 0){
		for(;online_data_two.length > i;i++){
			if(ulstate2.indexOf(online_data_two[i]) >= 0){
				break;
			}
		}
	}
	//i是状态 
	console.log(i + "==" + cusName2);
	checkTrTab(i,cusName2);
	
	trLen2 = checkTrTab(i,cusName2);
    $("#sumcus2").text("点位数："+trLen2+"件");
}

$('#myMod').each(function(){
	$(this).modal({backdrop: 'static', keyboard: false});
})

//日历查询跳转
$(function(){
    select_date = $(".calendar input").val();
    
	$(".calendar input").focus(
		function(){
		 if($(this).val() == select_date){
			 $(".message").css("display" , "none");
		 }else if($(this).val() > new Date().Format("yyyy-MM-dd")){
			 $(".message").css("display","block").text("不能大于当前系统时间!");
		 }else{
			 $(".message").css("display" , "none");
			 select_date = $(this).val();
			 $("#date2").text(select_date+"上线");
			 $("#dropdownMenu2").text(online_data_two[0]);
			
			 cx_state = null;
			 
	    	$("select[name=cusName2] option[value!='all']").remove();
	    	
	    	if(cusName2 != ""&& cusName2 != null){
	    		cusName2 = null;
	    	}
			    	
//			  console.log("状态："+cx_state+"客户："+cusName2);
	    	
	    	setCookie("select_date",select_date);
	    	
	    	console.log("选中的 "+select_date);
			 initData2(false);
		 }
	 }	
	)
//	 $.session.set('session_date',$("#date2").text());
});

//判断该点位是否支持曝光，点击
function setSupport(_tr){
	
	if($(_tr).attr("sup_click") != "1"){
		$(_tr).find("td").eq(8).html("不支持");
	}

	if($(_tr).attr("sup_exp") != "1"){
		$(_tr).find("td").eq(9).html("不支持");
	}
}

function checkTrTab(state,actName,micTab,activity2){
	var trLen = 0;
	var hideN = 0;	//用于记录DIV隐藏的TR数量
	var currentDiv = null;
	$("#tb2").find("tr").each(function(){
		var  name = $(this).find("td").eq(1).text();
		var  mic = $(this).find("td").eq(7).text();
		var  activity = $(this).find("td").eq(2).text();
		
		if(currentDiv == null || currentDiv.attr("id") != $(this).parent().parent().parent().attr("id")){
    		hideN = 0;
    		currentDiv = $(this).parent().parent().parent();
    		currentDiv.show();
    	}

		if( $(this).attr("mergin2") == 0){
			strState =  $(this).attr("state_t");
			
			if((state == strState || state == null) && (name == actName || actName == "all") && ( mic == micTab || micTab == null || micTab == "")
					&& ( activity2 == activity || activity2 == null || activity2 == "") ){
			   $(this).show();	//相等时显示
			   trLen++;
			}else if( (state == 0 || state == null)&& (name == actName || actName == 'all') && ( mic == micTab || micTab == null || micTab == "")
					&& ( activity2 == activity || activity2 == null || activity2 == "") ){
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

function isShowMyTab(select){
	checkValueTab = $(select).find("option:selected").val();
	initData2(true);
}

function isshowTab(){
	$("#tb2").find("tr").each(function(){
		var isshow = $($(this).find("td").eq(17).find("input")).attr('isshow');
		if(isshow != "null" && isshow == $('#currentUser').text()){
//			$($(this).find("td").eq(17).find("input")).attr('checked','checked');
			$($(this).find("td").eq(15).find("img")).attr('src','/amp/images/star2.png');
		}
	});
}