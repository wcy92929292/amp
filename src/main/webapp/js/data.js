   //活动状态
   	var actStateArr = new Array();
	    actStateArr[0] = "待审核";
	    actStateArr[1] = "具备上线条件";
	    actStateArr[2] = "不具备上线条件";
	    actStateArr[3] = "活动排期";
	    actStateArr[4] = "确认排期";
	    actStateArr[5] = "通知上线";
	    actStateArr[6] = "已上线";
	    actStateArr[7] = "已结束";
	    actStateArr[8] = "准备结案基础数据";
       
       //上线类型
       var goLiveTypeArr = new Array();
       goLiveTypeArr[0] = "正常上线";
       goLiveTypeArr[1] = "强制上线";
       goLiveTypeArr[2] = "--";
       
       //更改活动状态
       var updateStates = new Array();
       updateStates[0]="正常上线";
       updateStates[1]="强制上线";
       updateStates[2]="确认排期";
       updateStates[3]="通知上线";
       updateStates[4]="已上线";
       updateStates[5]="已结束";
       updateStates[6]="准备结案基础数据";
       
       //角色名称
       var roles = new Array();
       roles[0]="接口人";
       roles[1]="监测中心";
       roles[2]="后端支撑";
       roles[3]="前端支撑";
       
     /*  3;"接口人"
       5;"管理员"
       6;"客户"
       1;"后端支撑人员"
       2;"监测中心人员"
       4;"前端支撑人员"
       7;"超级管理员"
       8;"接口人+监测中心"
       9;"接口人+后端支撑"
       10;"监测中心+后端支撑"
       11;"接口人+监测中心+后端支撑"*/

       //涉及指标
       var involves = new Array();
       involves[1]="有效行为转换率";
       involves[2]="业务成功转换率";
       
       //统计类别
       var buttonTypes = new Array();
       buttonTypes[11]="终端";
       buttonTypes[12]="业务";
       buttonTypes[13]="充值";
       buttonTypes[14]="推广";
       buttonTypes[15]="配件";
       buttonTypes[16]="终端和配件";
       buttonTypes[21]="买手机";
       buttonTypes[22]="办业务";
       buttonTypes[23]="办套餐";
       buttonTypes[24]="挑配件";
       
       //修改URL状态
       var updateURLState = new Array();
       updateURLState[1]="待审核";
       updateURLState[2]="审核通过";
       updateURLState[3]="审核不通过";
       updateURLState[4]="已生效";
