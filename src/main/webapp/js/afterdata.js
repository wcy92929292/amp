function send() {
	var startDate = $("#startDate").val();
	var endDate = $("#endDate1").val();
	var txt = $.trim($("textarea").val());
			console.log(startDate + "   " + endDate);
	
	if (startDate != "" && endDate != "" && txt != "") {
		$.post('/amp/after1/list.do', {
			'startDate' : startDate,
			'endDate' : endDate,
			'mic' : txt
		},function(data){
			window.location.href =' /amp/weekExport/getExcelPath.do?filePath='+encodeURI(encodeURI(data));
		});
	}else{
		$('#hd_mes').text('页面上有空值！')
	}

};

