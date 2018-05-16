package com.udbac.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.udbac.entity.DataEndInfo;
import com.udbac.service.DataEndService;
import com.udbac.util.DataEndUntil;
import com.udbac.util.EXCELUtil;
import com.udbac.util.FilePathManager;
import com.udbac.util.JSONUtil;
import com.udbac.util.PinyinUtil;
/**
 * 结案数据报表
 * @author Wangli 
 *
 */
@Controller
@RequestMapping("/endExport")
public class DataEndController {
	
	@Autowired
	private DataEndService dataEndService;
	
	@Autowired
	private FilePathManager filePathManager;
	
	
	
	private static int meidaLength=21;//每个媒体名称下面数据条数
	private static int jianju=5;//sheet4中每个相同列的单元格间距
	
	//图片保存路径
	//private static String pathOfPicture = "E:/company.jpeg";  
	//private static FileOutputStream fileOut = null;  
	private static BufferedImage bufferImg = null; 
	/**
	 * 通过模糊查询获得活动名称的数量
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value="sumName.do",method = RequestMethod.POST)
	public String sumName(@RequestParam(value = "actName", required = true) String actName,
			HttpServletResponse response, HttpServletRequest request) throws IOException{
		    Integer sumName=dataEndService.sumName(actName);
		    String falg="";
		    if(sumName>0){
		    	falg="1";
			   }else{
				   falg="2";
			   }
		    return falg;
	}
	
	/**
	 * 通过模糊插查询出来的活动信息，放置到下拉框里面
	 */
	
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value="actNames.do",method = RequestMethod.POST)
	public List actName( @RequestParam(value = "actName", required = true) String actName,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		    List checkNum=dataEndService.checkName(actName);
		    return checkNum;
	}
	
	@ResponseBody
	@RequestMapping(value="actSame.do",method = RequestMethod.POST)
	public String actSame( @RequestParam(value = "actName", required = true) String actName,
			 @RequestParam(value = "actCode", required = true) String actCode,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		Integer sumName=dataEndService.sumSame(actName,actCode);
	    String falg="";
	    if(sumName==1){
	    	  falg="0";
		   }else if(sumName>1){
			   falg="2"; 
		   }else{
			   falg="1";
		   }
	    return falg;
	}
	
	/**
	 * 结案数据报表的导出
	 * @return
	 * @throws Exception 
	 */
   @SuppressWarnings({ "rawtypes", "unchecked", "unused"})
   @ResponseBody
   @RequestMapping(value = "/exportEnd.do", method = RequestMethod.POST)
   public ModelAndView listEndInfo(HttpServletResponse response, HttpServletRequest request,
		   @RequestParam(value = "actName", required = true) String actName,
		   @RequestParam(value = "actCode", required = true) String actCode
		  ) throws Exception{  
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 //1.通过活动id获得活动名称以及开始日期与结束日期
	 String time="";
	   String customer_id="46";//页面将信息注释掉了
	   DataEndInfo dateAct= dataEndService.listDateInfo(actName,actCode); 
	   String activityName=dateAct.getActivity_name();//取得活动名称
	   String activityCode=dateAct.getActivity_code();//取得活动编号
	   String customerName=dateAct.getCustomer_name();//取得投放单位信息
	   if(dateAct==null){
		   Date startDate=null;
		   Date endDate=null;
	   }else{
		   Date startDate=dateAct.getStartDate();//取得活动的开始日期
		   Date endDate=dateAct.getEndDate();
		   String smonth=sdf.format(startDate);//开始时间
		   String emonth=sdf.format(endDate);//结束时间
		  
		   smonth=smonth.substring(5, 7) + "" + smonth.substring(8, 10);
		   emonth=emonth.substring(5, 7) + "" + emonth.substring(8, 10);
		   time=smonth+"-"+emonth;  
	   }
	   //存放活动的汇总点位
	   String hzdw= dataEndService.showDW(actName,actCode); 
	   //存放的是拼接好的excel表的名称       活动名称中间为空格的时候，火狐浏览器导出的时候会出现问题
	   String entry =customerName+"-"+actName.replace(" ", "")+"结案数据报表"+time;// excel名称
	   String path = filePathManager.getTmpExcel() + entry+ ".xlsx";// 临时的文件目录
	   int pc=0;//频次
	   String mtmc="";//媒体名称
	   String xm="";//项目
	   String tfwz="";//投放位置
	   String tfxs="";//投放形式
	   String pjzfc="";//媒体名称+投放位置+投放形式
	   String mic="";//短代码
	   int ygbg=0;//预估曝光
       double bgcs=0;//曝光次数
	   int bgrs=0;//曝光人数
	   double bgdbl=0;//曝光达标率
	   int ygdj=0;//预估点击
	   int djcs=0;//点击次数
	   int djrs=0;//点击人数
	   double djdbl=0;//点击达标率
	   double djlCTR=0;//点击率CTR
	   int fwcs=0;//访问次数
	   double sydbl=0;//首页达标率
	   int fwrs=0;//访问人数
	   int lll=0;//浏览量
	   int tccs=0;//跳出次数
	   double tcl=0;//跳出率
	   double pjfwsj=0;//平均访问时间   
	   int fzcd=1;//分组长度
	   String unit="";//投放单位
	   double tfl=0;//投放量
	   String actiCode="";//活动编号
	   int sumPc=0;//频次
	   int sumYgbg=0;//预估曝光
       double sumBgcs=0;//曝光次数
	   int sumBgrs=0;//曝光人数
	   double sumBgdbl=0;//曝光达标率
	   int sumYgdj=0;//预估点击
	   int sumDjcs=0;//点击次数
	   int sumDjrs=0;//点击人数
	   double sumDjdbl=0;//点击达标率
	   double sumDjlCTR=0;//点击率CTR
	   int sumFwcs=0;//访问次数
	   double sumSydbl=0;//首页达标率
	   int sumFwrs=0;//访问人数
	   int sumLll=0;//浏览量
	   int sumTccs=0;//跳出次数
	   double sumTcl=0;//跳出率
	   double sumPjfwsj=0;//平均访问时间   
	   int sumFzcd=1;//分组长度
	   String sumUnit="";//投放单位
	   double sumTfl=0;//投放量
	   double sumYuGu=0;//曝光预估的汇总的值
	   double sumDiJi=0;//曝光预估的汇总的值
	   Map map=new HashMap();
	   List list1 =new ArrayList();//汇总表的表头信息
	   List list2 =new ArrayList();//基础表的表头信息
	   List ListJC=new ArrayList();//基础表的信息
	   List listHZ=new ArrayList();//汇总表的信息
       List listFMT=new ArrayList(); //曝光分媒体 表头及曝光分广告的表头
       List listFMTJC=new ArrayList();//曝光分媒体的基础数据
       List listZTBG=new ArrayList();//曝光整体表头
       List listZTBGJC=new ArrayList();//曝光整体数据
       List listFGGJC=new ArrayList();//曝光分广告位的基础数据
       List listDJFMT=new ArrayList();//点击分媒体 
       List listDJZT=new ArrayList();//点击整体表头
       List listZTDJJC=new ArrayList();//点击整体数据
       List listDJDYFB=new ArrayList();//点击地域分布表头
       List listDJDYJC=new ArrayList();//点击地域分布数据
       List listJCHZ=new ArrayList();//基础信息表的汇总
      // List listDJBZT=new ArrayList();//点击饼状图的取值
       List listDJFMTBT=new ArrayList();//点击分媒体的表头
       List listZong=new ArrayList();//汇总页面的总计的信息
       List listJCZ=new ArrayList();//基础页面的汇总一
       List listJCHZZ=new ArrayList();//基础页面的汇总二
       List listDJZJDY=new ArrayList();//点击地域汇总
       List listFGGFirst=new ArrayList();//曝光分广告位的基础数据
       
       String [] title={"项目","媒体","预估曝光","曝光次数","曝光达标率","预估点击","点击次数","点击达标率","CTR","访问次数","首页到达率","跳出次数","跳出率","访问时长"};
	   list1.add(title);
	   String [] titleJC1={"","","","","","","udbac广告监测数据","RED","","","","","","","","","udbac（后台数据提供方）提供","","","","","",""};
	   String [] titleJC2={"","","","","","","","","","","","","","","点击率CTR实际结果","","活动网站首页","","","","","",""};
	   String [] titleJC3={"媒体名称","项目","投放位置","投放形式","","","预估曝光","曝光次数","曝光人数","曝光达标率","预估点击","点击次数","点击人数","点击达标率","","","访问次数","首页到达率","访问人数","浏览量","跳出次数","跳出率","访问时长"};
	   list2.add(titleJC1);
	   list2.add(titleJC2);
	   list2.add(titleJC3);
	   //曝光频次分媒体
       String[] titleFMTBGBT={"频次","曝光次数","曝光人数","",""};//分媒体曝光
       listFMT.add(titleFMTBGBT);
       //曝光频次整体
       String[] titleZTBGBT1={activityName,"","","",""};//整体曝光表头1
       String[] titleZTBGBT2={"频次","曝光次数","曝光人数","Impression%","UV"};//整体曝光表头2
       listZTBG.add(titleZTBGBT1);
       listZTBG.add(titleZTBGBT2);
       listDJZT.add(titleZTBGBT1);
       //整体点击的表头信息
       String[] titleZTDJ={"频次","点击次数","点击人数","Click%","UV%"};
       listDJZT.add(titleZTDJ);
       //点击分媒体的表头信息
       String [] titleDJBT={"行标签","点击次数","点击人数"};
       listDJFMTBT.add(titleDJBT);
       //点击地域分布
       String[] titleDJDYFB={"地域名称","NO.","点击次数","点击人数","地域占比"};
       listDJDYFB.add(titleDJDYFB);
       
       //取得预估点击
        Integer ygdja=dataEndService.listYgdj(activityCode); 
        if(ygdja==null){
        	ygdja=1;
        }
     //结案数据汇总报表
	  /* ArrayList<DataEndInfo> huifo = (ArrayList<DataEndInfo>) dataEndService.listDataHuiInfo(actName,actCode,customer_id);   
	   	for(int i=0;i<huifo.size();i++){
	   	         DataEndInfo de=huifo.get(i);//取到当前的循环对象
	   	      if(i<huifo.size()-1){//1到i-1个对象
	   	    	  if(de.getMedia_name().equals(huifo.get(i+1).getMedia_name())){
	   	    	  }else{
	   	    		sumFzcd++; 
	   	    		// listHZ.add(new DataEndUntil().checkoutHui(de,3,ygdja,0,0));//汇总表的信息
	   	    	//总计的值取的是所有值得总计信息
	  			    DataEndInfo deSum= huifo.get(i);
	  			    sumYgbg+=deSum.getExposure_avg();
	       		    sumBgcs+=deSum.getDirty_imp_pv();
	       		    sumYgdj+=deSum.getClick_avg();
	       		    sumDjcs+=deSum.getDirty_clk_pv();
	       		    sumFwcs+=deSum.getVisits();
	       		    sumTccs+=deSum.getBounce_visit();
	       		    sumPjfwsj+=deSum.getVisits_time()*deSum.getVisits();
	       		    sumUnit=deSum.getUnit();
	       		    sumTfl+=deSum.getPut_value();
	       		    actiCode=deSum.getActivity_code();
	       		 //求总计的值得时候 
	       		   DataEndInfo deZong= new DataEndInfo();
	       		    deZong.setExposure_avg(sumYgbg);
	       		    deZong.setDirty_imp_pv(sumBgcs);
	       		    deZong.setClick_avg(sumYgdj);
	       		    deZong.setDirty_clk_pv(sumDjcs);
	       		    deZong.setVisits(sumFwcs);
	       		    deZong.setBounce_visit(sumTccs);
	       		    deZong.setVisits_time(sumPjfwsj/sumFwcs);
	       		    deZong.setUnit(sumUnit);
	       		    deZong.setPut_value(sumTfl);
	       		    deZong.setActivity_code(actiCode);
	       		   String arr[]=new DataEndUntil().checkoutHui(de,3,ygdja,0,0);
		   	    	if(arr[2].equals("N/A")){
	  		    	 }else{
	  		    		sumYuGu+= Double.parseDouble(arr[2].replaceAll(",", "")); 
	  		    	   }
	   	    	    if(arr[5].equals("N/A")){
 		    	    }else{
 		    		sumDiJi+= Double.parseDouble(arr[5].replaceAll(",", "")); 
  	    	         }
	   	           }
	   	    	  }else{
	   	    		  //最后两个参数为预估点击和预估曝光的总计的值
	   	    		//listHZ.add(new DataEndUntil().checkoutHui(de,3,ygdja,0,0));//最后一条数据信息
	   	    		String arr[]=new DataEndUntil().checkoutHui(de,3,ygdja,0,0);
		   	    	if(arr[2].equals("N/A")){
	  		    	 }else{
	  		    		sumYuGu+= Double.parseDouble(arr[2].replaceAll(",", "")); 
	  		    	   }
		   	    	if(arr[5].equals("N/A")){
 		    	    }else{
 		    		sumDiJi+= Double.parseDouble(arr[5].replaceAll(",", "")); 
  	    	        }
	   	    		sumYgbg+=huifo.get(i).getExposure_avg();
	   	    		sumBgcs+=huifo.get(i).getDirty_imp_pv();
	   	    		sumYgdj+=huifo.get(i).getClick_avg();
	   	    		sumDjcs+=huifo.get(i).getDirty_clk_pv();
	   	    		sumFwcs+=huifo.get(i).getVisits();
       		        sumTccs+=huifo.get(i).getBounce_visit();
       		        sumTfl+=huifo.get(i).getPut_value();//投放量
       		        //平均访问时间：每个点位的访问时间乘以访问次数/总的访问次数
       		        sumPjfwsj+=huifo.get(i).getVisits_time()*huifo.get(i).getVisits();
          
       		        unit=huifo.get(i).getUnit();
       		        actiCode=huifo.get(i).getActivity_code();
       		        DataEndInfo deZong= new DataEndInfo();
	       		    deZong.setExposure_avg(sumYgbg);
	       		    deZong.setDirty_imp_pv(sumBgcs);
	       		    deZong.setClick_avg(sumYgdj);
	       		    deZong.setDirty_clk_pv(sumDjcs);
	       		    deZong.setVisits(sumFwcs);
	       		    deZong.setBounce_visit(sumTccs);
	       		    if(sumPjfwsj==0||sumFwcs==0){
	       		     deZong.setVisits_time((double) 0);
	       		    }else{
		       		    deZong.setVisits_time(sumPjfwsj/sumFwcs);
	       		    }
	       		    deZong.setUnit(sumUnit);
	       		    deZong.setPut_value(sumTfl);
	       		    deZong.setActivity_code(actiCode);
	       		    if(i+1==huifo.size()){//取得最后一条的时候再做累加
	       		    	// listZong.add(new DataEndUntil().checkoutHui(deZong,0,ygdja,sumDiJi,sumYuGu));
	       		    	
	       		    }
	   	    	  }
	   	    	
	   	}*/
       //存放的是基础数据中的当前点位的曝光次数
       Map mapPv=new HashMap<String, Double>();
       //存放的是基础数据中的当前点位的曝光人数
       Map mapUv=new HashMap<String, Integer>();
       //存放的是汇总的曝光次数
 	   Map mapHPV=new HashMap<String,Double>();
 		//存放的是汇总的曝光人数
	   Map mapHUV=new HashMap<String,Integer>();
	   
	   //存放的是汇总的曝光次数(总计)
 	   Map mapZPV=new HashMap<String,Double>();
 		//存放的是汇总的曝光人数(总计)
	   Map mapZUV=new HashMap<String,Integer>();
	   
	   //存放的是汇总的点击次数(总计)
 	   Map mapCPV=new HashMap<String,Double>();
 	   //存放的是汇总的点击人数(总计)
	   Map mapCUV=new HashMap<String,Integer>();
	   
	   //存放的是汇总的点击次数
 	   Map mapDPV=new HashMap<String,Double>();
 	   //存放的是汇总的点击人数
	   Map mapDUV=new HashMap<String,Integer>();
	   
	   

	   //结案数据表基础数据
	   ArrayList<DataEndInfo> info = (ArrayList<DataEndInfo>) dataEndService.listDataEndInfo(actName,actCode,customer_id);   
	 
		
	   for(int i=0;i<info.size();i++){
        	  DataEndInfo de=info.get(i);//取到当前的循环对象
        	  //名称为：mic 值为：曝光次数
        	  mapPv.put(de.getMic(), de.getDirty_imp_pv());  
        	  //名称为：mic 值为：曝光人数
        	  mapUv.put(de.getMic(), de.getDirty_imp_uv());
        	  ListJC.add(new DataEndUntil().checkout(de,1,ygdja,0,0));
        	  if(i<info.size()-1){//1到i-1个对象
        		    mtmc=de.getMedia_name();
        		    xm=de.getActivity_name();
        		    tfwz=de.getPut_function();
        		    tfxs=de.getPoint_location();
        		    pjzfc=mtmc+tfwz+tfxs;
        		    mic=de.getMic();
        		    unit=de.getUnit();
        		    if(de.getExposure_avg()==-1){
        		    	ygbg+=0;
        		    }else{
        		         ygbg+=de.getExposure_avg();		
        		    }
        		    bgcs+=de.getDirty_imp_pv();
        		    bgrs+=de.getDirty_imp_uv();
        		    if(de.getClick_avg()==-1){
        		    	ygdj+=0;
        		    }else{
        		    	ygdj+=de.getClick_avg();	
        		    }
        		    djcs+=de.getDirty_clk_pv();
        		    djrs+=de.getDirty_clk_uv();
        		    fwcs+=de.getVisits();
        		    fwrs+=de.getVisitor();
        		    lll+=de.getPv();
				    tccs+=de.getBounce_visit();
        		    pjfwsj+=de.getVisits_time()*de.getVisits();
        		    tfl+=de.getPut_value();
        		    actiCode=de.getActivity_code();
        	 if(info.get(i).getMedia_name().equalsIgnoreCase(info.get(i+1).getMedia_name())){
        			fzcd++;
        		}else{
        		  DataEndInfo dei= new DataEndInfo();
        		    dei.setMedia_name(mtmc);
        		    dei.setActivity_name(xm);
        		    dei.setPut_function(tfwz);
        		    dei.setPoint_location(tfxs);
        		    dei.setMic(mic);
        	        dei.setExposure_avg(ygbg);
        		    dei.setDirty_imp_pv(bgcs);
        		    dei.setDirty_imp_uv(bgrs);
        		    dei.setClick_avg(ygdj);
        		    dei.setDirty_clk_pv(djcs);
        			dei.setDirty_clk_uv(djrs);
        			dei.setVisits(fwcs);
        			dei.setVisitor(fwrs);
        			dei.setBounce_visit(tccs);
        		    dei.setPv(lll);
        		    if(pjfwsj==0||fwcs==0){
        		    	dei.setVisits_time((double) 0);
   	       		    }else{
   	        			dei.setVisits_time(pjfwsj/fwcs);
   	       		    }
        			dei.setUnit(unit);
        			dei.setPut_value(tfl);
        			dei.setActivity_code(actiCode);
        			//需要从里面取得所需要的mic，曝光次数及曝光人数需要从结案数据中的基础数据里面取得？还没有实现呢
        			
        			ListJC.add(new DataEndUntil().checkout(dei,2,ygdja,0,0));//得到汇总信息   
        			 String arr[]=new DataEndUntil().checkout(dei,2,ygdja,0,0);
 		   	    	if(arr[6].equals("N/A")){
 	  		    	 }else{
 	  		    		sumYuGu+= Double.parseDouble(arr[6].replaceAll(",", "")); 
 	  		    	   }
 	   	    	    if(arr[10].equals("N/A")){
  		    	    }else{
  		    		sumDiJi+= Double.parseDouble(arr[10].replaceAll(",", "")); 
   	    	         }
 	   	           
        			listHZ.add(new DataEndUntil().checkout(dei,3,ygdja,0,0));//汇总表的信息
        			mapHPV.put(dei.getMedia_name(), dei.getDirty_imp_pv());
        			mapHUV.put(dei.getMedia_name(), dei.getDirty_imp_uv());
        			
        			mapDPV.put(dei.getMedia_name(), dei.getDirty_clk_pv());
        			mapDUV.put(dei.getMedia_name(), dei.getDirty_clk_uv());
        			
        			listJCHZ.add(new DataEndUntil().checkout(dei,4,ygdja,0,0));//基础表的汇总表的信息

        mtmc="";//媒体名称
        xm="";//项目
        tfwz="";//投放位置
        tfxs="";//投放形式
        mic="";//短代码
        ygbg=0;//预估曝光
        bgcs=0;//曝光次数
        bgrs=0;//曝光人数
        bgdbl=0;//曝光达标率
        ygdj=0;//预估点击
        djcs=0;//点击次数
        djrs=0;//点击人数
        djdbl=0;//点击达标率
        djlCTR=0;//点击率CTR
        fwcs=0;//访问次数
        sydbl=0;//首页达标率
        fwrs=0;//访问人数
        lll=0;//浏览量
        tccs=0;//跳出次数
        tcl=0;//跳出率
        pjfwsj=0;//平均访问时间   
        fzcd=1;
        unit="";
        tfl=0;
        actiCode="";//初始化活动名称
        		  } 
        	  }else{
        	         mtmc=info.get(i).getMedia_name();
        	    	 ygbg+=info.get(i).getExposure_avg();
            	     bgcs+=info.get(i).getDirty_imp_pv();
        	    	 bgrs+=info.get(i).getDirty_imp_uv();
        	    	 ygdj+=info.get(i).getClick_avg();
        	    	 djcs+=info.get(i).getDirty_clk_pv();
        	    	 djrs+=info.get(i).getDirty_clk_uv();
        		     fwcs+=info.get(i).getVisits();
        		     fwrs+=info.get(i).getVisitor();
        		     lll+=info.get(i).getPv();
				     tccs+=info.get(i).getBounce_visit();
        		     pjfwsj+=info.get(i).getVisits_time()*info.get(i).getVisits();
        		     unit=info.get(i).getUnit();
        		     tfl+=info.get(i).getPut_value();
        		     actiCode=info.get(i).getActivity_code();
        	    	
       /* if(info.get(i).getMedia_name().equalsIgnoreCase(info.get(i-1).getMedia_name())){
        			  fzcd++;
        		 }else{*/
        			DataEndInfo dei= new DataEndInfo();
        			dei.setMedia_name(mtmc);
        		    dei.setActivity_name(xm);
        			dei.setPut_function(tfwz);
        			dei.setPoint_location(tfxs);
        			dei.setMic(mic);
        			dei.setExposure_avg(ygbg);
        			dei.setDirty_imp_pv(bgcs);
        			dei.setDirty_imp_uv(bgrs);
        			dei.setClick_avg(ygdj);
        			dei.setDirty_clk_pv(djcs);
        			dei.setDirty_clk_uv(djrs);
        			dei.setVisits(fwcs);
        			dei.setVisitor(fwrs);
        			dei.setPv(lll);
        			dei.setBounce_visit(tccs);
        			if(pjfwsj==0||fwcs==0){
         		    	dei.setVisits_time((double) 0);
    	       		  }else{
    	        	    dei.setVisits_time(pjfwsj/fwcs);
    	       		  }
        			dei.setUnit(unit);
        			dei.setPut_value(tfl);
        			dei.setActivity_code(actiCode);
        			ListJC.add(new DataEndUntil().checkout(dei,2,ygdja,0,0));//得到汇总信息 
        			 String arr[]=new DataEndUntil().checkout(dei,2,ygdja,0,0);
  		   	    	if(arr[6].equals("N/A")){
  	  		    	 }else{
  	  		    		sumYuGu+= Double.parseDouble(arr[6].replaceAll(",", "")); 
  	  		    	   }
  	   	    	    if(arr[10].equals("N/A")){
   		    	    }else{
   		    		sumDiJi+= Double.parseDouble(arr[10].replaceAll(",", "")); 
    	    	         }
        			listHZ.add(new DataEndUntil().checkout(dei,3,ygdja,0,0));//汇总表的基本信息
        			mapHPV.put(dei.getMedia_name(), dei.getDirty_imp_pv());
        			mapHUV.put(dei.getMedia_name(), dei.getDirty_imp_uv());
        			
        			mapDPV.put(dei.getMedia_name(), dei.getDirty_clk_pv());
        			mapDUV.put(dei.getMedia_name(), dei.getDirty_clk_uv());
        		
        			listJCHZ.add(new DataEndUntil().checkout(dei,4,ygdja,0,0));//基础表的汇总表的信息
        mtmc="";//媒体名称
        xm="";//项目
        tfwz="";//投放位置
        tfxs="";//投放形式
        mic="";//短代码
        ygbg=0;//预估曝光
        bgcs=0;//曝光次数
        bgrs=0;//曝光人数
        bgdbl=0;//曝光达标率
        ygdj=0;//预估点击
        djcs=0;//点击次数
        djrs=0;//点击人数
        djdbl=0;//点击达标率
        djlCTR=0;//点击率CTR
        fwcs=0;//访问次数
        sydbl=0;//首页达标率
        fwrs=0;//访问人数
        lll=0;//浏览量
        tccs=0;//跳出次数
        tcl=0;//跳出率
        pjfwsj=0;//平均访问时间   
        actiCode="";
        		   }
       //取得是汇总的值	  
         DataEndInfo deSum= info.get(i);
         if(deSum.getExposure_avg()==-1){
        	 sumYgbg+=0;
         }else{
        	 sumYgbg+=deSum.getExposure_avg();
         }  		    
   		    sumBgcs+=deSum.getDirty_imp_pv();
   		    sumBgrs+=deSum.getDirty_imp_uv();
     		sumYgdj+=deSum.getClick_avg();
   		    sumDjcs+=deSum.getDirty_clk_pv();
   		    sumDjrs+=deSum.getDirty_clk_uv();
     		sumFwcs+=deSum.getVisits();
     		sumFwrs+=deSum.getVisitor();
     		sumLll+=deSum.getPv();
     	    sumTccs+=deSum.getBounce_visit();
     		sumPjfwsj+=deSum.getVisits_time()*deSum.getVisits();
   		    sumUnit=deSum.getUnit();
     		sumTfl+=deSum.getPut_value();
     		actiCode=deSum.getActivity_code();
   		   //求总计的值得时候 
   		   DataEndInfo deZong= new DataEndInfo();
   		    deZong.setExposure_avg(sumYgbg);
   		    deZong.setDirty_imp_pv(sumBgcs);
   		    deZong.setDirty_imp_uv(sumBgrs);
   		    deZong.setClick_avg(sumYgdj);
   		    deZong.setDirty_clk_pv(sumDjcs);
   		    deZong.setDirty_clk_uv(sumDjrs);
   		    deZong.setVisits(sumFwcs);
   		    deZong.setVisitor(sumFwrs);
   		    deZong.setPv(sumLll);
   		    deZong.setBounce_visit(sumTccs);
   		    if(sumPjfwsj==0||sumFwcs==0){
   		    	deZong.setVisits_time((double) 0);
    		  }else{
    	   		deZong.setVisits_time(sumPjfwsj/sumFwcs);
    		  }
   		    deZong.setUnit(sumUnit);
   		    deZong.setPut_value(sumTfl);
   		    deZong.setActivity_code(actiCode);
   		    if(i+1==info.size()){//取得最后一条的时候再做累加
   		    	 listJCZ.add(new DataEndUntil().checkout(deZong,0,ygdja,sumDiJi,sumYuGu));//基础表的总计（第一张报表）
   		    	 listJCHZZ.add(new DataEndUntil().checkout(deZong,6,ygdja,0,0));//基础表的总计（第二张表）
   		    	 //汇总的sheet页的总计的值
   		    	listZong.add(new DataEndUntil().checkout(deZong,5,ygdja,sumDiJi,sumYuGu));
   		    		mapZPV.put(activityName, deZong.getDirty_imp_pv());
   		    		mapZUV.put(activityName, deZong.getDirty_imp_uv());
   		    	
   		    	//存放的是总的点击次数
   		     	mapCPV.put(activityName, deZong.getDirty_clk_pv());
   		        mapCUV.put(activityName, deZong.getDirty_clk_uv());
   		    }
	   }
		//总计完之后需要对下面的值进行初始化，下面做基础总计是需要用到
    	sumYgbg=0;
    	sumBgcs=0;
    	sumBgrs=0;
    	sumYgdj=0;
    	sumDjcs=0;
    	sumDjrs=0;
    	sumFwcs=0;
    	sumFwrs=0;
    	sumLll=0;
    	sumTccs=0;
    	sumPjfwsj=0;
    	sumUnit="";
    	sumTfl=0;
    	actiCode="";
    	sumYuGu=0;
    	sumDiJi=0;
          /**
           * 曝光频次-分媒体
           */
          ArrayList<DataEndInfo> infoFMT = (ArrayList<DataEndInfo>) dataEndService.listDataFMTInfo(actName,actCode,customer_id);   
           /**
            * 取得曝光分媒体的媒体数量          
            */
         /* Integer meiNum=dataEndService.ListMeiNum(actName,actCode,customer_id);
          if(meiNum==null){
        	  meiNum=0;
          }*/
          for(int i=0;i<infoFMT.size();i++){
        	  DataEndInfo de=infoFMT.get(i);//取到当前的循环对象
         		 listFMTJC.add(new DataEndUntil().checkoutFMT(de,1));        	
          }
          /**
           * 曝光整体
           */
          ArrayList<DataEndInfo> infoZTBG= dataEndService.listDataZTBGInfo(actName, actCode, customer_id);
          /**
           * 曝光人数汇总
           */
         Integer bgrsHZ= dataEndService.listDataBGRSInfo(actName, actCode, customer_id);
         if(bgrsHZ==null){
        	 bgrsHZ=0;
         }
         /**
          * 曝光次数汇总
          */
         Double bgcsHZ= dataEndService.listDataBGCSInfo(actName, actCode, customer_id);
         if(bgcsHZ==null){
        	 bgcsHZ=(double) 0;
         }
          for (int i = 0; i < infoZTBG.size(); i++) {
			 DataEndInfo de =infoZTBG.get(i);
			 listZTBGJC.add(new DataEndUntil().checkoutZTBG(de, bgrsHZ,bgcsHZ));
          }
          
          /**
           * 曝光频次分广告位
           */
          ArrayList<DataEndInfo> infoGGW = (ArrayList<DataEndInfo>) dataEndService.listDataFGGWInfo(actName,actCode,customer_id);   
          /**
           * 获得曝光分广告位的数量
           */
        /*  Integer ggNum=dataEndService.listDataGGWNum(actName,actCode,customer_id);
          //
          if(ggNum==null){
        	  ggNum=0;
          }*/
          for(int i=0;i<infoGGW.size();i++){
        	  DataEndInfo de=infoGGW.get(i);//取到当前的循环对象
        	  listFGGJC.add(new DataEndUntil().checkoutFMT(de, 2));
          }
          
          ArrayList<DataEndInfo>infoGGWFirst= (ArrayList<DataEndInfo>) dataEndService.listDataFGGFirstInfo(actName,actCode,customer_id);   
               for(int i=0;i<infoGGWFirst.size();i++){
             	  DataEndInfo de=infoGGWFirst.get(i);//取到当前的循环对象
             	  listFGGFirst.add(new DataEndUntil().checkoutFMT(de, 3));
               }    
          
          
          /**
           * 点击频次分媒体
           */
          ArrayList<DataEndInfo> infoDJFMT = (ArrayList<DataEndInfo>) dataEndService.listDataDJFMTInfo(actName,actCode,customer_id);   
         /**
          * 点击频次分媒体的数量
          */
          Integer djNum=dataEndService.listDatDJNum(actName,actCode,customer_id);
          if(djNum==null){
        	  djNum=0;
          }
          for(int i=0;i<infoDJFMT.size();i++){
        	  DataEndInfo de=infoDJFMT.get(i);
        	  listDJFMT.add(new DataEndUntil().checkoutFMT(de, 4));
          }
          /**
           * 点击整体
           */
         ArrayList<DataEndInfo>infoDJZT=dataEndService.listDataDJZTInfo(actName, actCode, customer_id);
         /**
          * 点击人数汇总
          */
         Integer djrsHZ=dataEndService.listDataDJRSInfo(actName, actCode, customer_id);
         if(djrsHZ==null){
        	djrsHZ=0;
         }
         /**
          * 点击次数汇总
          */
         Integer djcsHZ=dataEndService.listDataDJCSInfo(actName, actCode, customer_id);
         if(djcsHZ==null){
        	 djcsHZ=0;
          }
         for (int i = 0; i < infoDJZT.size(); i++) {
        	 DataEndInfo de=infoDJZT.get(i);
        	 listZTDJJC.add(new DataEndUntil().checkoutZTDJ(de,djrsHZ,djcsHZ));
		}
         /**
          * 点击地域分布
          */
         ArrayList<DataEndInfo>infoDJDY=dataEndService.listDataDJDYInfo(actName, actCode, customer_id);
         /**
          * 点击地域媒体数量
          */
         Integer djMeiNum=dataEndService.listDataDJMeiNumInfo(actName, actCode, customer_id);
         DecimalFormat df0 = new DecimalFormat("#,##0");// 不显示小数点,千分位
         String media_names="";
         if(djMeiNum==null){
        	 djMeiNum=0;
          }
         int changDu=0;
         for (int i = 0; i < infoDJDY.size(); i++) {
        	 
        	 DataEndInfo de=infoDJDY.get(i);
        	 //每个媒体下面的数据长度不足21条的情况
        	 if(i<infoDJDY.size()-1){
        		 if(infoDJDY.get(i).getMedia_name().equalsIgnoreCase(infoDJDY.get(i+1).getMedia_name())){
        			 changDu++;
        			 listDJDYJC.add(new DataEndUntil().checkoutDJDY(de,1));
         			}else{
         			 listDJDYJC.add(new DataEndUntil().checkoutDJDY(de,1));
         			  if(changDu<20){//从0开始最大的值是20
         				 DataEndInfo des=new DataEndInfo();
           			  for(int j=changDu+1;j<=20;j++){//补充数据
           				  des.setMedia_name(de.getMedia_name());
           				 // System.out.println(des.getMedia_name()+"次数："+j);
           				  listDJDYJC.add(new DataEndUntil().checkoutDJDY(des,1));
           			      }  
         			  }
         			 changDu=0;
         			}
        	 }else{
        		 changDu++;
        		 listDJDYJC.add(new DataEndUntil().checkoutDJDY(de,1));
        		 if(changDu<21){
        			 DataEndInfo des=new DataEndInfo();
        			  for(int j=changDu+1;j<=21;j++){//需要补的数据
        				  des.setMedia_name(de.getMedia_name());
        				  listDJDYJC.add(new DataEndUntil().checkoutDJDY(des,1));
        			  }
        		 }
        	 }
        	 djcs+=de.getDirty_clk_pv();
        	 djrs+=de.getDirty_clk_uv();
        	 DataEndInfo des=new DataEndInfo();
        	 des.setDirty_clk_pv(djcs);
        	 des.setDirty_clk_uv(djrs);
        	 if(i+1==infoDJDY.size()){
        		 String[] checkout={"总计","",df0.format(des.getDirty_clk_pv()),df0.format(des.getDirty_clk_uv()),"100.00%"};
            	 listDJZJDY.add(checkout);  
        	 }
        	 
		}
        // System.out.println("测试："+listDJDYJC.size());
         /**
          * 点击分媒体汇总人数
          */
         Map maprsSum=new HashMap<String,Integer>();
         ArrayList<DataEndInfo>infoDJRSNum=dataEndService.listDataDJRSNumInfo(actName, actCode, customer_id);
         for (int i = 0; i < infoDJRSNum.size(); i++) {
			DataEndInfo de=infoDJRSNum.get(i);
			maprsSum.put(de.getMedia(), de.getSumuv());
		}
         /**
          * 点击地域分布中的饼状图的数据取值：截取每个媒体地域的前10位
          */
       /*  ArrayList<DataEndInfo>infoDJBZT=dataEndService.listDataDJDBZTInfo(actName, actCode, customer_id);
         for (int i = 0; i < infoDJBZT.size(); i++) {
        	 DataEndInfo de=infoDJBZT.get(i);
        	 listDJBZT.add(new DataEndUntil().checkoutDJDY(de,2));
		}*/
      //  map.put("listDJBZT",listDJBZT);
        map.put("listJCHZ", listJCHZ);
        map.put("maprsSum", maprsSum);
        map.put("listDJDYJC",listDJDYJC);
        map.put("listDJDYFB",listDJDYFB);
        map.put("listZTDJJC",listZTDJJC);
        map.put("listDJZT", listDJZT);
        map.put("listDJFMT", listDJFMT);
        map.put("listFGGJC", listFGGJC);  
        map.put("listZTBGJC", listZTBGJC);
        map.put("djMeiNum", djMeiNum);
      //  map.put("meiNum", meiNum);
       // map.put("ggNum", ggNum);
        map.put("djNum", djNum);
        map.put("listZTBG", listZTBG);  
        map.put("listFMTJC",listFMTJC); 
        map.put("listFMT",listFMT);   
        map.put("ListJC",ListJC);
        map.put("listHZ",listHZ);
	    map.put("list1", list1);
	    map.put("list2", list2 );
	    map.put("path", path);
	    map.put("smonth", time);//拼接的时间段：开始时间结束时间
	    map.put("activityName", activityName);//显示的是活动名称
	    map.put("listDJFMTBT",listDJFMTBT);//点击分媒体的表头
	    map.put("hzdw",hzdw);//汇总点位
	    map.put("listZong",listZong);//总计：汇总表的
	    map.put("listJCZ",listJCZ);//基础表：基础表的第一个报表的汇总
	    map.put("listJCHZZ",listJCHZZ);//基础表：基础表的第二个报表的汇总
	    map.put("listDJZJDY",listDJZJDY);//点击地域的总计
	    map.put("mapUv", mapUv);//存放的是基础表里面的曝光人数的信息
	    map.put("mapPv", mapPv);//存放的是基础表里面的曝光次数的信息
	    map.put("mapHPV", mapHPV);//存放的是基础表里面每个媒体名称下面的曝光次数的总和
	    map.put("mapHUV", mapHUV);//存放的是基础表里面的每个媒体名称下面的曝光人数的总和
	    map.put("mapZPV", mapZPV);//存放的是基础表里面的总计的曝光次数
	    map.put("mapZUV", mapZUV);//存放的是基础表里面的总计的曝光人数
	    map.put("mapCPV", mapCPV);//存放的是基础表里面的总计的点击次数
	    map.put("mapCUV", mapCUV);//存放的是基础表里面的总计的点击人数
	    map.put("mapDPV", mapDPV);//存放的是基础表里面的每个媒体名称下面的点击次数的总和
	    map.put("mapDUV", mapDUV);//存放的是基础表里面的每个媒体名称下面的点击人数的总和
	    map.put("listFGGFirst",listFGGFirst);//存放的是曝光广告位的每个广告位的信息
	    map.put("activityCode",activityCode);//活动编号
	    WriteExcel(map);
	    return this.LoadExcel(request, response, path);
	   
   }
  
   
   @SuppressWarnings({ "unchecked", "unused", "rawtypes", "resource", "deprecation" })
   public void WriteExcel(Map map){
       String outputFile = (String) map.get("path");
      // int mediaSize=(int) map.get("meiNum");//曝光媒体的数目
       int mediaSize =0;
       //得到的是曝光媒体的个数,从曝光广告位上面取得的
       List<String[]> lggw= (List<String[]>) map.get("listFGGJC");
       for(int i=0;i<lggw.size();i++){
       	if(i<lggw.size()-1){
       	if(lggw.get(i)[0].equals(lggw.get(i+1)[0])){
       	   }else{
       		mediaSize++;
       	   }
       	}else{
       		mediaSize++; 
     	  }
       }
      // int poSize=(int) map.get("ggNum");//曝光广告位的数目
       int poSize=0;
       poSize=lggw.size()/21;//直接从程序里面取得的值
       int meSize=(int) map.get("djNum");
       File file = new File( outputFile);
       XSSFWorkbook workbook; //实例化一个工作簿
       try {
           FileOutputStream out = new FileOutputStream(outputFile);
           workbook = new XSSFWorkbook();
           /**
            * 适用于汇总表的表头
            * 背景颜色一种蓝色
            * 字体颜色为白色，9号字，微软雅黑，字体加粗
            */
           XSSFCellStyle style1 = workbook.createCellStyle();
           new DataEndUntil().setBoderStyle(style1);//修改当前样式的格式信息
   		    style1.setFillForegroundColor(new XSSFColor(new Color(83, 141, 213))); // 汇总数据表头的背景颜色
   		    style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
   		    //字体：白色加粗(该字体被重用多次)
        	XSSFFont fonts = (XSSFFont) workbook.createFont();
        	new DataEndUntil().setFontChinese(fonts);
		    fonts.setColor(new XSSFColor(new Color(255, 255, 255))); // 字体颜色为白色
		    fonts.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		    style1.setFont(fonts);
		    /**
		     * 适用于基础表的表头
		     * 背景颜色为灰色
		     * 字体为9号微软雅黑，字体颜色为白色，有文本转换格式
		     * 
		     */
		    XSSFCellStyle style2 = workbook.createCellStyle();
		    new DataEndUntil().setBoderStyle(style2);//修改当前样式的格式信息
		    style2.setFillForegroundColor(new XSSFColor(new Color(128, 128, 128))); // 汇总数据表头的背景颜色
   		    style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		    style2.setFont(fonts);
		    style2.setWrapText(true);//设置文本的强制换行  
		    CreationHelper helper = workbook.getCreationHelper();
		    style2.setDataFormat(helper.createDataFormat().getFormat("yyyy-MM-dd"));// 设置日期的格式信息
		    /**
		     * 基础数据报表的第二张报表的表头
		     * 背景颜色为绿色，字体颜色为白色
		     * 字体为微软雅黑9号加粗，特殊的格式：文本的强制转换
		     */
		    XSSFCellStyle style3 = workbook.createCellStyle();
		    new DataEndUntil().setBoderStyle(style3);//修改当前样式的格式信息
		    style3.setFillForegroundColor(new XSSFColor(new Color(118, 147,60))); // 汇总数据表头的背景颜色
   		    style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
		    style3.setFont(fonts);//该字体为白色加粗字体
		    style3.setWrapText(true);//设置文本的强制换行  
		    /**
		     * 适用于所有显示的数据为中文
		     * 字体大小为9号
		     */
		    XSSFCellStyle stylezw = workbook.createCellStyle();
			new DataEndUntil().setBoderStyle(stylezw);//修改当前样式的格式信息
			XSSFFont fontzw= (XSSFFont) workbook.createFont();
		    new DataEndUntil().setFontChinese(fontzw);
		    stylezw.setFont(fontzw);
		    
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
		    
		    /**适用于所有显示的基础数据信息
		     * 字体格式为：calibri
			 * 字体大小为：9号，特殊的格式：#,##0
			 * 无边框，居中设置
		     */
			XSSFCellStyle stylemic = workbook.createCellStyle();
			stylemic.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			stylemic.setFont(fontjc);
		    /**
		     *适用于 基础数据报表的汇总部分
		     * 字体格式为：calibri
			 * 字体大小为：9号，特殊的格式：#,##0
		     */
			XSSFCellStyle styleHZ = workbook.createCellStyle();
			styleHZ.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
			XSSFFont fontHZ = (XSSFFont) workbook.createFont();
			new DataEndUntil().setStyleAshy(styleHZ, fontHZ); 
			/**
		     *适用于 基础数据报表的汇总部分
		     * 字体格式为：calibri
			 * 字体大小为：9号，特殊的格式：#,##0.00
		     */
			XSSFCellStyle StyleHZ = workbook.createCellStyle();
			StyleHZ.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
			XSSFFont FontHZ = (XSSFFont) workbook.createFont();
			new DataEndUntil().setStyleAshy(StyleHZ, FontHZ); 
			/**
		     *适用于 基础数据报表的汇总部分
		     * 字体格式为：calibri
			 * 字体大小为：9号，特殊的格式：0.00%
		     */
			XSSFCellStyle StyleHZBFB = workbook.createCellStyle();
			StyleHZBFB.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
			XSSFFont FontHZBFB = (XSSFFont) workbook.createFont();
			new DataEndUntil().setStyleAshy(StyleHZBFB, FontHZBFB); 
			/**
			 * 通用的表头格式
			 * 字体为微软雅黑，字号为9号，字体加粗
			 * 背景颜色为红色，字体颜色为白色
			 */
			XSSFCellStyle styleFMT = workbook.createCellStyle();
			XSSFFont fontFMT = (XSSFFont) workbook.createFont();
			fontFMT.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			new DataEndUntil().setStyleRed(styleFMT, fontFMT);
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
		    /**
		     * 适用excel导出公式做除法算出的百分数
		     * 背景颜色为灰黑色背景色
		     * 字体格式为：calibri
			 * 字体大小为：9号，特殊的格式：0.00%
		     */
		    XSSFCellStyle styleBFSHH = workbook.createCellStyle();
		    styleBFSHH.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
			new DataEndUntil().setBoderStyle(styleBFSHH);//修改当前样式的格式信息
			styleBFSHH.setFillForegroundColor(new XSSFColor(new Color(191, 191, 191))); // 汇总数据表头的背景颜色
			styleBFSHH.setFillPattern(CellStyle.SOLID_FOREGROUND);
			styleBFSHH.setFont(fontBFS);
			/**
			 * 字体格式为：calibri
			 * 字体大小为：9号
			 * 字体加粗
			 */
			CellStyle cellStyle = workbook.createCellStyle();
        	XSSFFont font = workbook.createFont();
        	new DataEndUntil().setFontNumber(font);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);
			/**
			 * 字体格式为：微软雅黑
			 * 字体大小为：9号
			 * 字体加粗
			 */
			CellStyle ceStyle = workbook.createCellStyle();
			XSSFFont fontStyle = workbook.createFont();
			new DataEndUntil().setFontChinese(fontStyle);
			fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			ceStyle.setFont(fontStyle);
			/**
			 * 该格式：适用用汇总基础表的表头
			 * 背景颜色为蓝色，字体颜色为白色
			 * 微软雅黑，9号字，字体加粗
			 */
			 XSSFCellStyle styleBlue = workbook.createCellStyle();
			 XSSFFont fontBlue = workbook.createFont();
		     new DataEndUntil().setStyleBlue(styleBlue, fontBlue);
		     /**
		      * 该格式：适用于汇总基础表的表头
		      * 背景颜色为红色，字体颜色为白色
		      * 微软雅黑，9号字，字体加粗
		      */
		     XSSFCellStyle styleRed = workbook.createCellStyle();
			 XSSFFont fontRed = workbook.createFont();
		     new DataEndUntil().setStyleRed(styleRed, fontRed);
		     /**
		      * 该格式：适用于基础信息的总计
		      * 背景颜色为灰色，字体颜色为白色
		      * 微软雅黑，9号字，字体加粗,特殊格式：#，##0
		      */
		     XSSFCellStyle styleGray = workbook.createCellStyle();
			 XSSFFont fontGray = workbook.createFont();
			 styleGray.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		     new DataEndUntil().setStyleGray(styleGray, fontGray);
		     
		     
		     /**
		      * 该格式：适用于基础信息的总计
		      * 背景颜色为灰色，字体颜色为白色
		      * 微软雅黑，9号字，字体加粗,特殊格式：#，##0.00
		      */
		     XSSFCellStyle StyleGray = workbook.createCellStyle();
		     XSSFFont FontGray = workbook.createFont();
		     StyleGray.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
		     new DataEndUntil().setStyleGray(StyleGray, FontGray);
		     /**
		      * 该格式：适用于基础信息的总计
		      * 背景颜色为灰色，字体颜色为白色
		      * 微软雅黑，9号字，字体加粗,特殊格式：0.00%
		      */
		     XSSFCellStyle StyleGrayBFB = workbook.createCellStyle();
		     XSSFFont FontGrayBFB = workbook.createFont();
		     StyleGrayBFB.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
		     new DataEndUntil().setStyleGray(StyleGrayBFB, FontGrayBFB);
		     
		     /**
		      * 该格式：适用于汇总sheet页的总计信息
		      * 背景颜色为绿色。字体颜色为黑色
		      * 微软雅黑，9号字,特殊格式：#,##0
		      */
		     XSSFCellStyle styleGreen = workbook.createCellStyle();
			 XSSFFont fontGreen = workbook.createFont();
			 styleGreen.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		     new DataEndUntil().setStyleGreen(styleGreen, fontGreen);
		     
		     /**
		      * 该格式：适用于汇总sheet页的总计信息
		      * 背景颜色为绿色。字体颜色为黑色
		      * 微软雅黑，9号字,特殊格式：#,##0.00
		      */
		     XSSFCellStyle StyleGreen = workbook.createCellStyle();
		     XSSFFont FontGreen = workbook.createFont();
		     StyleGreen.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##.00"));
		     new DataEndUntil().setStyleGreen(StyleGreen, FontGreen);
		     /**
		      * 该格式：适用于汇总sheet页的总计信息
		      * 背景颜色为绿色。字体颜色为黑色
		      * 微软雅黑，9号字,特殊格式：0.00%
		      */
		     XSSFCellStyle StyleGreenBFB = workbook.createCellStyle();
		     XSSFFont FontGreenBFB = workbook.createFont();
		     StyleGreenBFB.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
		     new DataEndUntil().setStyleGreen(StyleGreenBFB, FontGreenBFB);
		     
		     /**
		      *该格式：适用于基础数据的信息
		      *特殊格式：#,##.00
		      */
		     XSSFCellStyle Style = workbook.createCellStyle();
		     new DataEndUntil().setBoderStyle(Style);//修改当前样式的格式信息
		     Style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##.00"));
		     Style.setFont(fontjc);
		     /**
		      *该格式：适用于基础数据的信息
		      *特殊格式：0.00%
		      */
		     XSSFCellStyle StyleBFB = workbook.createCellStyle();
		     new DataEndUntil().setBoderStyle(StyleBFB);//修改当前样式的格式信息
		     StyleBFB.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
		     StyleBFB.setFont(fontjc);
    /**
	 * 第一张sheet表:数据汇总表
	 */
		   //汇总点位为8的时候，不再显示汇总表
		     String hzdw=(String) map.get("hzdw");
		     if(hzdw==null){
		    	 hzdw="1";
		     }
		    	 XSSFSheet sheet1 = workbook.createSheet("汇总数据");
			        sheet1.setDisplayGridlines(false);// 设置无边框
			        sheet1.setColumnHidden(16,true);//隐藏列
			        List<String[]> l = new ArrayList<String[]>();
			        l=(List<String[]>) map.get("list1");
		            int rowNum=1;//起始行的值
		            for(int i=0;i<l.size();i++){
		               XSSFRow row=sheet1.createRow(rowNum++);
		               String[] str = l.get(i);
		                   for(int j=0;j<str.length;j++){
		                	   row.setHeight((short) 580);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
		                       XSSFCell cell=row.createCell(j+1);
		                       cell.setCellValue(str[j]);
		                       cell.setCellStyle(style1);
		               }
		           }
		           
		           List<String[]> lHZ = new ArrayList<String[]>();
		           lHZ=(List<String[]>) map.get("listHZ");
		           rowNum=2;//起始行的值
		           for(int i=0;i<lHZ.size();i++){
		               XSSFRow row=sheet1.createRow(rowNum++);
		               String[] str = lHZ.get(i);
		                   for(int j=0;j<str.length;j++){
		                	   XSSFCell cell=row.createCell(j+1);
		                	   cell.setCellValue(str[j]);
		                	   if(str[0]!=null&&j==0||str[1]!=null&&j==1){
			                       cell.setCellStyle(stylezw);
		                	   }else{
		                    	   if(str[2]!=null&&j==2||str[3]!=null&&j==3||str[5]!=null&&j==5||str[6]!=null&&j==6||str[9]!=null&&j==9||str[11]!=null&&j==11){
		                    		    if(str[j].equals("N/A")||str[j].equals("")){
		                    		    	cell.setCellValue(str[j]); 
		                    		    	cell.setCellStyle(stylezw);
		                    		    }else{
		                    		    	 cell.setCellValue(Double.parseDouble(str[j])); 
				                    		 cell.setCellStyle(stylejc); 
		                    		    }
		                    		  
		                    	   }else{
		                    		   //含有百分比的数字
		                    		   if(str[13]!=null&&j==13){
		                    			   if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")||str[j].equals("")){
		                    				   cell.setCellValue(str[j]); 
		                    				   cell.setCellStyle(stylejc); 
		                    			   }else{
		                    				   cell.setCellValue(Double.parseDouble(str[j])); 
				                    		   cell.setCellStyle(Style);  
		                    			   }
		                    		   }else{
		                    			   if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")||str[j].equals("")){
		                    				   cell.setCellValue(str[j]); 
		                    				   cell.setCellStyle(stylejc);  
		                    			    }else{
		                    				   cell.setCellValue(Double.parseDouble(str[j])/100); 
				                    		   cell.setCellStyle(StyleBFB);  
		                    			   }
			                    		   
		                    		   }
		                    		 
		                    	   }
			                     
		                	   }
		               }
		                  
		           }
		           if (lHZ.size()>1) {
		        	   //当有数据的时候，再加这个汇总就不会报错了
	                   sheet1.addMergedRegion(new CellRangeAddress(2, 1+lHZ.size(), 1, 1));   

				}
		         
		           //第一张总计的汇总
		           	rowNum=2+lHZ.size();
			        List<String[]> lZong = new ArrayList<String[]>();
			        lZong=(List<String[]>) map.get("listZong");
			         for(int i=0;i<lZong.size();i++){
			               XSSFRow row=sheet1.createRow(rowNum++);
			               String[] str = lZong.get(i);
			                   for(int j=0;j<str.length;j++){
			                	   XSSFCell cell=row.createCell(j+1);
			                	   if(str[0]!=null&&j==0||str[1]!=null&&j==1){
			                		   cell.setCellValue(str[j]);
			                		   cell.setCellStyle(styleGreen);
			                	   }else{
			                    	   if(str[2]!=null&&j==2||str[3]!=null&&j==3||str[5]!=null&&j==5||str[6]!=null&&j==6||str[9]!=null&&j==9||str[11]!=null&&j==11){
			                    		  if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
			                    			  cell.setCellValue(str[j]); 
			                    			  cell.setCellStyle(styleGreen); 
			                    		  }else{
			                    			  if(str[j]==null||str[j].equals("")){
			                    				  cell.setCellValue("N/A"); 
					                    		  cell.setCellStyle(styleGreen);  
			                    			  }else{
			                    				  cell.setCellValue(Double.parseDouble(str[j])); 
					                    		  cell.setCellStyle(styleGreen);  
			                    			  }
			                    			  
			                    		  }
			                    	   }else{
			                    		   if(str[13]!=null&&j==13){
			                    			   if(str[j].equals("N/A")||str[j]==null||str[j].equals("")){
			                    				      cell.setCellValue(str[j]); 
					                    			  cell.setCellStyle(styleGreen); 
					                    		  }else{
					                    			  cell.setCellValue(Double.parseDouble(str[j])); 
						                    		  cell.setCellStyle(StyleGreen);   
					                    		  }
			                    			  
			                    		   }else{
			                    			   if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
			                    				   cell.setCellValue(str[j]); 	  
			                    				   cell.setCellStyle(styleGreen); 
					                    		  }else{
					                    			  cell.setCellValue(Double.parseDouble(str[j])/100); 
						                    		  cell.setCellStyle(StyleGreenBFB);  
					                    		  }
			                    		    }
			                    		  
			                    	   }
				                     
			                	   }
			                      
			               }
			              
			           }
			        
		  			sheet1.addMergedRegion(new CellRangeAddress(2+lHZ.size(), 2+lHZ.size(), 1, 2));
		  			
		   
	       
           
    /**
     * 第二张数据表，结案数据报表的明细报表
     */
           XSSFSheet sheet2 = workbook.createSheet("基础数据");//建立第二张表
           sheet2.setDisplayGridlines(false);// 设置无边框
           List<String[]> lBT = new ArrayList<String[]>();
           lBT=(List<String[]>) map.get("list2");
            rowNum=7;//起始行的值
           for(int i=0;i<lBT.size();i++){
               XSSFRow row=sheet2.createRow(rowNum++);
               if(rowNum==10){//设置特殊的行高
            	  row.setHeight((short) 660);// 目的是想把行高设置成33*20,poi转化为像素需要*20
               }
               String[] str = lBT.get(i);
                   for(int j=0;j<str.length;j++){
                       XSSFCell cell=row.createCell(j+1);
                       if(str[6].equals("udbac广告监测数据")&&j==6){
                    	   cell.setCellValue(str[j]);
                    	   cell.setCellStyle(styleRed);
                       }else if(str[7].equals("RED")&&j==15){//得把合并单元格的格数去除掉
                    	   cell.setCellStyle(styleRed);
                       }else if(str[16].equals("udbac（后台数据提供方）提供")&&j==16){
                    	   cell.setCellValue(str[j]);
                    	   cell.setCellStyle(styleBlue);
                       }else{
                    	   cell.setCellValue(str[j]);
                           cell.setCellStyle(style2); 
                       }
                  }
             }
           
             List<String[]> lJC = new ArrayList<String[]>();
             lJC=(List<String[]>) map.get("ListJC");
             rowNum=10;//起始行的值
             for(int i=0;i<lJC.size();i++){
                 XSSFRow row=sheet2.createRow(rowNum++);
                 String[] str = lJC.get(i);
                      for(int j=0;j<str.length;j++){
                          XSSFCell cell=row.createCell(j+1);
                             if(str[1]==null||str[1].equals("")){
                            	cell.setCellValue(str[j]); 
                                cell.setCellStyle(styleHZ); 
                                if(str[9]!=null&&j==9||str[13]!=null&&j==13||str[14]!=null&&j==14||str[15]!=null&&j==15||str[17]!=null&&j==17||str[21]!=null&&j==21){
                                	 if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                                		 cell.setCellValue(str[j]); 
                            			 cell.setCellStyle(styleHZ); 
                            		 }else{
                            			 cell.setCellValue(Double.parseDouble(str[j])/100); 
    		                    		 cell.setCellStyle(StyleHZBFB);  
                            		 }
                              	 }else if(str[22]!=null&&j==22){
                              		 if(str[j].equals("N/A")){
                              			cell.setCellValue(str[j]); 
                              			 cell.setCellStyle(styleHZ);
                              		 }else{
                              			 cell.setCellValue(Double.parseDouble(str[j])); 
      		                    		 cell.setCellStyle(StyleHZ);   
                              		 }
                              	 }else if(str[6]!=null&&j==6||str[7]!=null&&j==7||str[8]!=null&&j==8||str[10]!=null&&j==10||str[11]!=null&&j==11||str[12]!=null&&j==12||str[14]!=null&&j==14||str[16]!=null&&j==16||str[18]!=null&&j==18||str[19]!=null&&j==19||str[20]!=null&&j==20){
                               		if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                                 		 cell.setCellValue(str[j]); 
                              			 cell.setCellStyle(styleHZ); 
                              		    }else{
                              			 cell.setCellValue(Double.parseDouble(str[j])); 
   		                    		     cell.setCellStyle(styleHZ); 
                              		    }
                                 	 }
                             }else if(str[0]!=null&&j==0||str[1]!=null&&j==1||str[2]!=null&&j==2||str[3]!=null&&j==3||str[4]!=null&&j==4||str[5]!=null&&j==5||str[15]!=null&&j==15){
                            	         cell.setCellValue(str[j]); 
                            	         cell.setCellStyle(stylezw); 
                             }else{
                            	 if(str[9]!=null&&j==9||str[13]!=null&&j==13||str[14]!=null&&j==14||str[15]!=null&&j==15||str[17]!=null&&j==17||str[21]!=null&&j==21){
                            		 if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                            			 cell.setCellValue(str[j]); 
                            			 cell.setCellStyle(stylejc); 
                            		 }else{
                            			 cell.setCellValue(Double.parseDouble(str[j])/100); 
    		                    		 cell.setCellStyle(StyleBFB);  
                            		 }
                            	 }else if(str[22]!=null&&j==22){
                            		 if(str[j].equals("N/A")){
                            			 cell.setCellValue(str[j]); 
                              			 cell.setCellStyle(stylejc);
                              		 }else{
                              			 cell.setCellValue(Double.parseDouble(str[j])); 
      		                    		 cell.setCellStyle(Style);   
                              		 }
                            	 }else{
                            		if(str[j]!=null){
                            			 if(str[j].equals("N/A")){
                                    		   cell.setCellValue(str[j]); 
                                    		   cell.setCellStyle(stylejc);   
                                    		 }else{
                                    		   cell.setCellValue(Double.parseDouble(str[j])); 
          		                    		   cell.setCellStyle(stylejc);  
                                    		 }
                            		}else{
                            			cell.setCellValue("N/A"); 
                             		   cell.setCellStyle(stylejc);   
                            		}
                            			
                            		}
                            		
                            	 
                            	  
                               }
                         }
                      if(str[1]==null||str[1].equals("")){
           			  //合并单元格
           			 sheet2.addMergedRegion(new CellRangeAddress(10+i, 10+i, 1, 3));
           		}
             }
             //基础表的汇总信息
             List<String[]> lJCZ = new ArrayList<String[]>();
             lJCZ=(List<String[]>) map.get("listJCZ");
             rowNum=10+lJC.size();//起始行的值
             for(int i=0;i<lJCZ.size();i++){
                 XSSFRow row=sheet2.createRow(rowNum++);
                 String[] str = lJCZ.get(i);
                      for(int j=0;j<str.length;j++){
                          XSSFCell cell=row.createCell(j+1);
                           	 if(str[9]!=null&&j==9||str[13]!=null&&j==13||str[14]!=null&&j==14||str[15]!=null&&j==15||str[17]!=null&&j==17||str[21]!=null&&j==21){
                           		 if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                           		     cell.setCellValue(str[j]); 
                        			 cell.setCellStyle(styleGray); 
                        		 }else{
                        			 cell.setCellValue(Double.parseDouble(str[j])/100); 
    		                    	 cell.setCellStyle(StyleGrayBFB); 
                        		 }
                           	 }else if(str[22]!=null&&j==22){
                           		 if(str[j].equals("N/A")){
                           			cell.setCellValue(str[j]); 
                        			 cell.setCellStyle(styleGray); 
                        		 }else{
                        			 cell.setCellValue(Double.parseDouble(str[j])); 
    		                    	 cell.setCellStyle(StyleGray);
                        		 }
                           	 }else{
                           		 if(str[0]!=null&&j==0||str[1].equals("")&&j==1||str[2].equals("")&&j==2||str[3]!=null&&j==3||str[4]!=null&&j==4||str[5].equals("")&&j==5){
                           			 cell.setCellValue(str[j]);
                           			 cell.setCellStyle(styleGray);
                           		 }else{
                           			 if(str[j].equals("N/A")||str[j].equals("")){
                           				 cell.setCellValue(str[j]); 
                            			 cell.setCellStyle(styleGray); 
                            		 }else{
                            			 cell.setCellValue(Double.parseDouble(str[j])); 
        		                    	 cell.setCellStyle(styleGray);
                            		 }
                           		 }
                            }
                         }
                      sheet2.addMergedRegion(new CellRangeAddress(10+lJC.size()+i, 10+lJC.size()+i, 1, 3));
             }
             
            
      //第二张数据报表的汇总表信息
             String [] jcsjhz={"媒体 "," 曝光次数"," 曝光人数","点击次数","点击人数","点击转化率CTR(Imp-based)","点击转化率CTR(Imp-based)"};
             List listjcsjhz=new ArrayList();
             listjcsjhz.add(jcsjhz);
             rowNum=lJC.size()+16;//距离上面的表格相差6个单元格
             for (int i = 0; i < listjcsjhz.size(); i++) {
				String[] str=(String[]) listjcsjhz.get(i);
				 XSSFRow row=sheet2.createRow(rowNum++);
            	 row.setHeight((short) 915);// 目的是想把行高设置成45.75*20,poi转化为像素需要*20
				 for (int j = 0; j < str.length; j++) {
					  XSSFCell cell=row.createCell(j+7);//为了与上面的表头进行对齐
					  cell.setCellValue(str[j]);
					  cell.setCellStyle(style3);
				}
			 }
             rowNum=lJC.size()+17;//距离上面的表格相差6个单元格
             lBT=(List<String[]>) map.get("listJCHZ");
             for(int i=0;i<lBT.size();i++){
                 XSSFRow row=sheet2.createRow(rowNum++);
                 String[] str = lBT.get(i);
                     for(int j=0;j<str.length;j++){
                         XSSFCell cell=row.createCell(j+7);
                         if(str[1]!=null&&j==1||str[2]!=null&&j==2||str[3]!=null&&j==3||str[4]!=null&&j==4){
                        	 if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                        		 cell.setCellValue(str[j]); 
                    			 cell.setCellStyle(stylejc); 
                    		 }else{
                    			 cell.setCellValue(Double.parseDouble(str[j])); 
                      	     	 cell.setCellStyle(stylejc);  
                    		 }
                         }else if(str[5]!=null&&j==5||str[6]!=null&&j==6){
                        	 if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                        		 cell.setCellValue(str[j]); 
                    			 cell.setCellStyle(stylejc); 
                    		 }else{
                    			 cell.setCellValue(Double.parseDouble(str[j])/100); 
	                    		 cell.setCellStyle(StyleBFB);  
                    		 }
                         }else{
                        	 cell.setCellValue(str[j]);
                        	 cell.setCellStyle(stylejc); 
                         }
                        
                    }
              }
             
             List<String[]> lJHZZ = new ArrayList<String[]>();
             lJCZ=(List<String[]>) map.get("listJCHZZ");
             rowNum=17+lJC.size()+lBT.size();//起始行的值
             for(int i=0;i<lJCZ.size();i++){
                 XSSFRow row=sheet2.createRow(rowNum++);
                 String[] str = lJCZ.get(i);
                      for(int j=0;j<str.length;j++){
                          XSSFCell cell=row.createCell(j+7);
                          if(str[1]!=null&&j==1||str[2]!=null&&j==2||str[3]!=null&&j==3||str[4]!=null&&j==4){
                        	  if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                        		 cell.setCellValue(str[j]); 
                     			 cell.setCellStyle(styleGray); 
                     		 }else{
                     			 cell.setCellValue(Double.parseDouble(str[j])); 
                       	     	 cell.setCellStyle(styleGray);   
                     		 } 
                          }else if(str[5]!=null&&j==5||str[6]!=null&&j==6){
                        	  if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                        		 cell.setCellValue(str[j]); 
                      			 cell.setCellStyle(styleGray); 
                      		 }else{
                      			 cell.setCellValue(Double.parseDouble(str[j])/100); 
                     	     	 cell.setCellStyle(StyleGrayBFB);  
                      		 } 
                          }else{
                        	     cell.setCellValue(str[j]);
                         	     cell.setCellStyle(styleGray); 
                            }
                         }
             }
             
           //设置表头特殊的合并单元格的格式
         	sheet2.addMergedRegion(new CellRangeAddress(7, 7, 7, 15));
         	sheet2.addMergedRegion(new CellRangeAddress(7, 7, 17,23));
         	sheet2.addMergedRegion(new CellRangeAddress(8, 8, 7, 14));
         	sheet2.addMergedRegion(new CellRangeAddress(8, 8, 17,23));
         	sheet2.addMergedRegion(new CellRangeAddress(8, 9, 15,15));
         	sheet2.addMergedRegion(new CellRangeAddress(8, 9, 16,16));
            sheet2.addMergedRegion(new CellRangeAddress(5, 5, 1,15));
         	//
         
			XSSFRow row1 = sheet2.createRow(1);// 需要新建表格，来获取当前的信息
			XSSFRow row2 = sheet2.createRow(2);
			XSSFRow row3 = sheet2.createRow(3);

			XSSFCell cell1 = row1.createCell(1);
			XSSFCell cell2 = row2.createCell(1);
			XSSFCell cell3 = row3.createCell(1);
			XSSFCell cell4 = row3.createCell(2);
			XSSFCell cell5 = row2.createCell(2);
			XSSFCell cell6 = row1.createCell(2);
		   /**
		    * 设置第二个sheet页的左上角
		    */
			cell1.setCellValue("Campaign:");
			cell1.setCellStyle(cellStyle);
			cell2.setCellValue("Period:");
			cell2.setCellStyle(cellStyle);
			cell3.setCellValue("Preparedby:");
			cell3.setCellStyle(cellStyle);
			cell4.setCellValue("UDBAC");
			cell4.setCellStyle(cellStyle);
			cell5.setCellValue((String)map.get("smonth"));// 获得填写的日期信息
			cell5.setCellStyle(cellStyle);
			cell6.setCellValue((String)map.get("activityName"));//填写的是活动名称
			cell6.setCellStyle(ceStyle);
			//设置特殊列宽
			sheet2.setColumnWidth(3, 15 * 256);// 设置第三列的列宽，估计值
  			sheet2.setColumnWidth(4, 45 * 256);// 设置第四列的列宽，估计值
			sheet2.setColumnWidth(5, 60 * 256);// 设置第五列的列宽
			sheet2.setColumnWidth(6, 20 * 256);//设置第六列的列宽，取得是估计值
	/**
	 * 第三张表： 曝光频次-整体
	 */
       if(!hzdw.equals("8")){
				XSSFSheet sheet3=workbook.createSheet("曝光频次-整体");
				sheet3.setDisplayGridlines(false);//设置无边框
		        List list1=new ArrayList();
		        List<String[]> l1=(List<String[]>) map.get("listZTBG");
	            rowNum=0;//起始行的值
	         	for(int i=0;i<l1.size();i++){
	                XSSFRow row=sheet3.createRow(rowNum++);
	                String[] str = l1.get(i);
	                   for(int j=0;j<str.length;j++){
	                       XSSFCell cell=row.createCell(j+1);
	                         	    cell.setCellValue(str[j]); 
	                         	    cell.setCellStyle(styleFMT);
	                     }
	            }
	         	
	           
	         	l1=(List<String[]>) map.get("listZTBGJC");
	        	StringBuffer a=new StringBuffer();
            	StringBuffer c=new StringBuffer();
            	 for(int n=1;n<=mediaSize;n++){
            		 String b=new EXCELUtil().getColumnName(n*jianju-3);
            		 String d=new EXCELUtil().getColumnName(n*jianju-2);
            		 String[] str = l1.get(n);
            		   if(n==1&&mediaSize>1){
            			 a.append("sum('曝光频次-分媒体'!"+b+"5,"); 
            			 c.append("sum('曝光频次-分媒体'!"+d+"5,"); 
            		   }else if(n==1&&mediaSize==1){//考虑媒体名称为1的情况下
            			 a.append("sum('曝光频次-分媒体'!"+b+"5)"); 
              			 c.append("sum('曝光频次-分媒体'!"+d+"5)"); 
            		   } else if(n<mediaSize){
            			 a.append("'曝光频次-分媒体'!"+b+"5,");
            			 c.append("'曝光频次-分媒体'!"+d+"5,");
            		   }else{
            			 a.append("'曝光频次-分媒体'!"+b+"5)");
            			 c.append("'曝光频次-分媒体'!"+d+"5)");
            		 }
            	 }

	         	//动态生成行数
				int sheet3Row=l1.size();
	            rowNum=2;//起始行的值
	         	for(int i=0;i<l1.size();i++){
	         		XSSFRow row=sheet3.createRow(rowNum++);
	                String[] str = l1.get(i);
	                   for(int j=0;j<str.length;j++){
	                       XSSFCell cell=row.createCell(j+1);
	                       if(str[0].equals("21")&&j==0){
                      	     cell.setCellValue("21+"); 
                      	     cell.setCellStyle(stylejc);
	                      	}else if(!str[0].equals("21")&&j==0){
	                      		 cell.setCellValue(Double.parseDouble(str[0])); 
	                      		 cell.setCellStyle(stylejc);
	                      	}else if(str[1]!=null&&j==1){
	                        	 cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	                        	 int z=i+5;
	                        	 if(a.length()==0){
	                        	 }else{
	                        		 cell.setCellFormula(a.toString().replace("5", ""+z+""));  
	                        	 }
                            	 cell.setCellStyle(stylejc);
	                         }else if(str[2]!=null&&j==2){
	                        	 cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	                        	 int z=i+5;
	                        	 if(c.length()==0){
	                        	 }else{
	                            	 cell.setCellFormula(c.toString().replace("5", ""+z+"")); 
	                        	 }
                            	 
                            	 cell.setCellStyle(stylejc);
	                         }else if(str[3]!=null&&j==3||str[4]!=null&&j==4){
                       	       if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
                     	    	   cell.setCellStyle(stylejc);
                     	    	   cell.setCellValue(str[j]); 
                     	    	}else{
                     	    		cell.setCellValue(Double.parseDouble(str[j])/100); 
	                        	     	cell.setCellStyle(StyleBFB);   
                     	    	  }
                           }else{
	         	                       cell.setCellValue(str[j]); 
		                         	   cell.setCellStyle(stylejc);
	                         	    }
	                           }
	                  }
	           
	           if(l1.size()>0){
	        	   //校验数据
	                 XSSFRow row=sheet3.createRow(sheet3Row+2);
	                 XSSFCell cellcheck= row.createCell(1);
	                          cellcheck.setCellValue("校验数据：");
	                          cellcheck.setCellStyle(stylemic); 
	                          Map maps=(Map) map.get("mapZPV");
	                 XSSFCell cellcheckPv= row.createCell(2);
	                          cellcheckPv.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	                          if((Double)maps.get(map.get("activityName"))!=null){
		                          cellcheckPv.setCellFormula("IF("+(Double)maps.get(map.get("activityName"))+"-SUM(C3:C23)<=0,\"OK\",\"Check\")");
	                          }
	                          cellcheckPv.setCellStyle(stylemic);
	                          Map mapp=(Map) map.get("mapZUV");
	     	         XSSFCell cellcheckUv= row.createCell(3);
	     	                  cellcheckUv.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	     	                  if((Integer)mapp.get(map.get("activityName"))!=null){
		     	                  cellcheckUv.setCellFormula("IF("+(Integer)mapp.get(map.get("activityName"))+"-SUM(D3:D23)=0,\"OK\",\"Check\")");
	     	                  }
	     	                  cellcheckUv.setCellStyle(stylemic);
	                        
	            	 //通过excel的公式进行计算
	                  row=sheet3.createRow(sheet3Row+5);
	                XSSFCell cellztbg= row.createCell(1);
	                cellztbg.setCellValue("1~6");
	                cellztbg.setCellStyle(stylejc);
	                XSSFCell cellzt= row.createCell(2);
	                cellzt.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	                cellzt.setCellFormula("SUM(C3:C8)");
	                cellzt.setCellStyle(stylejc);
	                XSSFCell celluv= row.createCell(3);
	                celluv.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	                celluv.setCellFormula("SUM(D3:D8)");
	                celluv.setCellStyle(stylejc);
	                 
	                row=sheet3.createRow(sheet3Row+6);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("7~10");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	                cellzt.setCellFormula("SUM(C9:C12)");
	                cellzt.setCellStyle(stylejc);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("SUM(D9:D12)");
	                celluv.setCellStyle(stylejc);
	                 
	                row=sheet3.createRow(sheet3Row+7);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("11~20");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	                cellzt.setCellFormula("SUM(C13:C22)");

	                cellzt.setCellStyle(stylejc);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("SUM(D13:D22)");
	                celluv.setCellStyle(stylejc);
	                 
	                row=sheet3.createRow(sheet3Row+8);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("21+");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	                cellzt.setCellFormula("(C23)");
	                cellzt.setCellStyle(stylejc);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("(D23)");
	                celluv.setCellStyle(stylejc);
	                 
	                row=sheet3.createRow(sheet3Row+9);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("total");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	                cellzt.setCellFormula("SUM(C27:C30)");
	                cellzt.setCellStyle(stylejc);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("SUM(D27:D30)");
	                celluv.setCellStyle(stylejc);
	                
	                //通过excel的公式进行计算
	                row=sheet3.createRow(sheet3Row+12);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("1~6");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	               
	                cellzt.setCellFormula("IFERROR(C27/C31,\"N/A\")");
	                cellzt.setCellStyle(styleBFS);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("IFERROR(D27/D31,\"N/A\")");
	                celluv.setCellStyle(styleBFS);
	                  
	                row=sheet3.createRow(sheet3Row+13);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("7~10");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	                cellzt.setCellFormula("IFERROR(C28/C31,\"N/A\")");
	                cellzt.setCellStyle(styleBFS);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("IFERROR(D28/D31,\"N/A\")");
	                celluv.setCellStyle(styleBFS);
	                 
	                row=sheet3.createRow(sheet3Row+14);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("11~20");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	                cellzt.setCellFormula("IFERROR(C29/C31,\"N/A\")");
	                cellzt.setCellStyle(styleBFS);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("IFERROR(D29/D31,\"N/A\")");
	                celluv.setCellStyle(styleBFS);
	                 
	                row=sheet3.createRow(sheet3Row+15);
	                cellztbg= row.createCell(1);
	                cellztbg.setCellValue("21+");
	                cellztbg.setCellStyle(stylejc);
	                cellzt= row.createCell(2);
	                cellzt.setCellFormula("IFERROR(C30/C31,\"N/A\")");
	                cellzt.setCellStyle(styleBFS);
	                celluv= row.createCell(3);
	                celluv.setCellFormula("IFERROR(D30/D31,\"N/A\")");
	                celluv.setCellStyle(styleBFS);
	           }
	           
	        //曝光整体第三张表的表头
	            XSSFRow  row=sheet3.createRow(sheet3Row+11);
	         	String [] checkout2={"频次","整体曝光频次","整体曝光频次"};
	 	        list1.add(checkout2);
	            for (int i = 0; i <list1.size(); i++) {
	            	 String[] str = (String[]) list1.get(i);
	                 for(int j=0;j<str.length;j++){
	                   	 XSSFCell cell=row.createCell(j+1);
	                       	      cell.setCellValue(str[j]); 
	                       	      cell.setCellStyle(styleFMT);
	                       }
	            }
	         
	        //曝光整体第二张表的表头
	     	row=sheet3.createRow(sheet3Row+4);
	     	String [] checkout1={"频次","整体曝光频次","整体UV频次"};
		        list1.add(checkout1);
		        l1=list1;
	         for (int i = 0; i <l1.size(); i++) {
	        	 String[] str = l1.get(i);
	             for(int j=0;j<str.length;j++){
	               	 XSSFCell cell=row.createCell(j+1);
	                          cell.setCellValue(str[j]); 
	          	              cell.setCellStyle(styleFMT);
	                   }
	         }
	          //将合并单元格的信息放在这里
	          sheet3.addMergedRegion(new CellRangeAddress(0, 0, 1, 5));
	 		
	   }
			
	/**
	 * 第四张sheet表曝光分媒体
	 * 
	 */
	   if(!hzdw.equals("8")){
			XSSFSheet sheet4 = workbook.createSheet("曝光频次-分媒体");
	        sheet4.setDisplayGridlines(false);// 设置无边框
	        //曝光分媒体表头
        	 List<String[]>l1=(List<String[]>) map.get("listFMT");
        	 //通过循环将角标拿出来
        	 StringBuffer sbpv=new StringBuffer();
        	 StringBuffer sbuv=new StringBuffer();
         	  String activityCode=(String) map.get("activityCode");
             if(activityCode.contains("JT")&&!activityCode.contains("MGJT")){
            	 //用来做公式用的
            	 List<String[]>first=(List<String[]>) map.get("listFGGFirst");
            	 if(first.size()==1){//数据只有一条的时候：
             		sbpv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(2)+"5,");
         	     	sbuv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(3)+"5,");
         		}else{//数据大于一条的时候：
         			 for(int i=0;i<first.size();i++){
                    	 if(i==0&&!first.get(0)[0].equals(first.get(1)[0])){
                			//表示的是第一条与第二条数据相等的情况
                    		sbpv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i+2)+"5;");
                    		sbuv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i+3)+"5;");
                    	}else if(i==0&&first.get(0)[0].equals(first.get(1)[0])){
                    		sbpv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i+2)+"5,");
                    		sbuv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i+3)+"5,");
                    	}else if(i<first.size()-1){
                     	  if(first.get(i)[0].equals(first.get(i+1)[0])){
                     		sbpv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i*5+2)+"5,");
                     		sbuv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i*5+3)+"5,");
                     	 }else{
                     	    	sbpv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i*5+2)+"5;");
                     	    	sbuv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i*5+3)+"5;");
                     	      }
                     	}else{
                     		sbpv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i*5+2)+"5;");
                     		sbuv.append("'曝光频次-分广告位'!"+new EXCELUtil().getColumnName(i*5+3)+"5;");
                     	}
                     } 
         		} 
             }
        	
            rowNum=3;//起始行的值
            for(int i=0;i<l1.size();i++){
            	XSSFRow  row=sheet4.createRow(rowNum++);
                for (int n = 0; n < mediaSize; n++) {
                	 String[] str = l1.get(i);
                    for(int j=0;j<str.length;j++){
                      	 XSSFCell cell=row.createCell(j+n*jianju);
                      	 if(str[4].equals("")&&j==4||str[3].equals("")&&j==3){
                         }else{
                       	  cell.setCellValue(str[j]);
                          cell.setCellStyle(styleFMT); 
                         }
                      }
			        }
               }
          
            if(mediaSize==0){//查询的数据为空的时候
            	l1=(List<String[]>) map.get("listFMT");
                rowNum=1;//起始行的值
                for(int i=0;i<l1.size();i++){
                  XSSFRow rows=sheet4.createRow(rowNum++);
                  String[] str = l1.get(i);
                      for(int j=0;j<str.length;j++){
                          XSSFCell cell=rows.createCell(j+1);
                          if(str[4].equals("")&&j==4||str[3].equals("")&&j==3){
                          }else{
                        	  cell.setCellValue(str[j]);
                              cell.setCellStyle(styleFMT); 
                          }
                  }
               }
            }
            
            //曝光分媒体的数据展示 ,显示的数据是直接从数据库里面查询出来的,第一个大表：每个媒体查询出来的条数是相等的
            l1=(List<String[]>) map.get("listFMTJC");
            int sheet4Row=meidaLength;//显示的数据长度
            rowNum=4;//起始行的值
          
 
            String[] pvSrray = null;   
            pvSrray =sbpv.toString().split(";"); //拆分字符为"," ,然后把结果交给数组strArray 
            String[] uvSrray = null;   
            uvSrray =sbuv.toString().split(";"); //拆分字符为"," ,然后把结果交给数组strArray 

	      for(int i=0;i<meidaLength;i++){
            	XSSFRow  row=sheet4.createRow(rowNum++);
                for (int n = 0; n < mediaSize; n++) {
	                    String[] str=l1.get(n*meidaLength+i);
	                    for(int j=1;j<str.length;j++){
	                      	 XSSFCell cell=row.createCell(j+n*jianju-1);
	                      	if(str[1].equals("21")&&j==1){
                        	     cell.setCellValue("21+"); 
	                      	}else if(!str[1].equals("21")&&j==1){
	                      		 cell.setCellValue(Double.parseDouble(str[j]));  
	                      	}else if(str[2]!=null&&j==2&&activityCode.contains("JT")&&!activityCode.contains("MGJT")){
	                      		 int z=i+5;
	                      		 String x="SUM("+pvSrray[n]+")";
	                      		 cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	                      		 cell.setCellFormula(x.replace("5", ""+z+"")); 
                            	 cell.setCellStyle(stylejc);
	                      	 }else if(str[3]!=null&&j==3&&activityCode.contains("JT")&&!activityCode.contains("MGJT")){
	                      		 int z=i+5;
	                      		 String x="SUM("+uvSrray[n]+")";
		                         cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
	                             cell.setCellFormula(x.replace("5", ""+z+"")); 
	                             cell.setCellStyle(stylejc);
		                     }else{
		                    	  cell.setCellValue(Double.parseDouble(str[j])); 
		                    	  cell.setCellStyle(stylejc);
		                     }
	                             cell.setCellStyle(stylejc);
	                        }
				        }
                    
                }
            
            //将媒体的名称放到表头头的上面
            XSSFRow row=sheet4.createRow(2);
            for (int n = 0; n < mediaSize; n++) {
            	 String[] media=l1.get(n*meidaLength);
                 XSSFCell cell= row.createCell(n*jianju);
                	      cell.setCellValue(media[0]);
                	      cell.setCellStyle(styleFMT);
                	  
               //动态添加
              sheet4.addMergedRegion(new CellRangeAddress(2, 2, n*jianju,n*jianju+2)); 
			}
           // 
            //校验数据的时候用
            row=sheet4.createRow(sheet4Row+4);
            for(int n=1;n<=mediaSize;n++){
            	 String a=new EXCELUtil().getColumnName(n*jianju-3);
               	 String b=new EXCELUtil().getColumnName(n*jianju-2);
            	 String[] media=l1.get((n-1)*meidaLength);
            	 Map maps=(Map) map.get("mapHPV");
            	 Map mapss=(Map) map.get("mapHUV");
            	   if(maps.get(media[0])!=null){
            		   XSSFCell cell0= row.createCell(n*jianju-3);
            		            cell0.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell0.setCellFormula("IF("+mapss.get(media[0])+"-SUM("+b+"5:"+b+"25)=0,\"OK\",\"Check\")"); 
                                cell0.setCellStyle(stylemic);
                       XSSFCell cell= row.createCell(n*jianju-4);
            		            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                cell.setCellFormula("IF("+maps.get(media[0])+"-SUM("+a+"5:"+a+"25)<=0,\"OK\",\"Check\")"); 
                                cell.setCellStyle(stylemic);
            	   }
            	   XSSFCell cellbg=row.createCell(n*jianju-5);
            	            cellbg.setCellValue("校验数据：");
            	            cellbg.setCellStyle(stylemic);
            }
         
            /**
             * 曝光次数分频次取得和：第一组
             */
            //将媒体的名称放到标头的上面
            row=sheet4.createRow(sheet4Row+6);
            for (int n = 0; n < mediaSize; n++) {
           	 String[] media=l1.get(n*meidaLength);
                XSSFCell cell= row.createCell(n*jianju+1);
               	         cell.setCellValue(media[0]);
               	         cell.setCellStyle(styleFMT);
                XSSFCell cellbg=row.createCell(n*jianju);
                  	     cellbg.setCellValue("频次");
                  	     cellbg.setCellStyle(styleFMT);
             
			}
            row=sheet4.createRow(sheet4Row+7);
            for(int n=1;n<=mediaSize;n++){
            	String a=new EXCELUtil().getColumnName(n*5-3);
            	   XSSFCell cell= row.createCell(n*jianju-4);
            	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            	            cell.setCellFormula("SUM("+a+"5:"+a+"10)");
            	            cell.setCellStyle(stylejc);
            	   XSSFCell cellbg=row.createCell(n*jianju-5);
            	            cellbg.setCellValue("1~6");
            	            cellbg.setCellStyle(stylejc);
            }
            
            row=sheet4.createRow(sheet4Row+8);
            for(int n=1;n<=mediaSize;n++){
            	String a=new EXCELUtil().getColumnName(n*5-3);
            	   XSSFCell cell= row.createCell(n*jianju-4);
            	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            	            cell.setCellFormula("SUM("+a+"11:"+a+"14)");
            	            cell.setCellStyle(stylejc);
            	   XSSFCell cellbg=row.createCell(n*jianju-5);
            	            cellbg.setCellValue("7~10");
            	            cellbg.setCellStyle(stylejc);
            }
            row=sheet4.createRow(sheet4Row+9);
            for(int n=1;n<=mediaSize;n++){
            	String a=new EXCELUtil().getColumnName(n*5-3);
            	   XSSFCell cell= row.createCell(n*jianju-4);
            	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            	            cell.setCellFormula("SUM("+a+"15:"+a+"24)");
            	            cell.setCellStyle(stylejc);
            	   XSSFCell cellbg=row.createCell(n*jianju-5);
            	            cellbg.setCellValue("11~20");
            	            cellbg.setCellStyle(stylejc);
            }
            
            row=sheet4.createRow(sheet4Row+10);
            for(int n=1;n<=mediaSize;n++){
            	String a=new EXCELUtil().getColumnName(n*5-3);
            	   XSSFCell cell= row.createCell(n*jianju-4);
            	            cell.setCellFormula("("+a+"25)");
            	            cell.setCellStyle(stylejc);
            	   XSSFCell cellbg=row.createCell(n*jianju-5);
            	            cellbg.setCellValue("21+");
            	            cellbg.setCellStyle(stylejc);
            }
            //total数据
            row=sheet4.createRow(sheet4Row+11);
            for(int n=1;n<=mediaSize;n++){
            	String a=new EXCELUtil().getColumnName(n*5-3);
            	   XSSFCell cell= row.createCell(n*jianju-4);
            	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            	            cell.setCellFormula("SUM("+a+"29:"+a+"32)");
            	            cell.setCellStyle(stylejc);
            	   XSSFCell cellbg=row.createCell(n*jianju-5);
            	            cellbg.setCellValue("total");
            	            cellbg.setCellStyle(stylejc);
            }
            
            /**
             * 曝光媒体第二组数据表
             */
             
             //将媒体的名称放到标头的上面
             row=sheet4.createRow(sheet4Row+13);
             for (int n = 0; n < mediaSize; n++) {
            	 String[] media=l1.get(n*meidaLength);
                 XSSFCell cell= row.createCell(n*jianju+1);
                	      cell.setCellValue(media[0]);
                	      cell.setCellStyle(styleFMT);
                 XSSFCell cellbg=row.createCell(n*jianju);
                   	      cellbg.setCellValue("频次");
                   	      cellbg.setCellStyle(styleFMT);
              
			 }
             //求百分率的
             row=sheet4.createRow(sheet4Row+14);
             for(int n=1;n<=mediaSize;n++){
             	String a=new EXCELUtil().getColumnName(n*5-3);
             	   XSSFCell cell= row.createCell(n*jianju-4);
             	            cell.setCellFormula("IFERROR("+a+"29/"+a+"33,\"N/A\")");
             	            cell.setCellStyle(styleBFS);
             	   XSSFCell cellbg=row.createCell(n*jianju-5);
             	            cellbg.setCellValue("1~6");
             	            cellbg.setCellStyle(stylejc);
             }
             
             row=sheet4.createRow(sheet4Row+15);
             for(int n=1;n<=mediaSize;n++){
             	String a=new EXCELUtil().getColumnName(n*5-3);
             	   XSSFCell cell= row.createCell(n*jianju-4);
             	            cell.setCellFormula("IFERROR("+a+"30/"+a+"33,\"N/A\")");
             	            cell.setCellStyle(styleBFS);
             	   XSSFCell cellbg=row.createCell(n*jianju-5);
             	            cellbg.setCellValue("7~10");
             	            cellbg.setCellStyle(stylejc);
             }
             
             row=sheet4.createRow(sheet4Row+16);
             for(int n=1;n<=mediaSize;n++){
             	String a=new EXCELUtil().getColumnName(n*5-3);
             	   XSSFCell cell= row.createCell(n*jianju-4);
             	            cell.setCellFormula("IFERROR("+a+"31/"+a+"33,\"N/A\")");
             	            cell.setCellStyle(styleBFS);
             	   XSSFCell cellbg=row.createCell(n*jianju-5);
             	            cellbg.setCellValue("11~20");
             	            cellbg.setCellStyle(stylejc);
             }
             
             row=sheet4.createRow(sheet4Row+17);
             for(int n=1;n<=mediaSize;n++){
             	String a=new EXCELUtil().getColumnName(n*5-3);
             	   XSSFCell cell= row.createCell(n*jianju-4);
             	            cell.setCellFormula("IFERROR("+a+"32/"+a+"33,\"N/A\")");
             	            cell.setCellStyle(styleBFS);
             	   XSSFCell cellbg=row.createCell(n*jianju-5);
             	            cellbg.setCellValue("21+");
             	            cellbg.setCellStyle(stylejc);
             }
     /**
      * 曝光分媒体第四个表
      */
              //将媒体的名称放到标头的上面
              row=sheet4.createRow(sheet4Row+19);
              for (int n = 0; n < mediaSize; n++) {
             	 String[] media=l1.get(n*meidaLength);
                  XSSFCell cell= row.createCell(n+1);
                 	       cell.setCellValue(media[0]);
                 	       cell.setCellStyle(styleFMT);
                  XSSFCell cellbg=row.createCell(0);//只有第一个单元格为频次
                    	   cellbg.setCellValue("频次");
                    	   cellbg.setCellStyle(styleFMT);
 			}
              row=sheet4.createRow(sheet4Row+20);
              for(int n=1;n<=mediaSize;n++){
              	String a=new EXCELUtil().getColumnName(n*5-3);
              	   XSSFCell cell= row.createCell(n);
              	            cell.setCellFormula("("+a+"36)");
              	            cell.setCellStyle(styleBFS);
              	   XSSFCell cellbg=row.createCell(0);
			              	cellbg.setCellValue("1~6");
			              	cellbg.setCellStyle(stylejc);
              }
              
              row=sheet4.createRow(sheet4Row+21);
              for(int n=1;n<=mediaSize;n++){
              	String a=new EXCELUtil().getColumnName(n*5-3);
              	   XSSFCell cell= row.createCell(n);
		              	    cell.setCellFormula("("+a+"37)");
		              	    cell.setCellStyle(styleBFS);
              	   XSSFCell cellbg=row.createCell(0);
		              	    cellbg.setCellValue("7~10");
		              	    cellbg.setCellStyle(stylejc);
              }
              
              row=sheet4.createRow(sheet4Row+22);
              for(int n=1;n<=mediaSize;n++){
              	String a=new EXCELUtil().getColumnName(n*5-3);
              	   XSSFCell cell= row.createCell(n);
		              	    cell.setCellFormula("("+a+"38)");
		              	    cell.setCellStyle(styleBFS);
              	   XSSFCell cellbg=row.createCell(0);
		              	    cellbg.setCellValue("11~20");
		              	    cellbg.setCellStyle(stylejc);
              }
              
              row=sheet4.createRow(sheet4Row+23);
              for(int n=1;n<=mediaSize;n++){
                	String a=new EXCELUtil().getColumnName(n*5-3);
                	   XSSFCell cell= row.createCell(n);
		                	    cell.setCellFormula("("+a+"39)");
		                	    cell.setCellStyle(styleBFS);
                	   XSSFCell cellbg=row.createCell(0);
		                	    cellbg.setCellValue("21+");
		                	    cellbg.setCellStyle(stylejc);
                }
	    }
       /**
        *第五张表： 曝光频次-分广告为
        */
		if(!hzdw.equals("8")){
           	    XSSFSheet sheet5=workbook.createSheet("曝光频次-分广告位");
           		sheet5.setDisplayGridlines(false);//设置无边框
           		
     	        //曝光分广告位的表头与曝光分媒体的表头是一样的
           		List<String[]>  l1=(List<String[]>) map.get("listFMT");
                 rowNum=3;//起始行的值
                 for(int i=0;i<l1.size();i++){
                	 XSSFRow  row=sheet5.createRow(rowNum++);
                     for (int n = 0; n < poSize; n++) {
                     	 String[] str = l1.get(i);
                         for(int j=0;j<str.length;j++){
                           	 XSSFCell cell=row.createCell(j+n*jianju);
                           	if(str[3].equals("")&&j==3){
                           		Map mapss=(Map<String, Double>)map.get("mapPv");
                            	List<String[]> l2=(List<String[]>) map.get("listFGGJC");
                            	 for (int x = 0; x < poSize; x++) {
                                 	 String[] media=l2.get(n*meidaLength);
                                 	 if(!media[1].equals("")&&media[1]!=null){
                                 		 //存放的是当前点位的基础sheet页的曝光人数的信息
                                 		 if(mapss.get(media[1])!=null){
                                 			 cell.setCellValue((Double)mapss.get(media[1])); 
                                 		 }
                                 		
                                 	 }
                     			}
                            	 cell.setCellStyle(stylemic);
                            }else if(str[4].equals("")&&j==4){
                            	Map mapss=(Map<String, Integer>)map.get("mapUv");
                            	List<String[]> l2=(List<String[]>) map.get("listFGGJC");
                            	 for (int x = 0; x < poSize; x++) {
                                 	 String[] media=l2.get(n*meidaLength);
                                 	 if(!media[1].equals("")&&media[1]!=null){
                                 		 //存放的是当前点位的基础sheet页的曝光人数的信息
                                 		 if(mapss.get(media[1])!=null){
                                 			 cell.setCellValue((Integer)mapss.get(media[1])); 
                                 		 } 
                                 	 }
                     			}
                            	 cell.setCellStyle(stylemic);	
                            }else{
                          	  cell.setCellValue(str[j]);
                              cell.setCellStyle(styleFMT); 
                            }
                               }
     			        }
                    }
                 if(poSize==0){//查询的数据为空的时候，曝光分广告为的标题
                 	l1=(List<String[]>) map.get("listFMT");
                     rowNum=1;//起始行的值
                    for(int i=0;i<l1.size();i++){
                       XSSFRow rows=sheet5.createRow(rowNum++);
                       String[] str = l1.get(i);
                           for(int j=0;j<str.length;j++){
                               XSSFCell cell=rows.createCell(j+1);
                               if(str[4].equals("")&&j==4||str[3].equals("")&&j==3){
                               }else{
                             	  cell.setCellValue(str[j]);
                                  cell.setCellStyle(styleFMT); 
                               }
                       }
                    }
                 }
                 //曝光分广告位的数据展示 
                 l1=(List<String[]>) map.get("listFGGJC");
                 int sheet5row=25;//第五个页的行信息
                 rowNum=4;//起始行的值
                
	              
                 for(int i=0;i<meidaLength;i++){
                	 XSSFRow   row=sheet5.createRow(rowNum++);
                     for (int n = 0; n < poSize; n++) {
     	                    String[] str=l1.get(n*meidaLength+i);
     	                    for(int j=3;j<str.length;j++){
     	                    	 
     	                      	 XSSFCell cell=row.createCell(j+n*jianju-3);
     	                      	if(str[3].equals("21")&&j==3){
                           	     cell.setCellValue("21+"); 
   	                      	    }else if(!str[3].equals("21")&&j==3){
   	                      		 cell.setCellValue(Double.parseDouble(str[j])); 
   	                            	
   	                         	}else if(str[5]!=null&&j==5||str[4]!=null&&j==4){
   	                         		
  	                      	    	 if(str[j].equals("N/A")){
  	                      	    		cell.setCellValue(str[j]);  
  	                      	    	 }else{
  	                      	    	    cell.setCellValue(Double.parseDouble(str[j])); 
  	                      	    	 }
  	                      	     }else{
  	                      	        cell.setCellValue(str[j]); 
  	                      	        }
     	                            cell.setCellStyle(stylejc);
     	                          }
     				        }
                     }
                 //将媒体名称放在广告头
                 XSSFRow   row=sheet5.createRow(1);
                 for (int n = 0; n < poSize; n++) {
                 	 String[] media=l1.get(n*meidaLength);
                      XSSFCell cell= row.createCell(n*jianju);
                     	       cell.setCellValue(media[0]);
                     	       cell.setCellStyle(stylemic);
     			  }
                 //将广告位置放在头的位置
                 row=sheet5.createRow(2);
                 for (int n = 0; n < poSize; n++) {
                 	 String[] media=l1.get(n*meidaLength);
                     XSSFCell cell= row.createCell(n*jianju);
                     	      cell.setCellValue(media[2]);
                     	      cell.setCellStyle(styleFMT);
                    //不能写固定的值，需要写动态的值 
                   sheet5.addMergedRegion(new CellRangeAddress(2, 2, n*jianju,n*jianju+2)); 
     			}
                 //将广告位的短代码放在excel中
                 for (int n = 0; n < poSize; n++) {
                 	 String[] media=l1.get(n*meidaLength);
                     XSSFCell cell= row.createCell(n*jianju+3);
                     	      cell.setCellValue(media[1]);
                     	      cell.setCellStyle(stylemic);
     			}
                 /**
                  * 校验数据：
                  * 说明：频控是按照频控6次取得数据
                  * 
                  */
                 
                 row=sheet5.createRow(sheet5row);
                 for (int n = 1; n <= poSize; n++) {
                	 String a=new EXCELUtil().getColumnName(n*jianju-3);
                	 String b=new EXCELUtil().getColumnName(n*jianju-1);
                	 String c=new EXCELUtil().getColumnName(n*jianju-2);
                	 String d=new EXCELUtil().getColumnName(n*jianju);
                	 String e=new EXCELUtil().getColumnName(n*jianju-4);
                	 //判断当前的点位是否包含频控
                	 String[] media=l1.get((n-1)*meidaLength);//数组是从0开始的
                	 XSSFCell cell= row.createCell(n*jianju-4);
                              cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                	 if(media[2].indexOf("1次")!=-1){
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:,"+c+"5)+SUM("+c+"6:"+c+"25)*1)=0,\"OK\",\"Check\")");
      	                      cell.setCellStyle(stylemic);  
                	 }else if(media[2].indexOf("2次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"6,"+c+"5:"+c+"6)+SUM("+c+"7:"+c+"25)*2)=0,\"OK\",\"Check\")");
      	                      cell.setCellStyle(stylemic); 
                	 }else if(media[2].indexOf("3次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"7,"+c+"5:"+c+"7)+SUM("+c+"8:"+c+"25)*3)=0,\"OK\",\"Check\")");
      	                      cell.setCellStyle(stylemic); 
                	 }else if(media[2].indexOf("4次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"8,"+c+"5:"+c+"8)+SUM("+c+"9:"+c+"25)*4)=0,\"OK\",\"Check\")");
      	                      cell.setCellStyle(stylemic); 
                	 }else if(media[2].indexOf("5次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"9,"+c+"5:"+c+"9)+SUM("+c+"10:"+c+"25)*5)=0,\"OK\",\"Check\")");
      	                      cell.setCellStyle(stylemic); 
                	 }else if(media[2].indexOf("6次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"10,"+c+"5:"+c+"10)+SUM("+c+"11:"+c+"25)*6)=0,\"OK\",\"Check\")");
               	              cell.setCellStyle(stylemic); 
                	 }else if(media[2].indexOf("7次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"11,"+c+"5:"+c+"11)+SUM("+c+"12:"+c+"25)*7)=0,\"OK\",\"Check\")");
      	                      cell.setCellStyle(stylemic); 
       	             }else if(media[2].indexOf("8次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"12,"+c+"5:"+c+"12)+SUM("+c+"13:"+c+"25)*8)=0,\"OK\",\"Check\")");
	                          cell.setCellStyle(stylemic); 
	                 }else if(media[2].indexOf("9次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"13,"+c+"5:"+c+"13)+SUM("+c+"14:"+c+"25)*9)=0,\"OK\",\"Check\")");
                              cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("10次")!=-1){//频控的算法
                              cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"14,"+c+"5:"+c+"14)+SUM("+c+"15:"+c+"25)*10)=0,\"OK\",\"Check\")");
                              cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("11次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"15,"+c+"5:"+c+"15)+SUM("+c+"16:"+c+"25)*11)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("12次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"16,"+c+"5:"+c+"16)+SUM("+c+"17:"+c+"25)*12)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("13次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"17,"+c+"5:"+c+"17)+SUM("+c+"18:"+c+"25)*13)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("14次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"18,"+c+"5:"+c+"18)+SUM("+c+"19:"+c+"25)*14)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("15次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"19,"+c+"5:"+c+"19)+SUM("+c+"20:"+c+"25)*15)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("16次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"20,"+c+"5:"+c+"20)+SUM("+c+"21:"+c+"25)*16)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("17次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"21,"+c+"5:"+c+"21)+SUM("+c+"22:"+c+"25)*17)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("18次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"22,"+c+"5:"+c+"22)+SUM("+c+"23:"+c+"25)*18)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("19次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"23,"+c+"5:"+c+"23)+SUM("+c+"24:"+c+"25)*19)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("20次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"24,"+c+"5:"+c+"24)+SUM("+c+"25:"+c+"25)*20)=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else if(media[2].indexOf("21次")!=-1){//频控的算法
                         cell.setCellFormula("IF("+b+"4-(SUMPRODUCT("+e+"5:"+e+"24,"+c+"5:"+c+"25))=0,\"OK\",\"Check\")");
                         cell.setCellStyle(stylemic); 
                     }else{
                	
                                  cell.setCellFormula("IF("+b+"4-SUM("+a+"5:"+a+"25)=0,\"OK\",\"Check\")");
               	                  cell.setCellStyle(stylemic); 
                	 }
                	 XSSFCell cell0= row.createCell(n*jianju-3);
                              cell0.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                              cell0.setCellFormula("IF("+d+"4-SUM("+c+"5:"+c+"25)=0,\"OK\",\"Check\")");
           	                  cell0.setCellStyle(stylemic);
                     
                      XSSFCell cellbg=row.createCell(n*jianju-5);
                               cellbg.setCellValue("校验数据：");
                               cellbg.setCellStyle(stylemic);
     			  }
                 /**
                  * 曝光广告位的第二个表
                  */
                 	//第二个表的表头
                 row=sheet5.createRow(sheet5row+2);
                 for (int n = 0; n < poSize; n++) {
                 	 String[] media=l1.get(n*meidaLength);
                      XSSFCell cell= row.createCell(n*jianju+1);
                     	       cell.setCellValue(media[2]);
                     	       cell.setCellStyle(styleFMT);
                      XSSFCell cellbg=row.createCell(n*jianju);
                               cellbg.setCellValue("频次");
                               cellbg.setCellStyle(styleFMT);
     			  }
                 row=sheet5.createRow(sheet5row+3);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                 	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
               	                cell.setCellFormula("SUM("+a+"5:"+a+"10)");
                 	            cell.setCellStyle(stylejc);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("1~6");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 row=sheet5.createRow(sheet5row+4);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
               	                cell.setCellFormula("SUM("+a+"11:"+a+"14)");
                 	            cell.setCellStyle(stylejc);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("7~10");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 row=sheet5.createRow(sheet5row+5);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                 	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
              	                cell.setCellFormula("SUM("+a+"15:"+a+"24)");
                 	            cell.setCellStyle(stylejc);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("11~20");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 row=sheet5.createRow(sheet5row+6);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                	            cell.setCellFormula("("+a+"25)");
                 	            cell.setCellStyle(stylejc);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("21+");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 row=sheet5.createRow(sheet5row+7);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                 	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
             	                cell.setCellFormula("SUM("+a+"29:"+a+"32)");
                 	            cell.setCellStyle(stylejc);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("total");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 /**
                  * 曝光广告位置第3张表
                  */
                 row=sheet5.createRow(sheet5row+9);
                 for (int n = 0; n < poSize; n++) {
                 	 String[] media=l1.get(n*meidaLength);
                      XSSFCell cell= row.createCell(n*jianju+1);
                     	       cell.setCellValue(media[2]);
                     	       cell.setCellStyle(styleFMT);
                      XSSFCell cellbg=row.createCell(n*jianju);
                               cellbg.setCellValue("频次");
                               cellbg.setCellStyle(styleFMT);
     			}
                 row=sheet5.createRow(sheet5row+10);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                 	            cell.setCellFormula("IFERROR("+a+"29/"+a+"33,\"N/A\")");
                 	            cell.setCellStyle(styleBFS);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("1~6");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 
                 row=sheet5.createRow(sheet5row+11);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                 	            cell.setCellFormula("IFERROR("+a+"30/"+a+"33,\"N/A\")");
                 	            cell.setCellStyle(styleBFS);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("7~10");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 
                 row=sheet5.createRow(sheet5row+12);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                 	            cell.setCellFormula("IFERROR("+a+"31/"+a+"33,\"N/A\")");
                 	            cell.setCellStyle(styleBFS);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("11~20");
                 	            cellbg.setCellStyle(stylejc);
                 }
                 row=sheet5.createRow(sheet5row+13);
                 for(int n=1;n<=poSize;n++){
                 	String a=new EXCELUtil().getColumnName(n*jianju-3);
                 	   XSSFCell cell= row.createCell(n*jianju-4);
                 	            cell.setCellFormula("IFERROR("+a+"32/"+a+"33,\"N/A\")");
                 	            cell.setCellStyle(styleBFS);
                 	   XSSFCell cellbg=row.createCell(n*jianju-5);
                 	            cellbg.setCellValue("21+");
                 	            cellbg.setCellStyle(stylejc);
                 }
		}
      /**
       * 第六张：点击频次-整体
       */
        if(!hzdw.equals("8")){
                	 XSSFSheet sheet6=workbook.createSheet("点击频次-整体");
            			sheet6.setDisplayGridlines(false);//设置无边框
            			List<String[]> l1=(List<String[]>) map.get("listDJZT");
                     rowNum=0;//起始行的值
                  	for(int i=0;i<l1.size();i++){
                  		XSSFRow row=sheet6.createRow(rowNum++);
                         String[] str = l1.get(i);
                            for(int j=0;j<str.length;j++){
                                XSSFCell cell=row.createCell(j+1);
                                  	     cell.setCellValue(str[j]); 
                                  	     cell.setCellStyle(styleFMT);
                              }
                     }
                  	
                  	l1=(List<String[]>) map.get("listZTDJJC");
                   //考虑到线程安全的情况下使用StringBuffer
                	StringBuffer a=new StringBuffer();
                	StringBuffer c=new StringBuffer();
                	 for(int n=1;n<=meSize;n++){
                		 String b=new EXCELUtil().getColumnName(n*jianju-3);
                		 String d=new EXCELUtil().getColumnName(n*jianju-2);
                		   if(n==1&&meSize>1){
                			 a.append("sum('点击频次-分媒体'!"+b+"4,"); 
                			 c.append("sum('点击频次-分媒体'!"+d+"4,"); 
                		   }else if(n==1&&meSize==1){//考虑媒体名称为1的情况下
                			 a.append("sum('点击频次-分媒体'!"+b+"4)"); 
                  			 c.append("sum('点击频次-分媒体'!"+d+"4)"); 
                		   } else if(n<meSize){
                			 a.append("'点击频次-分媒体'!"+b+"4,");
                			 c.append("'点击频次-分媒体'!"+d+"4,");
                		   }else{
                			 a.append("'点击频次-分媒体'!"+b+"4)");
                			 c.append("'点击频次-分媒体'!"+d+"4)");
                		 }
                	 }
                     rowNum=2;//起始行的值
                  	for(int i=0;i<l1.size();i++){
                  		XSSFRow	 row=sheet6.createRow(rowNum++);
                         String[] str = l1.get(i);
                            for(int j=0;j<str.length;j++){
                                XSSFCell cell=row.createCell(j+1);
                                if(str[0].equals("7")&&j==0){
                              	     cell.setCellValue("7+");
                             	     cell.setCellStyle(stylejc);
      	                       }else if(!str[0].equals("7")&&j==0){
      	                      		 cell.setCellValue(Double.parseDouble(str[j]));  
                             	     cell.setCellStyle(stylejc);
      	                       }else if(str[1]!=null&&j==1){
                                	 cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                	 int z=i+4;
                                	 cell.setCellFormula(a.toString().replaceAll("4", ""+z+""));
                             	     cell.setCellStyle(stylejc);
                               }else if(str[2]!=null&&j==2){
                                 	 cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                 	 int z=i+4;
                                   	 cell.setCellFormula(c.toString().replaceAll("4", ""+z+""));
                                	 cell.setCellStyle(stylejc);
                                }else if(str[3]!=null&&j==3||str[4]!=null&&j==4){
                              	 if(str[j].equals("N/A")||str[j].equals("#Div/0!")||str[j].equals("0.00%")){
 	                      	    		cell.setCellValue(str[j]); 
 	                      	    	    cell.setCellStyle(stylejc);
 	                      	    	 }else{
 	                      	    	 cell.setCellValue(Double.parseDouble(str[j])/100); 
                           	     cell.setCellStyle(StyleBFB); 
 	                      	    	 }
                              }else{
                              	 cell.setCellValue(str[j]);
                                 cell.setCellStyle(stylejc);
                              }
                           }
                     }
                  	if(l1.size()>0){
                  		int sheet7Row=l1.size()+2;
                  		XSSFRow	 row=sheet6.createRow(sheet7Row);
                  		 XSSFCell cellcheck= row.createCell(1);
                  		          cellcheck.setCellValue("校验数据：");
                  		          cellcheck.setCellStyle(stylemic);
                  		        Map maps=(Map) map.get("mapCPV");
                  	     XSSFCell cellcheckPv= row.createCell(2);
     	                          cellcheckPv.setCellType(HSSFCell.CELL_TYPE_FORMULA);
     	                          if(maps.get(map.get("activityName"))!=null){
         	                          cellcheckPv.setCellFormula("IF("+(Integer)maps.get(map.get("activityName"))+"-SUM(C3:C9)<=0,\"OK\",\"Check\")");
 
     	                          }
     	                          cellcheckPv.setCellStyle(stylemic);
     	                          Map mapp=(Map) map.get("mapCUV");
     	     	         XSSFCell cellcheckUv= row.createCell(3);
     	     	                  cellcheckUv.setCellType(HSSFCell.CELL_TYPE_FORMULA);
     	     	                  if(mapp.get(map.get("activityName"))!=null){
         	     	                  cellcheckUv.setCellFormula("IF("+(Integer)mapp.get(map.get("activityName"))+"-SUM(D3:D9)<=0,\"OK\",\"Check\")");
     	     	                  }
     	     	                  cellcheckUv.setCellStyle(stylemic);
                  		
                  		//点击整体第二张表的表头
                  		 sheet7Row=l1.size()+4;
                  	     row=sheet6.createRow(sheet7Row);
                     	 XSSFCell cellztbg= row.createCell(1);
                         XSSFCell cellzt= row.createCell(2);
                         XSSFCell celluv= row.createCell(3);
                     	 cellzt=row.createCell(1);
                     	 cellzt.setCellValue("整体点击频次");
                     	 cellzt.setCellStyle(styleFMT);
                      
                      //通过excel的公式进行计算
                      row=sheet6.createRow(sheet7Row+1);
                      cellztbg= row.createCell(1);
                      cellztbg.setCellValue("1~3");
                      cellztbg.setCellStyle(stylejc);
                      cellzt= row.createCell(2);
                      cellzt.setCellFormula("SUM(C3:C5)");
                      cellzt.setCellStyle(stylejc);
                      celluv= row.createCell(3);
                      celluv.setCellFormula("SUM(D3:D5)");
                      celluv.setCellStyle(stylejc);
                      
                      row=sheet6.createRow(sheet7Row+2);
                      cellztbg= row.createCell(1);
                      cellztbg.setCellValue("4~6");
                      cellztbg.setCellStyle(stylejc);
                      cellzt= row.createCell(2);
                      cellzt.setCellFormula("SUM(C6:C8)");
                      cellzt.setCellStyle(stylejc);
                      celluv= row.createCell(3);
                      celluv.setCellFormula("SUM(D6:D8)");
                      celluv.setCellStyle(stylejc);
                      
                      row=sheet6.createRow(sheet7Row+3);
                      cellztbg= row.createCell(1);
                      cellztbg.setCellValue("7+");
                      cellztbg.setCellStyle(stylejc);
                      cellzt= row.createCell(2);
                      cellzt.setCellFormula("(C9)");
                      cellzt.setCellStyle(stylejc);
                      celluv= row.createCell(3);
                      celluv.setCellFormula("(D9)");
                      celluv.setCellStyle(stylejc);
                      
                      row=sheet6.createRow(sheet7Row+4);
                      cellztbg= row.createCell(1);
                      cellztbg.setCellValue("total");
                      cellztbg.setCellStyle(stylejc);
                      cellzt= row.createCell(2);
                      cellzt.setCellFormula("SUM(C13:C15)");
                      cellzt.setCellStyle(stylejc);
                      celluv= row.createCell(3);
                      celluv.setCellFormula("SUM(D13:D15)");
                      celluv.setCellStyle(stylejc);
                      
                      //点击整体第三张表的表头
                   	 row=sheet6.createRow(sheet7Row+6);
                   	 cellzt=row.createCell(1);
                   	 cellzt.setCellValue("整体点击频次");
                   	 cellzt.setCellStyle(styleFMT);
                      
                     row=sheet6.createRow(sheet7Row+7);
                     cellztbg= row.createCell(1);
                     cellztbg.setCellValue("1~3");
                     cellztbg.setCellStyle(stylejc);
                     cellzt= row.createCell(2);
                     cellzt.setCellFormula("IFERROR(C13/C16,\"N/A\")");
                     cellzt.setCellStyle(styleBFS);
                     celluv= row.createCell(3);
                     celluv.setCellFormula("IFERROR(D13/D16,\"N/A\")");
                     celluv.setCellStyle(styleBFS);
                     	 
                    row=sheet6.createRow(sheet7Row+8);
                    cellztbg= row.createCell(1);
                    cellztbg.setCellValue("4~6");
                    cellztbg.setCellStyle(stylejc);
                    cellzt= row.createCell(2);
                    cellzt.setCellFormula("IFERROR(C14/C16,\"N/A\")");
                    cellzt.setCellStyle(styleBFS);
                    celluv= row.createCell(3);
                    celluv.setCellFormula("IFERROR(D14/D16,\"N/A\")");
                    celluv.setCellStyle(styleBFS);
                    
                	row=sheet6.createRow(sheet7Row+9);
                    cellztbg= row.createCell(1);
                    cellztbg.setCellValue("7+");
                    cellztbg.setCellStyle(stylejc);
                    cellzt= row.createCell(2);
                    cellzt.setCellFormula("IFERROR(C15/C16,\"N/A\")");
                    cellzt.setCellStyle(styleBFS);
                    celluv= row.createCell(3);
                    celluv.setCellFormula("IFERROR(D15/D16,\"N/A\")");
                    celluv.setCellStyle(styleBFS);
                    
                    }
                  	  
                 sheet6.addMergedRegion(new CellRangeAddress(0, 0, 1, 5));
                 sheet6.addMergedRegion(new CellRangeAddress(11, 11, 1, 3)); 
                 sheet6.addMergedRegion(new CellRangeAddress(17, 17, 1, 3));
                	 
        }
           			
              
      /**
       * 第七张：点击频次-分媒体	
       */
                 
        if(!hzdw.equals("8")){ 
           		  XSSFSheet sheet7=workbook.createSheet("点击频次-分媒体");
           		  sheet7.setDisplayGridlines(false);//设置无边框
           		  int sheetRow=12;
           		//曝光分媒体表头
           		List<String[]> l1=(List<String[]>) map.get("listDJFMTBT");
                   rowNum=2;//起始行的值
                   for(int i=0;i<l1.size();i++){
                       XSSFRow row=sheet7.createRow(rowNum++);
                       for (int n = 0; n < meSize; n++) {
                       	 String[] str = l1.get(i);
                           for(int j=0;j<str.length;j++){
                             	 XSSFCell cell=row.createCell(j+n*jianju);
                             	if(str[1].equals("7")&&j==1){
                            	      cell.setCellValue("7+"); 
    	                      	     }else{
                            	      cell.setCellValue(str[j]); 
    	                      	   }
                                 	  cell.setCellStyle(styleFMT);
                                 }
       			        	}
                      }
                 
                   if(meSize==0){//查询的数据为空的时候，点击分媒体的标题
                 		l1=(List<String[]>) map.get("listDJFMTBT");
                         for(int i=0;i<l1.size();i++){
                           XSSFRow rows=sheet7.createRow(2);
                           String[] str = l1.get(i);
                               for(int j=0;j<str.length;j++){
                                   XSSFCell cell=rows.createCell(j+1);
                                            cell.setCellValue(str[j]);
                                            cell.setCellStyle(styleFMT);
                           }
                        }
                     }
           		  // 点击分媒体
                    l1=(List<String[]>) map.get("listDJFMT");
                    rowNum=3;//起始行的值
                    for(int i=0;i<7;i++){
                    	XSSFRow   row=sheet7.createRow(rowNum++);
                        for (int n = 0; n < meSize; n++) {
        	                    String[] str=l1.get(n*7+i);
        	                    for(int j=1;j<str.length;j++){
        	                      	 XSSFCell cell=row.createCell(j+n*jianju-1);
    	                      		 cell.setCellValue(str[j]);
    	                      		if(str[1].equals("7")&&j==1){
                                 	     cell.setCellValue("7+");
         	                       }else if(!str[1].equals("7")&&j==1){
         	                      		 cell.setCellValue(Double.parseDouble(str[j]));  
         	                       }else if(str[2]!=null&&j==2||str[3]!=null&&j==3){
        	                      		 cell.setCellValue(Double.parseDouble(str[j])); 
        	                      	 }
        	                          	 cell.setCellStyle(stylejc);
        	                          }
        	                    
        				        }
                        }
                    
                    //点击分媒体的媒体名称
                    XSSFRow  row=sheet7.createRow(1);
                    for (int n = 0; n < meSize; n++) {
                    	 String[] media=l1.get(n*7);
                         XSSFCell cell= row.createCell(n*jianju);
                        	      cell.setCellValue(media[0]);
                        	      cell.setCellStyle(styleFMT);
                       //不能写固定的值，需要写动态的值 
                      sheet7.addMergedRegion(new CellRangeAddress(1, 1, n*jianju,n*jianju+2)); 
        			}
                    
                    
                    //校验数据的时候用
                    row=sheet7.createRow(sheetRow-2);
                    for(int n=1;n<=meSize;n++){
                    	 String a=new EXCELUtil().getColumnName(n*jianju-3);
                       	 String b=new EXCELUtil().getColumnName(n*jianju-2);
                    	 String[] media=l1.get((n-1)*7);
                    	 Map maps=(Map) map.get("mapDPV");
                    	 Map mapss=(Map) map.get("mapDUV");
                    	   if(maps.get(media[0])!=null){
                    		   XSSFCell cell0= row.createCell(n*jianju-3);
                    		            cell0.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                        cell0.setCellFormula("IF("+mapss.get(media[0])+"-SUM("+b+"4:"+b+"10)<=0,\"OK\",\"Check\")"); 
                                        cell0.setCellStyle(stylemic);
                               XSSFCell cell= row.createCell(n*jianju-4);
                    		            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                        cell.setCellFormula("IF("+maps.get(media[0])+"-SUM("+a+"4:"+a+"10)<=0,\"OK\",\"Check\")"); 
                                        cell.setCellStyle(stylemic);
                    	   }
                    	 
                    	   XSSFCell cellbg=row.createCell(n*jianju-5);
                    	            cellbg.setCellValue("校验数据：");
                    	            cellbg.setCellStyle(stylemic);
                    }
                    
                      row=sheet7.createRow(sheetRow); 
                      for (int n = 0; n < meSize; n++) {
                    	 String[] media=l1.get(n*7);
                         XSSFCell cell= row.createCell(n*jianju+1);
                        	      cell.setCellValue(media[0]);
                        	      cell.setCellStyle(styleFMT);
                         XSSFCell cellbg=row.createCell(n*jianju);
                                  cellbg.setCellValue("频次");
                                  cellbg.setCellStyle(styleFMT);
                         sheet7.addMergedRegion(new CellRangeAddress(sheetRow, sheetRow, n*jianju+1,n*jianju+2)); 
        			}
                    
                    row=sheet7.createRow(sheetRow+1);
                    for(int n=1;n<=meSize;n++){
                    	 String b=new EXCELUtil().getColumnName(n*jianju-2);
                    	 String a=new EXCELUtil().getColumnName(n*jianju-3);
                    	 XSSFCell cells= row.createCell(n*jianju-3);
                    	          cells.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                  	              cells.setCellFormula("SUM("+b+"4:"+b+"6)");
                  	              cells.setCellStyle(stylejc);
                         XSSFCell cell= row.createCell(n*jianju-4);
                    	          cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                 	              cell.setCellFormula("SUM("+a+"4:"+a+"6)");
                    	          cell.setCellStyle(stylejc);
                    	 XSSFCell cellbg=row.createCell(n*jianju-5);
                    	          cellbg.setCellValue("1~3");
                    	          cellbg.setCellStyle(stylejc);
                    } 
                    
                    row=sheet7.createRow(sheetRow+2);
                    for(int n=1;n<=meSize;n++){
                    	String a=new EXCELUtil().getColumnName(n*jianju-3);
                   	    String b=new EXCELUtil().getColumnName(n*jianju-2);
                   	       XSSFCell cells= row.createCell(n*jianju-3);
                   	                cells.setCellType(HSSFCell.CELL_TYPE_FORMULA);
              	                    cells.setCellFormula("SUM("+b+"7:"+b+"9)");
            	                    cells.setCellStyle(stylejc);
                    	   XSSFCell cell= row.createCell(n*jianju-4);
                    	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                 	                cell.setCellFormula("SUM("+a+"7:"+a+"9)");
                    	            cell.setCellStyle(stylejc);
                    	   XSSFCell cellbg=row.createCell(n*jianju-5);
                    	            cellbg.setCellValue("4~6");
                    	            cellbg.setCellStyle(stylejc);
                    }
                    
                    row=sheet7.createRow(sheetRow+3);
                    for(int n=1;n<=meSize;n++){
                    	 String a=new EXCELUtil().getColumnName(n*jianju-3);
                    	 String b=new EXCELUtil().getColumnName(n*jianju-2);
                 	       XSSFCell cells= row.createCell(n*jianju-3);
          	                        cells.setCellFormula("("+b+"10)");
          	                        cells.setCellStyle(stylejc);
                    	   XSSFCell cell= row.createCell(n*jianju-4);
                    	            cell.setCellFormula("("+a+"10)");
                    	            cell.setCellStyle(stylejc);
                    	   XSSFCell cellbg=row.createCell(n*jianju-5);
                    	            cellbg.setCellValue("7+");
                    	            cellbg.setCellStyle(stylejc);
                    }
                   
                    row=sheet7.createRow(sheetRow+4);
                    for(int n=1;n<=meSize;n++){
                    	 String a=new EXCELUtil().getColumnName(n*jianju-3);
                    	 String b=new EXCELUtil().getColumnName(n*jianju-2);
             	           XSSFCell cells= row.createCell(n*jianju-3);
             	                    cells.setCellType(HSSFCell.CELL_TYPE_FORMULA);
             	                    cells.setCellFormula("SUM("+b+"14:"+b+"16)");
      	                            cells.setCellStyle(stylejc);
                    	   XSSFCell cell= row.createCell(n*jianju-4);
                    	            cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                 	                cell.setCellFormula("SUM("+a+"14:"+a+"16)");
                    	            cell.setCellStyle(stylejc);
                    	   XSSFCell cellbg=row.createCell(n*jianju-5);
                    	            cellbg.setCellValue("total");
                    	            cellbg.setCellStyle(stylejc);
                    }
                    
                    row=sheet7.createRow(sheetRow+6);
                    for (int n = 0; n < meSize; n++) {
                    	 String[] media=l1.get(n*7);
                         XSSFCell cell= row.createCell(n*jianju+1);
                        	      cell.setCellValue(media[0]);
                        	      cell.setCellStyle(styleFMT);
                         XSSFCell cellbg=row.createCell(n*jianju);
                                  cellbg.setCellValue("频次");
                                  cellbg.setCellStyle(styleFMT);
        			}
                    
                    row=sheet7.createRow(sheetRow+7);
                    for(int n=1;n<=meSize;n++){
                    	String a=new EXCELUtil().getColumnName(n*jianju-3);
                    	   XSSFCell cell= row.createCell(n*jianju-4);
                    	            cell.setCellFormula("IFERROR("+a+"14/"+a+"17,\"N/A\")");
                    	            cell.setCellStyle(styleBFS);
                    	   XSSFCell cellbg=row.createCell(n*jianju-5);
                    	            cellbg.setCellValue("1~3");
                    	            cellbg.setCellStyle(stylejc);
                    }
                    
                    row=sheet7.createRow(sheetRow+8);
                    for(int n=1;n<=meSize;n++){
                    	String a=new EXCELUtil().getColumnName(n*jianju-3);
                    	   XSSFCell cell= row.createCell(n*jianju-4);
                    	            cell.setCellFormula("IFERROR("+a+"15/"+a+"17,\"N/A\")");
                    	            cell.setCellStyle(styleBFS);
                    	   XSSFCell cellbg=row.createCell(n*jianju-5);
                    	            cellbg.setCellValue("4~6");
                    	            cellbg.setCellStyle(stylejc);
                    }
                    
                    row=sheet7.createRow(sheetRow+9);
                    for(int n=1;n<=meSize;n++){
                    	String a=new EXCELUtil().getColumnName(n*jianju-3);
                    	   XSSFCell cell= row.createCell(n*jianju-4);
                    	            cell.setCellFormula("IFERROR("+a+"16/"+a+"17,\"N/A\")");
                    	            cell.setCellStyle(styleBFS);
                    	   XSSFCell cellbg=row.createCell(n*jianju-5);
                    	            cellbg.setCellValue("7+");
                    	            cellbg.setCellStyle(stylejc);
                    }
                 }
                      
       /**
        * 第八张：地域分布点击		
        */
                 if(!hzdw.equals("8")){
                    int dyMeiNum=(int) map.get("djMeiNum");
           			XSSFSheet sheet8=workbook.createSheet("地域分布-点击");
           			sheet8.setDisplayGridlines(false);//设置无边框
           			List<String[]> l1=(List<String[]>) map.get("listDJDYFB");
           		    rowNum=1;//起始行的值
           		  for(int i=0;i<l1.size();i++){
           			XSSFRow  row=sheet8.createRow(rowNum++);
                     for (int n = 0; n < dyMeiNum; n++) {
                     	 String[] str = l1.get(i);
                         for(int j=0;j<str.length;j++){
                           	 XSSFCell cell=row.createCell(j+n*6+1);//间距调整
                               	      cell.setCellValue(str[j]); 
                               	      cell.setCellStyle(styleFMT);
                               }
     			        }
                    }
           		 if(dyMeiNum==0){//查询的数据为空的时候,点击地域分布情况
                 	l1=(List<String[]>) map.get("listDJDYFB");
                     rowNum=1;//起始行的值
                    for(int i=0;i<l1.size();i++){
                       XSSFRow rows=sheet8.createRow(rowNum++);
                       String[] str = l1.get(i);
                           for(int j=0;j<str.length;j++){
                               XSSFCell cell=rows.createCell(j+1);
                                        cell.setCellValue(str[j]);
                                        cell.setCellStyle(styleFMT);
                       }
                    }
                 }
           		
                   
                  l1=(List<String[]>) map.get("listDJDYJC");
                  if (l1.size()<21) {
                	  rowNum=2;//起始行的值
                      for(int i=0;i<l1.size();i++){
                    	  XSSFRow    row=sheet8.createRow(rowNum++);
          	                    String[] str=l1.get(i);
          	                    for(int j=1;j<str.length;j++){
          	                      	 XSSFCell cell=row.createCell(j);
          	                      	   if((str[2]==null||str[2].equals(""))&&j==2){
       	                          	      cell.setCellValue(i+1); 
           	                      		  cell.setCellStyle(stylejc); 
          	                      	   }else if(str[3]!=null&&j==3||str[4]!=null&&j==4){
          	                      		  cell.setCellValue(Double.parseDouble(str[j])); 
          	                      	      cell.setCellStyle(stylejc); 
          	                      	   }else if((str[5]==null||str[5].equals(""))&&j==5){
          	                      		//点击人数汇总
          	                      		   Map<String, Integer> maprsSUM=(Map<String, Integer>) map.get("maprsSum");
          	                      		   
          	                      		   	cell.setCellValue(Double.valueOf(str[4])/maprsSUM.get(str[0]));
          	                      		   	cell.setCellStyle(styleBFS);
          	                      	   }else if(str[1]!=null&&j==1){
          	                      		 cell.setCellValue(str[j]); 
          	                      		 cell.setCellStyle(stylezw); 
          	                      	   }else {
          	                      		 cell.setCellValue(str[j]); 
          	                      		 cell.setCellStyle(stylejc); 
          	                      		
          	                      	   }  
          	                      	 
          	                         }
   
          				        }
                          
				      }else{
					    rowNum=2;//起始行的值
	                    for(int i=0;i<meidaLength;i++){
	                    	XSSFRow  row=sheet8.createRow(rowNum++);
	                        for (int n = 0; n < dyMeiNum; n++) {
	                        		 String[] str=l1.get(n*meidaLength+i);
		        	                    for(int j=1;j<str.length;j++){
		        	                      	 XSSFCell cell=row.createCell(j+n*6);
		        	                      	   if((str[2]==null||str[2].equals(""))&&j==2){
		     	                          	         cell.setCellValue(i+1); 
		         	                      		     cell.setCellStyle(stylejc); 
		        	                      	   }else if(str[3]!=null&&j==3||str[4]!=null&&j==4){
		            	                      		 cell.setCellValue(Double.parseDouble(str[j])); 
		              	                      	     cell.setCellStyle(stylejc); 
		              	                       } else if((str[5]==null||str[5].equals(""))&&j==5){
		        	                      		   //点击人数汇总
		        	                      		   Map<String, Integer> maprsSUM=(Map<String, Integer>) map.get("maprsSum");
		        	                      		   	cell.setCellValue(Double.valueOf(str[4])/maprsSUM.get(str[0]));
		        	                      		   	cell.setCellStyle(styleBFS);
		        	                      	   }else if(str[1]!=null&&j==1){
		        	                      		    cell.setCellValue(str[j]); 
		        	                      		    cell.setCellStyle(stylezw); 
		        	                      	   }else{
		        	                      		    cell.setCellValue(str[j]); 
		        	                      		    cell.setCellStyle(stylejc); 
		        	                      	    }  
		        	                      	
		        	                          }
	                        		
	                        	        }
	        	                    
	                        }
	                    
					
				}
               
                   if(l1.size()>=21){
                	   int  sheet8H=meidaLength+2;
                	   XSSFRow  row=sheet8.createRow(sheet8H);
                       for(int n=1;n<=dyMeiNum;n++){
                    	   String c=new EXCELUtil().getColumnName(n*6);
                    	   XSSFCell cellxs= row.createCell(n*6-1);
                    	            cellxs.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    	            cellxs.setCellFormula("SUM("+c+"3:"+c+"23)");
                    	   cellxs.setCellStyle(styleBFSHH);                   	   
                    	 String b=new EXCELUtil().getColumnName(n*6-1);
                        	   XSSFCell cellcs= row.createCell(n*6-2);
                        	            cellcs.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        	            cellcs.setCellFormula("SUM("+b+"3:"+b+"23)");
                        	            cellcs.setCellStyle(styleHZ);   
                    	   
                       	String a=new EXCELUtil().getColumnName(n*6-2);
                       	   XSSFCell cell= row.createCell(n*6-3);
                        	        cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                 	                cell.setCellFormula("SUM("+a+"3:"+a+"23)");
                       	            cell.setCellStyle(styleHZ);
                       	   XSSFCell cellbg=row.createCell(n*6-5);
                       	            cellbg.setCellValue("总计");
                       	            cellbg.setCellStyle(styleHZ);
                       	sheet8.addMergedRegion(new CellRangeAddress(sheet8H, sheet8H, n*6-5,n*6-4)); 
                       }
                   }else{
                	   List<String[]> ld=(List<String[]>) map.get("listDJZJDY");  
                		int sheet8Row=l1.size()+2;
                        //rowNum=2;//起始行的值
                     	for(int i=0;i<ld.size();i++){
                     		XSSFRow	 row=sheet8.createRow(sheet8Row++);
                            String[] str = ld.get(i);
                               for(int j=0;j<str.length;j++){
                                   XSSFCell cell=row.createCell(j+1);
                                	        cell.setCellValue(str[j]); 
                                	        cell.setCellStyle(styleHZ);     
                                    }
                               }
                     //不能写固定的值，需要写动态的值 
                   sheet8.addMergedRegion(new CellRangeAddress(l1.size()+2, l1.size()+2, 1,2)); 
                	   
                   }
                   //将广告位置放在头的位置
                   XSSFRow row=sheet8.createRow(0);
                  for (int n = 0; n < dyMeiNum; n++) {
                  	 String[] media=l1.get(n*meidaLength);
                       XSSFCell cell= row.createCell(n*6+1);
                      	        cell.setCellValue(media[0]);
                      	        cell.setCellStyle(styleFMT);
                     //不能写固定的值，需要写动态的值 
                    sheet8.addMergedRegion(new CellRangeAddress(0, 0, n*6+1,n*6+5)); 
      			}
     }
           FileOutputStream fout=new FileOutputStream(outputFile);
           workbook.write(fout);
           fout.flush();
           fout.close();
         
       } catch (Exception e) {
    	
           e.printStackTrace();
       }
   }


@SuppressWarnings("unused")
   public ModelAndView LoadExcel(HttpServletRequest request, HttpServletResponse response,String obj)throws IOException{
       String path = obj.toString();
       try {
           // path是指欲下载的文件的路径。
           File file = new File(path);
         
           // 取得文件名。
           String filename = file.getName().trim();
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
		    	filename = java.net.URLEncoder.encode(filename, "UTF-8");
		    }
		    else{  //firefox/safari不转码
		    	filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		    }
		    response.setContentType("application/msexcel");
           // 设置response的Header
           response.addHeader("Content-Disposition", "attachment;filename=" +toUtf8String(filename));
           response.addHeader("Content-Length", "" + file.length());
           OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
           response.setContentType("application/octet-stream");
           toClient.write(buffer);
           toClient.flush();
           toClient.close();
       } catch (IOException ex) {
           ex.printStackTrace();
       }
       return null;
   }
   public static String toUtf8String(String s){
       StringBuffer sb = new StringBuffer();
       for (int i=0;i<s.length();i++){
           char c = s.charAt(i);
           if (c >= 0 && c <= 255){sb.append(c);}
           else{
               byte[] b;
               try { b = Character.toString(c).getBytes("utf-8");}
               catch (Exception ex) {
                   System.out.println(ex);
                   b = new byte[0];
               }
               for (int j = 0; j < b.length; j++) {
                   int k = b[j];
                   if (k < 0) k += 256;
                   sb.append("%" + Integer.toHexString(k).toUpperCase());
               }
           }
       }
       return sb.toString();
   }
   
   /**
    * 为每个饼状图赋值
    * @param list   所有媒体名称查询集合
    * @param length   有多少个媒体名称
    * @param maprsSUM 每个媒体的汇总点击人数
    * @return  n 代表第n个表格
    */
   
  /* private static DefaultPieDataset getDataset(List<String[]> list,int length,int n, Map<String, Integer> maprsSUM) {  
       DefaultPieDataset dpd = new DefaultPieDataset();  
       String bfb="";//用来获得百分比的值，并截取两位小数
    	   for (int k = n*length; k <(n+1)*length; k++) {
    		   DecimalFormat df1 = new DecimalFormat("##0.00");// 显示两位小数点，千分位
    		   bfb=df1.format(Double.valueOf(list.get(k)[2])/maprsSUM.get(list.get(k)[0])*100);
    		   // df1.format(Double.valueOf(list.get(k)[2])/maprsSUM.get(list.get(k)[0]));
    		   // System.out.println(Double.valueOf(list.get(k)[2])/maprsSUM.get(list.get(k)[0])*100);
    		   // System.out.println(df1.format(Double.valueOf(list.get(k)[2])/maprsSUM.get(list.get(k)[0])*100));
    		   //暂时先将汉字转化为英文
   	            dpd.setValue(PinyinUtil.stringArrayToString(PinyinUtil.getHeadByString(list.get(k)[1])," "),Double.valueOf(bfb));
	       }
       return dpd;  
   } 
  
   private static ByteArrayOutputStream handlePicture(String pathOfPicture) {  
       ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();  
       try {  
    	   //图片的生成地址从配置文件中取得
           bufferImg = ImageIO.read(new File(pathOfPicture));  
           ImageIO.write(bufferImg, "jpeg", byteArrayOut);  
       } catch (IOException e) {  
           // TODO Auto-generated catch block  
           e.printStackTrace();  
       }  
       return byteArrayOut;  
   }  
 */
   
	   
	   
	   
   
}
