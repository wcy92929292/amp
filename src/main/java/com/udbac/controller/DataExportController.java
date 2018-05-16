package com.udbac.controller;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.crypto.io.MacOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.udbac.entity.DataExport;
import com.udbac.entity.TbAmpBasicScheduleInfo;
import com.udbac.model.UserBean;
import com.udbac.service.DataExportService;
import com.udbac.util.DataEndUntil;
import com.udbac.util.FilePathManager;
import com.udbac.util.JSONUtil;
import com.udbac.util.XlsxUtil;

/**
 * 报告导出 2016-05-20
 * 
 * @author han
 *
 */

@Controller
@RequestMapping("/export")
public class DataExportController {

	@Autowired
	DataExportService exportService;

	@Autowired
	private FilePathManager filePathManager;
	/**
	 * 
	 * @param date
	 * @param response
	 * @param request
	 * @param actName
	 * @param actCode
	 * @param customerName
	 * @param endDate
	 * @param customer_id
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="sumDay.do",method = RequestMethod.POST)
	public String sumName(@RequestParam(value = "date", required = true) String date,
			HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "actName", required = false) String actName,
			@RequestParam(value = "actCode", required = false) String actCode,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "customer_id", required = true) String customer_id,
			@RequestParam(value = "sysTime", required = true) int sysTime,
			@RequestParam(value = "mic", required = true) String mic
			) throws IOException{
		    Date dt = null;
		    Date dte = null;//将存放的是结束日期
			String falg="";
			Integer sumName=0;
		//投放的单位性质为多选的形式
			if(customer_id==null||customer_id.equals("")){
			}else{
				 customer_id=customer_id.substring(0, customer_id.length()-1);

			}
	 if(sysTime==1){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    //确保time是当前天
			 Date date2 = new Date();
			String time = sdf.format(date2);
			 sumName=exportService.sumDays(customer_id,time, actName, actCode); 
		 }else if(sysTime==2){
			 //
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
				if (date != null && date != "") {
					try {
						dt = sdf.parse(date);
					} catch (ParseException e) {

						e.printStackTrace();
					}
				} else {// 如果没有选择时间的话，系统默认选择当前事件的前一天
					Date date1 = new Date();
					date1=getNextDay(date1);
					String time = sdf.format(date1);
					try {
						dt = sdf.parse(time);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(endDate!=null &&endDate!=""){
					try {
						dte=sdf.parse(endDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
			//按小时取得时间
			sumName=exportService.sumHour(customer_id,dt, actName, actCode,dte,mic.replaceAll(" ", ""));  
		 }else{
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (date != null && date != "") {
					try {
						dt = sdf.parse(date);
					} catch (ParseException e) {

						e.printStackTrace();
					}
				} else {// 如果没有选择时间的话，系统默认选择当前事件的前一天
					Date date1 = new Date();
					date1=getNextDay(date1);
					String time = sdf.format(date1);
					try {
						dt = sdf.parse(time);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if(endDate!=null &&endDate!=""){
					try {
						dte=sdf.parse(endDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
			
			 //按天取得时间
			 sumName=exportService.sumDay(customer_id,dt, actName, actCode,dte); 
		 }
		    if(sumName>0){
		    	falg="1";
			   }else{
				falg="2";
			   }
		    return falg;
	}
	
	@SuppressWarnings({ "rawtypes", "unused","unchecked" })
	@ResponseBody
	@RequestMapping("/exportDay.do")
	public ModelAndView ListInfo(@RequestParam(value = "date", required = true) String date,
			HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "actName", required = false) String actName,
			@RequestParam(value = "actCode", required = false) String actCode,
			@RequestParam(value = "customerName", required = true) String customerName,
			@RequestParam(value = "endDate", required = true) String endDate,
			@RequestParam(value = "customer_id", required = true) String customer_id,
			@RequestParam(value = "sysTime", required = true) int sysTime,
			@RequestParam(value = "mic", required = true) String mic,
			@RequestParam(value = "jumpRang", required = false) String jumpRang,
			@RequestParam(value = "ctrRangL", required = false) String ctrRangL,
			@RequestParam(value = "ctrRangM", required = false) String ctrRangM,
			@RequestParam(value = "wcRangL", required = false) String wcRangL, //曝光
			@RequestParam(value = "wcRangM", required = false) String wcRangM,
			@RequestParam(value = "djwcRangL", required = false) String djwcRangL,//点击
			@RequestParam(value = "djwcRangM", required = false) String djwcRangM
			) throws Exception {
		// 取得session中的user对象
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String userRealName=user.getREAL_NAME();
		//获得图片的路径
		String imgPath = session.getServletContext().getRealPath("/") + "images/logo.png";
		Date dt = null;
		Date dte=null;//存放的是结束时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String customerNames = new String(customerName.getBytes("iso-8859-1"), "utf-8");// 解决乱码问题
		//投放的单位性质为多选的形式
		 //customer_id=customer_id.substring(0, customer_id.length()-1);
		if(customer_id==null||customer_id.equals("")){
		}else{
			 customer_id=customer_id.substring(0, customer_id.length()-1);

		}
		//没有选择投放单位，显示的是全部省份的投放信息
		if(customerNames.equals("--请选择--")){
			customerNames="全部省份";
		}
		String custName = customerNames + "互联网广告监测日报";
		String emonth = "";// 结束时间
		String entry = "";// excel名称
		String smonth = "";// 开始时间
		String month="";//同时填写开始时间和结束时间，需要将月份的值存放在单元格里
		if(sysTime==1){
			Date date1 = new Date();
			month=sdf.format(date1);
			entry = "项目日报_" + customerNames + "互联网广告监测日报_" + month;
		}else if(sysTime==2){
			SimpleDateFormat sdH = new SimpleDateFormat("yyyy-MM-dd HH:00:00");	
			if (date != null && date != "") {
				smonth = date.substring(0, 4) + "-" + date.substring(5, 7) + "-" + date.substring(8, 10)+"-"+date.substring(11, 13)+"H";
				try {
					dt = sdH.parse(date);
					Date date1 = new Date();
					dte=weeHours(date1,1);//取得是前一天的23点
					if(sdH.format(dt).equals(sdH.format(date1))){
						month=smonth;//显示的文件信息
					}else{
						String dates=sdH.format(dte);//取到的是前一天的23h,若没有添加后面的结束日期
						
						month= smonth +"-"+dates.substring(0, 4) + "-" + dates.substring(5, 7) + "-" + dates.substring(8, 10)+"-"+dates.substring(11, 13)+"H";
					}
				} catch (ParseException e) {

					e.printStackTrace();
				}
			} else {// 如果没有选择时间的话，系统默认选择当前事件的前一天
				Date date1 = new Date();
				dt = weeHours(date1,0); //前一天的00:00:00
				dte=weeHours(date1,1);  //前一天的23:59:59
				String time = sdH.format(dte);
			    String stime=sdH.format(dt);
			    stime=stime.substring(0, 4) + "-" + stime.substring(5, 7) + "-" + stime.substring(8, 10)+"-"+stime.substring(11, 13)+"H";
				time = time.substring(0, 4) + "-" + time.substring(5, 7) + "-" + time.substring(8, 10)+"-"+time.substring(11, 13)+"H";
				month=stime+"-"+time;//开始的日期加结束的日期：给excel表名称命名
			}
			if(endDate!=null &&endDate!=""){
				try {
					dte=sdH.parse(endDate);
					 Date dts=sdH.parse(date);
					 if(dte.equals(dts)){//如果开始日期等于结束日期，单元格的日期是一个
						smonth = endDate.substring(0, 4) + "-" + endDate.substring(5, 7) + "-" + endDate.substring(8, 10)+"-"+endDate.substring(11, 13)+"H";
						month=smonth;
					 }else{
						 String dates=sdH.format(dte);//取到的是前一天的23h,若没有添加后面的结束日期
						month= smonth +"-"+dates.substring(0, 4) + "-" + dates.substring(5, 7) + "-" + dates.substring(8, 10)+"-"+dates.substring(11, 13)+"H";
					 }
				} catch (ParseException e) {
					e.printStackTrace();
				}
				 smonth=month;//将结束日期和开始日期赋值给smonth;
				entry = "项目日报_" + customerNames + "互联网广告监测日报_" + month;
			} else {
				entry = "项目日报_" + customerNames + "互联网广告监测日报_" + month;
			}
		}else{
			if (date != null && date != "") {
				smonth = date.substring(0, 4) + "-" + date.substring(5, 7) + "-" + date.substring(8, 10);
				try {
					dt = sdf.parse(date);
					Date date1 = new Date();
					date1=getNextDay(date1);
					
					if(sdf.format(dt).equals(sdf.format(date1))){
						month=sdf.format(dt);
					}else{
						month=sdf.format(dt)+"_"+sdf.format(date1);
					}
				} catch (ParseException e) {

					e.printStackTrace();
				}
			} else {// 如果没有选择时间的话，系统默认选择当前事件的前一天
				Date date1 = new Date();
				date1=getNextDay(date1);
				String time = sdf.format(date1);
				month=sdf.format(date1);
				try {
					dt = sdf.parse(time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(endDate!=null &&endDate!=""){
				try {
					dte=sdf.parse(endDate);
					 Date dts=sdf.parse(date);
					 if(dte.equals(dts)){//如果开始日期等于结束日期，单元格的日期是一个
						 month=sdf.format(dts);//去掉日期与小时之间的单元格，否则报错
					 }else{
						 month=sdf.format(dts)+"_"+sdf.format(dte);
	 
					 }
				} catch (ParseException e) {
					e.printStackTrace();
				}
				smonth=month;//将结束日期和开始日期赋值给smonth;
				entry = "项目日报_" + customerNames + "互联网广告监测日报_" + month;
			} else {
				entry = "项目日报_" + customerNames + "互联网广告监测日报_" + month;
			}
		}
			
		Map map = new HashMap();
		String path = filePathManager.getTmpExcel() + entry + ".xlsx";// 临时的文件目录
		List list1 = new ArrayList();
		List listHuiZong = new ArrayList();
		List listHeBing = new ArrayList();
		List infoHeBingGL = new ArrayList(); //过滤
		List hb = new ArrayList(); //（过滤后）包含四个要合并的条件的项 + 每项行数
		ArrayList<DataExport> infos = new ArrayList();  //原结果过滤后
		
		String[] title = { "项目名称", "投放单位","媒体名称", "广告位","投放形式","投放日期","营销代码","投放类型", "广告前端曝光点击监测","", "", "", "","","","活动网站监测",
				"", "", "", "", "","","完成率情况", "" ,"URL","备注"};
		String[] titles = { "", "","","","","","","","曝光预估", "曝光次数", "曝光人数", "点击预估","点击次数", "点击人数", "点击率CTR", "访问次数", "访问人数",
				" 到站点击 ", "浏览量","跳出次数", "跳出率", "平均访问时长(s)", "曝光完成率", "点击完成率" ,"",""};
		list1.add(title);
		list1.add(titles);
		if(actName!=null){
			actName=new String(actName.getBytes("iso-8859-1"), "utf-8");// 解决乱码问题
		}
		//start  主要查询出相同行   和每项的行数count
		int count = 1;
		if(sysTime==1){
			 Date date2 = new Date();
			String time=sdf.format(date2);
			ArrayList<DataExport> info = (ArrayList<DataExport>) exportService.listInfos(time, actName, actCode,customer_id);
			for (int i = 0; i < info.size(); i++) {
			DataExport de =info.get(i);
			if(de.getPv()==0&&de.getUv()==0&&de.get_pv()==0&&de.get_uv()==0&&de.getTime_s()==-1&&de.getA_pv()==-1&&de.getA_vv()==-1&&de.getA_uv()==-1){
				//前端数据为空的时候,拦截下
			}else{
				infos.add(de);
				if(de.getUnit().equals("CPM")){
		    		//明细CPM
					 listHeBing.add(new DataExportController().checkOut(de,true,sysTime));				
		    	}else{
		    		 //明细
					 listHeBing.add(new DataExportController().checkOut(de,false,sysTime));
		    	     }
			}
			
			}
			
					for(int i =0;i<infos.size();i=i+count){
						int k =i;
						DataExport de = (DataExport) infos.get(i);
						count = 1;
						if(((DataExport) infos.get(i)).getGroup_id()==0){
							String[] checkout2 = {de.getGroup_id()+"", count+""};
							hb.add(checkout2);
							continue;
						}else{
						for(int j=k;(j!=infos.size()-1) && ((DataExport) infos.get(j)).getGroup_id()==((DataExport) infos.get(j+1)).getGroup_id()&&((DataExport) infos.get(j)).getPut_date().compareTo(((DataExport) infos.get(j+1)).getPut_date())==0;j++){
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
					
					map.put("infoHeBing",hb);
		}else if(sysTime==2){
			//能够去掉所有的空格，而trim只能够去掉前后的空格而不能够去掉中间的空格
			ArrayList<DataExport> info = (ArrayList<DataExport>) exportService.listHourInfo(dt, actName, actCode,customer_id,dte,mic.replaceAll(" ", ""));
			for (int i = 0; i < info.size(); i++) {
				DataExport de =info.get(i);
				if(de.getPv()==0&&de.getUv()==0&&de.get_pv()==0&&de.get_uv()==0&&de.getTime_s()==-1&&de.getA_pv()==-1&&de.getA_vv()==-1&&de.getA_uv()==-1){
					//前端数据为空的时候,拦截下
				}else{
					infos.add(de);
					if(de.getUnit().equals("CPM")){
			    		//明细CPM
						 listHeBing.add(new DataExportController().checkOut(de,true,sysTime));				
			    	}else{
			    		 //明细
						 listHeBing.add(new DataExportController().checkOut(de,false,sysTime));
			    	     }
				}
				}
				for(int i =0;i<infos.size();i=i+count){
					int k =i;
					DataExport de = (DataExport) infos.get(i);
					count = 1;
					if(((DataExport) infos.get(i)).getGroup_id()==0){
						String[] checkout2 = {de.getGroup_id()+"", count+""};
						hb.add(checkout2);
						continue;
					}else{
					for(int j=k;(j!=infos.size()-1) &&((DataExport) infos.get(j)).getGroup_id()==((DataExport) infos.get(j+1)).getGroup_id()&&((DataExport) infos.get(j)).getPut_date().compareTo(((DataExport) infos.get(j+1)).getPut_date())==0;j++){
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
				
				map.put("infoHeBing",hb);
		
			
		}else{
			
			ArrayList<DataExport> info = (ArrayList<DataExport>) exportService.listInfo(dt, actName, actCode,customer_id,dte);
			for (int i = 0; i < info.size(); i++) {
			DataExport de =info.get(i);
			if(de.getPv()==0&&de.getUv()==0&&de.get_pv()==0&&de.get_uv()==0&&de.getTime_s()==-1&&de.getA_pv()==-1&&de.getA_vv()==-1&&de.getA_uv()==-1){
				//前端数据为空的时候,拦截下
			}else{
				infos.add(de);
				if(de.getUnit().equals("CPM")){
		    		//明细CPM
					 listHeBing.add(new DataExportController().checkOut(de,true,sysTime));				
		    	}else{
		    		 //明细
					 listHeBing.add(new DataExportController().checkOut(de,false,sysTime));
		    	     }
			}
			}
			for(int i =0;i<infos.size();i=i+count){
				int k =i;
				DataExport de = (DataExport) infos.get(i);
				count = 1;
				if(((DataExport) infos.get(i)).getGroup_id()==0){
					String[] checkout2 = {de.getGroup_id()+"", count+""};
					hb.add(checkout2);
					continue;
				}else{
				for(int j=k;(j!=infos.size()-1) &&((DataExport) infos.get(j)).getGroup_id()==((DataExport) infos.get(j+1)).getGroup_id()&&((DataExport) infos.get(j)).getPut_date().compareTo(((DataExport) infos.get(j+1)).getPut_date())==0;j++){
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
			
			map.put("infoHeBing",hb);
		}
		
			map.put("custName", custName);
			map.put("imgPath", imgPath);
			map.put("dt", month);
			map.put("list", listHeBing);
			map.put("list1", list1);
			map.put("path", path);
			map.put("sheetName", entry);
			
			
			WriteExcel(map,actCode,custName,jumpRang,ctrRangL,ctrRangM,wcRangL,wcRangM,userRealName,djwcRangL,djwcRangM);
			
		  
		return this.LoadExcel(request, response, path);
	}

	
	/****
	 * 获取Session中用户的省级编号
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getProvince.do", method = RequestMethod.POST)
	public @ResponseBody void getProvince(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBean user;
		try {
			user = (UserBean) request.getSession().getAttribute("user");
			// 取得user的Province_id
			response.getWriter().print(user.getPROVINCE_ID());
			//System.out.print("========"+user.getPROVINCE_ID());
		} catch (Exception e) {
			response.getWriter().print("error");
		}

	}
	//拼接数组	
	public String[] checkOut(DataExport de,boolean unit, int sysTime){
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
		String lll="";//浏览量
		String tcsc="";//跳出次数
		String pjfwsj="";//平均访问时间
		String dzdj="";//到站点击
		String fzxh="";//分组序号
		DecimalFormat df0 = new DecimalFormat("###0");// 不显示小数点,千分位
		DecimalFormat df1 = new DecimalFormat("###0.00");// 显示两位小数点，千分位
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdH = new SimpleDateFormat("yyyy-MM-dd HH:00:00");	
		//集团和辽宁才用这个算法,不包含咪咕集团
		if(de.getActivity_code().indexOf("JT")!=-1&&de.getActivity_code().indexOf("MGJT")==-1){
			if(de.getPut_value()==null||de.getPut_value()==0){
				  bgyg="N/A";
				  djyg ="N/A";
				  bgwcl="#Div/0!";
				  djwcl="#Div/0!";
				}else{
					if(unit){//投放单位为CPM
						if(de.getExposure_avg()==0){
							bgyg="0";
							bgwcl="#Div/0!";
						}else if(de.getExposure_avg()==-1){
							bgyg="N/A";
							bgwcl="N/A";
						}else{
							bgwcl=df1.format(de.getPv()/(de.getPut_value()*1000)*100);//曝光完成率
							bgyg =df0.format(de.getPut_value()*1000);//曝光预估
						}
						if(de.getClick_avg()==0){
							djyg ="0";//点击预估
							djwcl="#Div/0!";
						}else if(de.getClick_avg()==-1){
							djyg="N/A";
							djwcl="N/A";
						}else {
							djyg =df0.format(de.getPut_value()*de.getClick_avg());//点击预估
							djwcl=df1.format(de.get_pv()/(de.getPut_value()*de.getClick_avg()) * 100);//点击完成率
						}
					}else {//投放单位非CPM
						if(de.getExposure_avg()==0){
							bgyg="0";
							bgwcl="#Div/0!";
						}else if(de.getExposure_avg()==-1){
							bgyg="N/A";
							bgwcl="N/A";
						}else{
							bgwcl=df1.format(de.getPv() /de.getExposure_avg() * 100)+"%";
							bgyg=df0.format(de.getExposure_avg());
						}
						if(de.getClick_avg()==0){
							djwcl="#Div/0!";//除数为0的情况下
							djyg ="0";
						}else if(de.getClick_avg()==-1){//sql已经将空全部替换为-1了
							djwcl="N/A";
							djyg ="N/A";
						}else{
							djwcl=df1.format(de.get_pv()/de.getClick_avg() * 100);
							djyg=df0.format(de.getClick_avg());
						}
							
					}
					
				} 
		}else{
			if(de.getExposure_avg()==0){
				bgyg="0";
				bgwcl="#Div/0!";
			}else if(de.getExposure_avg()==-1){
				bgyg="N/A";
				bgwcl="N/A";
			}else{
				bgwcl=df1.format(de.getPv() /de.getExposure_avg() * 100);
				bgyg=df0.format(de.getExposure_avg());
			}
			if(de.getClick_avg()==0){
				djyg ="0";//点击预估
				djwcl="#Div/0!";
			}else if(de.getClick_avg()==-1){
				djyg="N/A";
				djwcl="N/A";
			}else {
				djyg =df0.format(de.getClick_avg());//点击预估
				djwcl=df1.format(de.get_pv()/de.getClick_avg() * 100);//点击完成率
			}
		}

		
		if(de.getA_vv()==0){
			fwcs="0";
			tcl="#Div/0!";
		}else if(de.getA_vv()==-1){
			fwcs="N/A";
			tcl="N/A";
		}else{
			tcl=df1.format(de.getBounce_t()/de.getA_vv()*100);
			fwcs=df0.format(de.getA_vv());//访问次数
		}
		if("1".equals(de.getSupportexposure())){
			if(de.getPv()==0){
				bgcs="0";
				djl="#Div/0!";
			}else if(de.getPv()==-1){
				bgcs="N/A";
				djl="N/A";
				bgwcl="N/A";
			}else{
				djl=df1.format(de.get_pv()/de.getPv()*100);
				bgcs=df0.format(de.getPv());
			}
			if(de.getUv()==0){
				bgrs="0";
			}else if(de.getUv()==-1){
				bgrs="N/A";
			}else{
				bgrs=df0.format(de.getUv());
			}
		}else{
			if(de.getPv()==0){
				bgcs="N/A";
				djl="#Div/0!";
			}else if(de.getPv()==-1){
				bgcs="N/A";
				djl="N/A";
				bgwcl="N/A";
			}else{
				djl=df1.format(de.get_pv()/de.getPv()*100);
				bgcs=df0.format(de.getPv());
			}
			if(de.getUv()==0){
				bgrs="N/A";
			}else if(de.getUv()==-1){
				bgrs="N/A";
			}else{
				bgrs=df0.format(de.getUv());
			}
		}
		if("1".equals(de.getSupportclick())){
			if(de.get_pv()==0){
				djcs="0";
			}else if(de.get_pv()==-1){
				djcs="N/A";
				djwcl="N/A";
			}else{
				djcs=df0.format(de.get_pv());
			}
			if(de.get_uv()==0){
				djrs="0";
			}else if(de.get_uv()==-1){
				djrs="N/A";
			}else{
				djrs=df0.format(de.get_uv());
			}
		}else{
			if(de.get_pv()==0){
				djcs="N/A";
			}else if(de.get_pv()==-1){
				djcs="N/A";
				djwcl="N/A";
			}else{
				djcs=df0.format(de.get_pv());
			}
			if(de.get_uv()==0){
				djrs="N/A";
			}else if(de.get_uv()==-1){
				djrs="N/A";
			}else{
				djrs=df0.format(de.get_uv());
			}
		}
		
		if(de.getA_uv()==0){
			fwrs="0";
		}else if(de.getA_uv()==-1){
			fwrs="N/A";
		}else{
			fwrs=df0.format(de.getA_uv());
		}
		if(de.getA_pv()==0){
			lll="0";
		}else if(de.getA_pv()==-1){
			lll="N/A";
		}else{
			lll=df0.format(de.getA_pv());
		}
		if(de.getBounce_t()==0){
			tcsc="0";
		}else if(de.getBounce_t()==-1){
			tcsc="N/A";
		}else{
			tcsc=df0.format(de.getBounce_t());
		}
		if(de.getTime_s()==0){
			pjfwsj="0";
		}else if(de.getTime_s()==-1){
			pjfwsj="N/A";
		}else{
			pjfwsj=df1.format(de.getTime_s());
		}
		if(de.getClk()==-1){
			dzdj="N/A";
		}else if(de.getClk()==0){
			dzdj="0";
		}else{
			dzdj=df0.format(de.getClk());
		 }
		if(de.getGroup_id()==0){
			fzxh="0";
		}else{
			fzxh=de.getGroup_id()+"";
		}
		//小时的技术
		if(sysTime==2){
			String[] checkout2 = {  de.getActivity_name(),de.getCustomer_name(), de.getMedia_name(), de.getPoint_location(),de.getPut_function(),sdH.format(de.getPut_date()), // 获得当前的日期
					  de.getMic(),de.getTerminal_Type(),bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,dzdj,lll,
						tcsc,tcl,pjfwsj,bgwcl,djwcl ,de.getUrl_pc(),"",fzxh};
			          return  checkout2;	
		}else{
			String[] checkout2 = {  de.getActivity_name(),de.getCustomer_name(), de.getMedia_name(), de.getPoint_location(),de.getPut_function(),sdf.format(de.getPut_date()), // 获得当前的日期
					  de.getMic(),de.getTerminal_Type(),bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,dzdj,lll,
						tcsc,tcl,pjfwsj,bgwcl,djwcl ,de.getUrl_pc(),"",fzxh};
			          return  checkout2;
		}
			 
		
	}
	
	public static void setCellStyle(XSSFCellStyle cellStyle, Workbook workbook) {
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中

		Font font = workbook.createFont();
		font.setFontName("calibri");
		font.setFontHeightInPoints((short) 9);// 设置字体大小

		cellStyle.setFont(font);// 选择需要用到的字体格式

	}

	/****
	 * 获取Session中用户角色 han 2016-06-07
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUserSession.do", method = RequestMethod.POST)
	public @ResponseBody void getUserSeesion(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBean user;
		try {
			user = (UserBean) request.getSession().getAttribute("user");
			// 取得user的userID
			response.getWriter().print(user.getROLE_ID());
		} catch (Exception e) {
			response.getWriter().print("error");
		}

	}
	/**
	 * 省级客户投放单位
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/queryCustomer.do")
	public List queryCustomer(@RequestParam(value = "province", required = true) String province) {
		List queryCus = exportService.queryCustomer(province);
		return queryCus;
	}

	/**
	 * 实例化一个excel
	 * 
	 * @param map
	 * @param actCode 
	 * @param custName 
	 * @param wcRangM 
	 * @param wcRangL 
	 * @param ctrRang 
	 * @param jumpRang 
	 * @param userRealName 
	 * @param djwcRangM 
	 * @param djwcRangL 
	 */
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked", "unused", "resource" })
	public void WriteExcel(Map map, String actCode, String custName, String jumpRang, String ctrRangL,String ctrRangM, String wcRangL, String wcRangM
			,String userRealName, String djwcRangL, String djwcRangM) {
		String outputFile = (String) map.get("path");
		File file = new File(outputFile);
		XSSFWorkbook workbook; // 实例化一个工作簿
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			FileOutputStream out = new FileOutputStream(outputFile);
			workbook = new XSSFWorkbook();
			XSSFCellStyle style1 = workbook.createCellStyle();
			XSSFFont font1 = workbook.createFont();
			font1.setFontName("calibri");
			font1.setFontHeightInPoints((short) 9);// 设置字体大小
			style1.setFont(font1);// 选择需要用到的字体格式
			new DataExportController().setBoderStyle(style1);
			
			//日期单元格
			XSSFCellStyle styler = workbook.createCellStyle();
			styler.setFont(font1);// 选择需要用到的字体格式
			
			CreationHelper helper = workbook.getCreationHelper();
			style1.setDataFormat(helper.createDataFormat().getFormat("yyyy-MM-dd"));// 设置日期的格式信息
			// 建立一张表格
			XSSFSheet sheet = workbook.createSheet((String) map.get("custName"));
			// 设置特殊列宽
			sheet.setColumnWidth(1, 25 * 256);// 设置第一列的列宽，估计值
			sheet.setColumnWidth(3, 10 * 256);// 设置第二列的列宽
			sheet.setColumnWidth(4, 40 * 256);//存放的是广告位的数据
			sheet.setColumnWidth(5, 22 * 256);
			sheet.setColumnWidth(7, 22 * 256);
			sheet.setColumnWidth(25, 40 * 256);
			sheet.setColumnWidth(27, 0 * 256);
			// 插入logo图片
			new DataExportController().insetImages((String)map.get("imgPath"), workbook, sheet);
			// 设置整个表头的格式
			sheet.setDisplayGridlines(false);// 设置无边框
			XSSFCellStyle style = workbook.createCellStyle();
			new DataExportController().setBoderStyle(style);
			style.setFillForegroundColor(new XSSFColor(new Color(55, 96, 145))); //表头的颜色
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			XSSFFont font2 = workbook.createFont();
			font2.setFontName("宋体");
			font2.setFontHeightInPoints((short) 9);// 设置字体大小
			font2.setColor(HSSFColor.WHITE.index);// 字体颜色
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			style.setFont(font2);
			//设置汇总的格式
			XSSFCellStyle styleHuiZong = workbook.createCellStyle();
			XSSFFont fontHui = workbook.createFont();
			fontHui.setFontName("calibri");
			fontHui.setFontHeightInPoints((short) 9);// 设置字体大小
			//fontHui.setColor(HSSFColor.WHITE.index);// 字体颜色
			fontHui.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			styleHuiZong.setFont(fontHui);// 选择需要用到的字体格式
			new DataExportController().setBoderStyle(styleHuiZong);
			styleHuiZong.setFillForegroundColor(new XSSFColor(new Color(219, 229, 241)));//汇总 的背景色
            styleHuiZong.setFillPattern(CellStyle.SOLID_FOREGROUND);
            //设置中文字的格式为9号
            XSSFCellStyle styleRedNum = workbook.createCellStyle();
            //正常中文
            XSSFFont fontZhong = workbook.createFont();
            fontZhong.setFontName("宋体");
            fontZhong.setFontHeightInPoints((short) 9);// 设置字体大小
            //正常数字
            XSSFFont fontNum = workbook.createFont();
            fontNum.setFontName("calibri");
            fontNum.setFontHeightInPoints((short) 9);
            //红色数字
            XSSFFont fontNumRed = workbook.createFont();
            fontNumRed.setFontName("calibri");
            fontNumRed.setFontHeightInPoints((short) 9);
            fontNumRed.setColor(Font.COLOR_RED);
            styleRedNum.setFont(fontNumRed);
            styleRedNum.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
            
            XSSFCellStyle stylezw = workbook.createCellStyle();
            XSSFCellStyle stylejc = workbook.createCellStyle();
            XSSFCellStyle stylejcL = workbook.createCellStyle();
            XSSFCellStyle StyleBFB = workbook.createCellStyle();
            XSSFCellStyle Style = workbook.createCellStyle();
       
            stylezw.setFont(fontZhong);
     		new DataExportController().setBoderStyleL(stylezw);//修改当前样式的格式信息
     	    stylejc.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
     		stylejc.setFont(fontNum);
     		new DataExportController().setBoderStyleR(stylejc);//修改当前样式的格式信息
     		StyleBFB.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
     		StyleBFB.setFont(fontNum);
     		new DataExportController().setBoderStyleR(StyleBFB);//修改当前样式的格式信息
     		Style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##.00"));
     		Style.setFont(fontNum);
     		new DataExportController().setBoderStyleR(Style);//修改当前样式的格式信息
    	    new DataExportController().setBoderStyleR(styleRedNum);
    		stylejcL.setFont(fontNum);
    	    new DataExportController().setBoderStyleL(stylejcL);
        
            
            
			// 单独的把表头提出来
			List<String[]> l1 = new ArrayList<String[]>();
			l1 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
			int rowIndex = 7;// 起始行
			for (int i = 0; i < l1.size(); i++) {
				XSSFRow row = sheet.createRow(rowIndex++);
				row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
				String[] str = l1.get(i);
				for (int j = 0; j < str.length; j++) {
					XSSFCell cell = row.createCell(j + 1);
					cell.setCellValue(str[j]);
					cell.setCellStyle(style);
				}
			}
			
			List<String[]> l = new ArrayList<String[]>();
			l = (List<String[]>) map.get("list");// 获得list对象，并且添加到excel中
			List<String[]> hb2 = new ArrayList<String[]>();
            hb2 = (List<String[]>) map.get("infoHeBing");
			rowIndex = 9;// 起始行
			
			
			
			for(int i=0;i<l.size();i++){

	               XSSFRow row=sheet.createRow(rowIndex++);
	               row.setHeight((short) 495);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
	               String[] str = l.get(i);
	                   for(int j=0;j<str.length;j++){
	                	   XSSFCell cell=row.createCell(j+1);
	                	   //中文文字
                          if(str[0]!=null&&j==0||str[1]!=null&&j==1||str[2]!=null&&j==2||str[3]!=null&&j==3||str[4]!=null&&j==4||str[25]!=null&&j==25
                         		 ){
                        	         cell.setCellValue(str[j]); 
                        	         cell.setCellStyle(stylezw); 
                         }else if(str[5]!=null&&j==5||str[6]!=null&&j==6||str[7]!=null&&j==7||str[24]!=null&&j==24){
                         	cell.setCellValue(str[j]);
                         	cell.setCellStyle(stylejcL);
                         }else{//数字
                         	if(str[14]!=null&&j==14||str[20]!=null&&j==20||str[23]!=null&&j==23||str[22]!=null&&j==22){//百分数
                         		if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("")){
                         			if(str[14]!=null&&j==14&&(ctrRangM==""||ctrRangM==null||ctrRangL==""||ctrRangL==null)){
                         				cell.setCellStyle(stylejc);
                         			}else if(str[20]!=null&&j==20&&(jumpRang==""||jumpRang==null)){
                         				cell.setCellStyle(stylejc);
                         			}else if(str[22]!=null&&j==22&&(wcRangM==""||wcRangM==null||wcRangL==""||wcRangL==null)){
                         				cell.setCellStyle(stylejc);
                         			}else if(str[23]!=null&&j==23&&(djwcRangM==""||djwcRangM==null||djwcRangL==""||djwcRangL==null)){
                         				cell.setCellStyle(stylejc);
                         			}else{
                         				if(str[14]!=null&&j==14&&str[8].equals("N/A")){
                         					cell.setCellStyle(stylejc);
                         				}else if(str[22]!=null&&j==22&&str[8].equals("N/A")){
                         					cell.setCellStyle(stylejc);
                         				}else if(str[23]!=null&&j==23&&str[11].equals("N/A")){
                         					cell.setCellStyle(stylejc);
                         				}else{
                         				cell.setCellStyle(styleRedNum);
                         				}
                         			}
                   				   cell.setCellValue(str[j]);
                   				   }else{
                   					  if(str[14]!=null&&j==14){
                   						cell.setCellFormula("IFERROR(N"+(i+10)+"/K"+(i+10)+",\"N/A\")");
                   						 if(ctrRangM==""||ctrRangM==null||ctrRangL==""||ctrRangL==null){
                     							  cell.setCellStyle(StyleBFB); 
                   						}else{
                   						  if(Float.parseFloat(str[14])>=Float.parseFloat(ctrRangL) && Float.parseFloat(str[14])<=Float.parseFloat(ctrRangM) ){
                   							  cell.setCellStyle(StyleBFB); 
                   						  }else{
                   							  cell.setCellStyle(styleRedNum);
                   						  }
                   						}
                   					  }else if(str[20]!=null&&j==20){
                   						  //跳出率
                   						cell.setCellFormula("IFERROR(U"+(i+10)+"/Q"+(i+10)+",\"N/A\")");
                   						if(jumpRang==""||jumpRang==null){
               							  cell.setCellStyle(StyleBFB); 
                   						}else{
                   							if(Float.parseFloat(str[20])<=Float.parseFloat(jumpRang) ){
                   							cell.setCellStyle(StyleBFB);
                   							}else{
                 							  cell.setCellStyle(styleRedNum);
                   							}
                   						}
                   						//表示曝光完成率
                   					  }else if(str[22]!=null&&j==22){
                   						 Double result=0.0;
                   						 Double jieguo=0.0;
                   						  if("0".equals(str[25])){
                   						cell.setCellFormula("IFERROR(K"+(i+10)+"/J"+(i+10)+",\"N/A\")");
                   						if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
               							  cell.setCellStyle(StyleBFB); 
                   						}else{
                   						if(Float.parseFloat(str[22]) >=Float.parseFloat(wcRangL) && Float.parseFloat(str[22]) <=Float.parseFloat(wcRangM)){
                   							cell.setCellStyle(StyleBFB);
                   						}else{
                   							cell.setCellStyle(styleRedNum);
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
                   								sbfzS="K"+(izs+10);
                   								sbfmS="J"+(izsd+10);
                   								izs++;
                   								sbfz.append(sbfzS);
                   								if(q==Integer.parseInt(hb2.get(izsd)[1])-1){
                   								}else{
                   								sbfz.append("+");
                   								}
                 	            		   }
                 	            		   String sfz = sbfz.toString();
                 	            		  
                 	            		  for(int d=0;d<Integer.parseInt(hb2.get(izsd)[1]);d++){
                 	            			 if("N/A".equals(l.get(i+d)[8])){
                  	            				l.get(i+d)[8]="0";
                  	            			 }

                 	            			 result=result+ Double.parseDouble(l.get(i+d)[8]);
                 	            		  }
                 	            		 if(str[8].equals("N/A")||str[8].equals("")){
                 	            		 }else{
                 	            		  jieguo = 100*result/Double.parseDouble(str[8]);
                 	            		 }
                 	            		  
                 	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
                   						
                   						
                   						if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
                 							  cell.setCellStyle(StyleBFB); 
                     					}else{
                     						if(jieguo >=Float.parseFloat(wcRangL) && jieguo <=Float.parseFloat(wcRangM)){
                     							cell.setCellStyle(StyleBFB);
                     						}else{
                     							cell.setCellStyle(styleRedNum);
                     						}
                     					}
                   						}
                   						  
                   						  //点击完成率
                   					  }else if(str[23]!=null&&j==23){
                   						 Double result=0.0;
                   						 Double jieguo=0.0;
                   						  if("0".equals(str[26])){
                   						cell.setCellFormula("IFERROR(N"+(i+10)+"/M"+(i+10)+",\"N/A\")");
                   						if(djwcRangL==""||djwcRangL==null||djwcRangM==""||djwcRangM==null){
                 							  cell.setCellStyle(StyleBFB); 
                     						}else{
                     						  if(Float.parseFloat(str[23])>=Float.parseFloat(djwcRangL) && Float.parseFloat(str[23])<=Float.parseFloat(djwcRangM)){
                     							cell.setCellStyle(StyleBFB);
                     						  }else{
                       					  cell.setCellStyle(styleRedNum);
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
                      								sbfzS="N"+(izs+10);
                      								sbfmS="M"+(izsd+10);
                      								izs++;
                      								sbfz.append(sbfzS);
                      								if(q==Integer.parseInt(hb2.get(izsd)[1])-1){
                      								}else{
                      								sbfz.append("+");
                      								}
                    	            		   }
                    	            		   String sfz = sbfz.toString();
                    	            		   for(int d=0;d<Integer.parseInt(hb2.get(izsd)[1]);d++){
                    	            			   if(l.get(i+d)[12].equals("N/A")){//判断下等于N/A
                    	            			   }else{
                             	            	    result=result+ Double.parseDouble(l.get(i+d)[12]);
                    	            			   }
                       	            		  }
                    	            		   if(str[11].equals("N/A")||str[11].equals("")){
                    	            		   }else{
                       	            		  jieguo = 100*result/Double.parseDouble(str[11]);
                    	            		   }
                    	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
                   						
                   						if(djwcRangL==""||djwcRangL==null||djwcRangM==""||djwcRangM==null){
               							  cell.setCellStyle(StyleBFB); 
                   						}else{
                   						  if(jieguo>=Float.parseFloat(djwcRangL) && jieguo<=Float.parseFloat(djwcRangM)){
                   							cell.setCellStyle(StyleBFB);
                   						  }else{
                     					  cell.setCellStyle(styleRedNum);
                         				 }
                   						}
                   						}
                   					  }
                   					  
                   					   
                   				   }
                         		
                         	}else if(str[21]!=null&&j==21){//停留时间小数点
                         		if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("")){
                    				   cell.setCellValue(str[j]);
                    				   cell.setCellStyle(stylejc);
                    				   }else{
                    					  cell.setCellValue(Double.parseDouble(str[j])); 
                    					  cell.setCellStyle(Style); 
                    				   }
                         	}else{//一般数字
                         		if(str[j]==null){
                         			cell.setCellValue("N/A"); 
                         			cell.setCellStyle(stylejc);
                         		}else{
                         			if(str[j].equals("N/A")||str[j].equals("")){
	                    		    	cell.setCellValue(str[j]); 
	                    		    	cell.setCellStyle(stylejc);
	                    		    }else{
	                    		    	 cell.setCellValue(Double.parseDouble(str[j])); 
			                    		 cell.setCellStyle(stylejc); 
	                    		    }
                         			
                         		}
                         		
                         	}
                         	
                         	
                        	 }
                        	  
                           
	               }
			}
			rowIndex = 9;
			for(int i=0;i<hb2.size();i++){
					if(Integer.parseInt(hb2.get(i)[1])!=1){
						 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 5, 5));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 9, 9));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 12, 12));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 23, 23));
	            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 24, 24));
					
				}
					rowIndex++;
			}
			
//			if("1".equals(isHeBing)){
//			
//             for(int i=0;i<hb2.size();i++){
//            	 if(Integer.parseInt(hb2.get(i)[4])!=1){
//            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[4]), 4, 4));
//            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[4]), 8, 8));
//            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[4]), 11, 11));
//            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[4]), 22, 22));
//            		 sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[4]), 23, 23));
//            	 }
//            	 
//            	 
//             }
                
             	   
                
//			} 
			//英文字体
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 9); // 9号字体
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			//汉字9号字体
			CellStyle ceStyle = workbook.createCellStyle();
			XSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			fontStyle.setFontHeightInPoints((short) 9);// 设置字体大小
			ceStyle.setFont(fontStyle);

			XSSFRow row1 = sheet.createRow(2);// 需要新建表格，来获取当前的信息
			XSSFRow row2 = sheet.createRow(3);
			XSSFRow row3 = sheet.createRow(4);
			XSSFRow row4 = sheet.createRow(5);

			XSSFCell cell1 = row1.createCell(1);
			XSSFCell cell2 = row2.createCell(1);
			XSSFCell cell3 = row3.createCell(1);
			XSSFCell cell7 = row4.createCell(1);
			XSSFCell cell4 = row3.createCell(2);
			XSSFCell cell5 = row2.createCell(2);
			XSSFCell cell6 = row1.createCell(2);
			XSSFCell cell8 = row4.createCell(2);
			
			cell1.setCellValue("项目名称:");
			cell1.setCellStyle(cellStyle);
			cell2.setCellValue("报表周期:");
			cell2.setCellStyle(cellStyle);
			cell3.setCellValue("交付日期:");
			cell3.setCellStyle(cellStyle);
			cell7.setCellValue("交付人:");
			cell7.setCellStyle(cellStyle);
			cell4.setCellValue(sdf.format(new Date()));
			cell4.setCellStyle(styler);
			cell5.setCellValue( (String)map.get("dt"));// 获得填写的日期信息
			cell5.setCellStyle(styler);
			cell6.setCellValue((String) map.get("custName"));
			cell6.setCellStyle(ceStyle);
			cell8.setCellValue(userRealName);
			cell8.setCellStyle(ceStyle);
			//设置表头的合并单元格信息
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 9, 15));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 16, 22));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 23, 24));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 6, 6));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 7, 7));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 8, 8));

			sheet.addMergedRegion(new CellRangeAddress(7, 8, 25, 25));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 26, 26));

			FileOutputStream fout = new FileOutputStream(outputFile);
			workbook.write(fout);
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param obj
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unused")
	public ModelAndView LoadExcel(HttpServletRequest request, HttpServletResponse response, String obj) throws IOException {
		String path = obj.toString();
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			
			String agent = request.getHeader("USER-AGENT").toLowerCase();
		    //根据浏览器类型处理文件名称
		    if(agent.indexOf("msie")>-1){
		      //extfilename = Tools.toUtf8String(extfilename);
		    	filename = java.net.URLEncoder.encode(filename, "UTF-8");
		    }
		    else{  //firefox/safari不转码
		    	filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		    }
		    response.setContentType("application/msexcel");
		    // 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + toUtf8String(filename));
			//System.out.println("==========="+toUtf8String(filename));
			//System.out.println("++++++"+filename);
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception ex) {
			//将错误信息发送到页面
			response.getWriter().print("error");
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	/*********************************** 分割线 ****************************************************/
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
//			response.getWriter().print(user.getROLE_ID());
			map.put("userId", user.getROLE_ID());  //得到当前用户的角色ID和所对应的客户ID，与页面的客户ID匹配，对应不同的操作
			map.put("customerId",user.getCustomerId());
			String msg=JSONUtil.beanToJson(map);
			response.setContentType("text/html;charset=UTF-8");
		    //设置不使用缓存   
		    response.setHeader("Cache-Control","no-cache"); 
		    response.getWriter().write(msg);
			
		} catch (Exception e) {
			response.getWriter().print("error");
		}
	}
	
	
	/**
	 * 得到一个图片，插入到excel中
	 * @param ImgPath  //传入的图片路径
	 * @param workbook //所在的工作簿对象
	 * @param sheet //工作簿的第几个sheet页
	 */
	public void insetImages(String ImgPath, XSSFWorkbook workbook, XSSFSheet sheet) {
		try {
			InputStream is = new FileInputStream(ImgPath);
			byte[] bytes = IOUtils.toByteArray(is);

			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

			CreationHelper helper = workbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();

			// 图片插入坐标
			anchor.setRow1(1); // 第一行
			anchor.setCol1(21); // 第21列

			// 插入图片
			Picture pict = drawing.createPicture(anchor, pictureIdx);
			pict.resize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 1.header头信息样式
	 * @param style
	 * @param workbook
	 */
	public void setHeaderStyle(XSSFCellStyle style, Workbook workbook, XSSFRow row) {
		style = (XSSFCellStyle) workbook.createCellStyle();
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		// 设置背景颜色
		style.setFillForegroundColor(new XSSFColor(new Color(55, 96, 145))); // 前景色为深蓝色
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 生成一个字体
		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setFontHeightInPoints((short) 9); // 9号字体
		font.setFontName("宋体");
		font.setColor(new XSSFColor(new Color(255, 255, 255))); // 字体颜色为白色
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		style.setFont(font);
		for (int i = 1; i <= 5; i++) {
			row.getCell(i).setCellStyle(style);
		}
		row.getCell(6).setCellStyle(style);
		row.getCell(11).setCellStyle(style);
	}

	public void setBoderStyle(XSSFCellStyle style){
		//设置边框样式
	     style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	     style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	     //设置边框颜色
	     style.setTopBorderColor(HSSFColor.BLACK.index);
	     style.setBottomBorderColor(HSSFColor.BLACK.index);
	     style.setLeftBorderColor(HSSFColor.BLACK.index);
	     style.setRightBorderColor(HSSFColor.BLACK.index);
	     //设置位置居中
	     style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
	     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
	}
	public void setBoderStyleL(XSSFCellStyle style){
		//设置边框样式
	     style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	     style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	     //设置边框颜色
	     style.setTopBorderColor(HSSFColor.BLACK.index);
	     style.setBottomBorderColor(HSSFColor.BLACK.index);
	     style.setLeftBorderColor(HSSFColor.BLACK.index);
	     style.setRightBorderColor(HSSFColor.BLACK.index);
	     //设置位置左对齐
	     style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左对齐
	     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
	}
	public void setBoderStyleR(XSSFCellStyle style){
		//设置边框样式
	     style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	     style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	     //设置边框颜色
	     style.setTopBorderColor(HSSFColor.BLACK.index);
	     style.setBottomBorderColor(HSSFColor.BLACK.index);
	     style.setLeftBorderColor(HSSFColor.BLACK.index);
	     style.setRightBorderColor(HSSFColor.BLACK.index);
	     //设置位置右对齐
	     style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 右对齐
	     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
	}
	
	//获得系统的前一天
		 public static Date getNextDay(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			date = calendar.getTime();
			return date;
		}
		 
		 /**
		     * 凌晨
		     * @param date
		     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
		     *       1 返回yyyy-MM-dd 23:59:59日期
		     * @return
		     */
		    public static Date weeHours(Date date, int flag) {
		        Calendar cal = Calendar.getInstance();
		        cal.setTime(date);
		        cal.add(Calendar.DAY_OF_MONTH, -1);//取得是前一天的数据
		        int hour = cal.get(Calendar.HOUR_OF_DAY);
		        int minute = cal.get(Calendar.MINUTE);
		        int second = cal.get(Calendar.SECOND);
		        //时分秒（毫秒数）
		        long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
		        //凌晨00:00:00
		        cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);
		         
		        if (flag == 0) {
		            return cal.getTime();
		        } else if (flag == 1) {
		            //凌晨23:59:59
		            cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);
		        }
		        return cal.getTime();
		    }
		    
	/**
	 * 短代码是否有效的check
	 * 
	 * @param dataCaliberMic
	 */
	@ResponseBody
	@RequestMapping(value = "/dataCaliberMicCheck.do", method = RequestMethod.POST)
	public String dataCaliberMicCheck(@RequestParam(value = "dataCaliberMic", required = true) String dataCaliberMic) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] dataCaliberMicList = dataCaliberMic.split("\n");
		map.put("dataCaliberMic", dataCaliberMicList);
		String mic = "";
		for (int i = 0; i < dataCaliberMicList.length; i++) {
			// 逐条判断是否是有效的短代码
			Integer count = exportService.checkMic(dataCaliberMicList[i].trim());
			if (count == 0) {
				// 返回无效的短代码
				mic += dataCaliberMicList[i].trim() + ",";
			}
		}
		// 删除最后的逗号
		if (mic != "") {
			mic = mic.substring(0, mic.length() - 1);
		}
		return mic;
	}
		    
	/**
	* 更新曝光口径和点击口径
	* 
	* @param dataCaliberMic
	* @param impDataCaliber
	* @param clkDataCaliber
	* @param request
	* @return
	*/
	@ResponseBody
	@RequestMapping(value="updateDataCaliber.do",method = RequestMethod.POST)
	public Integer updateDataCaliber(@RequestParam(value = "dataCaliberMic", required = true) String dataCaliberMic,
			@RequestParam(value = "impDataCaliber", required = true) String impDataCaliber,
			@RequestParam(value = "clkDataCaliber", required = true) String clkDataCaliber,
			@RequestParam(value = "micStatMark", required = true) String micStatMark,
			HttpServletRequest request){
		// 获取用户名
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String userId = user.getUSER_ID()+"";
		String[] dataCaliberMicList = dataCaliberMic.split("\n");
		List<TbAmpBasicScheduleInfo> list = new ArrayList<>();
		for (int i = 0; i < dataCaliberMicList.length; i++) {
			TbAmpBasicScheduleInfo info = new TbAmpBasicScheduleInfo();
			info.setMic(dataCaliberMicList[i].trim());
			info.setImpDataCaliber(impDataCaliber);
			info.setClkDataCaliber(clkDataCaliber);
			info.setMicStatMark(micStatMark);
			info.setUpdateUser(userId);
			list.add(info);
		}
		try {
			exportService.updateDataCaliber(list);
			// 更新成功
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			// 更新失败
			return 0;
		}
	}
}
