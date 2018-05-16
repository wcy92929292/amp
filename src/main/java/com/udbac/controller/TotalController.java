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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.udbac.entity.DataExport;
import com.udbac.entity.WeekDataExport;
import com.udbac.model.UserBean;
import com.udbac.service.MonthReportService;
import com.udbac.service.TotalDayReportService;
import com.udbac.service.TotalService;
import com.udbac.service.WeekReportService;
import com.udbac.util.DataEndUntil;
import com.udbac.util.FilePathManager;
import com.udbac.util.WeekUtil;

/**
 * 累计数据报表
 * @author wangli
 *
 */
@Controller
@RequestMapping("/total")
public class TotalController {
	
	@Autowired
	private FilePathManager filePathManager;
	
	@Autowired
	private TotalService totalService;
	
	@Autowired
	private TotalDayReportService totalDayReportService;
	
	public static String MonthLogo;
	
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 
	 * @param date
	 * @param response
	 * @param request
	 * @param actCode
	 * @param customer_id
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="sumTotal.do",method = RequestMethod.POST)
	public String sumName(@RequestParam(value = "date", required = true) String date,
			HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "actCode", required = true) String actCode,
			@RequestParam(value = "customer_id", required = true) String customer_id) throws IOException{
		    Date dt = null;
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					dt = sdf.parse(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		//数据条数为0的时候
		   Integer sumName=totalService.sumDay(dt,customer_id,actCode);
		    String falg="";
		    if(sumName>0){
		    	falg="1";
			   }else{
				falg="2";
			   }
		    return falg;
	}
	
	   @ResponseBody
	   @RequestMapping(value = "/totalExport.do", method = RequestMethod.POST)
	   @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	   public ModelAndView listEndInfo(HttpServletResponse response, HttpServletRequest request,
			   @RequestParam(value = "date", required = true) String date,
			   @RequestParam(value = "actCode", required = true) String actCode,
			   @RequestParam(value = "customerName", required = true) String customerName,
			   @RequestParam(value = "customer_id", required = true) String customer_id,
			   @RequestParam(value = "jumpRang", required = false) String jumpRang,
			   @RequestParam(value = "ctrRangL", required = false) String ctrRangL,
			   @RequestParam(value = "ctrRangM", required = false) String ctrRangM,
			   @RequestParam(value = "wcRangL", required = false) String wcRangL,
			   @RequestParam(value = "wcRangM", required = false) String wcRangM,
			   @RequestParam(value = "djwcRangL", required = false) String djwcRangL,
			   @RequestParam(value = "djwcRangM", required = false) String djwcRangM
			    ) throws IOException, ParseException {
			String customerNames = new String(customerName.getBytes("iso-8859-1"), "utf-8");// 解决乱码问题
			String custName = customerNames ;
			//获得图片的路径
			// 取得session中的user对象
			HttpSession session = request.getSession();
			UserBean user = (UserBean) session.getAttribute("user");
			
			//获得图片的路径
			String imgPath = session.getServletContext().getRealPath("/") + "images/logo.png";
			Date dt = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String smonth = date.substring(0, 4) + "-" + date.substring(5, 7) + "-" + date.substring(8, 10);
			
			String entry=custName+ "累计数据报表_按周_"+smonth;//存放excel名称的
			String path = filePathManager.getTmpExcel() + entry + ".xlsx";// 临时的文件目录

			try {
				dt=sdf.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List list1 = new ArrayList();
			List listAddMergin = new ArrayList();
			List listAddMergin2 = new ArrayList();
			List listAddMergin3 = new ArrayList();
			ArrayList<WeekDataExport> infos = new ArrayList();  //原结果过滤后
//			ArrayList<WeekDataExport> sumInfo = new ArrayList();  //原结果过滤后
			List hb = new ArrayList(); //（过滤后）包含四个要合并的条件的项 + 每项行数
			List hb3 = new ArrayList(); //（过滤后）包含四个要合并的条件的项 + 每项行数
			 int count=1;//合并的分组的长度
			Map map = new HashMap();
			
			String[] header = { "项目名称", "媒体名称", "终端类型","投放形式","广告位","广告前端曝光点击监测","", "","", "", "", "","","","活动网站监测",
					"", "", "", "", "","","完成率情况", "" ,"URL","备注"};
			String[] headers = { "", "","","","","营销识别码","日期","曝光预估", "曝光次数", "曝光人数", "点击预估","点击次数", "点击人数", "点击率CTR", "访问次数", "访问人数",
					" 到站点击 ", "浏览量","跳出次数", "跳出率", "平均访问时长(s)", "曝光完成率", "点击完成率" ,"",""};
			list1.add(header);
			list1.add(headers);
			//第一张sheet表
			List<WeekDataExport> infojichu = totalService.listTotalInfo(dt,customer_id,actCode);	
			//第二张sheet表
			List<WeekDataExport> info = totalService.listMediaInfo(dt,customer_id,actCode);	
			
			//第三张sheet表
			List<WeekDataExport> sumInfo = totalService.listHuiZongInfo(dt,customer_id,actCode);
			/**
			 * 汇总的是媒体名称
			 */
			    String hzmc="";//汇总名称
			    String hzggw="";//汇总的广告位
			    String mic = "";
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
			    String actiCode="";//活动编号
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
			    double sumYuGu=0;//曝光预估的汇总的值(第三张表中表示的是分媒体的汇总数据)
				double sumDiJi=0;//曝光预估的汇总的值(第三张表中表示的是分媒体的汇总数据)
				
				  double _sumYuGu=0;//曝光预估的汇总的值(第三张表中表示的是分项目名称的数据)
					double _sumDiJi=0;//曝光预估的汇总的值(第三张表中表示的是分项目名称的数据)
			    
			/***************************************基础数据**********************************************/
			for (int i = 0; i < infojichu.size(); i++) {  //只有基础信息 第一个sheet页
				WeekDataExport de = infojichu.get(i);
				
				if(de.getImpPv()==0&&de.getImpUv()==0&&de.getClkPv()==0&&de.getClkUv()==0&&de.getVisit()==-1&&de.getVisitor()==-1&&de.getPageView()==-1&&de.getBounceTimes()==-1){
					//去掉前后端数据为空的数据
				}else{
					infos.add(de);
					if (de.getUnit().toUpperCase().equals("CPM")) {
						// 为CPM
						listAddMergin.add(new DataEndUntil().checkOutTotal(de, true, 1,0,0));
					} else {
						// 非CPM的
						listAddMergin.add(new DataEndUntil().checkOutTotal(de, false, 1,0,0));
					}
				}
			}
			for(int i =0;i<infos.size();i=i+count){
				WeekDataExport de = (WeekDataExport) infos.get(i);
				int k =i;
				count = 1;
				if(((WeekDataExport) infos.get(i)).getGroup_id()==0){
					String[] checkout2 = {de.getGroup_id()+"", count+""};
					hb.add(checkout2);
					continue;
				}else{
				for(int j=k;(j!=infos.size()-1) && ((WeekDataExport) infos.get(j)).getGroup_id()==((WeekDataExport) infos.get(j+1)).getGroup_id()
						&&((WeekDataExport) infos.get(j)).getPutDate().compareTo(((WeekDataExport) infos.get(j)).getPutDate())==0 ;j++){
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
			    map.put("hb", hb);//将合并信息存放到map中
			/***************************************分媒体数据**********************************************/
			if(info.size()==1){//查询数据==1条   第二个sheet页
		    	WeekDataExport de=info.get(0);
		    	//投放单位为cpm的时候，曝光预估=日历表里的投放量*1000；其他情况直接取排期点位表里的日均报告预估。当分母为0时，比率表示为"-"
		    	if(de.getUnit().toUpperCase().equals("CPM")){
		    		//明细CPM
		    		 unit=true;
					 listAddMergin2.add(new DataEndUntil().checkOutTotal(de,unit,1,0,0));
					 listAddMergin2.add(new DataEndUntil().checkOutTotal(de,unit,2,0,0));
		    	}else{
		    		 unit=false;
		    		 //明细
		    		 listAddMergin2.add(new DataEndUntil().checkOutTotal(de,unit,1,0,0));
		    		 listAddMergin2.add(new DataEndUntil().checkOutTotal(de,unit,2,0,0));
		    	}
		    	
		    }else{//查询数据大于1条时
		    	for (int i = 0; i < info.size(); i++) {
				WeekDataExport de = info.get(i);
				/*if(de.getImpPv()==0&&de.getImpUv()==0&&de.getClkPv()==0&&de.getClkUv()==0&&de.getVisit()==-1&&de.getVisitor()==-1&&de.getPageView()==-1&&de.getBounceTimes()==-1){
					}else{*/
				//投放单位为cpm的时候，曝光预估=日历表里的投放量*1000；其他情况直接取排期点位表里的日均报告预估。当分母为0时，比率表示为"-"
					    if(de.getUnit().toUpperCase().equals("CPM")){
					    	unit=true;//标记位投放单位
					    	listAddMergin2.add(new DataEndUntil().checkOutTotal(de,unit,1,0,0));
					    }else{
					    	unit=false;
					    	listAddMergin2.add(new DataEndUntil().checkOutTotal(de,unit,1,0,0));
					    }
					    String arr[]=new DataEndUntil().checkOutTotal(de,unit,1,0,0);
					  
				    if(i<info.size()-1){//1至n-1个元素
				    	 hzmc=de.getMediaName();
				    	 actiCode=de.getActivityCode();
				    	 hzggw=de.getPointLocation();
				    	 mic = de.getMic();
				    	 bgyg += de.getExposureAvg();
				    	 djyg+=de.getClickAvg();
				    	 //相邻的两个数字相等的话
				    		 bgcs += de.getImpPv(); 
					    	 bgrs += de.getImpUv();
				    		 djcs += de.getClkPv();
				    		 djrs += de.getClkUv(); 
				    	 
				    	 String arrs[]=new DataEndUntil().checkOutTotal(de,unit,1,0,0);
				   	    	if(arr[7].equals("N/A")){
			  		    	 }else{
			  		    		sumYuGu+= Double.parseDouble(arr[7].replaceAll(",", "")); 
			  		    	   }
			   	    	    if(arr[10].equals("N/A")){
		 		    	    }else{
		 		    		sumDiJi+= Double.parseDouble(arr[10].replaceAll(",", "")); 
		  	    	         }
				   
	 		    		 //后端数据表字段没有默认值
	 		    		 if(de.getVisit()==-1){
	 		    			fscs+=0;
	 		    		 }else{
				    		fscs+=de.getVisit();
	 		    		 }
	 		    		 if(de.getVisitor()==-1){
	 		    			fwrs+=0; 
	 		    		 }else{
				    		fwrs+=de.getVisitor(); 
	 		    		 }
	 		    		 if(de.getClick()==-1){
	 		    			clk+=0;
	 		    		 }else{
	 		    			clk += de.getClick(); 
	 		    		 }
			    		 if(de.getPageView()==-1){
			    			 lll+=0; 
			    		 }else{
			    			 lll+=de.getPageView(); 
			    		 }
			    		 if(de.getBounceTimes()==-1){
			    			 tccs+=0; 
			    		 }else{
				    		 tccs+=de.getBounceTimes();
			    		 }
			    		 if(de.getActivityCode().indexOf("JT")!=-1&&de.getActivityCode().indexOf("MGJT")==-1){
			    			 if(de.getViewTime()==-1){
				    			 pjfwsc+=0; 
				    		 }else{
				    			 pjfwsc+=de.getViewTime()*de.getVisit(); 
				    		 }  
			    			 
			    		 }else{
			    			 if(de.getViewTime()==-1){
				    			 pjfwsc+=0; 
				    		 }else{
				    			 pjfwsc+=de.getViewTime(); 
				    		 } 
			    		 }
			    		
			    		 url=de.getUrlPc();
			    		 tfl+= de.getPut_value();
				    	 tfrq=sdf.format(de.getPutDate());
				    	 if(de.getMediaName().equals(info.get(i+1).getMediaName())){
				    			 fzggw++;
				    	 }else{
			    			 WeekDataExport des=new WeekDataExport();
			    			    des.setActivityCode(actiCode);
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
					    		if(actiCode.indexOf("JT")!=-1&&actiCode.indexOf("MGJT")==-1){
					    			if(fscs==0){
					    				 des.setViewTime((double) -1);
					    			}else{
					    				des.setViewTime(pjfwsc/fscs);	
					    			}
					    	    
					    		}else{
					    		des.setViewTime(pjfwsc/fzggw);	
					    		}
					    		des.setUrlPc(url);
					    		des.setPut_value(tfl);
						    	des.setPutDate(sdf.parse(tfrq));
					    		//媒体名称相等
					    		listAddMergin2.add(new DataEndUntil().checkOutTotal(des,unit,2,sumYuGu,sumDiJi));
                                 actiCode="";
						    	 fzcd=1;	
						    	 hzmc="";
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
					    		 sumYuGu=0;
					    		 sumDiJi=0;
				    		  }
				    	}else{//最后一条数据
				    		 String arrs[]=new DataEndUntil().checkOutTotal(de,unit,1,0,0);
					   	    	if(arr[7].equals("N/A")){
				  		    	 }else{
				  		    		sumYuGu+= Double.parseDouble(arr[7].replaceAll(",", "")); 
				  		    	   }
				   	    	    if(arr[10].equals("N/A")){
			 		    	    }else{
			 		    		sumDiJi+= Double.parseDouble(arr[10].replaceAll(",", "")); 
			  	    	         }
				    		actiCode=de.getActivityCode();
				    		 hzmc=de.getMediaName();
					    	 hzggw=de.getPointLocation();
					    	 mic = de.getMic();
					    	 bgyg += de.getExposureAvg();
					    	 bgcs += de.getImpPv();
					    	 bgrs += de.getImpUv();
					    	 djcs += de.getClkPv();
					    	 djrs += de.getClkUv();
				    		 fscs+=de.getVisit();
				    		 fwrs+=de.getVisitor();
				    		 clk += de.getClick();
				    		 lll+=de.getPageView();
				    		 tccs+=de.getBounceTimes();
				    		 if(de.getActivityCode().indexOf("JT")!=-1&&de.getActivityCode().indexOf("MGJT")==-1){
				    			 if(de.getViewTime()==-1){
					    			 pjfwsc+=0; 
					    		 }else{
					    			 pjfwsc+=de.getViewTime()*de.getVisit(); 
					    		 }  
				    		 }else{
				    			 if(de.getViewTime()==-1){
					    			 pjfwsc+=0; 
					    		 }else{
					    			 pjfwsc+=de.getViewTime(); 
					    		 } 
				    		 }
				    		 tfl+=de.getPut_value();
				    		 url=de.getUrlPc();
					    	 tfrq=sdf.format(de.getPutDate());
					    	 
					    	WeekDataExport des=new WeekDataExport();
					    	 des.setActivityCode(actiCode);
					    	 des.setPointLocation(hzggw);
					    	 des.setMediaName(hzmc);
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
				    		 if(actiCode.indexOf("JT")!=-1&&actiCode.indexOf("MGJT")==-1){
					    			 if(fscs==0){
					    				 des.setViewTime((double) -1);
					    			}else{
					    				des.setViewTime(pjfwsc/fscs);	
					    			}	
						    	}else{
						    	    des.setViewTime(pjfwsc/fzggw);	
						    		}
				    		 des.setUrlPc(url);
				    		 des.setPut_value(tfl);
					    	 des.setPutDate(sdf.parse(tfrq));
					    	listAddMergin2.add(new DataEndUntil().checkOutTotal(des,unit,2,sumYuGu,sumDiJi));
					    	 actiCode="";
					    	 hzmc="";
					    	 fzcd=1;
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
				    		 sumYuGu=0;
				    		 sumDiJi=0;
				    }
				 
				}
		    }
/**********************************************第三张sheet表部分********************************************************************/
			    
			if(sumInfo.size()==1){//查询数据==1条   第三个sheet页
		    	WeekDataExport des=sumInfo.get(0);
		    	//投放单位为cpm的时候，曝光预估=日历表里的投放量*1000；其他情况直接取排期点位表里的日均报告预估。当分母为0时，比率表示为"-"
		    	if(des.getUnit().toUpperCase().equals("CPM")){
		    		//明细CPM
		    		 unit=true;
					 listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,1,0,0));
					 listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,2,0,0));
					 listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,3,0,0));
		    	}else{
		    		 unit=false;
		    		 //明细
		    		 listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,1,0,0));
		    		 listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,2,0,0));
		    		 listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,3,0,0));
		    	}
		    }else{//查询数据大于1条时
			    for (int i = 0; i < sumInfo.size(); i++) {
					WeekDataExport de = sumInfo.get(i);
				if(i>0){
				if(sumInfo.get(i).getActivityName().equals(sumInfo.get(i-1).getActivityName())){	//上一个对象和当前对象 项目名称相同
		    		 fzcd++;
	    		 if(sumInfo.get(i).getMediaName().equals(sumInfo.get(i-1).getMediaName())){  //媒体名称相同
	    			 fzggw++;
		    		 }
				}
				}
				/*if(de.getImpPv()==0&&de.getImpUv()==0&&de.getClkPv()==0&&de.getClkUv()==0&&de.getVisit()==-1&&de.getVisitor()==-1&&de.getPageView()==-1&&de.getBounceTimes()==-1){
					}else{*/
						//投放单位为cpm的时候，曝光预估=日历表里的投放量*1000；其他情况直接取排期点位表里的日均报告预估。当分母为0时，比率表示为"-"
					    if(de.getUnit().toUpperCase().equals("CPM")){
					    	unit=true;//标记位投放单位
					    	listAddMergin3.add(new DataEndUntil().checkOutTotal(de,unit,4,0,0));
					    }else{
					    	unit=false;
					    	listAddMergin3.add(new DataEndUntil().checkOutTotal(de,unit,4,0,0));
					    }
					   
				    if(i<sumInfo.size()-1){//1至n-1个元素
				    	 actiCode=de.getActivityCode();
				    	 hzmc=de.getMediaName();
				    	 hzggw=de.getActivityName();
				    	 mic = de.getMic();
				    	 bgyg += de.getExposureAvg();
				    	 djyg+=de.getClickAvg();
				    	 bgcs+=de.getImpPv();
				    	 bgrs+=de.getImpUv();
	 		    		 djcs+=de.getClkPv();
	 		    		 djrs+=de.getClkUv();
	 		    		 //后端数据表字段没有默认值
	 		    		 if(de.getVisit()<=-1){
	 		    			fscs+=0;
	 		    		 }else{
				    		fscs+=de.getVisit();
	 		    		 }
	 		    		 if(de.getVisitor()<=-1){
	 		    			fwrs+=0; 
	 		    		 }else{
				    		fwrs+=de.getVisitor(); 
	 		    		 }
	 		    		 if(de.getClick()<=-1){
	 		    			clk+=0;
	 		    		 }else{
	 		    			clk += de.getClick(); 
	 		    		 }
			    		 if(de.getPageView()<=-1){
			    			 lll+=0; 
			    		 }else{
			    			 lll+=de.getPageView(); 
			    		 }
			    		 if(de.getBounceTimes()<=-1){
			    			 tccs+=0; 
			    		 }else{
				    		 tccs+=de.getBounceTimes();
			    		 }
			    		 
			    		 if(de.getActivityCode().indexOf("JT")!=-1&&de.getActivityCode().indexOf("MGJT")==-1){
			    			 if(de.getViewTime()<=-1){
				    			 pjfwsc+=0; 
				    		 }else{
				    			 pjfwsc+=de.getViewTime()*de.getVisit(); 
				    		  } 
			    		 }else{
			    			 if(de.getViewTime()<=-1){
				    			 pjfwsc+=0; 
				    		 }else{
				    			 pjfwsc+=de.getViewTime(); 
				    		  }  
			    		 }
			    		 url=de.getUrlPc();
			    		 tfl+= de.getPut_value();
			    		 if(sumInfo.get(i).getPutDate()==null){
			    		 }else{
			    			 tfrq=sdf.format(de.getPutDate()); 
			    		 }
				    	     _actiCode=sumInfo.get(i).getActivityCode();
			    			 _bgcs+= sumInfo.get(i).getImpPv();
				    		 _bgrs+= sumInfo.get(i).getImpUv();
				    	 if(sumInfo.get(i).getExposureAvg() <= -1){
				    	 }else{
				    		 _bgyg+= sumInfo.get(i).getExposureAvg();
				    	 }
				    	 if(sumInfo.get(i).getClickAvg() <= -1){
				    	 }else{
				    		 _djyg+=sumInfo.get(i).getClickAvg();
				    	 }
				    		 _djcs+=sumInfo.get(i).getClkPv();
		    				 _djrs+=sumInfo.get(i).getClkUv();
		    			 
			    		 if(sumInfo.get(i).getVisit() <= -1){
			    			 _fscs+=0;
			    		 }else{
			    			 _fscs+=sumInfo.get(i).getVisit();
			    		 }
			    		 if(sumInfo.get(i).getVisitor() <= -1){
			    			 _fwrs+=0; 
			    		 }else{
			    			 _fwrs+=sumInfo.get(i).getVisitor();
			    		 }
			    		 if(sumInfo.get(i).getClick() <= -1){
			    			 _clk+=0;
			    		 }else{
			    			 _clk+=sumInfo.get(i).getClick();
			    		 }
			    		 if(sumInfo.get(i).getPageView() <= -1){
			    			 _lll+=0;
			    		 }else{
			    			 _lll+=sumInfo.get(i).getPageView();
			    		 }
			    		 if(sumInfo.get(i).getBounceTimes() <= -1){
			    			 _tccs+=0;
			    		 }else{
			    			 _tccs+=sumInfo.get(i).getBounceTimes();
			    		 }
			    		 //平均访问时间
			    		 if(de.getActivityCode().indexOf("JT")!=-1&&de.getActivityCode().indexOf("MGJT")==-1){
			    			 if(de.getViewTime()<=-1){
				    			 _pjfwsc+=0; 
				    		 }else{
				    			 _pjfwsc+=de.getViewTime()*de.getVisit(); 
				    		  } 
			    		 }else{
			    			 if(de.getViewTime()<=-1){
				    			 _pjfwsc+=0; 
				    		 }else{
				    			 _pjfwsc+=de.getViewTime(); 
				    		  }  
			    		 }
	
			    		 if(sumInfo.get(i).getPut_value() == -1){
			    			 _tfl+=0; 
			    		 }else{
			    			 _tfl+=sumInfo.get(i).getPut_value();
			    		 }
			    		 
			    		String arrs[]=new DataEndUntil().checkOutTotal(de,unit,1,0,0);
				   	    	if(arrs[7].equals("N/A")){
			  		    	 }else{
			  		    		sumYuGu+= Double.parseDouble(arrs[7].replaceAll(",", "")); 
			  		    	   }
			   	    	    if(arrs[10].equals("N/A")){
		 		    	    }else{
		 		    		sumDiJi+= Double.parseDouble(arrs[10].replaceAll(",", "")); 
		  	    	         }
			   	    	    
			   	    		String arry[]=new DataEndUntil().checkOutTotal(de,unit,1,0,0);
				   	    	if(arry[7].equals("N/A")){
			  		    	 }else{
			  		    		_sumYuGu+= Double.parseDouble(arry[7].replaceAll(",", "")); 
			  		    	   }
			   	    	    if(arry[10].equals("N/A")){
		 		    	    }else{
		 		    		_sumDiJi+= Double.parseDouble(arry[10].replaceAll(",", "")); 
		  	    	         }  
			     		if(sumInfo.get(i).getActivityName().equals(sumInfo.get(i+1).getActivityName())){	//上一个对象和当前对象 项目名称相同
				    		 fzcd++;
			    		 if(sumInfo.get(i).getMediaName().equals(sumInfo.get(i+1).getMediaName())){  //媒体名称相同
			    			 fzggw++;
				    		 }else{
				    			 WeekDataExport des=new WeekDataExport();
				    			    des.setActivityCode(actiCode);
						    		des.setMediaName(hzmc);
						    		des.setActivityName(hzggw);
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
						    		if(actiCode.indexOf("JT")!=-1&&actiCode.indexOf("MGJT")==-1){
						    			 if(fscs==0){
						    				 des.setViewTime((double) -1);
						    			}else{
						    				des.setViewTime(pjfwsc/fscs);	
						    			}	
						    		}else{
						    			des.setViewTime(pjfwsc/fzggw);
						    		}
						    		des.setUrlPc(url);
						    		des.setPut_value(tfl);
							    	listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,2,sumYuGu,sumDiJi));
							    	     actiCode="";
								    	 hzmc="";
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
							    		 mic = "";
							    		 fzggw=1;
							    		 sumDiJi=0;
							    		 sumYuGu=0;
				    		 }
				    	}else{
				    		 WeekDataExport des=new WeekDataExport();
				    		    des.setActivityCode(actiCode);
					    		des.setMediaName(hzmc);
					    		des.setActivityName(hzggw);
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
					    		 if(actiCode.indexOf("JT")!=-1&&actiCode.indexOf("MGJT")==-1){
					    			 if(fscs==0){
					    				 des.setViewTime((double) -1);
					    			}else{
					    				des.setViewTime(pjfwsc/fscs);	
					    			}	
						    		}else{
						    		des.setViewTime(pjfwsc/fzggw);
						    		}
					    		des.setUrlPc(url);
					    		des.setPut_value(tfl);
					    	
						    	listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,2,0,0));
						    	 WeekDataExport desum=new WeekDataExport();
						    	 desum.setMediaName(hzmc);
						    	 desum.setActivityName(hzggw);
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
						    	 if(_actiCode.indexOf("JT")!=-1&&_actiCode.indexOf("MGJT")==-1){
							    		 if(_fscs==0){
							    			 desum.setViewTime((double) -1);
						    			}else{
						    				desum.setViewTime(_pjfwsc/_fscs);	
						    			}	
						    		}else{
						    		 desum.setViewTime(_pjfwsc/fzcd);
						    		}
						    	 desum.setUrlPc(url);
						    	 desum.setPut_value(_tfl);
					    		listAddMergin3.add(new DataEndUntil().checkOutTotal(desum,unit,3,_sumYuGu,_sumDiJi));// 分完媒体再分项目汇总
					    		fzcd=1;	
						    	 hzmc="";
						    	 actiCode="";
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
					    		 mic = "";
					    		 fzggw=1;
					    		 _actiCode="";
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
						    
			    	 }else{
				    		    hzmc=sumInfo.get(i).getMediaName();
				    		     actiCode=sumInfo.get(i).getActivityCode();
						    	 hzggw=sumInfo.get(i).getActivityName();
						    	 mic = sumInfo.get(i).getMic();
						    	 if(sumInfo.get(i).getImpPv() == -1){
					    		 }else{
					    			 bgcs+= sumInfo.get(i).getImpPv();
					    		 }
						    	 if(sumInfo.get(i).getImpUv() == -1){
						    	 }else{
						    		 bgrs+= sumInfo.get(i).getImpUv();
						    	 }
						    	 if(sumInfo.get(i).getExposureAvg() < 0|| sumInfo.get(i).getExposureAvg() == null){
						    	 }else{
						    		 bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
						    	 if(sumInfo.get(i).getClickAvg() < 0 || sumInfo.get(i).getClickAvg()==null){
						    	 }else{
						    		 djyg+=sumInfo.get(i).getClickAvg();
						    	 }
						    	 if(sumInfo.get(i).getClkPv() == -1){
						    	 }else{
						    		 djcs+=sumInfo.get(i).getClkPv();
						    	 }
				    			 if(sumInfo.get(i).getClkUv() == -1){
				    			 }else{
				    				 djrs+=sumInfo.get(i).getClkUv();
				    			 }
					    		 if(sumInfo.get(i).getVisit() == -1){
					    		 }else{
					    			 fscs+=sumInfo.get(i).getVisit();
					    		 }
					    		 if(sumInfo.get(i).getVisitor() == -1){
					    		 }else{
					    			 fwrs+=sumInfo.get(i).getVisitor();
					    		 }
					    		 if(sumInfo.get(i).getClick()== -1){
					    		 }else{
					    			 clk+=sumInfo.get(i).getClick();
					    		 }
					    		 if(sumInfo.get(i).getPageView() == -1){
					    		 }else{
					    			 lll+=sumInfo.get(i).getPageView();
					    		 }
					    		 if(sumInfo.get(i).getBounceTimes() == -1){
					    		 }else{
					    			 tccs+=sumInfo.get(i).getBounceTimes();
					    		 }
					    		 if(sumInfo.get(i).getActivityCode().indexOf("JT")!=-1&&sumInfo.get(i).getActivityCode().indexOf("MGJT")==-1){
					    			 if(sumInfo.get(i).getViewTime()<=-1){
						    			 pjfwsc+=0; 
						    		 }else{
						    			 pjfwsc+=sumInfo.get(i).getViewTime()*sumInfo.get(i).getVisit(); 
						    		  } 
					    		 }else{
					    			 if(sumInfo.get(i).getViewTime()<=-1){
						    			 pjfwsc+=0; 
						    		 }else{
						    			 pjfwsc+=sumInfo.get(i).getViewTime(); 
						    		  }  
					    		 }
					    		 url=sumInfo.get(i).getUrlPc();
						    	 tfl+=sumInfo.get(i).getPut_value();
						    	 WeekDataExport des=new WeekDataExport();
						    	 des.setActivityCode(actiCode);
						    	 des.setActivityName(hzggw);
						    	 des.setMediaName(hzmc);
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
					    if(actiCode.indexOf("JT")!=-1&&actiCode.indexOf("MGJT")==-1){
								    	 if(fscs==0){
						    				 des.setViewTime((double) -1);
						    			}else{
						    				des.setViewTime(pjfwsc/fscs);	
						    			}	
						    		}else{
						    			des.setViewTime(pjfwsc/fzggw);
						    		}
					    		 des.setUrlPc(url);
					    		 des.setPut_value(tfl);
					    			String arr[]=new DataEndUntil().checkOutTotal(de,unit,1,0,0);
						   	    	if(arr[7].equals("N/A")){
					  		    	 }else{
					  		    		sumYuGu+= Double.parseDouble(arr[7].replaceAll(",", "")); 
					  		    	   }
					   	    	    if(arr[10].equals("N/A")){
				 		    	    }else{
				 		    		sumDiJi+= Double.parseDouble(arr[10].replaceAll(",", "")); 
				  	    	         }
					    		 listAddMergin3.add(new DataEndUntil().checkOutTotal(des,unit,2,sumYuGu,sumDiJi));
					    		 _actiCode=sumInfo.get(i).getActivityCode();
				    			 if(sumInfo.get(i).getImpPv() == -1){
					    		 }else{
					    			 _bgcs+= sumInfo.get(i).getImpPv();
					    		 }
						    	 if(sumInfo.get(i).getImpUv() == -1){
						    	 }else{
						    		 _bgrs+= sumInfo.get(i).getImpUv();
						    	 }
						    	 if(sumInfo.get(i).getExposureAvg() == -1){
						    	 }else{
						    		 _bgyg+= sumInfo.get(i).getExposureAvg();
						    	 }
						    	 if(sumInfo.get(i).getClickAvg() == -1){
						    	 }else{
						    		 _djyg+=sumInfo.get(i).getClickAvg();
						    	 }
						    	 if(sumInfo.get(i).getClkPv() == -1){
						    	 }else{
						    		 _djcs+=sumInfo.get(i).getClkPv();
						    	 }
				    			 if(sumInfo.get(i).getClkUv() == -1){
				    			 }else{
				    				 _djrs+=sumInfo.get(i).getClkUv();
				    			 }
					    		 if(sumInfo.get(i).getVisit() == -1){
					    		 }else{
					    			 _fscs+=sumInfo.get(i).getVisit();
					    		 }
					    		 if(sumInfo.get(i).getVisitor() == -1){
					    		 }else{
					    			 _fwrs+=sumInfo.get(i).getVisitor();
					    		 }
					    		 if(sumInfo.get(i).getClick()== -1){
					    		 }else{
					    			 _clk+=sumInfo.get(i).getClick();
					    		 }
					    		 if(sumInfo.get(i).getPageView() == -1){
					    		 }else{
					    			 _lll+=sumInfo.get(i).getPageView();
					    		 }
					    		 if(sumInfo.get(i).getBounceTimes() == -1){
					    		 }else{
					    			 _tccs+=sumInfo.get(i).getBounceTimes();
					    		 }
					     if(sumInfo.get(i).getActivityCode().indexOf("JT")!=-1&&sumInfo.get(i).getActivityCode().indexOf("MGJT")==-1){
					    			 if(sumInfo.get(i).getViewTime()<=-1){
						    			 _pjfwsc+=0; 
						    		 }else{
						    			 _pjfwsc+=sumInfo.get(i).getViewTime()*sumInfo.get(i).getVisit(); 
						    		  } 
					    		 }else{
					    			 if(sumInfo.get(i).getViewTime()<=-1){
						    			 _pjfwsc+=0; 
						    		 }else{
						    			 _pjfwsc+=sumInfo.get(i).getViewTime(); 
						    		  }  
					    		 }
					    		 if(sumInfo.get(i).getPut_value()==-1){
					    		 }else{
					    			_tfl+=sumInfo.get(i).getPut_value(); 
					    		 }
				    		WeekDataExport deSumMedia=new WeekDataExport();
				    	 	deSumMedia.setMediaName(hzmc);
				    	 	deSumMedia.setActivityCode(_actiCode);
				    	    deSumMedia.setActivityName(hzggw);
				    	    deSumMedia.setMic(mic);
				    	    deSumMedia.setExposureAvg(_bgyg);
				    		deSumMedia.setClickAvg(_djyg);
				    		deSumMedia.setImpPv(_bgcs);
				    		deSumMedia.setImpUv(_bgrs);
				    		deSumMedia.setClkPv(_djcs);
				    		deSumMedia.setClkUv(_djrs);
				    		deSumMedia.setVisit(_fscs);
				    		deSumMedia.setVisitor(_fwrs);
				    		deSumMedia.setClick(_clk);
				    		deSumMedia.setPageView(_lll);
				    		deSumMedia.setBounceTimes(_tccs);
				    		 if(_actiCode.indexOf("JT")!=-1&&_actiCode.indexOf("MGJT")==-1){
				    			 if(_fscs==0){
				    				 deSumMedia.setViewTime((double) -1);
				    			}else{
				    				deSumMedia.setViewTime(_pjfwsc/_fscs);	
				    			}	
					    		}else{
					    		deSumMedia.setViewTime(_pjfwsc/fzcd);
					    		}
				    		deSumMedia.setUrlPc(url);
				    		deSumMedia.setPut_value(_tfl);
				    		String arry[]=new DataEndUntil().checkOutTotal(de,unit,1,0,0);
				   	    	if(arry[7].equals("N/A")){
			  		    	 }else{
			  		    		_sumYuGu+= Double.parseDouble(arry[7].replaceAll(",", "")); 
			  		    	   }
			   	    	    if(arry[10].equals("N/A")){
		 		    	    }else{
		 		    		_sumDiJi+= Double.parseDouble(arry[10].replaceAll(",", "")); 
		  	    	         }  
				    		listAddMergin3.add(new DataEndUntil().checkOutTotal(deSumMedia,unit,3,_sumYuGu,_sumDiJi));// 分完项目再分媒体汇总
				    		 hzmc="";
				    		 _actiCode="";
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
				    		 url="";
				    		 _tfl=0;
				    		 tfrq="";
				    		 hzggw="";
				    		 mic = "";
				    		 fzggw=1;
				    		  fzcd=1;
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
					    	  actiCode="";
					    	  sumDiJi=0;
					    	  sumYuGu=0;
					    	  _sumDiJi=0;
					    	  _sumYuGu=0;
					    	  
				       }
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
			
			    map.put("hb3", hb3);//将合并信息存放到map中
			    
		    map.put("user_name", user.getREAL_NAME());
			map.put("path", path);
			map.put("imgPath",imgPath);
			map.put("custName",custName);
			map.put("smonth",smonth);
			map.put("list1", list1);
			map.put("list", listAddMergin);
			map.put("list2", listAddMergin2);
			map.put("list3", listAddMergin3);
			map.put("jumpRang", jumpRang);
			map.put("ctrRangL", ctrRangL);
			map.put("ctrRangM", ctrRangM);
			map.put("wcRangL", wcRangL);
			map.put("wcRangM", wcRangM);
            map.put("djwcRangL", djwcRangL);
            map.put("djwcRangM",djwcRangM);
			 WriteExcel(map);
			return this.LoadExcel(request, response, path);
					
	}
	   /**
		 * 实例化一个excel
		 * 
		 * @param map
		 */
		@SuppressWarnings({ "rawtypes", "deprecation", "unchecked", "unused", "resource" })
		public void WriteExcel(Map map) {
			String outputFile = (String) map.get("path");
			//跳出率
			String jumpRang=(String) map.get("jumpRang");
			//点击率ctr
			String ctrRangL=(String) map.get("ctrRangL"); 
			String ctrRangM=(String) map.get("ctrRangM"); 
			//曝光完成率
			String wcRangL=(String) map.get("wcRangL"); 
			String wcRangM=(String) map.get("wcRangM"); 
			//点击完成率
			String djwcRangL=(String) map.get("djwcRangL");
			String djwcRangM=(String) map.get("djwcRangM");

			//获得合并的数组
			List <String[]> hb2=(List<String[]>) map.get("hb");
			List <String[]> hb3=(List<String[]>) map.get("hb3");
			
			File file = new File(outputFile);
			XSSFWorkbook workbook; // 实例化一个工作簿
			try {
				FileOutputStream out = new FileOutputStream(outputFile);
				workbook = new XSSFWorkbook();
				
				XSSFCellStyle style1 = workbook.createCellStyle();
				XSSFFont font1 = workbook.createFont();
				font1.setFontName("calibri");
				font1.setFontHeightInPoints((short) 9);// 设置字体大小
				style1.setFont(font1);// 选择需要用到的字体格式
				new DataExportController().setBoderStyle(style1);
				style1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
			    /**
			      *该格式：适用于基础数据的信息
			      *特殊格式：#,##.00
			      */
			     XSSFCellStyle Style = workbook.createCellStyle();
			     new DataEndUntil().setBoderStyle(Style);//修改当前样式的格式信息
			     Style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##.00"));
			     Style.setFont(font1);
			     Style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
				
				 /**适用于所有显示的基础数据信息
			     * 字体格式为：calibri
				 * 字体大小为：9号，特殊的格式：#,##0
			     */
				XSSFCellStyle stylejc = workbook.createCellStyle();
				new DataEndUntil().setBoderStyle(stylejc);//修改当前样式的格式信息
				stylejc.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
				XSSFFont fontjc = (XSSFFont) workbook.createFont();
			    new DataEndUntil().setFontNumber(fontjc);
			    stylejc.setFont(fontjc);
			    stylejc.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
			   
				/**
				 * 适用于excel导出公式做除法算出的百分数
				 * 字体格式为：calibri
				 * 字体大小为：9号
				 */
				XSSFCellStyle styleBFS = workbook.createCellStyle();
				styleBFS.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
				new DataEndUntil().setBoderStyle(styleBFS);//修改当前样式的格式信息
				XSSFFont fontBFS = (XSSFFont) workbook.createFont();
			    new DataEndUntil().setFontNumber(fontBFS);
			    styleBFS.setFont(fontBFS);
			    styleBFS.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
			    /**
			     *适用于 基础数据报表的汇总部分
			     * 字体格式为：calibri
				 * 字体大小为：9号，特殊的格式：#,##0
			     */
				XSSFCellStyle styleHZ = workbook.createCellStyle();
				styleHZ.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
				XSSFFont fontHZ = (XSSFFont) workbook.createFont();
				new DataEndUntil().setHuiStyle(styleHZ, fontHZ); 
				styleHZ.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
				 /**
			     *适用于 基础数据报表的汇总部分
			     * 字体格式为：calibri
				 * 字体大小为：9号，特殊的格式：#,##0
			     */
				XSSFCellStyle styleHZXS = workbook.createCellStyle();
				styleHZ.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
				XSSFFont fontHZXS = (XSSFFont) workbook.createFont();
				new DataEndUntil().setHuiStyle(styleHZXS, fontHZXS); 
				styleHZXS.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
				 /**
			     *适用于 基础数据报表的汇总部分
			     * 字体格式为：calibri
				 * 字体大小为：9号，特殊的格式：#,##0.00
			     */
				XSSFCellStyle styleHZBFS = workbook.createCellStyle();
				styleHZBFS.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
				XSSFFont FontHZBFS = (XSSFFont) workbook.createFont();
				new DataEndUntil().setHuiStyle(styleHZBFS, FontHZBFS); 	
				styleHZBFS.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
				
				 /**
			     *适用于 基础数据报表的汇总部分
			     * 字体格式为：calibri
				 * 字体大小为：9号，特殊的格式：#,##0.00
				 * 字体颜色为红色
			     */
				XSSFCellStyle styleHZRed = workbook.createCellStyle();
				styleHZRed.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
				XSSFFont FontHZRed = (XSSFFont) workbook.createFont();
				FontHZRed.setColor(Font.COLOR_RED);
				new DataEndUntil().setHuiStyle(styleHZRed, FontHZRed); 	
				styleHZRed.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
				
				XSSFCellStyle styleHC = workbook.createCellStyle();
				XSSFFont FontHC = (XSSFFont) workbook.createFont();
				new DataEndUntil().setHuiChStyle(styleHC, FontHC); 	
				styleHC.setAlignment(HSSFCellStyle.ALIGN_LEFT);//汉字靠左对齐
				
				//表示的是做校验的百分率的数据
				XSSFCellStyle styleRed = workbook.createCellStyle();
				XSSFFont FontRed = (XSSFFont) workbook.createFont();
				FontRed.setColor(Font.COLOR_RED);
				new DataEndUntil().setRedStyle(styleRed, FontRed); 
				styleRed.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
				styleRed.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//数字靠右对齐
				
				
				//日期英文的格式为靠左对齐
				XSSFCellStyle styleDay = workbook.createCellStyle();
				XSSFFont FontDay = (XSSFFont) workbook.createFont();
				new DataEndUntil().setRedStyle(styleDay, FontDay);
				styleDay.setAlignment(HSSFCellStyle.ALIGN_LEFT);//日期,英文靠左对齐
				
				XSSFCellStyle styleHZDay = workbook.createCellStyle();
				XSSFFont FontHZDay = (XSSFFont) workbook.createFont();
				FontHZRed.setColor(Font.COLOR_RED);
				new DataEndUntil().setHuiStyle(styleHZDay, FontHZDay); 	
				styleHZDay.setAlignment(HSSFCellStyle.ALIGN_LEFT);//日期,英文靠左对齐
				
				CreationHelper helper = workbook.getCreationHelper();
				style1.setDataFormat(helper.createDataFormat().getFormat("yyyy-MM-dd"));// 设置日期的格式信息
				// 建立一张表格
				XSSFSheet sheet1 = workbook.createSheet("基础数据");
				XSSFSheet sheet2 = workbook.createSheet("分媒体数据");
				XSSFSheet sheet3 = workbook.createSheet("汇总数据");
				//设置冻结窗口  前一个参数代表列；后一个参数代表行。
				//sheet1.createFreezePane(0,9);
			
				// 插入logo图片
				new DataExportController().insetImages((String)map.get("imgPath"), workbook, sheet1);
				new DataExportController().insetImages((String)map.get("imgPath"), workbook, sheet2);
				new DataExportController().insetImages((String)map.get("imgPath"), workbook, sheet3);
				// 设置整个表头的格式
				sheet1.setDisplayGridlines(false);// 设置无边框
				sheet2.setDisplayGridlines(false);// 设置无边框
				sheet3.setDisplayGridlines(false);// 设置无边框
				//表头的文本格式
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
				
	            //设置中文字的格式为9号
	            XSSFCellStyle styleZ = workbook.createCellStyle();
	            new DataExportController().setBoderStyle(styleZ);
	            XSSFFont fontZhong = workbook.createFont();
	            fontZhong.setFontName("宋体");
	            fontZhong.setFontHeightInPoints((short) 9);// 设置字体大小
	            styleZ.setFont(fontZhong);
	            styleZ.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 汉字靠左对齐
	            
	           
				
				// 单独的把表头提出来
				List<String[]> l1 = new ArrayList<String[]>();
				l1 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
				int rowIndex = 7;// 起始行
				for (int i = 0; i < l1.size(); i++) {
					XSSFRow row = sheet1.createRow(rowIndex++);
					row.setHeight((short) 495);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
					String[] str = l1.get(i);
					for (int j = 0; j < str.length; j++) {
						XSSFCell cell = row.createCell(j + 1);
						cell.setCellValue(str[j]);
						cell.setCellStyle(style);
					}
				}
				
				//第一个数据填充
				List<String[]> l = new ArrayList<String[]>();
				l = (List<String[]>) map.get("list");// 获得list对象，并且添加到excel中
				rowIndex = 9;// 起始行
				for (int i = 0; i < l.size(); i++) {
					XSSFRow row = sheet1.createRow(rowIndex++);
					String[] str = l.get(i);//获得当前遍历的对象
					for (int j = 0; j < str.length; j++) {
						row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
					    XSSFCell cell = row.createCell(j + 1);
					    if(str[0]!=null&&j==0||str[1]!=null&&j==1||str[3]!=null&&j==3||str[4]!=null&&j==4){
					    	   cell.setCellValue(str[j]); 
					    	   
                     	       cell.setCellStyle(styleZ); 
					    }else if(str[2]!=null&&j==2||str[5]!=null&&j==5||str[23]!=null&&j==23||str[24]!=null&&j==24||str[6]!=null&&j==6
                        		 ){
                       	         cell.setCellValue(str[j]); 
                       	         cell.setCellStyle(styleDay); 
                        }else if(str[20]!=null&&j==20){
                        	   if(str[j].equals("N/A")){
                   		    	cell.setCellValue(str[j]); 
                   		    	cell.setCellStyle(Style);
                   		    }else{
                   		    	 cell.setCellValue(Double.parseDouble(str[j])); 
		                    		 cell.setCellStyle(Style); 
                   		    }
                        }else if(str[13]!=null&&j==13||str[19]!=null&&j==19||str[21]!=null&&j==21||str[22]!=null&&j==22){
                        	if(str[13]!=null&&j==13){
                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")"); 
                        		cell.getCellFormula();
                        		 if(ctrRangM==""||ctrRangM==null||ctrRangL==""||ctrRangL==null){
       							  cell.setCellStyle(styleBFS); 
     						}else{
     						  if(Float.parseFloat(str[13])>=Float.parseFloat(ctrRangL) && Float.parseFloat(str[13])<=Float.parseFloat(ctrRangM) ){
     							  cell.setCellStyle(styleBFS); 
     						  }else{
     							  cell.setCellStyle(styleRed);
     						  }
     						}
                        	}else if(str[19]!=null&&j==19){
                        		cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
                        		if(jumpRang==""||jumpRang==null){
        							  cell.setCellStyle(styleBFS); 
            						}else{
            							if(Float.parseFloat(str[19])<=Float.parseFloat(jumpRang)){
            							cell.setCellStyle(styleBFS);
            							}else{
          							  cell.setCellStyle(styleRed);
            							}
            						}		
                        	}else if(str[21]!=null&&j==21){
          						 Double result=0.0;
          						 Double jieguo=0.0;
          						  if("0".equals(str[25])){
          						cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
          						if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
      							  cell.setCellStyle(styleBFS); 
          						}else{
          						if(Float.parseFloat(str[21]) >=Float.parseFloat(wcRangL) && Float.parseFloat(str[21]) <=Float.parseFloat(wcRangM)){
          							cell.setCellStyle(styleBFS);
          						}else{
          							cell.setCellStyle(styleRed);
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
        	            			  if(l.get(i+d)[8].equals("N/A")){
        	            			  }else{
             	            			 result=result+ Double.parseDouble(l.get(i+d)[8]);
        	            			  }
        	            		  }
        	            		  if(str[7].equals("N/A")){
        	            		  }else{
        	            			  jieguo = 100*result/Double.parseDouble(str[7]);  
        	            		  }
        	            		
        	            		  
        	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
          						
          						
          						if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
        							  cell.setCellStyle(styleBFS); 
            					}else{
            						if(jieguo >=Float.parseFloat(wcRangL) && jieguo <=Float.parseFloat(wcRangM)){
            							cell.setCellStyle(styleBFS);
            						}else{
            							cell.setCellStyle(styleRed);
            						}
            			     	}
          					}
          					//}
                        	}else if(str[22]!=null&&j==22){
          						 Double result=0.0;
          						 Double jieguo=0.0;
          						  if("0".equals(str[25])){
          						cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
          						if(djwcRangL==""||djwcRangL==null||djwcRangM==""||djwcRangM==null){
        							  cell.setCellStyle(styleBFS); 
            						}else{
            						  if(Float.parseFloat(str[22])>=Float.parseFloat(djwcRangL) && Float.parseFloat(str[22])<=Float.parseFloat(djwcRangM)){
            							cell.setCellStyle(styleBFS);
            						  }else{
              					  cell.setCellStyle(styleRed);
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
           	            			   if(l.get(i+d)[11].equals("N/A")){
           	            			   }else{
                	            			 result=result+ Double.parseDouble(l.get(i+d)[11]);
           	            			   }
              	            		  }
           	            if(str[10].equals("N/A")){
           	            		   }else{
               	            		  jieguo = 100*result/Double.parseDouble(str[10]);
           	            		   }
           	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
          						
          						if(djwcRangL==""||djwcRangL==null||djwcRangM==""||djwcRangM==null){
      							  cell.setCellStyle(styleBFS); 
          						}else{
          						  if(jieguo>=Float.parseFloat(djwcRangL) && jieguo<=Float.parseFloat(djwcRangM)){
          							cell.setCellStyle(styleBFS);
          						  }else{
            					  cell.setCellStyle(styleRed);
                				 }
          						}
          						}
          					  
                        	}
                        }else if(str[25]!=null&&j==25){
                        	//存放合并数据的单元格信息
                        }else{
                        	 if(str[j].equals("N/A")){
                    		    	cell.setCellValue(str[j]); 
                    		    	cell.setCellStyle(stylejc);
                    		    }else{
                    		    	 cell.setCellValue(Double.parseDouble(str[j])); 
 		                    		 cell.setCellStyle(stylejc); 
                    		    }
 					      } 
					   }
				}
				rowIndex = 9;
				for(int i=0;i<hb2.size();i++){
					//一个单元格不能够进行合并单元格操作,容易报异常
					if(Integer.parseInt(hb2.get(i)[1])!=1){
						 sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 4, 4));
	            		 sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 8, 8));
	            		 sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 11, 11));
	            		 sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 22, 22));
	            		 sheet1.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb2.get(i)[1]), 23, 23));
				}
					rowIndex++;
			}
				
				// 单独的把表头提出来
				 l1 = new ArrayList<String[]>();
				l1 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
				 rowIndex = 7;// 起始行
				for (int i = 0; i < l1.size(); i++) {
					XSSFRow row = sheet2.createRow(rowIndex++);
					row.setHeight((short) 495);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
					String[] str = l1.get(i);
					for (int j = 0; j < str.length; j++) {
						XSSFCell cell = row.createCell(j + 1);
						cell.setCellValue(str[j]);
						cell.setCellStyle(style);
					}
				}
				
				//第二个数据填充
				List<String[]> l3 = new ArrayList<String[]>();
				l3 = (List<String[]>) map.get("list2");// 获得list对象，并且添加到excel中
				int rowIndex2 = 9;// 起始行
				for (int i = 0; i < l3.size(); i++) {
					XSSFRow row = sheet2.createRow(rowIndex2++);
					String[] str = l3.get(i);//获得当前遍历的对象
					
					for (int j = 0; j < str.length; j++) {
						row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
					    XSSFCell cell = row.createCell(j + 1);
						if(str[5]==null||str[5].equals("")){
						if(str[0]!=null&&j==0||str[1]!=null&&j==1||str[3]!=null&&j==3||str[4]!=null&&j==4){
							 cell.setCellValue(str[j]); 
                  	         cell.setCellStyle(styleHC); 
						}else if(str[2]!=null&&j==2||str[5]!=null&&j==5||str[23]!=null&&j==23||str[24]!=null&&j==24||str[6]!=null&&j==6
	                        		 ){
	                       	         cell.setCellValue(str[j]); 
	                       	         cell.setCellStyle(styleHZDay); 
	                        }else if(str[20]!=null&&j==20){
	                        	  if(str[j].equals("N/A")){
	                   		    	cell.setCellValue(str[j]); 
	                   		    	cell.setCellStyle(styleHZXS);
	                   		    }else{
	                   		    	 cell.setCellValue(Double.parseDouble(str[j])); 
			                    		 cell.setCellStyle(styleHZXS); 
	                   		    }
	                        }else if(str[13]!=null&&j==13||str[19]!=null&&j==19||str[21]!=null&&j==21||str[22]!=null&&j==22){
	                        	if(str[13]!=null&&j==13){
	                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")");
	                            	if(ctrRangM==""||ctrRangM==null||ctrRangL==""||ctrRangL==null){
	           							  cell.setCellStyle(styleHZBFS); 
	         						}else{
	         						  if(Float.parseFloat(str[13])>=Float.parseFloat(ctrRangL) && Float.parseFloat(str[13])<=Float.parseFloat(ctrRangM) ){
	         							  cell.setCellStyle(styleHZBFS); 
	         						  }else{
	         							  cell.setCellStyle(styleHZRed);
	         						  }
	         						}
	                        	}else if(str[19]!=null&&j==19){
	                        		cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
	                        		if(jumpRang==""||jumpRang==null){
	        							  cell.setCellStyle(styleHZBFS); 
	            						}else{
	            							if(Float.parseFloat(str[19])<=Float.parseFloat(jumpRang) ){
	            							cell.setCellStyle(styleHZBFS);
	            							}else{
	          							  cell.setCellStyle(styleHZRed);
	            							}
	            						}
	                        	}else if(str[21]!=null&&j==21){
	                        		cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
	                        		if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
	        							  cell.setCellStyle(styleHZBFS); 
	            					}else{
	            						if(Float.parseFloat(str[21])>=Float.parseFloat(wcRangL) && Float.parseFloat(str[21])<=Float.parseFloat(wcRangM)){
	            							cell.setCellStyle(styleHZBFS);
	            						}else{
	            							cell.setCellStyle(styleHZRed);
	            						}
	            					}
	                        	}else if(str[22]!=null&&j==22){
	                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
	                        		if(djwcRangL==""||djwcRangL==null||djwcRangM==""||djwcRangM==null){
	        							  cell.setCellStyle(styleHZBFS); 
	            					}else{
	            						if(Float.parseFloat(str[22])>=Float.parseFloat(djwcRangL) && Float.parseFloat(str[22])<=Float.parseFloat(djwcRangM)){
	            							cell.setCellStyle(styleHZBFS);
	            						}else{
	            							cell.setCellStyle(styleHZRed);
	            						}
	            					}
	                        	}
	                        }else{
	                        	 if(str[j].equals("N/A")){
	                    		    	cell.setCellValue(str[j]); 
	                    		    	cell.setCellStyle(styleHZ);
	                    		    }else{
	                    		    	 cell.setCellValue(Double.parseDouble(str[j])); 
	 		                    		 cell.setCellStyle(styleHZ); 
	                    		    }
	 					      } 
							
						}else{
							if(str[0]!=null&&j==0||str[1]!=null&&j==1||str[3]!=null&&j==3||str[4]!=null&&j==4){
								 cell.setCellValue(str[j]); 
	                  	         cell.setCellStyle(styleZ); 
							}else if(str[2]!=null&&j==2||str[5]!=null&&j==5||str[23]!=null&&j==23||str[24]!=null&&j==24||str[6]!=null&&j==6
	                        		 ){
	                       	         cell.setCellValue(str[j]); 
	                       	         cell.setCellStyle(styleDay); 
	                        }else if(str[20]!=null&&j==20){
	                        	   if(str[j].equals("N/A")){
	                   		    	cell.setCellValue(str[j]); 
	                   		    	cell.setCellStyle(Style);
	                   		    }else{
	                   		    	 cell.setCellValue(Double.parseDouble(str[j])); 
			                    		 cell.setCellStyle(Style); 
	                   		    }
	                        }else if(str[13]!=null&&j==13||str[19]!=null&&j==19||str[21]!=null&&j==21||str[22]!=null&&j==22){
	                        	if(str[13]!=null&&j==13){
	                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")"); 
	                        		 if(ctrRangM==""||ctrRangM==null||ctrRangL==""||ctrRangL==null){
	       							  cell.setCellStyle(styleBFS); 
	     						}else{
	     						  if(Float.parseFloat(str[13])>=Float.parseFloat(ctrRangL) && Float.parseFloat(str[13])<=Float.parseFloat(ctrRangM) ){
	     							  cell.setCellStyle(styleBFS); 
	     						  }else{
	     							  cell.setCellStyle(styleRed);
	     						  }
	     						}
	                        	}else if(str[19]!=null&&j==19){
	                        		cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
	                        		if(jumpRang==""||jumpRang==null){
	        							  cell.setCellStyle(styleBFS); 
	            						}else{
	            							if(Float.parseFloat(str[19])<=Float.parseFloat(jumpRang) ){
	            							cell.setCellStyle(styleBFS);
	            							}else{
	          							  cell.setCellStyle(styleRed);
	            							}
	            						}		
	                        	}else if(str[21]!=null&&j==21){
	                        		
	                        		cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
	                        		if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
	      							  cell.setCellStyle(styleBFS); 
	          					   }else{
	          						if(Float.parseFloat(str[21])>=Float.parseFloat(wcRangL) && Float.parseFloat(str[21])<=Float.parseFloat(wcRangM)){
	          							cell.setCellStyle(styleBFS);
	          						}else{
	          							cell.setCellStyle(styleRed);
	          						}
	          					}
	                        		
	                        	}else if(str[22]!=null&&j==22){
	                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
	                        		String a=cell.getCellFormula();
	                        		if(djwcRangL==""||djwcRangL==null||djwcRangM==""||djwcRangM==null){
	        							  cell.setCellStyle(styleBFS); 
	            					}else{
	            						if(Float.parseFloat(str[22])>=Float.parseFloat(djwcRangL) && Float.parseFloat(str[22])<=Float.parseFloat(djwcRangM)){
	            							cell.setCellStyle(styleBFS);
	            						}else{
	            							cell.setCellStyle(styleRed);
	            						}
	            					}
	                        	}
	                        }else if(str[25]!=null&&j==25){
	                        	//存放合并数据的单元格信息
	                        }else{
	                        	 if(str[j].equals("N/A")){
	                    		    	cell.setCellValue(str[j]); 
	                    		    	cell.setCellStyle(stylejc);
	                    		    }else{
	                    		    	 cell.setCellValue(Double.parseDouble(str[j])); 
	 		                    		 cell.setCellStyle(stylejc); 
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
				
				
				// 单独的把表头提出来
				 l1 = new ArrayList<String[]>();
				 l1 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
				 rowIndex = 7;// 起始行
				for (int i = 0; i < l1.size(); i++) {
					XSSFRow row = sheet3.createRow(rowIndex++);
					row.setHeight((short) 495);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
					String[] str = l1.get(i);
					for (int j = 0; j < str.length; j++) {
						XSSFCell cell = row.createCell(j + 1);
						if(str[6].equals("日期")&&j==6){
							cell.setCellValue("投放周期");
						}else{
							cell.setCellValue(str[j]);	
						}
						cell.setCellStyle(style);
					}
				}
				
				
				//第三个数据填充
				List<String[]> l5 = new ArrayList<String[]>();
				l5 = (List<String[]>) map.get("list3");// 获得list对象，并且添加到excel中
				int rowIndex4 = 9;// 起始行
				for (int i = 0; i < l5.size(); i++) {
					XSSFRow row = sheet3.createRow(rowIndex4++);
					String[] str = l5.get(i);//获得当前遍历的对象
					
					for (int j = 0; j < str.length; j++) {
						row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
					    XSSFCell cell = row.createCell(j + 1);
						if(str[5]==null||str[5].equals("")){
							if(str[0]!=null&&j==0||str[1]!=null&&j==1||str[3]!=null&&j==3||str[4]!=null&&j==4){
								 cell.setCellValue(str[j]); 
	                  	         cell.setCellStyle(styleHC); 
							}else if(str[2]!=null&&j==2||str[5]!=null&&j==5||str[23]!=null&&j==23||str[24]!=null&&j==24||str[6]!=null&&j==6
	                        		 ){
	                       	         cell.setCellValue(str[j]); 
	                       	         cell.setCellStyle(styleHZDay); 
	                        }else if(str[20]!=null&&j==20){
	                        	   if(str[j].equals("N/A")){
	                   		    	cell.setCellValue(str[j]); 
	                   		    	cell.setCellStyle(styleHZXS);
	                   		    }else{
	                   		    	 cell.setCellValue(Double.parseDouble(str[j])); 
			                    		 cell.setCellStyle(styleHZXS); 
	                   		    }
	                        }else if(str[13]!=null&&j==13||str[19]!=null&&j==19||str[21]!=null&&j==21||str[22]!=null&&j==22){
	                        	if(str[13]!=null&&j==13){
	                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")");
	                            	if(ctrRangM==""||ctrRangM==null||ctrRangL==""||ctrRangL==null){
	           							  cell.setCellStyle(styleHZBFS); 
	         						}else{
	         						  if(Float.parseFloat(str[13])>=Float.parseFloat(ctrRangL) && Float.parseFloat(str[13])<=Float.parseFloat(ctrRangM) ){
	         							  cell.setCellStyle(styleHZBFS); 
	         						  }else{
	         							  cell.setCellStyle(styleHZRed);
	         						  }
	         						}
	                        	}else if(str[19]!=null&&j==19){
	                        		cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
	                        		if(jumpRang==""||jumpRang==null){
	        							  cell.setCellStyle(styleHZBFS); 
	            						}else{
	            							if(Float.parseFloat(str[19])<=Float.parseFloat(jumpRang) ){
	            							cell.setCellStyle(styleHZBFS);
	            							}else{
	          							  cell.setCellStyle(styleHZRed);
	            							}
	            						}
	                        	}else if(str[21]!=null&&j==21){
	                        		cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
	                        		if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
	        							  cell.setCellStyle(styleHZBFS); 
	            					}else{
	            						if(Float.parseFloat(str[21])>=Float.parseFloat(wcRangL) && Float.parseFloat(str[21])<=Float.parseFloat(wcRangM)){
	            							cell.setCellStyle(styleHZBFS);
	            						}else{
	            							cell.setCellStyle(styleHZRed);
	            						}
	            					}
	                        	}else if(str[22]!=null&&j==22){
	                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
	                        		if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
	        							  cell.setCellStyle(styleHZBFS); 
	            					}else{
	            						if(Float.parseFloat(str[22])>=Float.parseFloat(wcRangL) && Float.parseFloat(str[22])<=Float.parseFloat(wcRangM)){
	            							cell.setCellStyle(styleHZBFS);
	            						}else{
	            							cell.setCellStyle(styleHZRed);
	            						}
	            					}
	                        	}
	                        	
	                        }else{
	                        	 if(str[j].equals("N/A")){
	                    		    	cell.setCellValue(str[j]); 
	                    		    	cell.setCellStyle(styleHZ);
	                    		    }else{
	                    		    	 cell.setCellValue(Double.parseDouble(str[j])); 
	 		                    		 cell.setCellStyle(styleHZ); 
	                    		    }
	 					      } 
						}else if(!str[6].equals("")&&j==6){//获得该活动的起始日期及截止日期
							//cell.setCellValue(str[j]+"-"+((String)map.get("smonth")).replace("-","/"));
							cell.setCellValue(str[j]);
							cell.setCellStyle(style1);
						}else{
							if(str[0]!=null&&j==0||str[1]!=null&&j==1||str[3]!=null&&j==3||str[4]!=null&&j==4){
								 cell.setCellValue(str[j]); 
	                  	         cell.setCellStyle(styleZ); 
							}else if(str[2]!=null&&j==2||str[5]!=null&&j==5||str[23]!=null&&j==23||str[24]!=null&&j==24
	                        		 ){
	                       	         cell.setCellValue(str[j]); 
	                       	         cell.setCellStyle(styleDay); 
	                        }else if(str[20]!=null&&j==20){
	                        	   if(str[j].equals("N/A")){
	                   		    	cell.setCellValue(str[j]); 
	                   		    	cell.setCellStyle(Style);
	                   		    }else{
	                   		    	 cell.setCellValue(Double.parseDouble(str[j])); 
			                    		 cell.setCellStyle(Style); 
	                   		    }
	                        }else if(str[13]!=null&&j==13||str[19]!=null&&j==19||str[21]!=null&&j==21||str[22]!=null&&j==22){
	                        	if(str[13]!=null&&j==13){
	                        		cell.setCellFormula("IFERROR(M"+(i+10)+"/J"+(i+10)+",\"N/A\")"); 
	                        		 if(ctrRangM==""||ctrRangM==null||ctrRangL==""||ctrRangL==null){
	       							  cell.setCellStyle(styleBFS); 
	     						}else{
	     						  if(Float.parseFloat(str[13])>=Float.parseFloat(ctrRangL) && Float.parseFloat(str[13])<=Float.parseFloat(ctrRangM) ){
	     							  cell.setCellStyle(styleBFS); 
	     						  }else{
	     							  cell.setCellStyle(styleRed);
	     						  }
	     						}
	                        	}else if(str[19]!=null&&j==19){
	                        		cell.setCellFormula("IFERROR(T"+(i+10)+"/P"+(i+10)+",\"N/A\")");
	                        		if(jumpRang==""||jumpRang==null){
	        							  cell.setCellStyle(styleBFS); 
	            						}else{
	            							if(Float.parseFloat(str[19])<=Float.parseFloat(jumpRang) ){
	            							cell.setCellStyle(styleBFS);
	            							}else{
	          							  cell.setCellStyle(styleRed);
	            							}
	            						}		
	                        	}else if(str[21]!=null&&j==21){
	                        		Double result=0.0;
	          						 Double jieguo=0.0;
	          						  if("0".equals(str[25])){
	                        		cell.setCellFormula("IFERROR(J"+(i+10)+"/I"+(i+10)+",\"N/A\")");
	                        		if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
	        							  cell.setCellStyle(styleBFS); 
	            					}else{
	            						if(Float.parseFloat(str[21])>=Float.parseFloat(wcRangL) && Float.parseFloat(str[21])<=Float.parseFloat(wcRangM)){
	            							cell.setCellStyle(styleBFS);
	            						}else{
	            							cell.setCellStyle(styleRed);
	            						}
	            					}
	          					  }else{
		          						StringBuffer sbfz = new StringBuffer();
		          						 StringBuffer sbfm = new StringBuffer();
		          						 String sbfzS = new String();
		         						 String sbfmS = new String();
		         						 int izs =i;
		         						 int izsd =i ; //不变的i
		         						 if(Integer.parseInt(hb3.get(i)[1])==0){
		         							cell.setCellStyle(styleBFS);
		         						 }else{
		          							for(int q = 0; q < Integer.parseInt(hb3.get(izsd)[1]); q++)
		        	            		   { 
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
		        	            			  if(l5.get(i+d)[8].equals("N/A")){
		        	            			  }else{
		             	            			 result=result+ Double.parseDouble(l5.get(i+d)[8]);
		        	            			  }
		        	            		  }
		        	            		  if(str[7].equals("N/A")){
		        	            		  }else{
		        	            			  jieguo = 100*result/Double.parseDouble(str[7]);  
		        	            		  }
		        	            		
		        	            		  
		        	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
		        	            		  if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
		        							  cell.setCellStyle(styleBFS); 
		            					}else{
		            						if(jieguo >=Float.parseFloat(wcRangL) && jieguo <=Float.parseFloat(wcRangM)){
		            							cell.setCellStyle(styleBFS);
		            						}else{
		            							cell.setCellStyle(styleRed);
		            						}
		            			     	}
	          					  }
		          					  }
	                        		
	                        		
	                        	}else if(str[22]!=null&&j==22){
	                        	 Double result=0.0;
         						 Double jieguo=0.0;
         						  if("0".equals(str[25])){
                       		cell.setCellFormula("IFERROR(M"+(i+10)+"/L"+(i+10)+",\"N/A\")");
                       		if(wcRangL==""||wcRangL==null||wcRangM==""||wcRangM==null){
       							  cell.setCellStyle(styleBFS); 
           					}else{
           						if(Float.parseFloat(str[22])>=Float.parseFloat(wcRangL) && Float.parseFloat(str[22])<=Float.parseFloat(wcRangM)){
           							cell.setCellStyle(styleBFS);
           						}else{
           							cell.setCellStyle(styleRed);
           						}
           					}
                       	}else{
                       		StringBuffer sbfz = new StringBuffer();
       						 StringBuffer sbfm = new StringBuffer();
       						 String sbfzS = new String();
      						 String sbfmS = new String();
      						 int izs =i;
      						 int izsd =i ; //不变的i
      						 if(Integer.parseInt(hb3.get(i)[1])==0){
      							cell.setCellStyle(styleBFS);
      						 }else{
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
     	            			   if(l5.get(i+d)[11].equals("N/A")){
     	            			   }else{
          	            			 result=result+ Double.parseDouble(l5.get(i+d)[11]);
     	            			   }
        	            		  }
     	            if(str[10].equals("N/A")){
     	            		   }else{
         	            		  jieguo = 100*result/Double.parseDouble(str[10]);
     	            		   }
     	            		  cell.setCellFormula("IFERROR(("+sfz+")/"+sbfmS+",\"N/A\")");
    						
    						if(djwcRangL==""||djwcRangL==null||djwcRangM==""||djwcRangM==null){
							  cell.setCellStyle(styleBFS); 
    						}else{
    						  if(jieguo>=Float.parseFloat(djwcRangL) && jieguo<=Float.parseFloat(djwcRangM)){
    							cell.setCellStyle(styleBFS);
    						  }else{
      					  cell.setCellStyle(styleRed);
          				 }
    					}
                       }
      						 }
         						  }
	                        }else{
	                        	 if(str[j].equals("N/A")){
	                    		    	cell.setCellValue(str[j]); 
	                    		    	cell.setCellStyle(stylejc);
	                    		    }else{
	                    		    	 cell.setCellValue(Double.parseDouble(str[j])); 
	 		                    		 cell.setCellStyle(stylejc); 
	                    		    }
	 					      } 
						}
						if (Integer.parseInt(hb3.get(i)[1])==0){
							row.createCell(8).setCellValue("0");
							row.createCell(11).setCellValue("0");
						 };
					}
					
					//判断汇总行，添加合并的单元格
					if(str[5]==null||str[5].equals("")){
						//合并单元格到日期
						sheet3.addMergedRegion(new CellRangeAddress(9+i, 9+i, 1, 7));
					}
				}
				rowIndex = 9;
				for(int i=0;i<hb3.size();i++){
					//一个单元格不能够进行合并单元格操作,容易报异常
					if(Integer.parseInt(hb3.get(i)[1])!=1&&Integer.parseInt(hb3.get(i)[1])!=0){
						 sheet3.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb3.get(i)[1]), 4, 4));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb3.get(i)[1]), 8, 8));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb3.get(i)[1]), 11, 11));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb3.get(i)[1]), 22, 22));
	            		 sheet3.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex-1+Integer.parseInt(hb3.get(i)[1]), 23, 23));
				}
					rowIndex++;
			}
				//设置列宽
				WeekUtil.setColumnWidth(workbook);
				//设置合并单元格的格式
				WeekUtil.addMergedRegion(workbook);
				
			  //设置顶部文本信息及样式:该信息与导出的周报信息有差异
			   DataEndUntil.setTitle(workbook, sd, map);
			
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
				response.addHeader("Content-Disposition", "attachment;filename=" + DataExportController.toUtf8String(filename));
				response.addHeader("Content-Length", "" + file.length());
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@RequestMapping("/months.do")
		public String MonthReport(@RequestParam("month_customer") String month_customer,
								@RequestParam("month_date") String month_date,
								@RequestParam("actCode") String actCode,
								@RequestParam("dayIf") String dayIfs,
								@RequestParam("isRegion") String isRegions,
								HttpServletRequest request
								){
			
		    	MonthLogo = request.getRealPath("/") + "/images/udbac_logo.png";
		    	HttpSession session = request.getSession();
		    	String filePathName;
				UserBean user = (UserBean) session.getAttribute("user");
			try {
				new FileInputStream(MonthLogo);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			 Date dt = null;
			   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						dt = sdf.parse(month_date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Integer dayIf = Integer.parseInt(dayIfs);
					Integer isRegion = Integer.parseInt(isRegions);
					if(dayIf == 1){
						filePathName = totalService.monthReportList(month_customer,dt,user.getREAL_NAME(),actCode,dayIf,isRegion);
					}else{
						filePathName = totalDayReportService.monthReportList(month_customer,dt,user.getREAL_NAME(),actCode,dayIf,isRegion);
					}
					
//			filePathName = totalService.monthReportList(month_customer,dt,user.getREAL_NAME(),actCode,dayIf,isRegion);
			//未找到数据
			if("0".equals(filePathName)){
				return "forward:sendMessage.do";
			}
			 
			request.setAttribute("filePath",filePathName);
			request.setAttribute("fileName",filePathName.substring(filePathName.lastIndexOf("/") + 1));
			return "forward:/report/exportReport.do";
		}
		
		/**
		 * 返回信息
		 * @param request
		 * @param response
		 * @return
		 */
		@ResponseBody
		@RequestMapping("/sendMessage.do")
		public String sendMessage(HttpServletRequest request,HttpServletResponse response){
				return "<script>parent.setTol('0')</script>";
		}//end sendMessage()
		
	
}
