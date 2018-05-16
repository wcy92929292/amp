
$(function () {
	//////////////////////////////////////////////////////////////////
	//历史点位展示
		//$(".highchart-head:eq(0)").text(aName+"-"+media);
	
		var url = location.toString();
//		"/amp/page/check/realData.html?mic=" + mic + "&date=" +  
//		dateStr + "&state=" + state + "&act=" + act + "&media=" + media + "&sid="+new Date().getTime();
	
		var mic = url.substring(url.indexOf("?mic=") + 5, url.indexOf("&date="));
		var endDate = url.substring(url.indexOf("&date=") + 6, url.indexOf("&state="));
		var state = url.substring(url.indexOf("&state=") + 7, url.indexOf("&act="));
		var act = decodeURI(url.substring(url.indexOf("&act=") + 5, url.indexOf("&media=")));
		var media = decodeURI(url.substring(url.indexOf("&media=") + 7, url.indexOf("&point=")));
		var point = decodeURI(url.substring(url.indexOf("&point=") + 7, url.indexOf("&sid=")));
		var tab_url=decodeURI(url.substring(url.indexOf("&tab_url=")+9));

		$("#historyState ul li").eq(state - 1).css("background","red");
		
		if(tab_url=="tab_url"){
			$("#myTab li:eq(1)").css("display","none");
			show($("#myTab li").eq(0)[0],"#right",$("#myTab li").eq(1)[0],"#left");
			
		}else if(tab_url=="undefined"){
			show($("#myTab li").eq(0)[0],"#right",$("#myTab li").eq(1)[0],"#left");
		}else{
			show($("#myTab li").eq(0)[0],"#right",$("#myTab li").eq(1)[0],"#left");
		}
		
		function getPV(data){
			console.log(data.impDataCaliber);
			switch(data.impDataCaliber){
				case 1:return data.cleanImpPV;
				case 2:return data.dCleanImpPV;
				default:return data.dirtyImpPV;
			}
		}
		
		function getPC(data){
			switch(parseInt(data.clkDataCaliber)){
				case 1:return data.cleanClkPV; break;
				case 2:return data.dCleanClkPV; break;
				default:return data.dirtyClkPV; break;
			}
		}
		
		Highcharts.setOptions({ 
		    colors: ['#FFFF33', '#FF3333', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', 
		'#FFF263', '#6AF9C4']
		}); 
		
		//请求历史数据
		var historyObj = {
				date:[],
				mediaName:"",
				activityName:"",
				pointLocation:"",
				pv:[],
				pc:[]
		};
		var historyUrl = "/amp/real/history.do";
		var historyData = {};
		$.post(historyUrl,{"mic":mic,"endDate":endDate},function(data){
			
			historyObj.mediaName = media;
			historyObj.activityName = act;
			historyObj.pointLocation = point;
			
			for(var i=0;i<data.length;i++){
				historyObj.date.push(FormatDate(data[i].putDate));
				historyObj.pv.push(getPV(data[i]));
				historyObj.pc.push(getPC(data[i]));
			}
			
			//回显历史数据列表
			$("#historyDataList ul:gt(0)").each(function(i){
				$(this).find("li:eq(0)").text(historyObj.date[i]);
				$(this).find("li:eq(1)").text(historyObj.pv[i]);
				$(this).find("li:eq(2)").text(historyObj.pc[i]);
			});
		//////////////////////显示历史信息//////////////////////////////////
			historyData = {
					
		        title: {
		            text: historyObj.activityName +  "——" + historyObj.mediaName,
		            x: 20, //center
		            style:{
		            	color:"RGB( 255, 255, 255)",
		            	fontWeight: 'bold',
		            }
		            
		        },
		        subtitle: {
		            text: mic,
		            x: 20,
		            style:{
		            	color:"RGB( 255, 255, 255)",
		            	fontWeight: 'bold',
		            }
		        },
		        chart: {
		        	inverted:false,
		            type: 'line',
		            backgroundColor: 'rgba(0,0,0,0)',
		           
		            style:{
		            	color:"RGB( 255, 255, 255)",
		            	fontWeight: 'bold',
		            }
		        },
		        xAxis: {
		        	gridLineWidth: 1,
		            categories: historyObj.date,
//		            style:{
//		            	color:'red',
//		            },
		            labels: {
		                style: {
		                    color: '#fff',
		                    fontWeight: 'bold',
		                    
		                }
		            },
		        },
		        yAxis: {
		            title: {
		                text: '点击、曝光/次数',
		                style:{
		                	color: ' RGB( 250 250 250)',
		                    fontWeight: 'bold',
			            }
		            },
		            labels: {
		                style: {
		                    color: '#fff',
		                    fontWeight: 'bold',
		                }
		            },
		            plotLines: [{
		                value: 0,
		                width: 1,
		            }]
		        },
		        tooltip: {
		            valueSuffix: ' 次'
		        },
		        legend: {
		            layout: 'vertical',
		            align: 'right',
		            verticalAlign: 'middle',
		            borderWidth: 0,
		            itemStyle : {
		                'color' : '#fff'
		            }
		        },
		        series: [{
		            name: 'PV',
		            data: historyObj.pv,
		            style:{
		            	backgroundColor:'RGB( 255, 255, 0)',
		            },
		            marker:{
		            	fillColor:'RGB( 255, 255, 0)',
		            }
		        }, {
		            name: 'PC',
		            data: historyObj.pc,
		            marker:{
		            	fillColor:'RGB(9, 249, 218)',
		            }
		        }]
		    };
			
			
			//刷新数据
			$('#historyData').highcharts(historyData);
		});
		
		var hourData = {};
		var hourUrl = "/amp/real/hour.do";
		var hourDataObj = {
			activityName:"",
			mediaName:"",
			pointLocation:"",
			h:[],
			pv:[],
			pc:[]
		};
		//获取小时数据
		$.post(hourUrl,{mic:mic},function(data){
			for(var i=0;i<data.length;i++){
				hourDataObj.h[data[i].hour] = data[i].hour;
				hourDataObj.pv[data[i].hour] = data[i].himp;
				hourDataObj.pc[data[i].hour] = data[i].hclk;
			}
			hourDataObj.activityName = act;
			hourDataObj.mediaName = media;
			hourDataObj.pointLocation = point;
			
			//回显小时数据列表
			$("#hourDataList ul[class!='stitle']").each(function(j){
				if(j + 1 > hourDataObj.h.length) 
					return;
				var h = hourDataObj.h[j];
				//$(this).find("li:eq(0)").text(h + "~" + (h +1) +"dian" );
				$(this).find("li:eq(1)").text(hourDataObj.pv[j]);
				$(this).find("li:eq(2)").text(hourDataObj.pc[j]);
			});
			
			
			$("#hourDataList ul").mouseover(function(){
				$(this).find("li").css("background","#0410ac");
			}).mouseout(function(){
				$(this).find("li").css("background","");
			});
			$("#historyDataList ul").mouseover(function(){
				$(this).find("li").css("background","#0410ac");
			}).mouseout(function(){
				$(this).find("li").css("background","");
			});
			
			//封装小时数据
			hourData = {
					chart: {
			            type: 'line',
			            backgroundColor: 'rgba(0,0,0,0)',
			            style:{
			            	color:"RGB( 255, 255, 255)",
			            	fontWeight: 'bold',
			            }
			        },
			        title: {
			            text: hourDataObj.activityName + "——" + hourDataObj.mediaName,
			            x: 20, //center
			            style:{
			            	color:"RGB( 255, 255, 255)",
			            	fontWeight: 'bold',
			            }
			        },
			        subtitle: {
			            text: mic,
			            x: 20,
			            style:{
			            	color:"RGB( 255, 255, 255)",
			            	fontWeight: 'bold',
			            }
			        },
			        xAxis: {
			        	gridLineWidth: 1,
			            categories: hourDataObj.h,
			            style:{
			            	color:"RGB( 255, 255, 255)",
			            	fontWeight: 'bold',
			            },
			            labels: {
			                style: {
			                    color: '#fff',
			                    fontWeight: 'bold',
			                }
			            },
			        },
			        yAxis: {
			        	gridLineWidth: 1,
			            title: {
			                text: '点击、曝光/次数',
			                style:{
				            	color:"RGB( 255, 255, 255)",
				            	fontWeight: 'bold',
				            }
			            },
			            labels: {
			                style: {
			                    color: '#fff',
			                    fontWeight: 'bold',
			                }
			            },
			            plotLines: [{
			                value: 0,
			                width: 1,
			                color: '#0f0'
			            }]
			        },
			        tooltip: {
			            valueSuffix: ' 次'
			        },
			        legend: {
			            layout: 'vertical',
			            align: 'right',
			            verticalAlign: 'middle',
			            borderWidth: 0,
			            style:{
			            	color:"RGB( 255, 255, 255)",
			            	fontWeight: 'bold',
			            },
			            itemStyle : {
			                'color' : '#fff'
			            }
			        },
			        series: [{
			            name: 'PV',
			            data: hourDataObj.pv,
			            marker:{
			            	fillColor:'RGB( 255, 255, 0)',
			            }
			        }, {
			            name: 'PC',
			            data: hourDataObj.pc,
			            marker:{
			            	fillColor:'RGB(9, 249, 218)',
			            }
			        }]
			    };
			
			//刷新页面小时数据
			$('#hourData').highcharts(hourData);
			hourState();
		});//end 获取小时数据请求
		
		/////////////////////////////////////////////////////////////
			
//		});// end post()
		

		
	
	
	$("#myTab li").eq(0).click(function(){
		show(this,"#right",$("#myTab li").eq(1)[0],"#left");
		$('#hourData').highcharts(hourData);
	});
	$("#myTab li").eq(1).click(function(){
		show(this,"#left",$("#myTab li").eq(0)[0],"#right");
		
		$('#historyData').highcharts(historyData);
	});
		

	
	function show(showLi,showDiv,hideLi,hideDiv){
		
		$(showLi).css("background","#C6DDFF");
		$(showDiv).show();
		
		$(hideLi).css("background","#004B54");
		$(hideDiv).hide();
	}
	
	//关闭，返回上一级页面
	$(".clo").click(function(){
		location = "/amp/page/check/check.html?back";
	});
	
	//小时数据状态
	function hourState(){
		var li = $("#hourState ul li");
		li.eq(1).text(mic);
		li.eq(3).text(hourDataObj.pointLocation);
		li.eq(4).text("第"+hourDataObj.h[hourDataObj.h.length - 1]+"时监测效果：");
		li.eq(5).html("PV：<b>" +hourDataObj.pv[hourDataObj.pv.length - 1]+"</b>");
		li.eq(6).html("PC：<b>"+hourDataObj.pc[hourDataObj.pc.length - 1]+"</b>");
		li.eq(7).html("<b>"+formatdate(new Date().getTime())+"</b>&nbsp;总监测：");
		li.eq(8).html("PV：<b>"+sumData(hourDataObj.pv)+"</b>");
		li.eq(9).html("PC：<b>"+sumData(hourDataObj.pc)+"</b>");
	}
	
	/**
	 * 对数组进行求和
	 */
	function sumData(data){
		var sum = null;
		for(var i=0;i<data.length;i++){
			sum += data[i];
		}
		return sum;
	}
		
});//end 历史点位展示
  
