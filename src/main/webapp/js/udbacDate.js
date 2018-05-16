var _year = 2016;
var _month = 11;
var _date = 23;
//一天的毫秒数
var _oneDateTime = 86400000; //1000 * 60 * 60 *24;

//中国日期格式
var chinaDate = {
	monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	monthNamesShort: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
	dayNames: ["一", "二", "三", "四", "五", "六","日"],
	dayNamesShort: ["一", "二", "三", "四", "五", "六","日"],
};

//英文的日期格式
var englishDate = {
	monthNames: ['January','February','March','April','May','June','July','August','September','October','November','December'], 
	monthNamesShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'], 
	dayNames: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday','Sunday'], 
	dayNamesShort: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat','Sun'],
	dayNamesMin: ['Mo','Tu','We','Th','Fr','Sa','Su'],
};


$(function(){
	//var date = new Date(_year,_month,_day);
	var date = new Date();
//	alert(date.getFullYear());
//	alert(date.getMonth());
//	alert(date.getDate());
//	alert(getDayIndex(date));
	var currentYear;
	var currentMonth;
	
	$("#selectYear").change(function(){
		currentYear = this.value;
		console.log(currentYear);
	});
});

/**
 * 是否为闰年
 * @param year
 * @return
 */
window.isLeapYear = function(year){
	return (year%100==0) ? year%400==0 : year%4==0;
};// end //isLeapYear


/**
 *	获取某天在当月最大的日期
 *	1 3 5 7 8 10 12 == 31
 *	2 == leap ? 29 : 28
 *	4 6 9 11 == 30
 * @param date
 * @return 
 */
window.getMonthLastDate = function(date){
	if(!date instanceof Date){
		return;
	}
	var lastDay;
	switch (date.getMonth()) {	//date.getMonth 是从 0开始算的
		case 3:
		case 5:
		case 8:
		case 10:{
			lastDay = 30;
			break;
		}
		case 1:{
			lastDay = (isLeapYear(date.getYear()) ? 29 : 28);
		}
		default:
			lastDay = 31;
		break;
	}
	
	return lastDay;
};//end getMonthLastDate


/**
 * 获取从周一 下标 为 0 开始的星期
 */
window.getDayIndex = function(date){
	return	(date.getDay() + 6) % 7;
}; //end getDayIndex()


/**
 * 获取两天之间相差多少天。
 */
window.getBetweenDate = function(oneDate,anotherDate){
	oneDate.getDate() - anotherDate.getDate();
};

/**
 * 格式化日期
 * YYYY
 * MM
 * dd
 * HH
 * mm
 * ss
 */
window.formatDate = function(date,format){
	
};





