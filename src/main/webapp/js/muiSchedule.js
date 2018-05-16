$(function(){
	var userName= $.cookie('userName');
	if(userName=="luoyingying"||userName=="guantiantian"||userName=="wangchunyu"
		||userName=="wangli"||userName=="wuchenyang"||userName=="liyan"){
		$("#addMedia").css("display","block");
	}else{
		$("#addMedia").css("display","none");
	}
	
	//取到不同的媒体类型
	var str2;
  $.each(media_type.values, function (k, v1) {
      str2 ='<option value="'+k+'">'+v1+'</option>';
       $("#mediaType").append(str2);
  });
	$("#mediaInput").bind('keyup',function(){
		$("#message").html("");
		var MediaName=$.trim($("#mediaInput").val());
		if(MediaName==""){
		}else{
		   showMedia();
	   }
	});	
	 MediaMapper();
	 addMedias();
});
function addMedias(){
	$("#addMedias").click(function(){
		var mediaName=$("#mediaName").val();
		var mediaType=$("#mediaType").val();
		//获得当前style：display的内容
		// var getDisplay = document.getElementById('mediaType').style.display;
		if(mediaName == ""){
			$("#message").html("请填写新媒体名称");
			return false;
		}
		if(mediaType == "0"){
			$("#message").html("请选择新媒体类型");
			return false;
		}
		$.post("/amp/media/addMedia.do",{mediaName:mediaName,mediaType:mediaType},function(data){
			if(data == "1"){
				 $("#mediaName").val("");
				 $("#mediaType option:first").prop("selected", 'selected'); 
				$("#message").html("新增标准媒体成功。（标准媒体："+mediaName+"）");
			}else if("2"){
				$("#message").html("该媒体已经存在。（标准媒体："+mediaName+"）");
			}else{
				$("#message").html(data);
			}
		});
	});
	
}

var arr=[];
function showMedia(){
	
	$.ajaxSetup({
		async : false
	});
	//媒体名
	$.post("/amp/media/list.do",{},function(data){
		arr.length = 0;//将数组置空
		for(var i=0;i<data.length;i++){
			if (data[i].parentMedia == null){
				arr.push(data[i]['mediaName']);
			}
		}
	});//媒体名
	
	 $("#mediaInput").autocomplete(arr, {   
	     width: $('#mediaInput').width(),
		 minChars: 0,//minChars表示在自动完成激活之前填入的最小字符，这里我们设置为0，在我们双击文本框，不输入字符的时候，就会把数据显示出来 
		 max:10, //下拉框最多显示数
		 autoFill: false,//autoFill表示自动填充
		 highlight: false,   
		 multiple: false, //是否输入一个字符就查询一次
		 multipleSeparator: "",   
		 scroll: true,   
		 scrollHeight: 600,  
		 matchCase:true, 
		 position:{my:"rigth top", at:"right bottom"},
		 matchContains:true//决定比较时是否与下拉框中的字符串内部查看匹配，如ba是否与foo bar中的ba匹配，测试过程中发现如果设置大小写不敏感，那么当再次和下拉框中匹配的时候会忽略matchcase属性  
	 });
}
var flag=true;
function checkMedia(mediaInputVal){
	$.ajaxSetup({
		async : false
	});
	$.post("/amp/media/checkMedia.do",{mediaInputVal:mediaInputVal},function(data){
	 if(data==0){
		flag=false;
	 }else{
		 flag=true;
	 }
	
	});
	 return flag;
}
function MediaMapper(){
	var newMediaName;                                 
	var mediaInputVal;
	//映射媒体名称
	$("#mapMedia").click(function(){
		
		$("#message").html("");
		newMediaName = $("#newMediaName").val().replace(" ","");
		mediaInputVal = $("#mediaInput").val();
		
		//判读新的媒体是否存在于数据库里
		var flag=checkMedia(mediaInputVal);
		if($.inArray(mediaInputVal, arr)>0||flag==true){
			//$("#mediaType").css("display","none");
		}else{
			//加载的时候默认选择第一个
			 $("#newMediaName").val("");
			 $("#mediaInput").val("");
			$("#message").html("标准媒体不存在，请联系部门专人新增标准媒体");
			return false;
			  
			//$("#mediaType").css("display","inline");
		}
		
		if(newMediaName == ""){
			$("#message").html("请填写新媒体名称");
			return false;
		}
		
		$.post("/amp/media/mapMedia.do",{newMediaName:newMediaName,mediaInputVal:mediaInputVal},function(data){
			mediaId = "";
			if(data == "1"){
				 $("#newMediaName").val("");
				 $("#mediaInput").val("");
				$("#message").html("排期媒体"+newMediaName+"泛化成功,请继续插码。（标准媒体："+mediaInputVal+"）");
			}else if("401"){
				location = "/amp/page/login/login.html"; 
			}else{
				$("#message").html(data);
			}
		});
	});//end 映射媒体名称	
	
	
}
