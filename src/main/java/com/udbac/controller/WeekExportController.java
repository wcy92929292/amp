package com.udbac.controller;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.DataExport;
import com.udbac.entity.WeekDataExport;
import com.udbac.model.UserBean;
import com.udbac.service.WeekReportService;
import com.udbac.util.FilePathManager;
import com.udbac.util.JSONUtil;
import com.udbac.util.WeekUtil;
import com.udbac.util.XlsxUtil;

/**
 * 报告导出 2016-05-20
 * @author han
 */

@SuppressWarnings({ "unused", "deprecation" })
@Controller
@RequestMapping("/weekExport")
public class WeekExportController {

	@Autowired
	WeekReportService weekService;

	@Autowired
	private FilePathManager filePathManager;
	
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

	private XSSFWorkbook workbook;

	/****
	 * 获取Session中用户角色 han 2016-06-07
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUserInfoSeesion.do", method = RequestMethod.POST)
	public @ResponseBody void getUserInfoSeesion(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBean user;
		Map<String, Object> map = new HashMap<>();
		try {
			user = (UserBean) request.getSession().getAttribute("user");
			// 取得user的userID
			map.put("userRoleId", user.getROLE_ID()); // 得到当前用户的角色ID和所对应的客户ID，与页面的客户ID匹配，对应不同的操作
			map.put("customerId", user.getCustomerId());
			map.put("customerName", user.getCustomerName());
			map.put("userName", user.getREAL_NAME());
			
			String msg = JSONUtil.beanToJson(map);
			
			response.setContentType("text/html;charset=UTF-8");
			// 设置不使用缓存
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(msg);

		} catch (Exception e) {
			response.getWriter().print("error");
		}
	}

	/**
	 * 周报导出
	 * 
	 * @param request
	 * @param response
	 * @param stratDate 周报起始时间
	 * @param actCode 活动编号
	 * @param endDate 周报结束时间
	 * @param cusName 客户名称
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/exportWeek.do")
	public void listWeekInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startDate", required = true) String stratDate,
			@RequestParam(value = "actCode", required = false) String actCode,
			@RequestParam(value = "cusName", required = true) String cusName,
			@RequestParam(value = "cusCode", required = true) String cusCode,
			@RequestParam(value = "mediaName", required = true) String mediaName,
			@RequestParam(value = "point", required = false) boolean point,
			@RequestParam(value = "tcl", required = false) String tcl,
			@RequestParam(value = "ctrs", required = false) String ctrs,
			@RequestParam(value = "ctre", required = false) String ctre,
			@RequestParam(value = "bwcls", required = false) String bwcls,
			@RequestParam(value = "bwcle", required = false) String bwcle,
			@RequestParam(value = "dwcls", required = false) String dwcls,
			@RequestParam(value = "dwcle", required = false) String dwcle
			){

		System.out.println("客户编号："+cusCode);
		// 取得session中的user对象
		HttpSession session = request.getSession();
		String imgPath = session.getServletContext().getRealPath("/") + "images/logo.png";

		Date dt = null;
		Date dt1 = null;
		Date dt2 = null;
		int rowNum = 0;
		// 日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (stratDate != null) {
			try {
				dt = sdf.parse(stratDate);
				// 根据开始时间算出自然周的结束日期
				Map<String, Object> cycle = RegoinExportController.getCycle(dt, true);
				dt1 = (Date) cycle.get("weekFirst");
				dt2 = (Date) cycle.get("weekLast");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("是否过滤百度"+mediaName);
		// 0是过滤掉百度媒体的，1是不过滤的
		if("0".equals(mediaName)){
			mediaName = "百度";
		}else{
			mediaName = null;
		}
		
		Map<String, Object> map = new HashMap<>();
		try {
			workbook = new XSSFWorkbook();

			System.out.println("自然周期： "+dt1 +"   "+dt2);
			List<WeekDataExport> basicsInfo = weekService.listInfo(dt1, dt2, actCode, cusCode,mediaName);
			List<WeekDataExport> info = weekService.listWeekInfo(dt1, dt2, actCode, cusCode,mediaName);
			List<WeekDataExport> sumInfo = weekService.listWeekSumInfo(dt1, dt2, actCode, cusCode,mediaName);
			System.out.println("basicsInfo  "+basicsInfo.size()+"条");
			System.out.println("info  "+info.size()+"条");
			System.out.println("sumInfo    "+sumInfo.size()+"条");
			
			Map<String, Object> mp = new HashMap<String, Object>();
			
			List list1 = new ArrayList();
			List listSum = new ArrayList();
			List listAddMergin = new ArrayList();
			List listAddMergin2 = new ArrayList();
			List listAddMergin3 = new ArrayList();
			List hb = new ArrayList(); //（过滤后）包含四个要合并的条件的项 + 每项行数
			List hb3 = new ArrayList(); //（过滤后）包含四个要合并的条件的项 + 每项行数
			String[] header = { "项目名称", "媒体名称","终端类型","投放形式" ,"广告位", "广告曝光监测", "", "", "", "", "", "", "", "", "活动网站监测", "", "", "",
					"","", "", "完成率情况", "", "URL", "备注" };
			String[] headers = { "", "", "","","", "营销识别码", "日期", "曝光预估", "曝光次数", "曝光人数", "点击预估", "点击次数", "点击人数", "点击率CTR",
					"访问次数", "访问人数","到站点击", "浏览量", "跳出次数", "跳出率", "平均访问时长(s)", "曝光完成率", "点击完成率", "", "" };
			list1.add(header);
			list1.add(headers);
			if(info.size() == 0 && sumInfo.size() == 0) { // 没查询到数据
				map.put("msg", "1");
				String msg = JSONUtil.beanToJson(map);
				response.getWriter().write(msg);
			}else{ //有数据的时候才创建workbook
				/**
				 * 汇总信息
				 */
				    String hzmc="";//汇总名称
				    String hzggw="";//汇总的广告位
				    String mic = "";
				    String zdlx="";
				    String tfxs="";
				    double bgyg=0;//曝光预估
				    double  bgcs=0;//曝光次数
				    double  bgrs=0;//曝光人数
				    double djyg=0;//点击预估
				    double djcs=0;//点击次数
				    double  djrs=0;////点击人数
				    double  fscs=0;//访问次数
				    double  fwrs=0;//访问人数
				    double clk = 0; //点击量
				    double lll=0;//浏览率
				    double 	 tccs=0;//跳出次数
				    double  pjfwsc=0;//平均访问时间
				    String  url="";//url
				    int fzcd=1;//分组长度
				    int fzggw=1;//分组短代码
				    double tfl=0;//投放量
				    boolean unit=false;//投放单位的标记位
				    int flge=0;//标记汇总与明细的差别
				    String tfrq="";//投放日期
				    String code = "";
				    String fzxh="";//分组序号
				    
				  //汇总的是项目名称
				    String _hzmc="";//汇总名称
				    String _hzggw = "";
				    double _bgyg=0;//曝光预估
				    double  _bgcs=0;//曝光次数
				    double  _bgrs=0;//曝光人数
				    double _djyg=0;//点击预估
				    double _djcs=0;//点击次数
				    double  _djrs=0;////点击人数
				    double  _fscs=0;//访问次数
				    double  _fwrs=0;//访问人数
				    double _clk = 0; //点击量
				    double _lll=0;//浏览率
				    double 	 _tccs=0;//跳出次数
				    double  _pjfwsc=0;//平均访问时间
				    double _tfl=0;//投放量
				    String _actiCode="";//活动编号
				    String _code;//客户编号

				    List sumDaylist = new ArrayList<>();
				    int sumDay = 0;
				    Integer sumDate = 0;
				    //start  主要查询出相同行   和每项的行数count
					int count = 1;
				    /***************************************基础数据**********************************************/
					for (int i = 0; i < basicsInfo.size(); i++) {  //只有基础信息 第一个sheet页
						WeekDataExport de = basicsInfo.get(i);
						listAddMergin.add(checkOut(de, 1,true));
					}
					
					for(int i =0;i<basicsInfo.size();i=i+count){
						int k =i;
						WeekDataExport de = (WeekDataExport) basicsInfo.get(i);
						count = 1;
						if(((WeekDataExport) basicsInfo.get(i)).getGroup_id()==0){
							String[] checkout2 = {de.getGroup_id()+"", count+""};
							hb.add(checkout2);
							continue;
						}else{
						for(int j=k;(j!=basicsInfo.size()-1) && ((WeekDataExport) basicsInfo.get(j)).getGroup_id()==((WeekDataExport) basicsInfo.get(j+1)).getGroup_id()&&((WeekDataExport) basicsInfo.get(j)).getPutDate().compareTo(((WeekDataExport) basicsInfo.get(j+1)).getPutDate())==0;j++){
							count++;
							}
						String[] checkout2 = {de.getGroup_id()+"", count+""};
						hb.add(checkout2);
						for(int z=1;z<count;z++){
							String[] checkout3 = {de.getGroup_id()+"", "1"};
							hb.add(checkout3);
						}
						}
					}
					
					mp.put("infoHeBing",hb);
					
					/***************************************分媒体数据**********************************************/
					if(info.size()==1){//查询数据==1条   第二个sheet页
				    	WeekDataExport de=info.get(0);
						 listAddMergin2.add(checkOut(de,1,true));
						 listAddMergin2.add(checkOut(de,2,true));
				    }else{//查询数据大于1条时
				    	for (int i = 0; i < info.size(); i++) {
						WeekDataExport de = info.get(i);
						
						listAddMergin2.add(checkOut(de,1,true));
						
						    if(i<info.size()-1){//1至n-1个元素
						    	 hzmc=de.getMediaName();
						    	 hzggw=de.getPointLocation();
						    	 zdlx = de.getTerminal_type();
						    	 tfxs = de.getPutFunction();
						    	 mic = de.getMic();
						    	 bgyg += de.getExposureAvg();
						    	 djyg+=de.getClickAvg();
//						    	 bgcs+=de.getImpPv();    分媒体sheet汇总时 前端数据不再累加
//						    	 bgrs+=de.getImpUv();
//			 		    		 djcs+=de.getClkPv();
//			 		    		 djrs+=de.getClkUv();
						    	 if(point){
						    		 bgcs += de.getImpPv();
							    	 bgrs += de.getImpUv();
							    	 djcs += de.getClkPv();
							    	 djrs += de.getClkUv();
						    	 }else{
						    		 bgcs = de.getSumImpPv();
							    	 bgrs = de.getSumImpUv();
							    	 djcs = de.getSumClkPv();
							    	 djrs = de.getSumClkUv();
						    	 }
						    	 
			 		    		 //后端数据表字段没有默认值
			 		    		 if(de.getVisit()==-1){
			 		    		 }else{
						    		fscs+=de.getVisit();
			 		    		 }
			 		    		 if(de.getVisitor()==-1){
			 		    		 }else{
						    		fwrs+=de.getVisitor(); 
			 		    		 }
			 		    		 if(de.getClick()==-1){
			 		    		 }else{
			 		    			clk += de.getClick(); 
			 		    		 }
					    		 if(de.getPageView()==-1){
					    		 }else{
					    			 lll+=de.getPageView(); 
					    		 }
					    		 if(de.getBounceTimes()==-1){
					    		 }else{
						    		 tccs+=de.getBounceTimes();
					    		 }
					    		 if(de.getViewTime()==-1){
					    		 }else{
					    			 pjfwsc+=de.getViewTime(); 
					    		 }
					    		 url=de.getUrlPc();
					    		 tfl = de.getPut_value();
						    	 tfrq=sdf.format(de.getPutDate());
						    	 fzxh = String.valueOf(de.getGroup_id());
						    	
						    	 if(point){//选择了按投放形式分组来的
						    		 if(de.getPutFunction().equals(info.get(i+1).getPutFunction())){ 
						    			 fzggw++;
						    	 }else{
					    			 WeekDataExport des=new WeekDataExport();
					    			 	des.setTerminal_type(zdlx);
					    			 	des.setPutFunction(tfxs);
							    		des.setMediaName(hzmc);
							    		des.setPointLocation(hzggw);
							    		des.setMic(mic);
							    		des.setExposureAvg(bgyg);
							    		des.setImpPv(bgcs);
							    		des.setImpUv(bgrs);
							    		des.setClickAvg(djyg);
							    		des.setClkPv(djcs);
							    		des.setClkUv(djrs);
							    		des.setVisit(fscs);
							    		des.setVisitor(fwrs);
							    		des.setClick(clk);
							    		des.setPageView(lll);
							    		des.setBounceTimes(tccs);
							    		des.setViewTime(pjfwsc/fzggw);
							    		des.setUrlPc(url);
							    		des.setPut_value(tfl);
								    	des.setPutDate(sdf.parse(tfrq));
								    	des.setGroup_id(Integer.valueOf(fzxh));
								    	
							    		//媒体名称相等
							    		listAddMergin2.add(checkOut(des,2,true));

								    	fzcd=1;	
								    	hzmc="";
								    	tfxs="";
								    	zdlx="";
							    		 bgyg=0;
							    		 bgcs=0;
							    		 bgrs=0;
							    		 djyg=0;
							    		 djcs=0;
							    		 djrs=0;
							    		 fscs=0;
							    		 fwrs=0;
							    		 clk = 0;
							    		 lll=0;
							    		 tccs=0;
							    		 pjfwsc=0;
							    		 url="";
							    		 tfl=0;
							    		 tfrq="";
							    		 hzggw="";
							    		 mic = "";
							    		 fzggw=1;
							    		 fzxh ="";
						    		 }
								}else{//默认的分组形式
									if(de.getMic().equals(info.get(i+1).getMic())){ 
						    			 fzggw++;
						    	 }else{
					    			 WeekDataExport des=new WeekDataExport();
					    			 	des.setTerminal_type(zdlx);
					    			 	des.setPutFunction(tfxs);
							    		des.setMediaName(hzmc);
							    		des.setPointLocation(hzggw);
							    		des.setMic(mic);
							    		des.setExposureAvg(bgyg);
							    		des.setImpPv(bgcs);
							    		des.setImpUv(bgrs);
							    		des.setClickAvg(djyg);
							    		des.setClkPv(djcs);
							    		des.setClkUv(djrs);
							    		des.setVisit(fscs);
							    		des.setVisitor(fwrs);
							    		des.setClick(clk);
							    		des.setPageView(lll);
							    		des.setBounceTimes(tccs);
							    		des.setViewTime(pjfwsc/fzggw);
							    		des.setUrlPc(url);
							    		des.setPut_value(tfl);
								    	des.setPutDate(sdf.parse(tfrq));
								    	des.setGroup_id(Integer.valueOf(fzxh));
								    	
							    		//媒体名称相等
							    		listAddMergin2.add(checkOut(des,2,true));

								    	fzcd=1;	
								    	hzmc="";
								    	tfxs="";
								    	zdlx="";
							    		 bgyg=0;
							    		 bgcs=0;
							    		 bgrs=0;
							    		 djyg=0;
							    		 djcs=0;
							    		 djrs=0;
							    		 fscs=0;
							    		 fwrs=0;
							    		 clk = 0;
							    		 lll=0;
							    		 tccs=0;
							    		 pjfwsc=0;
							    		 url="";
							    		 tfl=0;
							    		 tfrq="";
							    		 hzggw="";
							    		 mic = "";
							    		 fzggw=1;
							    		 fzxh ="";
						    		 }
								}
						    	 
						    	}else{//最后一条数据
						    		 hzmc=de.getMediaName();
							    	 hzggw=de.getPointLocation();
							    	 tfxs=de.getPutFunction();
							    	 zdlx=de.getTerminal_type();
							    	 mic = de.getMic();
							    	 bgyg += de.getExposureAvg();
							    	 djyg+=de.getClickAvg();

//							    	 bgcs+=de.getImpPv();  分媒体sheet汇总时 前端数据不再累加
//							    	 bgrs+=de.getImpUv();
//				 		    		 djcs+=de.getClkPv();
//				 		    		 djrs+=de.getClkUv();
				 		    		 if(point){
				 		    			bgcs += de.getImpPv();
								    	 bgrs += de.getImpUv();
								    	 djcs += de.getClkPv();
								    	 djrs += de.getClkUv();
				 		    		 }else{
				 		    			bgcs = de.getSumImpPv();
								    	 bgrs = de.getSumImpUv();
								    	 djcs = de.getSumClkPv();
								    	 djrs = de.getSumClkUv();
				 		    		 }
						    		 fscs+=de.getVisit();
						    		 fwrs+=de.getVisitor();
						    		 clk += de.getClick();
						    		 lll+=de.getPageView();
						    		 tccs+=de.getBounceTimes();
						    		 pjfwsc+=de.getViewTime();
						    		 tfl=de.getPut_value();
						    		 url=de.getUrlPc();
							    	 tfrq=sdf.format(de.getPutDate());
							    	 fzxh = String.valueOf(de.getGroup_id());

							    	 WeekDataExport des=new WeekDataExport();
							    	 des.setPointLocation(hzggw);
							    	 des.setMediaName(hzmc);
							    	 des.setPutFunction(tfxs);
							    	 des.setTerminal_type(zdlx);
							    	 des.setMic(mic);
						    		 des.setExposureAvg(bgyg);
						    		 des.setImpPv(bgcs);
						    		 des.setImpUv(bgrs);
						    		 des.setClickAvg(djyg);
						    		 des.setClkPv(djcs);
						    		 des.setClkUv(djrs);
						    		 des.setVisit(fscs);
						    		 des.setVisitor(fwrs);
						    		 des.setClick(clk);
						    		 des.setPageView(lll);
						    		 des.setBounceTimes(tccs);
						    		 des.setViewTime(pjfwsc/fzggw);
						    		 des.setUrlPc(url);
						    		 des.setPut_value(tfl);
							    	 des.setPutDate(sdf.parse(tfrq));
							    	 des.setGroup_id(Integer.valueOf(fzxh));
						    	
							    	listAddMergin2.add(checkOut(des,2,true));
							    	
							    	fzcd=1;	
							    	hzmc="";
							    	zdlx="";
							    	tfxs="";
						    		 bgyg=0;
						    		 bgcs=0;
						    		 bgrs=0;
						    		 djyg=0;
						    		 djcs=0;
						    		 djrs=0;
						    		 fscs=0;
						    		 fwrs=0;
						    		 clk = 0;
						    		 lll=0;
						    		 tccs=0;
						    		 pjfwsc=0;
						    		 url="";
						    		 tfl=0;
						    		 tfrq="";
						    		 hzggw="";
						    		 mic = "";
						    		 fzggw=1;
						    		 fzxh ="";
						    }
						}
				    }
				//}  
			/**************************************************汇总数据************************************************************/	
					
				    if(sumInfo.size()==1){//查询数据==1条
				    	WeekDataExport de=sumInfo.get(0);
			    		 listAddMergin3.add(checkOut(de,3,false));//明细数据
			    		 listAddMergin3.add(checkOut(de,2,false));//分媒体数据
						 listAddMergin3.add(checkOut(de,5,false));//分项目汇总数据
				    }
				    else{//查询数据大于1条时
				    	Integer object =0;
				    	for (int i = 0; i < sumInfo.size(); i++) {
				    		
						WeekDataExport de = sumInfo.get(i);
						
						/**
						 * 第三个sheet基础数据填充
						 */
						if(i>0){
						if(sumInfo.get(i).getActivityName().equals(sumInfo.get(i-1).getActivityName())){	//上一个对象和当前对象 项目名称相同
//				    		 fzcd++;
			    		 if(sumInfo.get(i).getMediaName().equals(sumInfo.get(i-1).getMediaName())){  //媒体名称相同
//			    			 fzggw++;
			    			 if(sumInfo.get(i).getGroup_id().equals(sumInfo.get(i-1).getGroup_id())&& sumInfo.get(i).getGroup_id()!=0){
			    				 de.setExposureAvg(0.0);
			    				 de.setClickAvg(0.0);
			    			 }
			    		 }
						}
						}else{
							
						}
					    listAddMergin3.add(checkOut(de,3,true));
					    
					    if(i<sumInfo.size()-1){ //1至n-1个元素
					    	 //区分集团客户
						    if("JT".equals(sumInfo.get(i).getCustomerCode())){ //是集团客户、全周期频控的
						    	hzmc=sumInfo.get(i).getMediaName();
						    	 hzggw=sumInfo.get(i).getActivityName();
						    	 tfxs=sumInfo.get(i).getPutFunction();
						    	 zdlx =  sumInfo.get(i).getTerminal_type();
						    	 mic = sumInfo.get(i).getMic();
						    	 code = sumInfo.get(i).getCustomerCode();
						    	 if(sumInfo.get(i).getSumImpPv() == null ){
						    	 }else{
						    		 bgcs+= sumInfo.get(i).getSumImpPv();
						    	 }
						    	 if(sumInfo.get(i).getSumImpUv() == null ){
						    	 }else{
						    		 bgrs+=sumInfo.get(i).getSumImpUv();
						    	 }
					    		 if(sumInfo.get(i).getExposureAvg() == null || sumInfo.get(i).getExposureAvg() < 0 ){
						    	 }else{
						    		 bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
					    		 if(sumInfo.get(i).getClickAvg() == null || sumInfo.get(i).getClickAvg() < 0){
					    		 }else{
					    			 djyg+=sumInfo.get(i).getClickAvg();
					    		 }
					    		 if(sumInfo.get(i).getSumClkPv() == null){
					    		 }else{
					    			 djcs+=sumInfo.get(i).getSumClkPv();
					    		 }
					    		 if(sumInfo.get(i).getSumClkUv() == null){
					    		 }else{
					    			 djrs+=sumInfo.get(i).getSumClkUv();
					    		 }
					    		 if(sumInfo.get(i).getSumVisit() == null){
					    		 }else{
					    			 fscs+=sumInfo.get(i).getSumVisit();
					    		 }
					    		 if(sumInfo.get(i).getSumVisitor() == null){
					    		 }else{
					    			 fwrs+=sumInfo.get(i).getSumVisitor();
					    		 }
					    		 if(sumInfo.get(i).getSumClick()== null){
					    		 }else{
					    			 clk+=sumInfo.get(i).getSumClick();
					    		 }
					    		 if(sumInfo.get(i).getSumPageView() == null){
					    		 }else{
					    			 lll+=sumInfo.get(i).getSumPageView();
					    		 }
					    		 if(sumInfo.get(i).getSumBounceTimes() == null){
					    		 }else{
					    			 tccs+=sumInfo.get(i).getSumBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getSumViewTime() == null){
					    		 }else{
					    			 pjfwsc+=sumInfo.get(i).getSumViewTime();
					    		 }
					    		 url=sumInfo.get(i).getUrlPc();
						    	 tfl+=sumInfo.get(i).getPut_value();
						    	 
						    	 
						    	 _actiCode=sumInfo.get(i).getActivityCode();
						    	 _code = sumInfo.get(i).getCustomerCode();
				    			 _bgcs+= sumInfo.get(i).getSumImpPv();
					    		 _bgrs+= sumInfo.get(i).getSumImpUv();
					    		 if(sumInfo.get(i).getExposureAvg() == null || sumInfo.get(i).getExposureAvg() < 0 ){
						    	 }else{
						    		 _bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
					    		 if(sumInfo.get(i).getClickAvg() == null || sumInfo.get(i).getClickAvg() < 0){
					    		 }else{
					    			 _djyg+=sumInfo.get(i).getClickAvg();
					    		 }
					    		 _djcs+=sumInfo.get(i).getSumClkPv();
			    				 _djrs+=sumInfo.get(i).getSumClkUv();
			    				 if(sumInfo.get(i).getSumVisit() == null){
					    		 }else{
					    			 _fscs+=sumInfo.get(i).getSumVisit();
					    		 }
					    		 if(sumInfo.get(i).getSumVisitor() == null){
					    		 }else{
					    			 _fwrs+=sumInfo.get(i).getSumVisitor();
					    		 }
					    		 if(sumInfo.get(i).getSumClick()== null){
					    		 }else{
					    			 _clk+=sumInfo.get(i).getSumClick();
					    		 }
					    		 if(sumInfo.get(i).getSumPageView() == null){
					    		 }else{
					    			 _lll+=sumInfo.get(i).getSumPageView();
					    		 }
					    		 if(sumInfo.get(i).getSumBounceTimes() == null){
					    		 }else{
					    			 _tccs+=sumInfo.get(i).getSumBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getSumViewTime() == null){
					    		 }else{
					    			 _pjfwsc+=sumInfo.get(i).getSumViewTime();
					    		 }
					    		 _tfl+=sumInfo.get(i).getPut_value();
				    		if(sumInfo.get(i).getActivityName().equals(sumInfo.get(i+1).getActivityName())){  //上一个对象和当前对象 项目名称相同
						    		 fzcd++;
					    		 if(sumInfo.get(i).getMediaName().equals(sumInfo.get(i+1).getMediaName())){  //媒体名称相同
					    			 fzggw++;
						    		 }else{
						    			 WeekDataExport des=new WeekDataExport();
								    		des.setMediaName(hzmc);
								    		des.setActivityName(hzggw);
								    		des.setTerminal_type(zdlx);
								    		des.setPutFunction(tfxs);
								    		des.setCustomerCode(code);
								    		des.setMic(mic);
								    		des.setExposureAvg(bgyg);
								    		des.setClickAvg(djyg);
								    		des.setSumImpPv(bgcs);
								    		des.setSumImpUv(bgrs);
								    		des.setSumClkPv(djcs);
								    		des.setSumClkUv(djrs);
								    		des.setSumVisit(fscs);
								    		des.setSumVisitor(fwrs);
								    		des.setSumClick(clk);
								    		des.setSumPageView(lll);
								    		des.setSumBounceTimes(tccs);
								    		des.setSumViewTime(pjfwsc/fzcd);
								    		des.setUrlPc(url);
								    		des.setPut_value(tfl);
									    	
									    	listAddMergin3.add(checkOut(des,2,false));
									    		
//									    	fzcd=1;	
									    	hzmc="";
									    	tfxs="";
									    	zdlx="";
									    	code="";
								    		 bgyg=0;
								    		 bgcs=0;
								    		 bgrs=0;
								    		 djyg=0;
								    		 djcs=0;
								    		 djrs=0;
								    		 fscs=0;
								    		 fwrs=0;
								    		 clk=0;
								    		 lll=0;
								    		 tccs=0;
								    		 pjfwsc=0;
								    		 url="";
								    		 tfl=0;
								    		 tfrq="";
								    		 hzggw="";
								    		 mic = "";
								    		 fzggw=1;
						    		 }
						    	}else{
						    		
						    		 WeekDataExport des=new WeekDataExport();
							    		des.setMediaName(hzmc);
							    		des.setActivityName(hzggw);
							    		des.setPutFunction(tfxs);
							    		des.setTerminal_type(zdlx);
							    		des.setCustomerCode(code);
							    		des.setMic(mic);
							    		des.setExposureAvg(bgyg);
							    		des.setSumImpPv(bgcs);
							    		des.setSumImpUv(bgrs);
							    		des.setClickAvg(djyg);
							    		des.setSumClkPv(djcs);
							    		des.setSumClkUv(djrs);
							    		des.setSumVisit(fscs);
							    		des.setSumVisitor(fwrs);
							    		des.setSumClick(clk);
							    		des.setSumPageView(lll);
							    		des.setSumBounceTimes(tccs);
							    		des.setSumViewTime(pjfwsc/fzcd);
							    		des.setUrlPc(url);
							    		des.setPut_value(tfl);
								    	
								    	listAddMergin3.add(checkOut(des,2,false));
								    	
								    	WeekDataExport desum=new WeekDataExport();
								    	 desum.setMediaName(hzmc);
								    	 desum.setActivityName(hzggw);
								    	 desum.setMic(mic);
								    	 desum.setCustomerCode(_code);
								    	 desum.setActivityCode(_actiCode);
								    	 desum.setExposureAvg(_bgyg);
								    	 desum.setSumImpPv(_bgcs);
								    	 desum.setSumImpUv(_bgrs);
								    	 desum.setClickAvg(_djyg);
								    	 desum.setSumClkPv(_djcs);
								    	 desum.setSumClkUv(_djrs);
								    	 desum.setSumVisit(_fscs);
								    	 desum.setSumVisitor(_fwrs);
								    	 desum.setSumClick(_clk);
								    	 desum.setSumPageView(_lll);
								    	 desum.setSumBounceTimes(_tccs);
								    	 desum.setSumViewTime(_pjfwsc/fzcd);
								    	 desum.setUrlPc(url);
								    	 desum.setPut_value(_tfl);
								    	 
									    listAddMergin3.add(checkOut(desum,5,false));// 分完媒体再分项目汇总

								    	fzcd=1;	
								    	 hzmc="";
								    	 code="";
								    	 zdlx="";
								    	 tfxs="";
							    		 bgyg=0;
							    		 bgcs=0;
							    		 bgrs=0;
							    		 djyg=0;
							    		 djcs=0;
							    		 djrs=0;
							    		 fscs=0;
							    		 fwrs=0;
							    		 clk=0;
							    		 lll=0;
							    		 tccs=0;
							    		 pjfwsc=0;
							    		 url="";
							    		 tfl=0;
							    		 tfrq="";
							    		 hzggw="";
							    		 mic = "";
							    		 fzggw=1;
							    		 
							    		 _actiCode="";
							    		 _code="";
							    		 _bgyg=0;
							    		 _bgcs=0;
							    		 _bgrs=0;
							    		 _djyg=0;
							    		 _djcs=0;
							    		 _djrs=0;
							    		 _fscs=0;
							    		 _fwrs=0;
							    		 _clk =0;
							    		 _lll=0;
							    		 _tccs=0;
							    		 _pjfwsc=0;
							    		 _tfl=0;
							    		 hzggw="";
								    } //集团的end
						    }else{ /******************正常取数的开始**********************/
						    	
						    	 hzmc=sumInfo.get(i).getMediaName();
						    	 hzggw=sumInfo.get(i).getActivityName();
						    	 tfxs=sumInfo.get(i).getPutFunction();
						    	 zdlx=sumInfo.get(i).getTerminal_type();
						    	 code = sumInfo.get(i).getCustomerCode();
						    	 mic = sumInfo.get(i).getMic();
				    			 bgcs+= sumInfo.get(i).getImpPv();
					    		 bgrs+= sumInfo.get(i).getImpUv();
					    		 djcs+=sumInfo.get(i).getClkPv();
			    				 djrs+=sumInfo.get(i).getClkUv();
						    	 if(sumInfo.get(i).getExposureAvg() == null || sumInfo.get(i).getExposureAvg() < 0){
						    	 }else{
						    		 bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
						    	 if(sumInfo.get(i).getClickAvg() == null || sumInfo.get(i).getClickAvg() < 0){
						    	 }else{
						    		 djyg+=sumInfo.get(i).getClickAvg();
						    	 }
					    		 if(sumInfo.get(i).getVisit() == null){
					    		 }else{
					    			 fscs+=sumInfo.get(i).getVisit();
					    		 }
					    		 if(sumInfo.get(i).getVisitor() == null){
					    		 }else{
					    			 fwrs+=sumInfo.get(i).getVisitor();
					    		 }
					    		 if(sumInfo.get(i).getClick()== null){
					    		 }else{
					    			 clk+=sumInfo.get(i).getClick();
					    		 }
					    		 if(sumInfo.get(i).getPageView() == null){
					    		 }else{
					    			 lll+=sumInfo.get(i).getPageView();
					    		 }
					    		 if(sumInfo.get(i).getBounceTimes() == null){
					    		 }else{
					    			 tccs+=sumInfo.get(i).getBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getViewTime() == null){
					    		 }else{
					    			 pjfwsc+=sumInfo.get(i).getViewTime();
					    		 }
					    		 if(sumInfo.get(i).getPut_value() == null){
					    		 }else{
					    			 tfl+=sumInfo.get(i).getPut_value();
					    		 }
					    		 url=sumInfo.get(i).getUrlPc();
					    		 fzxh = String.valueOf(sumInfo.get(i).getGroup_id());
					    		 
					    		 _actiCode=sumInfo.get(i).getActivityCode();
						    	 _code = sumInfo.get(i).getCustomerCode();
				    			 _bgcs+= sumInfo.get(i).getImpPv();
					    		 _bgrs+= sumInfo.get(i).getImpUv();
					    		 if(sumInfo.get(i).getExposureAvg() == null || sumInfo.get(i).getExposureAvg() < 0 ){
						    	 }else{
						    		 _bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
					    		 if(sumInfo.get(i).getClickAvg() == null || sumInfo.get(i).getClickAvg() < 0){
					    		 }else{
					    			 _djyg+=sumInfo.get(i).getClickAvg();
					    		 }
					    		 _djcs+=sumInfo.get(i).getClkPv();
			    				 _djrs+=sumInfo.get(i).getClkUv();
			    				 if(sumInfo.get(i).getVisit() == null){
					    		 }else{
					    			 _fscs+=sumInfo.get(i).getVisit();
					    		 }
					    		 if(sumInfo.get(i).getVisitor() == null){
					    		 }else{
					    			 _fwrs+=sumInfo.get(i).getVisitor();
					    		 }
					    		 if(sumInfo.get(i).getClick()== null){
					    		 }else{
					    			 _clk+=sumInfo.get(i).getClick();
					    		 }
					    		 if(sumInfo.get(i).getPageView() == null){
					    		 }else{
					    			 _lll+=sumInfo.get(i).getPageView();
					    		 }
					    		 if(sumInfo.get(i).getBounceTimes() == null){
					    		 }else{
					    			 _tccs+=sumInfo.get(i).getBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getViewTime() == null){
					    		 }else{
					    			 _pjfwsc+=sumInfo.get(i).getViewTime();
					    		 }
					    		 _tfl+=sumInfo.get(i).getPut_value();
					    		 
					    	WeekDataExport des=new WeekDataExport();
				    		if(sumInfo.get(i).getActivityName().equals(sumInfo.get(i+1).getActivityName())){	//上一个对象和当前对象 项目名称相同
						    		 fzcd++;
					    		 if(sumInfo.get(i).getMediaName().equals(sumInfo.get(i+1).getMediaName())){  //媒体名称相同
					    			 fzggw++;
//					    			 if(sumInfo.get(i).getPutFunction().equals(sumInfo.get(i+1).getPutFunction())){
//					    				 des.setExposureAvg(0.0);
//					    				 des.setClickAvg(0.0);
//					    			 }
						    		 }else{
						    			 
								    		des.setMediaName(hzmc);
								    		des.setActivityName(hzggw);
								    		des.setTerminal_type(zdlx);
								    		des.setPutFunction(tfxs);
								    		des.setCustomerCode(code);
								    		des.setMic(mic);
								    		des.setExposureAvg(bgyg);
								    		des.setImpPv(bgcs);
								    		des.setImpUv(bgrs);
								    		des.setClickAvg(djyg);
								    		des.setClkPv(djcs);
								    		des.setClkUv(djrs);
								    		des.setVisit(fscs);
								    		des.setVisitor(fwrs);
								    		des.setClick(clk);
								    		des.setPageView(lll);
								    		des.setBounceTimes(tccs);
								    		des.setViewTime(pjfwsc/fzggw);
								    		des.setUrlPc(url);
								    		des.setPut_value(tfl);
								    		des.setGroup_id(Integer.valueOf(fzxh));
								    		
									    	listAddMergin3.add(checkOut(des,2,false));
									    	
//										    	 fzcd=1;	
										    	 hzmc="";
										    	 code="";
										    	 tfxs="";
										    	 zdlx="";
									    		 bgyg=0;
									    		 bgcs=0;
									    		 bgrs=0;
									    		 djyg=0;
									    		 djcs=0;
									    		 djrs=0;
									    		 fscs=0;
									    		 fwrs=0;
									    		 clk=0;
									    		 lll=0;
									    		 tccs=0;
									    		 pjfwsc=0;
									    		 url="";
									    		 tfl=0;
									    		 tfrq="";
									    		 hzggw="";
									    		 mic = "";
									    		 fzggw=1;
									    		 fzxh ="";
									    		 
						    		 }
						    	}else{
							    		des.setMediaName(hzmc);
							    		des.setPutFunction(tfxs);
							    		des.setTerminal_type(zdlx);
							    		des.setActivityName(hzggw);
							    		des.setCustomerCode(code);
							    		des.setMic(mic);
							    		des.setExposureAvg(bgyg);
							    		des.setImpPv(bgcs);
							    		des.setImpUv(bgrs);
							    		des.setClickAvg(djyg);
							    		des.setClkPv(djcs);
							    		des.setClkUv(djrs);
							    		des.setVisit(fscs);
							    		des.setVisitor(fwrs);
							    		des.setClick(clk);
							    		des.setPageView(lll);
							    		des.setBounceTimes(tccs);
							    		des.setViewTime(pjfwsc/fzggw);
							    		des.setUrlPc(url);
							    		des.setPut_value(tfl);
							    		des.setGroup_id(Integer.valueOf(fzxh));
							    		
								    	listAddMergin3.add(checkOut(des,2,false));
							    		
								    	WeekDataExport desum=new WeekDataExport();
								    	 desum.setMediaName(hzmc);
								    	 desum.setActivityName(hzggw);
								    	 desum.setMic(mic);
								    	 desum.setCustomerCode(_code);
								    	 desum.setActivityCode(_actiCode);
								    	 desum.setExposureAvg(_bgyg);
								    	 desum.setImpPv(_bgcs);
								    	 desum.setImpUv(_bgrs);
								    	 desum.setClickAvg(_djyg);
								    	 desum.setClkPv(_djcs);
								    	 desum.setClkUv(_djrs);
								    	 desum.setVisit(_fscs);
								    	 desum.setVisitor(_fwrs);
								    	 desum.setClick(_clk);
								    	 desum.setPageView(_lll);
								    	 desum.setBounceTimes(_tccs);
								    	 desum.setViewTime(_pjfwsc/fzcd);
								    	 desum.setUrlPc(url);
								    	 desum.setPut_value(_tfl);
								    	 desum.setGroup_id(Integer.valueOf(fzxh));
							    		listAddMergin3.add(checkOut(desum,5,false));// 分完媒体再分项目汇总
								    	
								    	 fzcd=1;	
								    	 hzmc="";
								    	 tfxs="";
								    	 zdlx="";
								    	 code="";
							    		 bgyg=0;
							    		 bgcs=0;
							    		 bgrs=0;
							    		 djyg=0;
							    		 djcs=0;
							    		 djrs=0;
							    		 fscs=0;
							    		 fwrs=0;
							    		 clk=0;
							    		 lll=0;
							    		 tccs=0;
							    		 pjfwsc=0;
							    		 url="";
							    		 tfl=0;
							    		 tfrq="";
							    		 hzggw="";
							    		 mic = "";
							    		 fzggw=1;
							    		 fzxh="";
							    		 
							    		 _actiCode="";
							    		 _code="";
							    		 _bgyg=0;
							    		 _bgcs=0;
							    		 _bgrs=0;
							    		 _djyg=0;
							    		 _djcs=0;
							    		 _djrs=0;
							    		 _fscs=0;
							    		 _fwrs=0;
							    		 _clk =0;
							    		 _lll=0;
							    		 _tccs=0;
							    		 _pjfwsc=0;
							    		 _tfl=0;
							    		 hzggw="";
								    	}
								    }//普通的END
					    	 }
					    else{ //最后的一个比较开始
					    		 if("JT".equals(sumInfo.get(i).getCustomerCode())){ //是集团客户、全周期频控的
					    		 hzmc=sumInfo.get(i).getMediaName();
						    	 hzggw=sumInfo.get(i).getActivityName();
						    	 tfxs=sumInfo.get(i).getPutFunction();
						    	 zdlx=sumInfo.get(i).getTerminal_type();
						    	 code = sumInfo.get(i).getCustomerCode();
						    	 mic = sumInfo.get(i).getMic();
						    	 if(sumInfo.get(i).getSumImpPv() == null ){
						    	 }else{
						    		 bgcs+= sumInfo.get(i).getSumImpPv();
						    	 }
						    	 if(sumInfo.get(i).getSumImpUv() == null ){
						    	 }else{
						    		 bgrs+= sumInfo.get(i).getSumImpUv();
						    	 }
						    	 if(sumInfo.get(i).getExposureAvg() == null || sumInfo.get(i).getExposureAvg() < 0){
						    	 }else{
						    		 bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
					    		 if(sumInfo.get(i).getClickAvg() == null || sumInfo.get(i).getClickAvg()< 0){
					    		 }else{
					    			 djyg+=sumInfo.get(i).getClickAvg();
					    		 }
					    		 if(sumInfo.get(i).getSumClkPv() == null){
					    		 }else{
					    			 djcs+=sumInfo.get(i).getSumClkPv();
					    		 }
					    		 if(sumInfo.get(i).getSumClkUv() == null){
					    		 }else{
					    			 djrs+=sumInfo.get(i).getSumClkUv();
					    		 }
					    		 if(sumInfo.get(i).getSumVisit() == null){
					    		 }else{
					    			 fscs+=sumInfo.get(i).getSumVisit();
					    		 }
					    		 if(sumInfo.get(i).getSumVisitor() == null){
					    		 }else{
					    			 fwrs+=sumInfo.get(i).getSumVisitor();
					    		 }
					    		 if(sumInfo.get(i).getSumClick()== null){
					    		 }else{
					    			 clk+=sumInfo.get(i).getSumClick();
					    		 }
					    		 if(sumInfo.get(i).getSumPageView() == null){
					    		 }else{
					    			 lll+=sumInfo.get(i).getSumPageView();
					    		 }
					    		 if(sumInfo.get(i).getSumBounceTimes() == null){
					    		 }else{
					    			 tccs+=sumInfo.get(i).getSumBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getSumViewTime() == null){
					    		 }else{
					    			 pjfwsc+=sumInfo.get(i).getSumViewTime();
					    		 }
					    		 url=sumInfo.get(i).getUrlPc();
						    	 tfl+=sumInfo.get(i).getPut_value();
						    	
						    	 WeekDataExport des=new WeekDataExport();
						    	 des.setActivityName(hzggw);
						    	 des.setMediaName(hzmc);
						    	 des.setCustomerCode(code);
						    	 des.setPutFunction(tfxs);
						    	 des.setTerminal_type(zdlx);
						    	 des.setCustomerCode(code);
						    	 des.setMic(mic);
					    		 des.setExposureAvg(bgyg);
					    		 des.setSumImpPv(bgcs);
					    		 des.setSumImpUv(bgrs);
					    		 des.setClickAvg(djyg);
					    		 des.setSumClkPv(djcs);
					    		 des.setSumClkUv(djrs);
					    		 des.setSumVisit(fscs);
					    		 des.setSumVisitor(fwrs);
					    		 des.setSumClick(clk);
					    		 des.setSumPageView(lll);
					    		 des.setSumBounceTimes(tccs);
					    		 des.setSumViewTime(pjfwsc/fzcd);
					    	 	 des.setUrlPc(url);
					    		 des.setPut_value(tfl);

					    		 listAddMergin3.add(checkOut(des,2,false));
					    		 
					    		 _actiCode=sumInfo.get(i).getActivityCode();
						    	 _code = sumInfo.get(i).getCustomerCode();
				    			 _bgcs+= sumInfo.get(i).getSumImpPv();
					    		 _bgrs+= sumInfo.get(i).getSumImpUv();
					    		 if(sumInfo.get(i).getExposureAvg() == null || sumInfo.get(i).getExposureAvg() < 0 ){
						    	 }else{
						    		 _bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
					    		 if(sumInfo.get(i).getClickAvg() == null || sumInfo.get(i).getClickAvg() < 0){
					    		 }else{
					    			 _djyg+=sumInfo.get(i).getClickAvg();
					    		 }
					    		 _djcs+=sumInfo.get(i).getSumClkPv();
			    				 _djrs+=sumInfo.get(i).getSumClkUv();
			    				 if(sumInfo.get(i).getSumVisit() == null){
					    		 }else{
					    			 _fscs+=sumInfo.get(i).getSumVisit();
					    		 }
					    		 if(sumInfo.get(i).getSumVisitor() == null){
					    		 }else{
					    			 _fwrs+=sumInfo.get(i).getSumVisitor();
					    		 }
					    		 if(sumInfo.get(i).getSumClick()== null){
					    		 }else{
					    			 _clk+=sumInfo.get(i).getSumClick();
					    		 }
					    		 if(sumInfo.get(i).getSumPageView() == null){
					    		 }else{
					    			 _lll+=sumInfo.get(i).getSumPageView();
					    		 }
					    		 if(sumInfo.get(i).getSumBounceTimes() == null){
					    		 }else{
					    			 _tccs+=sumInfo.get(i).getSumBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getSumViewTime() == null){
					    		 }else{
					    			 _pjfwsc+=sumInfo.get(i).getSumViewTime();
					    		 }
					    		 _tfl+=sumInfo.get(i).getPut_value();
					    		
					    		 WeekDataExport desum=new WeekDataExport();
						    	 desum.setMediaName(hzmc);
						    	 desum.setActivityName(hzggw);
						    	 desum.setCustomerCode(_code);
						    	 desum.setMic(mic);
						    	 desum.setActivityCode(_actiCode);
						    	 desum.setExposureAvg(_bgyg);
						    	 desum.setSumImpPv(_bgcs);
						    	 desum.setSumImpUv(_bgrs);
						    	 desum.setClickAvg(_djyg);
						    	 desum.setSumClkPv(_djcs);
						    	 desum.setSumClkUv(_djrs);
						    	 desum.setSumVisit(_fscs);
						    	 desum.setSumVisitor(_fwrs);
						    	 desum.setSumClick(_clk);
						    	 desum.setSumPageView(_lll);
						    	 desum.setSumBounceTimes(_tccs);
						    	 desum.setSumViewTime(_pjfwsc/fzcd);
						    	 desum.setUrlPc(url);
						    	 desum.setPut_value(_tfl);
					    		 listAddMergin3.add(checkOut(desum,5,false));// 分完项目再分媒体汇总
					    		fzcd=1;	
						    	hzmc="";
						    	tfxs="";
						    	zdlx="";
						    	code="";
					    		 bgyg=0;
					    		 bgcs=0;
					    		 bgrs=0;
					    		 djyg=0;
					    		 djcs=0;
					    		 djrs=0;
					    		 fscs=0;
					    		 fwrs=0;
					    		 clk=0;
					    		 lll=0;
					    		 tccs=0;
					    		 pjfwsc=0;
					    		 url="";
					    		 tfl=0;
					    		 tfrq="";
					    		 
					    		 _actiCode="";
					    		 _code="";
					    		 _bgyg=0;
					    		 _bgcs=0;
					    		 _bgrs=0;
					    		 _djyg=0;
					    		 _djcs=0;
					    		 _djrs=0;
					    		 _fscs=0;
					    		 _fwrs=0;
					    		 _clk =0;
					    		 _lll=0;
					    		 _tccs=0;
					    		 _pjfwsc=0;
					    		 _tfl=0;
					    		 hzggw="";
					    	}else{ 
					    		//普通的开始
					    		 hzmc=sumInfo.get(i).getMediaName();
						    	 hzggw=sumInfo.get(i).getActivityName();
						    	 zdlx=sumInfo.get(i).getTerminal_type();
						    	 tfxs=sumInfo.get(i).getPutFunction();
						    	 mic = sumInfo.get(i).getMic();
						    	 code =sumInfo.get(i).getCustomerCode();
				    			 bgcs+= sumInfo.get(i).getImpPv();
					    		 bgrs+= sumInfo.get(i).getImpUv();
					    		 djcs+=sumInfo.get(i).getClkPv();
			    				 djrs+=sumInfo.get(i).getClkUv();
						    	 if(sumInfo.get(i).getExposureAvg() < 0|| sumInfo.get(i).getExposureAvg() == null){
						    	 }else{
						    		 bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
						    	 if(sumInfo.get(i).getClickAvg() < 0 || sumInfo.get(i).getClickAvg()==null){
						    	 }else{
						    		 djyg+=sumInfo.get(i).getClickAvg();
						    	 }
					    		 if(sumInfo.get(i).getVisit() == null){
					    		 }else{
					    			 fscs+=sumInfo.get(i).getVisit();
					    		 }
					    		 if(sumInfo.get(i).getVisitor() == null){
					    		 }else{
					    			 fwrs+=sumInfo.get(i).getVisitor();
					    		 }
					    		 if(sumInfo.get(i).getClick()== null){
					    		 }else{
					    			 clk+=sumInfo.get(i).getClick();
					    		 }
					    		 if(sumInfo.get(i).getPageView() == null){
					    		 }else{
					    			 lll+=sumInfo.get(i).getPageView();
					    		 }
					    		 if(sumInfo.get(i).getBounceTimes() == null){
					    		 }else{
					    			 tccs+=sumInfo.get(i).getBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getViewTime() == null){
					    		 }else{
					    			 pjfwsc+=sumInfo.get(i).getViewTime();
					    		 }
					    		 url=sumInfo.get(i).getUrlPc();
						    	 tfl+=sumInfo.get(i).getPut_value();
						    	 fzxh = String.valueOf(sumInfo.get(i).getGroup_id());
						    	
//					    		 
						    	 WeekDataExport des=new WeekDataExport();
							    	 des.setActivityName(hzggw);
							    	 des.setMediaName(hzmc);
							    	 des.setPutFunction(tfxs);
							    	 des.setTerminal_type(zdlx);
							    	 des.setCustomerCode(code);
							    	 des.setMic(mic);
						    		 des.setExposureAvg(bgyg);
						    		 des.setImpPv(bgcs);
						    		 des.setImpUv(bgrs);
						    		 des.setClickAvg(djyg);
						    		 des.setClkPv(djcs);
						    		 des.setClkUv(djrs);
						    		 des.setVisit(fscs);
						    		 des.setVisitor(fwrs);
						    		 des.setClick(clk);
						    		 des.setPageView(lll);
						    		 des.setBounceTimes(tccs);
						    		 des.setViewTime(pjfwsc/fzggw);
						    		 des.setUrlPc(url);
						    		 des.setPut_value(tfl);
						    		 des.setGroup_id(Integer.valueOf(fzxh));

						    		 listAddMergin3.add(checkOut(des,2,false));

						    		 
						    		 _actiCode=sumInfo.get(i).getActivityCode();
							    	 _code = sumInfo.get(i).getCustomerCode();
					    			 _bgcs+= sumInfo.get(i).getImpPv();
						    		 _bgrs+= sumInfo.get(i).getImpUv();
						    		 if(sumInfo.get(i).getExposureAvg() == null || sumInfo.get(i).getExposureAvg() < 0 ){
							    	 }else{
							    		 _bgyg+= sumInfo.get(i).getExposureAvg();
							    	 }
						    		 if(sumInfo.get(i).getClickAvg() == null || sumInfo.get(i).getClickAvg() < 0){
						    		 }else{
						    			 _djyg+=sumInfo.get(i).getClickAvg();
						    		 }
						    		 _djcs+=sumInfo.get(i).getClkPv();
				    				 _djrs+=sumInfo.get(i).getClkUv();
				    				 if(sumInfo.get(i).getVisit() == null){
						    		 }else{
						    			 _fscs+=sumInfo.get(i).getVisit();
						    		 }
						    		 if(sumInfo.get(i).getVisitor() == null){
						    		 }else{
						    			 _fwrs+=sumInfo.get(i).getVisitor();
						    		 }
						    		 if(sumInfo.get(i).getClick()== null){
						    		 }else{
						    			 _clk+=sumInfo.get(i).getClick();
						    		 }
						    		 if(sumInfo.get(i).getPageView() == null){
						    		 }else{
						    			 _lll+=sumInfo.get(i).getPageView();
						    		 }
						    		 if(sumInfo.get(i).getBounceTimes() == null){
						    		 }else{
						    			 _tccs+=sumInfo.get(i).getBounceTimes();
						    		 }
						    		 if(sumInfo.get(i).getViewTime() == null){
						    		 }else{
						    			 _pjfwsc+=sumInfo.get(i).getViewTime();
						    		 }
						    		 _tfl+=sumInfo.get(i).getPut_value();
						    		
						    		 WeekDataExport desum=new WeekDataExport();
							    	 desum.setMediaName(hzmc);
							    	 desum.setActivityName(hzggw);
							    	 desum.setCustomerCode(_code);
							    	 desum.setMic(mic);
							    	 desum.setActivityCode(_actiCode);
							    	 desum.setExposureAvg(_bgyg);
							    	 desum.setImpPv(_bgcs);
							    	 desum.setImpUv(_bgrs);
							    	 desum.setClickAvg(_djyg);
							    	 desum.setClkPv(_djcs);
							    	 desum.setClkUv(_djrs);
							    	 desum.setVisit(_fscs);
							    	 desum.setVisitor(_fwrs);
							    	 desum.setClick(_clk);
							    	 desum.setPageView(_lll);
							    	 desum.setBounceTimes(_tccs);
							    	 desum.setViewTime(_pjfwsc/fzcd);
							    	 desum.setUrlPc(url);
							    	 desum.setPut_value(_tfl);
							    	 desum.setGroup_id(Integer.valueOf(fzxh));
						    		 listAddMergin3.add(checkOut(desum,5,false));// 分完媒体再分项目汇总
						    		 
						    		 fzcd=1;	
							    	hzmc="";
							    	tfxs="";
							    	zdlx="";
							    	code="";
						    		 bgyg=0;
						    		 bgcs=0;
						    		 bgrs=0;
						    		 djyg=0;
						    		 djcs=0;
						    		 djrs=0;
						    		 fscs=0;
						    		 fwrs=0;
						    		 clk=0;
						    		 lll=0;
						    		 tccs=0;
						    		 pjfwsc=0;
						    		 url="";
						    		 tfl=0;
						    		 tfrq="";
						    		 fzxh ="";
						    		 
						    		 _actiCode="";
						    		 _code="";
						    		 _bgyg=0;
						    		 _bgcs=0;
						    		 _bgrs=0;
						    		 _djyg=0;
						    		 _djcs=0;
						    		 _djrs=0;
						    		 _fscs=0;
						    		 _fwrs=0;
						    		 _clk =0;
						    		 _lll=0;
						    		 _tccs=0;
						    		 _pjfwsc=0;
						    		 _tfl=0;
						    		 hzggw="";
					    		
					    	}//普通的结束
					   }//最后的一个比较结束
				     }
				  }
			   
				    
				    for(int i =0;i<listAddMergin3.size();i=i+count){
						int k =i;
						String[] de = (String[]) listAddMergin3.get(i);
						count = 1;
						if(Integer.parseInt(de[25])==0){
							String[] checkout2 = {de[25], count+""};
							hb3.add(checkout2);
							continue;
						}else{
						for(int j=k;((String[]) listAddMergin3.get(j))[25].equals(((String[]) listAddMergin3.get(j+1))[25]);j++){
							count++;
							}
						String[] checkout2 = {de[25], count+""};
						hb3.add(checkout2);
						for(int z=1;z<count;z++){
							String[] checkout3 = {de[25], "0"};
							hb3.add(checkout3);
						 }
						}
					}
					
					mp.put("infoHeBing3",hb3);
					
				UserBean user = (UserBean) request.getSession().getAttribute("user");
				
				mp.put("custName", cusName);
				mp.put("imgPath", imgPath);
				mp.put("list", listAddMergin);
				mp.put("list2", listAddMergin2);
				mp.put("list3", listAddMergin3);
				mp.put("list1", list1);
				mp.put("sheetName", "导出数据");
				mp.put("dt1", dt1);
				mp.put("dt2", dt2);
				mp.put("username", user.getREAL_NAME());
				mp.put("tcl",tcl);
				mp.put("ctrs",ctrs);
				mp.put("ctre",ctre);
				mp.put("bwcls",bwcls);
				mp.put("bwcle",bwcle);
				mp.put("dwcls",dwcls);
				mp.put("dwcle",dwcle);
				WriteExcel(mp, response);
			  }
		} catch (Exception e) {
			e.printStackTrace();
			map.put("errorMsg", "error");
			try {
				response.getWriter().write(JSONUtil.beanToJson(map));
			}catch (Exception e1) {
			}
	   }
	}

		/**
		 * 
		 * @param de 传入的一个对象
		 * @param flag 是根据什么来分组
		 * @param detail 区分是明细数据还是汇总数据
		 * @param isSumDataAvg 第三个sheet汇总数据基础数据中曝光和点击预估是总的
		 * @return 
		 * 拼接数组
		 */
		public String[] checkOut(WeekDataExport de,int flag,boolean detail){
			String bgwcl="";//曝光完成率
			String djwcl ="";//点击完成率
			String djyg ="";//点击预估
			String tcl="";//跳出率 
			String fwcs="";//访问次数
			String bgcs="";//曝光次数
			String djl="";//点击率CTR
			String bgyg="";//曝光预估
			String bgrs="";//曝光人数
			String djcs="";//点击次数
			String djrs="";//点击人数
			String fwrs="";//访问人数
			String clk = ""; //到站点击
			String lll="";//浏览量
			String tcsc="";//跳出次数
			String pjfwsj="";//平均访问时间
			String tfrq="";//投放日期
			String sumDate ="";
			String fzxh="";//分组序号
			
//			DecimalFormat df0 = new DecimalFormat("#,##0");// 不显示小数点,千分位
//			DecimalFormat df1 = new DecimalFormat("#,##0.00");// 显示两位小数点，千分位
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(detail){ //全部是明细数据
				if("JT".equals(de.getCustomerCode() )){ //判断是否是辽宁和集团的明细数据(点击预估CPM=投放量*点击预估)
						if(de.getClickAvg()==0){  //点击预估值的计算
							djyg ="0";//点击预估
							djwcl="#Div/0!";
						}else if(de.getClickAvg()<0){
							djyg="N/A";
							djwcl="N/A";
						}else{
							if("CPM".equals(de.getUnit().toUpperCase())){ //并且又是点位为CPM的
								djyg = String.valueOf(de.getPut_value()*de.getClickAvg()); //点击预估 = 日历投放量*预估值
							}else{
								djyg = String.valueOf(de.getClickAvg());//点击预估 = 日历投放量*预估值
							}
							djwcl = String.valueOf(de.getClkPv()/(de.getPut_value()*de.getClickAvg()));//点击完成率
						}
						
						if( null == de.getExposureAvg()  || de.getExposureAvg() < 0){//曝光预估值的计算
							bgyg="N/A";
							bgwcl="N/A";
						}else if(de.getExposureAvg()==0){
							bgyg="0";
							bgwcl="#Div/0!";
						}else{
							if("CPM".equals(de.getUnit().toUpperCase())){ //并且又是点位为CPM的
								bgyg=String.valueOf(de.getExposureAvg() * 1000); //曝光预估 = 投放量 * 1000
							}else{
								bgyg=String.valueOf(de.getExposureAvg()); 
							}
							bgwcl=String.valueOf(de.getImpPv() /de.getExposureAvg() / 10);
					}
				}else{//点位不是CPM的时候 正常取曝光预估值
					if(de.getClickAvg()<0 || de.getClickAvg()== null){
						djwcl="N/A";
						djyg ="N/A";
					}else if(de.getClickAvg()==0){
						djwcl="#Div/0!";//除数为0的情况下
						djyg ="0";
					}else{
						djwcl=String.valueOf(de.getClkPv()/de.getClickAvg());
						djyg=String.valueOf(de.getClickAvg());
					}
					
					if( null == de.getExposureAvg()  || de.getExposureAvg() < 0){
						bgyg="N/A";
						bgwcl="N/A";
					}else if(de.getExposureAvg()==0){
						bgyg="0";
						bgwcl="#Div/0!";
					}else{
						bgwcl=String.valueOf(de.getImpPv() /de.getExposureAvg()  );
						bgyg=String.valueOf(de.getExposureAvg()); //曝光预估
					}
				}
				
				/*************   明细通用的取值   **************/
				if(de.getImpPv()==0){
					bgcs="N/A";
					djl="#Div/0!";
					bgwcl="#VALUE!";
				}else if(de.getImpPv()==-1){
					bgcs="N/A";
					djl="N/A";
					bgwcl="N/A";
				}else{
					djl=String.valueOf(de.getClkPv()/de.getImpPv());
					bgcs=String.valueOf(de.getImpPv()); //曝光次数
				}
				if(de.getImpUv()==0){
					bgrs="N/A";
				}else if(de.getImpUv()==-1){
					bgrs="N/A";
				}else{
					bgrs=String.valueOf(de.getImpUv()); //曝光人数
				}
				if(de.getClkPv()==0){
					djcs="0";
					djwcl="#VALUE!";
				}else if(de.getClkPv()==-1){
					djcs="N/A";
					djwcl="N/A";
				}else{
					djcs=String.valueOf(de.getClkPv()); //点击次数
				}
				if(de.getClkUv()==0){
					djrs="0";
				}else if(de.getClkUv() == -1){
					djrs="N/A";
				}else{
					djrs=String.valueOf(de.getClkUv()); //点击人数
				}
				if(null ==de.getBounceTimes() || de.getBounceTimes() == -1){
					tcsc="N/A";
					tcl="#VALUE!";
				}else if(de.getBounceTimes()==0){
					tcsc="N/A";
				}else{
					tcsc=String.valueOf(de.getBounceTimes());
				}
				if(null == de.getVisit() || de.getVisit() == -1){
					fwcs="N/A";
					tcl="N/A";
				}else if(de.getVisit()==0){
					fwcs="N/A";
					tcl="#Div/0!";
				}else{
					if(de.getBounceTimes()!=null && de.getVisit()!=null){
						tcl=String.valueOf(de.getBounceTimes()/de.getVisit());
					}
					fwcs=String.valueOf(de.getVisit());//访问次数
				}
				if(null == de.getVisitor() || de.getVisitor() == -1){
					fwrs="N/A";
				}else if( de.getVisitor()==0){
					fwrs="N/A";
				}else{
					fwrs=String.valueOf(de.getVisitor());
				}
				if(null == de.getClick() || de.getClick()== -1){
					clk="N/A";
				}else if(de.getClick()==0){
					clk="N/A";
				}else{
					clk=String.valueOf(de.getClick());
				 }
				if(null == de.getPageView() || de.getPageView() == -1){
					lll="N/A";
				}else if(de.getPageView()==0){
					lll="N/A";
				}else{
					lll=String.valueOf(de.getPageView());
				}
				if(null == de.getViewTime() || de.getViewTime() == -1){
					pjfwsj="N/A";
				}else if(de.getViewTime()==0){
					pjfwsj="N/A";
				}else{
					pjfwsj=String.valueOf(de.getViewTime());
				}
				sumDate = de.getSumDate();
				
				if(de.getGroup_id()==0){
					fzxh="0";
				}else{
					fzxh=de.getGroup_id()+"";
				}
			}else{ //否则是第三个sheet的汇总数据
				/*******************   汇总的曝光预估、点击预估都是求得总的  *******************/
				if(null == de.getClickAvg() || de.getClickAvg() < 0){ //汇总点击预估
					djwcl="N/A";
					djyg ="N/A";
				}else if(de.getClickAvg()==0){
					djwcl="#Div/0!";//除数为0的情况下
					djyg ="0";
				}else{
					if(de.getClkPv() !=null && de.getClickAvg()!=null){
						djwcl=String.valueOf(de.getClkPv()/de.getClickAvg() );
					}
					djyg=String.valueOf(de.getClickAvg());
				}
				if(null  == de.getExposureAvg()  || de.getExposureAvg() < 0){
					bgyg="N/A";
					bgwcl="N/A";
				}else if(de.getExposureAvg()==0){
					bgyg="0";
					bgwcl="#Div/0!";
				}else{
					if(de.getImpPv() !=null && de.getExposureAvg() != null){
						bgwcl=String.valueOf(de.getImpPv() /de.getExposureAvg() );
					}
					bgyg=String.valueOf(de.getExposureAvg()); //汇总曝光预估
				}
				
				/***********************  辽宁和集团的汇总是累加的   *********************/
				if("JT".equals(de.getCustomerCode().toUpperCase()) ){//汇总也分集团辽宁和其他省份的
					if(null == de.getSumImpPv()){
						bgcs="N/A";
						djl="N/A";
						bgwcl="N/A";
					}else if(de.getSumImpPv()==0){
						bgcs="N/A";
						djl="#Div/0!";
						bgwcl="#VALUE!";
					}else{
						djl=String.valueOf(de.getSumClkPv()/de.getSumImpPv());
						bgcs=String.valueOf(de.getSumImpPv()); //曝光次数
					}
					if(null == de.getSumImpUv()){
						bgrs="N/A";
					}else if(de.getSumImpUv()==0){
						bgrs="N/A";
					}else{
						bgrs=String.valueOf(de.getSumImpUv()); //曝光人数
					}
					if(null == de.getSumClkPv()){
						djcs="N/A";
						djwcl="N/A";
					}else if(de.getSumClkPv()==0){
						djcs="0";
						djwcl="#VALUE!";
					}else{
						djcs=String.valueOf(de.getSumClkPv()); //点击次数
					}
					if(null == de.getSumClkUv()){
						djrs="N/A";
					}else if(de.getSumClkUv()==0){
						djrs="0";
					}else{
						djrs=String.valueOf(de.getSumClkUv()); //点击人数
					}
					if(null == de.getSumBounceTimes()){
						tcsc="N/A";
						tcl = "N/A";
					}else if(de.getSumBounceTimes()==0){
						tcsc="0";
					}else{
						tcsc=String.valueOf(de.getSumBounceTimes());
					}
					if( null == de.getSumVisit()){
						fwcs="N/A";
						tcl="N/A";
					}else if(de.getSumVisit()==0){
						fwcs="0";
						tcl="#Div/0!";
					}else{
						if(de.getSumBounceTimes()!=null&&de.getSumVisit()!=null){
							tcl=String.valueOf(de.getSumBounceTimes()/de.getSumVisit());
						}
						fwcs=String.valueOf(de.getSumVisit());//访问次数
					}
					if(null == de.getSumVisitor()){
						fwrs="N/A";
					}else if(de.getSumVisitor()==0){
						fwrs="0";
					}else{
						fwrs=String.valueOf(de.getSumVisitor());  //访客
					}
					if(null == de.getSumClick()){
						clk="N/A";
					}else if(de.getSumClick()==0){
						clk="0";
					}else{
						clk=String.valueOf(de.getSumClick());
					 }
					if(null == de.getSumPageView()){
						lll="N/A";
					}else if(de.getSumPageView()==0){
						lll="0";
					}else{
						lll=String.valueOf(de.getSumPageView());
					}
					if(null == de.getSumViewTime()){
						pjfwsj="N/A";
					}else if(de.getSumViewTime()==0){
						pjfwsj="0";
					}else{
						pjfwsj=String.valueOf(de.getSumViewTime());
					}
					sumDate = de.getSumDate();
				}else{
					/******************** 除辽宁集团以外的客户的正常汇总数据  ***********************/
					if(null == de.getImpPv()){
						bgcs="N/A";
						djl="N/A";
						bgwcl="N/A";
					}else if(de.getImpPv()==0){
						bgcs="N/A";
						djl="#Div/0!";
						bgwcl="#VALUE!";
					}else{
						djl=String.valueOf(de.getClkPv()/de.getImpPv());
						bgcs=String.valueOf(de.getImpPv()); //曝光次数
					}
					if(null == de.getImpUv()){
						bgrs="N/A";
					}else if(de.getImpUv()==0){
						bgrs="N/A";
					}else{
						bgrs=String.valueOf(de.getImpUv()); //曝光人数
					}
					if(null == de.getClkPv()){
						djcs="N/A";
						djwcl="N/A";
					}else if(de.getClkPv()==0){
						djcs="0";
						djwcl="#VALUE!";
					}else{
						djcs=String.valueOf(de.getClkPv()); //点击次数
					}
					if(null == de.getClkUv()){
						djrs="N/A";
					}else if(de.getClkUv()==0){
						djrs="0";
					}else{
						djrs=String.valueOf(de.getClkUv()); //点击人数
					}
					if(null ==de.getBounceTimes() || de.getBounceTimes() == -1){
						tcsc="N/A";
					}else if(de.getBounceTimes()==0){
						tcsc="N/A";
					}else{
						tcsc=String.valueOf(de.getBounceTimes());
					}
					if(null == de.getVisit() ||de.getVisit()  == -1){
						fwcs="N/A";
						tcl="N/A";
					}else if(de.getVisit()==0){
						fwcs="N/A";
						tcl="#Div/0!";
					}else{
						if(de.getBounceTimes() !=null && de.getVisit() !=null){
							tcl=String.valueOf(de.getBounceTimes()/de.getVisit());
						}
						fwcs=String.valueOf(de.getVisit());//访问次数
					}
					if(null == de.getVisitor() || de.getVisitor() ==-1){
						fwrs="N/A";
					}else if( de.getVisitor()==0){
						fwrs="N/A";
					}else{
						fwrs=String.valueOf(de.getVisitor());
					}
					if(null == de.getClick() || de.getClick() == -1){
						clk="N/A";
					}else if(de.getClick()==0){
						clk="N/A";
					}else{
						clk=String.valueOf(de.getClick());
					 }
					if(null == de.getPageView() || de.getPageView() == -1){
						lll="N/A";
					}else if(de.getPageView()==0){
						lll="N/A";
					}else{
						lll=String.valueOf(de.getPageView());
					}
					if(null == de.getViewTime() || de.getViewTime() ==-1){
						pjfwsj="N/A";
					}else if(de.getViewTime()==0){
						pjfwsj="N/A";
					}else{
						pjfwsj=String.valueOf(de.getViewTime());
					}
					sumDate = de.getSumDate();
					
					if(de.getGroup_id()==0){
						fzxh="0";
					}else{
						fzxh=de.getGroup_id()+"";
					}
				}
			}

			if(flag==1){//明细不汇总的
				 String[] checkout2 = { de.getActivityName(), de.getMediaName(),de.getTerminal_type(),de.getPutFunction(), de.getPointLocation(),
						    de.getMic(),sdf.format(de.getPutDate()), // 获得当前的日期
							bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,clk,lll,
							tcsc,tcl,pjfwsj,bgwcl,djwcl ,de.getUrlPc(),"",fzxh};
				          return  checkout2;
			}else if(flag==2){//分媒体汇总
				 String[] checkout2 = { de.getMediaName()+"汇总", "","","","","","", // 获得当前的日期
							bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,clk,lll,
							tcsc,tcl,pjfwsj,bgwcl,djwcl ,de.getUrlPc(),"","0"};
							return checkout2;
			}else if(flag == 3){//第三个sheet明细()
				 String[] checkout2 = { de.getActivityName(), de.getMediaName(),de.getTerminal_type(),de.getPutFunction(),de.getPointLocation(), de.getMic(),sumDate+"天", // 获得当前的日期
							bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,clk,lll,
							tcsc,tcl,pjfwsj,bgwcl,djwcl ,de.getUrlPc(),"",fzxh};
				          return  checkout2;
			}else{ //分项目名
				 String[] checkout2 = { de.getActivityName()+"汇总", "","","","","","", // 获得当前的日期
							bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,clk,lll,
							tcsc,tcl,pjfwsj,bgwcl,djwcl,"-","","0"};
							return checkout2;
			}
		}

	/**
	 * 实例化一个excel
	 * @param map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void WriteExcel(Map map,HttpServletResponse response) {
			XSSFWorkbook workbook; // 实例化一个工作簿
			workbook = new XSSFWorkbook();	
			
			// 建立一张表格
			XSSFSheet sheet = workbook.createSheet("基础数据");
			XSSFSheet sheet2 = workbook.createSheet("分媒体数据");
			XSSFSheet sheet3 = workbook.createSheet("汇总数据");

			sheet.setDisplayGridlines(false);// 设置无边框
			
			sheet2.setDisplayGridlines(false);// 设置无边框
			
			sheet3.setDisplayGridlines(false);// 设置无边框
			
			XSSFFont fontNumRed = (XSSFFont)workbook.createFont();
    		fontNumRed.setColor(Font.COLOR_RED);
			
			//设置列宽
			WeekUtil.setColumnWidth(workbook);
			
			// 插入logo图片
			WeekUtil.insetImages((String)map.get("imgPath"), workbook);
			
			// 单独的把表头提出来  第一个sheet
			List<String[]> l1 = new ArrayList<String[]>();
			l1 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
			int rowIndex = 7;// 起始行
			for (int i = 0; i < l1.size(); i++) {
				XSSFRow row = sheet.createRow(rowIndex++);
				row.setHeight((short) 400);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
				String[] str = l1.get(i);
				for (int j = 0; j < str.length; j++) {
					XSSFCell cell = row.createCell(j + 1);
					cell.setCellValue(str[j]);
					WeekUtil.setStyle(workbook, cell,1);
				}
			}
			// 单独的把表头提出来  第二个sheet 
			List<String[]> l2 = new ArrayList<String[]>();
			l2 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
			int rowIndex2 = 7;// 起始行
			for (int i = 0; i < l2.size(); i++) {
				XSSFRow row = sheet2.createRow(rowIndex2++);
				row.setHeight((short) 400);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
				String[] str = l2.get(i);
				for (int j = 0; j < str.length; j++) {
					XSSFCell cell = row.createCell(j + 1);
					cell.setCellValue(str[j]);
					WeekUtil.setStyle(workbook,cell, 1);
				}
			}
			
			// 单独的把表头提出来  第三个sheet 
			List<String[]> l4 = new ArrayList<String[]>();
			l2 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
			int rowIndex4 = 7;// 起始行
			for (int i = 0; i < l1.size(); i++) {
				XSSFRow row = sheet3.createRow(rowIndex4++);
				row.setHeight((short) 400);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
				String[] str = l1.get(i);
				for (int j = 0; j < str.length; j++) {
					XSSFCell cell = row.createCell(j + 1);
					cell.setCellValue(str[j]);
					WeekUtil.setStyle(workbook,cell, 1);
				}
			}
			
//			XSSFCellStyle styleQFW = workbook.createCellStyle();
//			DataFormat format = workbook.createDataFormat();
//			styleQFW.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
			
			
			//第一个数据填充
			List<String[]> l = new ArrayList<String[]>();
			l = (List<String[]>) map.get("list");// 获得list对象，并且添加到excel中
			List<String[]> hb2 = new ArrayList<String[]>();
            hb2 = (List<String[]>) map.get("infoHeBing");
			rowIndex = 9;// 起始行
			for (int i = 0; i < l.size(); i++) {
				XSSFRow row = sheet.createRow(rowIndex++);
				String[] str = l.get(i);//获得当前遍历的对象
				
				for (int j = 0; j < str.length; j++) {
					row.setHeight((short) 315);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
				    XSSFCell cell = row.createCell(j + 1);
				    
					cell.setCellValue(str[j]);
					
					if(str[5]==null||str[5].equals("")){
						WeekUtil.setStyle(workbook,cell, 3);
					}else{
						WeekUtil.setStyle(workbook, cell,0);
					}
					
					if(str[13]!=null&&j==13){
						cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")"); 
						row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
						row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
						if(str[13].equals("N/A") || str[13].equals("#Div/0!") || str[13].equals("#VALUE!")
                				){
						}else{
                		if((String)map.get("ctrs") != null && !"".equals((String)map.get("ctrs")) &&(String)map.get("ctre") != null && !"".equals((String)map.get("ctre")) ){
                			if(100*Float.parseFloat(str[13])>=Float.parseFloat((String)map.get("ctrs")) && Float.parseFloat(str[13])<=Float.parseFloat((String)map.get("ctre")) ){
  						    }else{
  						    	row.getCell(j+1).getCellStyle().setFont(fontNumRed);
  						    	row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
  						   }
                		}
                		
                		 }
                		
                	}else if(str[19]!=null&&j==19){
    					 cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
    					 row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    					 row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
    					 row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
    					 if(str[19].equals("N/A") || str[19].equals("#Div/0!") || str[19].equals("#VALUE!")
                 				){
 						}else{
 							if((String)map.get("tcl") != null && !"".equals((String)map.get("tcl"))){
 	    						 if(100*Float.parseFloat(str[19])<=Float.parseFloat((String)map.get("tcl"))){
 	  							}else{
 	  								
 	  								row.getCell(j+1).getCellStyle().setFont(fontNumRed);
 	  								row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
 	  						  }
 	    					 }
 						}
					 }else if(str[21]!=null&&j==21){
						 row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    					 row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
    					 row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
						 if(str[21].equals("N/A") || str[21].equals("#Div/0!") || str[21].equals("#VALUE!")
	                 				){
	 						}else{
	 							
	 	   						 Double result=0.0;
	 	   						 Double jieguo=0.0;
	 	   						  if("0".equals(str[25])){
	 	   							  cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
	 	   							if((String)map.get("bwcls") != null && !"".equals((String)map.get("bwcls")) &&(String)map.get("bwcle") != null && !"".equals((String)map.get("bwcle")) ){
		 	                			if(100*Float.parseFloat(str[21])>=Float.parseFloat((String)map.get("bwcls")) && 100*Float.parseFloat(str[21])<=Float.parseFloat((String)map.get("bwcle")) ){
		 	                			}else{
		 	                				row.getCell(j+1).getCellStyle().setFont(fontNumRed);
		 	                				row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
		 	                				}
		 	                			}
	 	   						  }else{
	 		   						 StringBuffer sbfz = new StringBuffer();
	 		   						 StringBuffer sbfm = new StringBuffer();
	 		   						 String sbfzS = new String();
	 		  						 String sbfmS = new String();
	 		  						 int izs =i;
	 		  						 int izsd =i ; //不变的i
	 	  						 
	 	   						for(int q = 0; q < Integer.parseInt(hb2.get(izsd)[1]); q++){ 
	 	   								sbfzS="J"+(izs+10);
	 	   								sbfmS="I"+(izsd+10);
	 	   								izs++;
	 	   								sbfz.append(sbfzS);
	 	   								if(q==Integer.parseInt(hb2.get(izsd)[1])-1){
	 	   								}else{
	 	   								sbfz.append("+");
	 	   								}
	 	 	            		   }
	 	 	            		   String sfz = sbfz.toString();
	 	 	            		  
	 	 	            		  for(int d=0;d<Integer.parseInt(hb2.get(izsd)[1]);d++){
	 	 	            			 result=result+ Double.parseDouble(l.get(i+d)[8]);
	 	 	            		  }
	 	 	            		  jieguo = 100*result/Double.parseDouble(str[7]);
	 	 	            		  
	 	 	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
	 	   						
	 	   						if((String)map.get("bwcls")==""||(String)map.get("bwcls")==null||(String)map.get("bwcls")==""||(String)map.get("bwcls")==null){
//      							  cell.setCellStyle(styleBFS); 
          					}else{
          						if(jieguo >=Float.parseFloat((String)map.get("bwcls")) && jieguo <=Float.parseFloat((String)map.get("bwcle"))){
          						}else{
          							row.getCell(j+1).getCellStyle().setFont(fontNumRed);
          						}
          			     	}
	 						}
	 						}
						
   				    }else if(str[22]!=null&&j==22){
   				    	row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
   				    	row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
   				    	row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
   				    	if(str[22].equals("N/A") || str[22].equals("#Div/0!") || str[22].equals("#VALUE!")
                 				){
 						}else{
 							
 							Double result=0.0;
 	  						 Double jieguo=0.0;
 	  						  if("0".equals(str[25])){
 	  						cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
 	  						if((String)map.get("dwcls") != null && !"".equals((String)map.get("dwcls")) &&(String)map.get("dwcle") != null && !"".equals((String)map.get("dwcle")) ){
 	                			if(100*Float.parseFloat(str[22])>=Float.parseFloat((String)map.get("dwcls")) && 100*Float.parseFloat(str[22])<=Float.parseFloat((String)map.get("dwcle")) ){
 	                			}else{
 	                				row.getCell(j+1).getCellStyle().setFont(fontNumRed);
 	                				row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
 	                				}
 	                			}
 	  						}else{
 	  							StringBuffer sbfz = new StringBuffer();
 	     						 StringBuffer sbfm = new StringBuffer();
 	     						 String sbfzS = new String();
 	    						 String sbfmS = new String();
 	    						 int izs =i;
 	    						 int izsd =i ; //不变的i
 	    						 
 	     							for(int q = 0; q < Integer.parseInt(hb2.get(izsd)[1]); q++)
 	   	            		   { 
 	     								sbfzS="M"+(izs+10);
 	     								sbfmS="L"+(izsd+10);
 	     								izs++;
 	     								sbfz.append(sbfzS);
 	     								if(q==Integer.parseInt(hb2.get(izsd)[1])-1){
 	     								}else{
 	     								sbfz.append("+");
 	     								}
 	   	            		   }
 	   	            		   String sfz = sbfz.toString();
 	   	            		   for(int d=0;d<Integer.parseInt(hb2.get(izsd)[1]);d++){
 	      	            			 result=result+ Double.parseDouble(l.get(i+d)[11]);
 	      	            		  }
 	      	            		  jieguo = 100*result/Double.parseDouble(str[10]);
 	   	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
 	  						
 	  						
 	  						if((String)map.get("dwcls")==""||(String)map.get("dwcls")==null||(String)map.get("dwcls")==""||(String)map.get("dwcls")==null){
//							  cell.setCellStyle(styleBFS); 
    					}else{
    						if(jieguo >=Float.parseFloat((String)map.get("dwcls")) && jieguo <=Float.parseFloat((String)map.get("dwcle"))){
    						}else{
    							row.getCell(j+1).getCellStyle().setFont(fontNumRed);
    						}
    			     	}
 	  					  }
 				    }	
 						}
				} 
			}
			
			rowIndex = 9;
			for(int i=0;i<hb2.size();i++){
					if(Integer.parseInt(hb2.get(i)[1])!=1){
						 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 4, 4));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 8, 8));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 11, 11));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 22, 22));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 23, 23));
					
				}
					rowIndex++;
			}
			
			//第二个数据填充
			List<String[]> l3 = new ArrayList<String[]>();
			l3 = (List<String[]>) map.get("list2");// 获得list对象，并且添加到excel中
			rowIndex2 = 9;// 起始行
			for (int i = 0; i < l3.size(); i++) {
				XSSFRow row = sheet2.createRow(rowIndex2++);
				String[] str = l3.get(i);//获得当前遍历的对象
				
				for (int j = 0; j < str.length; j++) {
					row.setHeight((short) 315);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
				    XSSFCell cell = row.createCell(j + 1);
					cell.setCellValue(str[j]);
					if(str[5]==null||str[5].equals("")){
						WeekUtil.setStyle(workbook,cell,3);
					}else{
						WeekUtil.setStyle(workbook,cell,0);
					}
					if(j==13 || j==19 || j==21 ||j==22){
					row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
					row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
					if(str[13]!=null&&j==13){
                		cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")");
                		if(str[13].equals("N/A") || str[13].equals("#Div/0!") || str[13].equals("#VALUE!")
                				){
						}else{
                		if((String)map.get("ctrs") != null && !"".equals((String)map.get("ctrs")) &&(String)map.get("ctre") != null && !"".equals((String)map.get("ctre")) ){
                			if(100*Float.parseFloat(str[13])>=Float.parseFloat((String)map.get("ctrs")) && Float.parseFloat(str[13])<=Float.parseFloat((String)map.get("ctre")) ){
  						    }else{
  						    	row.getCell(j+1).getCellStyle().setFont(fontNumRed);
  						    	row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
  						     }
                		    }
                	    }
                	}else if(str[19]!=null&&j==19){
                		cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
                		if(str[19].equals("N/A") || str[19].equals("#Div/0!") || str[19].equals("#VALUE!")
                				){
						}else{
							if((String)map.get("tcl") != null && !"".equals((String)map.get("tcl"))){
	    						 if(100*Float.parseFloat(str[19])<=Float.parseFloat((String)map.get("tcl"))){
	  							}else{
	  								
	  								row.getCell(j+1).getCellStyle().setFont(fontNumRed);
	  								row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
	  						  }
	    					 }
						}
                	}else if(str[21]!=null&&j==21){
                		cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
                		if(str[21].equals("N/A") || str[21].equals("#Div/0!") || str[21].equals("#VALUE!")){
 						}else{
 							if((String)map.get("bwcls") != null && !"".equals((String)map.get("bwcls")) &&(String)map.get("bwcle") != null && !"".equals((String)map.get("bwcle")) ){
 	                			if(100*Float.parseFloat(str[21])>=Float.parseFloat((String)map.get("bwcls")) && 100*Float.parseFloat(str[21])<=Float.parseFloat((String)map.get("bwcle")) ){
 	                			}else{
 	                				row.getCell(j+1).getCellStyle().setFont(fontNumRed);
 	                				row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
 	                				}
 	                		}
 						}
                	}else if(str[22]!=null&&j==22){
                		cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
                		if(str[22].equals("N/A") || str[22].equals("#Div/0!") || str[22].equals("#VALUE!")){
 						}else{
 							if((String)map.get("dwcls") != null && !"".equals((String)map.get("dwcls")) &&(String)map.get("dwcle") != null && !"".equals((String)map.get("dwcle")) ){
 	                			if(100*Float.parseFloat(str[22])>=Float.parseFloat((String)map.get("dwcls")) && 100*Float.parseFloat(str[22])<=Float.parseFloat((String)map.get("dwcle")) ){
 	                			}else{
 	                				row.getCell(j+1).getCellStyle().setFont(fontNumRed);
 	                				row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
 	                				}
 	                		}
 						}
                	}
					}
				}
				//判断汇总行，添加合并的单元格
				if(str[5]==null||str[5].equals("")){
					//合并单元格到日期
					sheet2.addMergedRegion(new CellRangeAddress(9+i, 9+i, 1, 7));
				}
			}
			
			//第三个数据填充
			List<String[]> l5 = new ArrayList<String[]>();
			l5 = (List<String[]>) map.get("list3");// 获得list对象，并且添加到excel中
			List<String[]> hb3 = new ArrayList<String[]>();
            hb3 = (List<String[]>) map.get("infoHeBing3");
			rowIndex4 = 9;// 起始行
			for (int i = 0; i < l5.size(); i++) {
				XSSFRow row = sheet3.createRow(rowIndex4++);
				String[] str = l5.get(i);//获得当前遍历的对象
				
				for (int j = 0; j < str.length; j++) {
					row.setHeight((short) 315);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
				    XSSFCell cell = row.createCell(j + 1);
					cell.setCellValue(str[j]);
					if(str[5]==null||str[5].equals("")){
						WeekUtil.setStyle(workbook,cell,3);
					}else{
						WeekUtil.setStyle(workbook,cell, 0);
					}
					if (Integer.parseInt(hb3.get(i)[1])==0){
						row.createCell(8).setCellValue("0");
						row.createCell(11).setCellValue("0");
					 };
				
					 if(str[13]!=null&&j==13){
						cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")"); 
						row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
						row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
						row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
						if(str[13].equals("N/A") || str[13].equals("#Div/0!") || str[13].equals("#VALUE!")
                				){
						}else{
                		if((String)map.get("ctrs") != null && !"".equals((String)map.get("ctrs")) &&(String)map.get("ctre") != null && !"".equals((String)map.get("ctre")) ){
                			if(100*Float.parseFloat(str[13])>=Float.parseFloat((String)map.get("ctrs")) && Float.parseFloat(str[13])<=Float.parseFloat((String)map.get("ctre")) ){
  						    }else{
  						    	row.getCell(j+1).getCellStyle().setFont(fontNumRed);
  						    	row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
  						     }
                		    }
                	    }
                	}else if(str[19]!=null&&j==19){
   					 cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
   					 row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
   					 row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
   					 row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
   					 if(str[19].equals("N/A") || str[19].equals("#Div/0!") || str[19].equals("#VALUE!")
                				){
						}else{
							if((String)map.get("tcl") != null && !"".equals((String)map.get("tcl"))){
	    						 if(100*Float.parseFloat(str[19])<=Float.parseFloat((String)map.get("tcl"))){
	  							}else{
	  								
	  								row.getCell(j+1).getCellStyle().setFont(fontNumRed);
	  								row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
	  						  }
	    					 }
						}
					 }else if(str[21]!=null&&j==21){
						 row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    					 row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
    					 row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
						 if(str[21].equals("N/A") || str[21].equals("#Div/0!") || str[21].equals("#VALUE!")
	                 				){
	 						}else{
	 							
	 	   						 Double result=0.0;
	 	   						 Double jieguo=0.0;
	 	   						  if("0".equals(str[25])){
	 	   							  cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
	 	   							if((String)map.get("bwcls") != null && !"".equals((String)map.get("bwcls")) &&(String)map.get("bwcle") != null && !"".equals((String)map.get("bwcle")) ){
		 	                			if(100*Float.parseFloat(str[21])>=Float.parseFloat((String)map.get("bwcls")) && 100*Float.parseFloat(str[21])<=Float.parseFloat((String)map.get("bwcle")) ){
		 	                			}else{
		 	                				row.getCell(j+1).getCellStyle().setFont(fontNumRed);
		 	                				row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
		 	                				}
		 	                			}
	 	   						  }else{
	 		   						 StringBuffer sbfz = new StringBuffer();
	 		   						 StringBuffer sbfm = new StringBuffer();
	 		   						 String sbfzS = new String();
	 		  						 String sbfmS = new String();
	 		  						 int izs =i;
	 		  						 int izsd =i ; //不变的i
	 	  						 
	 	   						for(int q = 0; q < Integer.parseInt(hb3.get(izsd)[1]); q++){ 
	 	   								sbfzS="J"+(izs+10);
	 	   								sbfmS="I"+(izsd+10);
	 	   								izs++;
	 	   								sbfz.append(sbfzS);
	 	   								if(q==Integer.parseInt(hb3.get(izsd)[1])-1){
	 	   								}else{
	 	   								sbfz.append("+");
	 	   								}
	 	 	            		   }
	 	 	            		   String sfz = sbfz.toString();
	 	 	            		  
	 	 	            		  for(int d=0;d<Integer.parseInt(hb3.get(izsd)[1]);d++){
	 	 	            			  if("N/A".equals(l.get(i+d)[8]) ||"#Div/0!".equals(l.get(i+d)[8])){
	 	 	            				result=result+0.0;
	 	 	            			  }else{
	 	 	            			 result=result+ Double.parseDouble(l.get(i+d)[8]);
	 	 	            			  }
	 	 	            			  }
	 	 	            		  
	 	 	            		  jieguo = 100*result/Double.parseDouble(str[7]);
	 	 	            		  if(sfz.equals("") || sbfmS.equals("")){
	 	 	            			  System.out.println(sbfmS+"11");
	 	 	            		  }else{
	 	 	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
	 	 	            		  }
	 	   						if((String)map.get("bwcls")==""||(String)map.get("bwcls")==null||(String)map.get("bwcls")==""||(String)map.get("bwcls")==null){
          					}else{
          						if(jieguo >=Float.parseFloat((String)map.get("bwcls")) && jieguo <=Float.parseFloat((String)map.get("bwcle"))){
          						}else{
          							row.getCell(j+1).getCellStyle().setFont(fontNumRed);
          						}
          			     	}
	 						}
	 						}
						
   				    }else if(str[22]!=null&&j==22){

   				    	row.getCell(j+1).getCellStyle().setAlignment(HSSFCellStyle.ALIGN_RIGHT);
   				    	row.getCell(j+1).getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
   				    	row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
   				    	if(str[22].equals("N/A") || str[22].equals("#Div/0!") || str[22].equals("#VALUE!")
                 				){
 						}else{
 							
 							Double result=0.0;
 	  						 Double jieguo=0.0;
 	  						  if("0".equals(str[25])){
 	  						cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
 	  						if((String)map.get("dwcls") != null && !"".equals((String)map.get("dwcls")) &&(String)map.get("dwcle") != null && !"".equals((String)map.get("dwcle")) ){
 	                			if(100*Float.parseFloat(str[22])>=Float.parseFloat((String)map.get("dwcls")) && 100*Float.parseFloat(str[22])<=Float.parseFloat((String)map.get("dwcle")) ){
 	                			}else{
 	                				row.getCell(j+1).getCellStyle().setFont(fontNumRed);
 	                				row.getCell(j+1).getCellStyle().getFont().setFontHeightInPoints((short) 9);
 	                				}
 	                			}
 	  						}else{
 	  							StringBuffer sbfz = new StringBuffer();
 	     						 StringBuffer sbfm = new StringBuffer();
 	     						 String sbfzS = new String();
 	    						 String sbfmS = new String();
 	    						 int izs =i;
 	    						 int izsd =i ; //不变的i
 	    						 
 	     							for(int q = 0; q < Integer.parseInt(hb3.get(izsd)[1]); q++)
 	   	            		   { 
 	     								sbfzS="M"+(izs+10);
 	     								sbfmS="L"+(izsd+10);
 	     								izs++;
 	     								sbfz.append(sbfzS);
 	     								if(q==Integer.parseInt(hb3.get(izsd)[1])-1){
 	     								}else{
 	     								sbfz.append("+");
 	     								}
 	   	            		   }
 	   	            		   String sfz = sbfz.toString();
 	   	            		   for(int d=0;d<Integer.parseInt(hb3.get(izsd)[1]);d++){
 	   	            			   if("N/A".equals(l.get(i+d)[11])||"#Div/0!".equals(l.get(i+d)[11])){
 	   	            				result=result+0.0;
 	   	            			   }else{
 	      	            			 result=result+ Double.parseDouble(l.get(i+d)[11]);
 	   	            			   }
 	   	            			   }
 	      	            		  jieguo = 100*result/Double.parseDouble(str[10]);
 	      	            		if(sfz.equals("") || sbfmS.equals("")){
	 	 	            			  System.out.println(sbfmS+"11");
	 	 	            		  }else{
	 	 	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
	 	 	            		  }
 	  						
 	  						if((String)map.get("dwcls")==""||(String)map.get("dwcls")==null||(String)map.get("dwcls")==""||(String)map.get("dwcls")==null){
//							  cell.setCellStyle(styleBFS); 
    					}else{
	    						if(jieguo >=Float.parseFloat((String)map.get("dwcls")) && jieguo <=Float.parseFloat((String)map.get("dwcle"))){
	    						}else{
    							row.getCell(j+1).getCellStyle().setFont(fontNumRed);
    						 }
    			     		}
 	  					  }
 						}	
   				      }
				 }
				//判断汇总行，添加合并的单元格
				if(str[5]==null||str[5].equals("")){
					//合并单元格到日期
					sheet3.addMergedRegion(new CellRangeAddress(9+i, 9+i, 1, 7));
				}
			}
			
			rowIndex4 = 9;
			for(int i=0;i<hb3.size();i++){
					if(Integer.parseInt(hb3.get(i)[1])!=1&&Integer.parseInt(hb3.get(i)[1])!=0){
						 sheet3.addMergedRegion(new CellRangeAddress(rowIndex4, rowIndex4-1+Integer.parseInt(hb3.get(i)[1]), 4, 4));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex4, rowIndex4-1+Integer.parseInt(hb3.get(i)[1]), 8, 8));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex4, rowIndex4-1+Integer.parseInt(hb3.get(i)[1]), 11, 11));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex4, rowIndex4-1+Integer.parseInt(hb3.get(i)[1]), 22, 22));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex4, rowIndex4-1+Integer.parseInt(hb3.get(i)[1]), 23, 23));
					
				}
					rowIndex4++;
			}

			//设置顶部文本信息及样式
			WeekUtil.setTitle(workbook, sd, map);
			
			//设置表头合并信息
			WeekUtil.addMergedRegion(workbook);
			
			//设置数值格式及公式输出
			WeekUtil.setNumricValue(workbook,map);
			
			String custName = (String) map.get("custName");
			
			//保存文件到磁盘目录
			WeekUtil.saveExcel(custName, workbook, response,(Date)map.get("dt1"), (Date)map.get("dt2"), filePathManager, sd);
	}

	/**
	 * 从本地读取文件页面弹出下载框
	 * @param filePath
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getExcelPath.do")
	public static void outputWorkBook(String filePath, HttpServletResponse response) throws IOException {

		filePath = java.net.URLDecoder.decode(filePath, "UTF-8");

		File file = new File(filePath);
		String fileName = "";
		fileName = file.getName();
		// 以流的形式下载文件。
		InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		// 清空response
		response.reset();
		// 定义输出类型
		response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setHeader("Content-disposition","attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
		response.setCharacterEncoding("utf-8");
		OutputStream ouputStream = new BufferedOutputStream(response.getOutputStream());
		ouputStream.write(buffer);
		ouputStream.flush();
		ouputStream.close();
	}
	
}
