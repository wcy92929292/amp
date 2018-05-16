package com.udbac.util;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.udbac.controller.DataExportController;
import com.udbac.entity.DataEndInfo;
import com.udbac.entity.WeekDataExport;
/**
 * 结案报表数据的工具类
 *  @author Wangli  2016-06-21
 */
public class DataEndUntil {
	
	//拼接数组	
	public String[] checkOutTotal(WeekDataExport de,boolean unit,int flag, double sumYuGu, double sumDiJi){
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
		String ksrq="";//实际的开始日期
		String jsrq="";//实际的结束日期
		String hbfz="";//获得当前需要分组点位
		String sumbg="";//汇总的预估曝光
		String sumdj="";//汇总的预估点击
		DecimalFormat df0 = new DecimalFormat("###0");// 不显示小数点,千分位
		DecimalFormat df1 = new DecimalFormat("###0.00");// 显示两位小数点，千分位
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//集团的用户才用（不包含咪咕集团）
		if(de.getActivityCode().indexOf("JT")!=-1&&de.getActivityCode().indexOf("MGJT")==-1){
			if(unit){
				if(de.getClickAvg()<=0||de.getClickAvg()==null||de.getPut_value()==0){
					djyg="N/A";
					djwcl="0";
				}else{
					djyg =df0.format(de.getPut_value()*de.getClickAvg());//点击预估
					djwcl=df1.format(de.getClkPv()/(de.getPut_value()*de.getClickAvg()) * 100);//点击完成率
				}
				if(de.getExposureAvg()<=0||de.getExposureAvg()==null||de.getPut_value()==0){
					bgyg="N/A";
					bgwcl="0";
				}else{
					bgyg=df0.format(de.getPut_value()*1000);//曝光预估
					bgwcl=df1.format(de.getImpPv() /(de.getPut_value()*1000) * 100);
				}
			}else{
				if(de.getClickAvg()<=0 || de.getClickAvg()== null){//sql已经将空全部替换为-1了
					djwcl="0";
					djyg ="N/A";
				}else{
					djwcl=df1.format(de.getClkPv()/de.getClickAvg() * 100);
					djyg=df0.format(de.getClickAvg());
				}
				if(de.getExposureAvg()<=0||de.getExposureAvg()==null){
					bgyg="N/A";
					bgwcl="0";
				}else{
					bgyg=df0.format(de.getExposureAvg());//曝光预估
					bgwcl=df1.format(de.getImpPv() /de.getExposureAvg()* 100);
				}
			}
		}else{
			if(de.getClickAvg()<=0 || de.getClickAvg()== null){//sql已经将空全部替换为-1了
				djwcl="0";
				djyg ="N/A";
			}else{
				djwcl=df1.format(de.getClkPv()/de.getClickAvg() * 100);
				djyg=df0.format(de.getClickAvg());
			}
			if(de.getExposureAvg()<=0||de.getExposureAvg()==null){
				bgyg="N/A";
				bgwcl="0";
			}else{
				bgyg=df0.format(de.getExposureAvg());//曝光预估
				bgwcl=df1.format(de.getImpPv() /de.getExposureAvg()* 100);
			}
		}
				
			if(de.getImpPv()<=0){
				bgcs="N/A";
				djl="0";
			}else{
				djl=df1.format(de.getClkPv()/de.getImpPv()*100);
				bgcs=df0.format(de.getImpPv()); //曝光次数
			}
			if(de.getImpUv()<=0){
				bgrs="N/A";
			}else {
				bgrs=df0.format(de.getImpUv()); //曝光人数
			}
			if(de.getClkPv()<=0){
				djcs="N/A";
			}else{
				djcs=df0.format(de.getClkPv()); //点击次数
			}
			if(de.getClkUv()<=0){
				djrs="N/A";
			}else{
				djrs=df0.format(de.getClkUv()); //点击人数
			}
			if(de.getVisit()<=0){
				fwcs="N/A";
				tcl="0";
			}else{
				tcl=df1.format(de.getBounceTimes()/de.getVisit()*100);
				fwcs=df0.format(de.getVisit());//访问次数
			}
			if(de.getVisitor()<=0){
				fwrs="N/A";
			}else{
				fwrs=df0.format(de.getVisitor());
			}
			
			if(de.getClick()<=0){
				clk="N/A";
			}else{
				clk=df0.format(de.getClick());
			 }
			if(de.getPageView()<=0){
				lll="N/A";
			}else{
				lll=df0.format(de.getPageView());
			}
			if(de.getBounceTimes()<=0){
				tcsc="N/A";
			}else{
				tcsc=df0.format(de.getBounceTimes());
			}
			if(de.getViewTime()<=0||de.getViewTime()==null){
				pjfwsj="N/A";
			}else{
				pjfwsj=df1.format(de.getViewTime());
			}
			if(de.getPutDate()==null){
				tfrq="N/A";
			}else{
				tfrq=sdf.format(de.getPutDate());
			}
			if(de.getStartDate()==null&&de.getEndDate()==null){
				ksrq="N/A";
				jsrq="N/A";
			}else{
				ksrq=sdf.format(de.getStartDate());
				jsrq=sdf.format(de.getEndDate());
			}
	        
			
		if(flag==1){//明细不汇总的
			if(de.getGroup_id()==0){
	        	 hbfz="0"; 
	         }else{
	        	 hbfz=de.getGroup_id().toString();
	        	 }
			 String[] checkout2 = { de.getActivityName(), de.getMediaName(),de.getTerminal_type() ,de.getPointLocation(),de.getPutFunction(),de.getMic(),tfrq.replace("-", "/"),// 获得当前的日期
						bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,clk,lll,
						tcsc,tcl,pjfwsj,bgwcl,djwcl ,de.getUrlPc(),"",hbfz};
			          return  checkout2;
		}else if(flag==2) {//分媒体汇总
			if(sumYuGu==0){
				sumbg="N/A";
			}else{
				sumbg= df0.format(sumYuGu);
				bgwcl=df1.format(de.getImpPv()/sumYuGu*100);
			}
			if(sumDiJi==0){
				sumdj="N/A";
			}else{
				sumdj=df0.format(sumDiJi);
				djwcl=df1.format(de.getClkPv()/sumDiJi*100);
			}
			 String[] checkout2 = { de.getMediaName()+"汇总", "","","","","", "",// 获得当前的日期
					 sumbg,bgcs,bgrs, sumdj, djcs,djrs,djl, fwcs,fwrs,clk,lll,
						tcsc,tcl,pjfwsj,bgwcl,djwcl,de.getUrlPc(),"","0"};
						return checkout2;
		     
		}else if(flag==3)  {//分活动名称汇总
			if(sumYuGu==0){
				sumbg="N/A";
			}else{
				sumbg= df0.format(sumYuGu);
				bgwcl=df1.format(de.getImpPv()/sumYuGu*100);
			}
			if(sumDiJi==0){
				sumdj="N/A";
			}else{
				sumdj=df0.format(sumDiJi);
				djwcl=df1.format(de.getClkPv()/sumDiJi*100);
			}
			 String[] checkout2 = { de.getActivityName()+"汇总", "","","","","","", // 获得当前的日期
					 sumbg,bgcs,bgrs, sumdj, djcs,djrs,djl, fwcs,fwrs,clk,lll,
						tcsc,tcl,pjfwsj,bgwcl,djwcl,de.getUrlPc(),"","0"};
						return checkout2;
		}else{
			//确保投放周期为真是的投放日期而不是页面上输入的截止日期
			if(de.getGroup_id()==0){
	        	 hbfz="0"; 
	         }else{
	        	 hbfz=de.getGroup_id().toString();
	        	 }
			 String[] checkout2 = { de.getActivityName(), de.getMediaName(),de.getTerminal_type() ,de.getPutFunction(),de.getPointLocation(),de.getMic(),ksrq.replace("-", "/")+"-"+jsrq.replace("-", "/"),// 获得当前的日期
						bgyg,bgcs,bgrs, djyg, djcs,djrs,djl, fwcs,fwrs,clk,lll,
						tcsc,tcl,pjfwsj,bgwcl,djwcl ,de.getUrlPc(),"",hbfz};
			          return  checkout2;
			
		}
	}


	
	/**
	 * 结案数据报表中的汇总sheet页的值
	 * @param de 
	 * @param i 
	 * @param ygdja 
	 * @param sumDiJi
	 * @param sumBaoG
	 * @return
	 */
	
	@SuppressWarnings("unused")
	public  String[]  checkoutHui(DataEndInfo de, int i, Integer ygdja, double sumDiJi, double sumBaoG) {
		  String mtmc="";//媒体名称
		    String xm="";//项目名称
		    String tfwz="";//投放位置
		    String tfxs="";//投放形式
		    String pjzfc="";//媒体名称+投放位置+投放形式
		    String mic="";//短代码
		    String ygbg="";//预计曝光
		    String bgcs="";//曝光次数
		    String bgrs="";//曝光人数
		    String bgdbl="";//曝光达标率
		    String ygdj="";//预估点击
		    String djcs="";//点击次数
		    String djrs="";//点击人数
		    String djdbl="";//点击达标率
		    String djlCTR="";//点击率CTR
		    String fwcs="";//访问次数
		    String sydbl="";//首页达标率
		    String fwrs="";//访问人数
		    String lll="";//浏览量
		    String tccs="";//跳出次数
		    String tcl="";//跳出率
		    String pjfwsj="";//平均访问时间  
		    String djbbg="";//点击人数/曝光人数
		    String tfl="";//投放量
		    String unit="";//投放单位
		    
		    DecimalFormat df0 = new DecimalFormat("0");// 不显示小数点,千分位
			DecimalFormat df1 = new DecimalFormat("0.00");// 显示两位小数点，千分位
			//集团和辽宁的用户才用这个算法(其中不包含咪咕集团)
			if(de.getActivity_code().indexOf("JT")!=-1&&de.getActivity_code().indexOf("MGJT")==-1||de.getActivity_code().indexOf("LN")!=-1){
				if(de.getUnit().equals("CPM")){
					if(de.getPut_value()==0||de.getPut_value()==null){
						bgdbl="#Div/0!";
					}else{
						ygbg=df0.format(de.getPut_value()*1000);
						bgdbl=df1.format((double)de.getDirty_imp_pv()/(de.getPut_value()*1000)*100);
					}
					if(de.getClick_avg()==null||de.getClick_avg()==0){
						ygdj="0";
						djdbl="#Div/0!";
					}else{
						ygdj=df0.format(de.getPut_value()*ygdja);
						djdbl=df0.format((double)de.getDirty_clk_pv()/(de.getPut_value()*ygdja)*100);
					}
					
				}else{
					if(de.getExposure_avg()==0){
						ygbg="0";
						bgdbl="#Div/0!";
					}else if(de.getExposure_avg()==-1){
						ygbg="N/A";
						bgdbl="N/A";
					}else{
						ygbg=df0.format(de.getExposure_avg());
						bgdbl=df1.format((double)de.getDirty_imp_pv()/de.getExposure_avg()*100);
					}
					if(de.getClick_avg()==0){
						ygdj="0";
						djdbl="#Div/0!";
					}else if(de.getClick_avg()==-1){
						ygdj="N/A";
						djdbl="N/A";
					}else{
						ygdj=df0.format(de.getClick_avg());
						djdbl=df1.format((double)de.getDirty_clk_pv()/de.getClick_avg()*100);
					}
				}
			}else{
					if(de.getExposure_avg()==0){
						ygbg="0";
						bgdbl="#Div/0!";
					}else if(de.getExposure_avg()==-1){
						ygbg="N/A";
						bgdbl="N/A";
					}else{
						ygbg=df0.format(de.getExposure_avg());
						bgdbl=df1.format((double)de.getDirty_imp_pv()/de.getExposure_avg()*100);
					}
					if(de.getClick_avg()==0){
						ygdj="0";
						djdbl="#Div/0!";
					}else if(de.getClick_avg()==-1){
						ygdj="N/A";
						djdbl="N/A";
					}else{
						ygdj=df0.format(de.getClick_avg());
						djdbl=df1.format((double)de.getDirty_clk_pv()/de.getClick_avg()*100);
					}
				}
			
				
			if(de.getDirty_imp_pv()==0){
				bgcs="0";
				djlCTR="#Div/0!";
			}else if (de.getDirty_imp_pv()==-1) {
				bgcs="N/A";
				bgdbl="N/A";
				djlCTR="N/A";
			}else{
				bgcs=df0.format(de.getDirty_imp_pv());
				djlCTR=df1.format((double)de.getDirty_clk_pv()/de.getDirty_imp_pv()*100);
			}
			if(de.getDirty_clk_pv()==0){
				djcs="0";
				sydbl="#Div/0!";
			}else if (de.getDirty_clk_pv()==-1) {
				djcs="N/A";
				djdbl="N/A";
				djlCTR="N/A";
				sydbl="N/A";
			}else{
				djcs=df0.format(de.getDirty_clk_pv());
				sydbl=df1.format((double)de.getVisits()/de.getDirty_clk_pv()*100);
			}
			 if (de.getVisits()==0) {
				fwcs="0";
				sydbl="0.00%";
				tcl="#Div/0!";
			}else if (de.getVisits()<=-1) {
				fwcs="N/A";
				sydbl="N/A";
				tcl="N/A";
			}else{
				fwcs=df0.format(de.getVisits());
				tcl=df1.format((double)de.getBounce_visit()/de.getVisits()*100);

			}
			
			if(de.getBounce_visit()==0){
				tccs="0";
			}else if (de.getBounce_visit()<=-1) {
				tccs="N/A";
				tcl="N/A";
			}else{
				tccs=df0.format(de.getBounce_visit());
			}
			 if (de.getVisits_time()<=0) {
				pjfwsj="N/A";
			}else{
				pjfwsj=df1.format(de.getVisits_time());
			}
			mtmc=de.getMedia_name();//媒体名称
			xm=de.getActivity_name();//项目
			tfwz=de.getPut_function();//投放位置
			tfxs=de.getPoint_location();//投放形式
			pjzfc=mtmc+tfxs+tfwz;//拼接字符
			mic=de.getMic();//短代码
			
			 if(i==0&&de.getDirty_imp_pv()>0&&sumBaoG>0){
				ygbg=df0.format(sumBaoG);
	              bgdbl=df1.format(de.getDirty_imp_pv()/sumBaoG*100);				
			}else if(i==0&&sumBaoG<0){
			 ygbg="N/A";
			 }
			 if(i==3){
				String[]checkout={xm,mtmc,ygbg,bgcs,bgdbl,ygdj,djcs,djdbl,djlCTR,fwcs,sydbl,tccs,tcl,pjfwsj};
				return checkout;
			}else{
				if(i==0&&de.getDirty_clk_pv()>0&&sumDiJi>0){
					  ygdj=df0.format(sumDiJi);
		              djdbl=df1.format(de.getDirty_clk_pv()/sumDiJi*100);
				}else if(sumDiJi<=0){
					 ygdj="N/A";
				 }
      //其中的i为预估曝光的总计值
				String[]checkout={"总计","",ygbg,bgcs,bgdbl,ygdj,
						djcs,djdbl,djlCTR,fwcs,sydbl,tccs,tcl,pjfwsj};
				return checkout;
			}
			
			  
	}
	
	
	public String[] checkoutDJDY(DataEndInfo de, int i) {
		 DecimalFormat df0 = new DecimalFormat("0");// 不显示小数点,千分位
	 	 String djrs="";
		 String djcs="";
		 if(de.getDirty_clk_uv()==null||de.getDirty_clk_uv()==0){
			 djrs="0";
		 } else{
			djrs=de.getDirty_clk_uv().toString();
		 }
		 if(de.getDirty_clk_pv()==null||de.getDirty_clk_pv()==0){
			 djcs="0";
		 } else{
			djcs=df0.format(de.getDirty_clk_pv());
		 }
		 if(i==1){
			 //点击地域的信息
			 String[] checkout={de.getMedia_name(),de.getProvince(),"",djcs,djrs,""};
		     return checkout; 
		 }else{
			 //点击地域的饼状图的信息
			 String[] checkout={de.getMedia_name(),de.getProvince(),djrs};
		     return checkout; 
		     
		 }
		
	}

	/**
	 * 
	 * @param de
	 * @param djrsHZ
	 * @param djcsHZ
	 * @param i
	 * @return
	 */
	public String[] checkoutZTDJ(DataEndInfo de, int djrsHZ, int djcsHZ) {
		 DecimalFormat df0 = new DecimalFormat("0");// 不显示小数点,千分位
		 DecimalFormat df1 = new DecimalFormat("0.00");// 显示两位小数点，千分位
	 	String djrs="";
		String djcs="";
		String clpv="";
		String cluv="";
		 if(de.getDirty_clk_uv()==null||de.getDirty_clk_uv()==0){
			 djrs="0";
			 cluv="#Div/0!";
		 } else{
			djrs=df0.format(de.getDirty_clk_uv());
			cluv=df1.format((double)de.getDirty_clk_uv()/djrsHZ*100);
		 }
		 if(de.getDirty_clk_pv()==null||de.getDirty_clk_pv()==0){
			 djcs="0";
			 clpv="#Div/0!";
		 } else{
			djcs=df0.format(de.getDirty_clk_pv());
			clpv=df1.format((double)de.getDirty_clk_pv()/djcsHZ*100);
		 }
		 String[] checkout={df0.format(de.getNum_fre()),djcs,djrs,clpv,cluv};
	    return checkout;
	}
	/**
	 * 整体曝光数据
	 * @param de
	 * @param bgrsHZ
	 * @param bgcsHZ
	 * @return
	 */
	public String[] checkoutZTBG(DataEndInfo de, int bgrsHZ, double bgcsHZ) {
		 DecimalFormat df0 = new DecimalFormat("0");// 不显示小数点,千分位
		 DecimalFormat df1 = new DecimalFormat("0.00");// 显示两位小数点，千分位
	 	String bgrs="";
		String bgcs="";
		String impv="";
		String imuv="";
		 if(de.getDirty_imp_uv()==null||de.getDirty_imp_uv()==0){
			 bgrs="0";
			 imuv="#Div/0!";
		 } else{
			bgrs=df0.format(de.getDirty_imp_uv());
			imuv=df1.format((double)de.getDirty_imp_uv()/bgrsHZ*100);
		 }
		 if(de.getDirty_imp_pv()==null||de.getDirty_imp_pv()==0){
			 bgcs="0";
			 impv="#Div/0!";
		 } else{
			bgcs=df0.format(de.getDirty_imp_pv());
			impv=df1.format((double)de.getDirty_imp_pv()/bgcsHZ*100);
		 }
		 
			 String[] checkout={df0.format(de.getNum_fre()),bgcs,bgrs,impv,imuv};
		    	return checkout;
		}

	/**
	 * 曝光分媒体的拼接数组
	 * @param de
	 * @param i 
	 * @return
	 */
	 @SuppressWarnings("unused")
	public String[] checkoutFMT(DataEndInfo de, int i){
		 DecimalFormat df0 = new DecimalFormat("0");// 不显示小数点,千分位
		 DecimalFormat df1 = new DecimalFormat("0.00");// 显示两位小数点，千分位
			String pc="";//频次
			String bgcs="";//曝光次数
			String bgrs="";//曝光人数
			String djcs="";//点击次数
			String djrs="";//点击人数
			
			if(de.getNum_fre()==null){
				pc="0";
			}else{
				pc=df0.format(de.getNum_fre());
			}
			if(de.getDirty_imp_pv()==null){
				bgcs="0";
			}else{
				bgcs=df0.format(de.getDirty_imp_pv());
			}
			if(de.getDirty_imp_uv()==null){
				bgrs="0";
			}else{
				bgrs=df0.format(de.getDirty_imp_uv());
			}
			if (de.getDirty_clk_pv()==null) {
				djcs="0";
			}else{
				djcs=df0.format(de.getDirty_clk_pv());
			}
			if(de.getDirty_clk_uv()==null){
				djrs="0";
			}else{
				djrs=df0.format(de.getDirty_clk_uv());
			}
			
			if(i==1){
				 String[] checkout={de.getMedia_name(),pc,bgcs,bgrs};
					return checkout;
			}else if(i==2){
				//分媒体广告位
				String[] checkout={de.getMedia_name(),de.getMic(),de.getPoint_location()+de.getPut_function(),pc,bgcs,bgrs};
			
				return checkout;
				
			}else if(i==3){
				String[] checkout={de.getMedia_name()};
				return checkout;
			}else{
				 String[] checkout={de.getMedia_name(),pc,djcs,djrs};
					return checkout;
			}
		  
		}  
	
	
	/**
	 * 结案数据报表的基础数据报表信息
	 * @param de
	 * @param i
	 * @param ygdja
	 * @param sumDiJi
	 * @param sumBaoG
	 * @return
	 */
	 
	 @SuppressWarnings("unused")
	public String[] checkout(DataEndInfo de, int i, Integer ygdja, double sumDiJi,double sumBaoG){
		    String mtmc="";//媒体名称
		    String xm="";//项目名称
		    String tfwz="";//投放位置
		    String tfxs="";//投放形式
		    String pjzfc="";//媒体名称+投放位置+投放形式
		    String mic="";//短代码
		    String ygbg="";//预计曝光
		    String bgcs="";//曝光次数
		    String bgrs="";//曝光人数
		    String bgdbl="";//曝光达标率
		    String ygdj="";//预估点击
		    String djcs="";//点击次数
		    String djrs="";//点击人数
		    String djdbl="";//点击达标率
		    String djlCTR="";//点击率CTR
		    String fwcs="";//访问次数
		    String sydbl="";//首页达标率
		    String fwrs="";//访问人数
		    String lll="";//浏览量
		    String tccs="";//跳出次数
		    String tcl="";//跳出率
		    String pjfwsj="";//平均访问时间  
		    String djbbg="";//点击人数/曝光人数
		    String tfl="";//投放量
		    String unit="";//投放单位
		    String sumYgdj="";
		    String sumdjbbg="";
		    
		    DecimalFormat df0 = new DecimalFormat("0");// 不显示小数点,千分位
			DecimalFormat df1 = new DecimalFormat("0.00");// 显示两位小数点，千分位
			//集团和辽宁的用户才用这个算法
			if(de.getActivity_code().indexOf("JT")!=-1&&de.getActivity_code().indexOf("MGJT")==-1||de.getActivity_code().indexOf("LN")!=-1){
				if(de.getUnit().equals("CPM")){
					if(de.getPut_value()==0||de.getPut_value()==null){
						bgdbl="#Div/0!";
					}else{
						ygbg=df0.format(de.getPut_value()*1000);
						bgdbl=df1.format((double)de.getDirty_imp_pv()/(de.getPut_value()*1000)*100);
					}
					if(de.getClick_avg()==null||de.getClick_avg()==0){
						ygdj="0";
						djdbl="#Div/0!";
					}else{
						ygdj=df0.format(de.getPut_value()*ygdja);
						djdbl=df0.format((double)de.getDirty_clk_pv()/(de.getPut_value()*ygdja)*100);
					}
					
				}else{
					if(de.getExposure_avg()==0){
						ygbg="N/A";
						bgdbl="#Div/0!";
					}else if(de.getExposure_avg()==-1){
						ygbg="N/A";
						bgdbl="N/A";
					}else{
						ygbg=df0.format(de.getExposure_avg());
						bgdbl=df1.format((double)de.getDirty_imp_pv()/de.getExposure_avg()*100);
					}
					if(de.getClick_avg()==0){
						ygdj="N/A";
						djdbl="#Div/0!";
					}else if(de.getClick_avg()==-1){
						ygdj="N/A";
						djdbl="N/A";
					}else{
						ygdj=df0.format(de.getClick_avg());
						djdbl=df1.format((double)de.getDirty_clk_pv()/de.getClick_avg()*100);
					}
				}
			}else{
					if(de.getExposure_avg()==0){
						ygbg="N/A";
						bgdbl="#Div/0!";
					}else if(de.getExposure_avg()==-1){
						ygbg="N/A";
						bgdbl="N/A";
					}else{
						ygbg=df0.format(de.getExposure_avg());
						bgdbl=df1.format((double)de.getDirty_imp_pv()/de.getExposure_avg()*100);
					}
					if(de.getClick_avg()==0){
						ygdj="N/A";
						djdbl="#Div/0!";
					}else if(de.getClick_avg()==-1){
						ygdj="N/A";
						djdbl="N/A";
					}else{
						ygdj=df0.format(de.getClick_avg());
						djdbl=df1.format((double)de.getDirty_clk_pv()/de.getClick_avg()*100);
					}
				}
			
				
			if(de.getDirty_imp_pv()==0){
				bgcs="N/A";
				djlCTR="#Div/0!";
			}else{
				bgcs=df0.format(de.getDirty_imp_pv());
				djlCTR=df1.format((double)de.getDirty_clk_pv()/de.getDirty_imp_pv()*100);
			}
			if(de.getDirty_imp_uv()==0){
				bgrs="N/A";
				djbbg="#Div/0!";
			}else{
				bgrs=df0.format(de.getDirty_imp_uv());
				djbbg=df1.format((double)de.getDirty_clk_uv()/(double)de.getDirty_imp_uv()*100);
			}
			if(de.getDirty_clk_pv()==0){
				djcs="N/A";
				sydbl="#Div/0!";
			}else{
				djcs=df0.format(de.getDirty_clk_pv());
				sydbl=df1.format((double)de.getVisits()/de.getDirty_clk_pv()*100);
			}
			if(de.getDirty_clk_uv()==0){
				djrs="N/A";
			}else{
				djrs=df0.format(de.getDirty_clk_uv());
			}
			if (de.getVisits()<=0) {
				fwcs="N/A";
				sydbl="N/A";
				tcl="N/A";
			}else{
				fwcs=df0.format(de.getVisits());
				tcl=df1.format((double)de.getBounce_visit()/de.getVisits()*100);
			}
			 if (de.getVisitor()<=0) {
				fwrs="N/A";
			}else{
				fwrs=df0.format(de.getVisitor());
			}
			if (de.getPv()<=0) {
				lll="N/A";
			}else{
				lll=df0.format(de.getPv());
			}
			if (de.getBounce_visit()<=0) {
				tccs="N/A";
				tcl="N/A";
			}else{
				tccs=df0.format(de.getBounce_visit());
			}
			 if (de.getVisits_time()<=0) {
				pjfwsj="N/A";
			}else{
				pjfwsj=df1.format(de.getVisits_time());
			}

			mtmc=de.getMedia_name();//媒体名称
			xm=de.getActivity_name();//项目
			tfwz=de.getPut_function();//投放位置
			tfxs=de.getPoint_location();//投放形式
			pjzfc=mtmc+tfxs+tfwz;//拼接字符
			mic=de.getMic();//短代码
             if(de.getDirty_imp_pv()>0&&sumBaoG>0){
            	ygbg= df0.format(sumBaoG);
            	bgdbl=df1.format(de.getDirty_imp_pv()/sumBaoG*100);
             }else if(i==0&&sumBaoG<=0){
            	 ygbg="N/A";
            	 bgdbl="N/A";
             }
			 if(i==1){
				String[] checkout={mtmc,xm,tfxs,tfwz,pjzfc,mic,ygbg,bgcs,
						bgrs,bgdbl,ygdj,djcs,djrs,djdbl,djlCTR,"",fwcs,sydbl,fwrs,lll,tccs,tcl,pjfwsj};
		     	return checkout;
			}else if(i==2){
				String[] checkout={mtmc+"总计","",mtmc+"总计",mtmc+"总计",mtmc+"总计","",ygbg,bgcs,
						bgrs,bgdbl,ygdj,djcs,djrs,djdbl,djlCTR,djbbg,fwcs,sydbl,fwrs,lll,tccs,tcl,pjfwsj};
		     	return checkout;
			}else if(i==3){
				String[]checkout={xm,mtmc,ygbg,bgcs,bgdbl,ygdj,djcs,djdbl,djlCTR,fwcs,sydbl,tccs,tcl,pjfwsj};
				return checkout;
			}else if(i==4){
				String[]checkout={mtmc,bgcs,bgrs,djcs,djrs,djlCTR,djbbg};
				return checkout;
			}else if(i==5){
				
				if(sumDiJi<=0){
					sumYgdj="N/A";
	            	 djdbl="N/A";
				}else if(sumDiJi>0&&de.getDirty_clk_pv()>0){
					 sumYgdj=df0.format(sumDiJi);
	            	 djdbl=df1.format(de.getDirty_clk_pv()/sumDiJi*100);
				}
				String[]checkout={"总计","",ygbg,bgcs,bgdbl,sumYgdj,djcs,djdbl,djlCTR,fwcs,sydbl,tccs,tcl,pjfwsj};
				return checkout;
			}else if(i==6){
				String[] checkout={"总计",bgcs,bgrs,djcs,djrs,djlCTR,djbbg};
		     	return checkout;
			}else {
				if(sumDiJi<=0){
					sumYgdj="N/A";
	            	 djdbl="N/A";
				}else if(sumDiJi>0&&de.getDirty_clk_pv()>0){
					 sumYgdj=df0.format(sumDiJi);
	            	 djdbl=df1.format(de.getDirty_clk_pv()/sumDiJi*100);
				}
				String[] checkout={"总计","","","","","",ygbg,bgcs,
						bgrs,bgdbl,sumYgdj,djcs,djrs,djdbl,
						djlCTR,djbbg,fwcs,sydbl,fwrs,lll,tccs,tcl,pjfwsj};
		     	return checkout;
			}
			
			  
	   }
	   
	   /**
	    * 设置边框的样式
	    * @param style
	    */
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
	   /**
	    * 设置一个字体颜色为白色加粗 ，背景颜色为红色 宋体
	    */
	   public void setStyleRed(XSSFCellStyle style,XSSFFont font){
		   new DataEndUntil().setBoderStyle(style);
		   style.setFillForegroundColor(new XSSFColor(new Color(190, 0, 0))); // 汇总数据表头的背景颜色
		   style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
		   new DataEndUntil().setBoderStyle(style);
		   new DataEndUntil().setFontChinese(font);
		   font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		   font.setColor(new XSSFColor(new Color(255, 255, 255))); // 字体颜色为白色
		   style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 汉字靠左对齐
		   style.setFont(font);
			
	   }
	   //设置背景颜色为蓝色字体为白色
	   public void setStyleBlue(XSSFCellStyle style,XSSFFont font){
		   new DataEndUntil().setBoderStyle(style);
		   style.setFillForegroundColor(new XSSFColor(new Color(83, 141, 213))); // 汇总数据表头的背景颜色
		   style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		   new DataEndUntil().setBoderStyle(style);
           new DataEndUntil().setFontChinese(font);
		   font.setColor(new XSSFColor(new Color(255, 255, 255))); // 字体颜色为白色
		   style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 汉字靠左对齐
		   style.setFont(font);
			
	   }
	   //设置背景颜色为灰色字体颜色为白色
	   public void setStyleGray(XSSFCellStyle style,XSSFFont font){
		   new DataEndUntil().setBoderStyle(style);
		   style.setFillForegroundColor(new XSSFColor(new Color(90, 90, 90))); // 总计数据表头的背景颜色
		   style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		   new DataEndUntil().setBoderStyle(style);
           new DataEndUntil().setFontChinese(font);
		   font.setColor(new XSSFColor(new Color(255, 255, 255))); // 字体颜色为白色
		   style.setFont(font);
			
	   }
	   //设置背景颜色为绿色
	   public void setStyleGreen(XSSFCellStyle style,XSSFFont font){
		   new DataEndUntil().setBoderStyle(style);
		   style.setFillForegroundColor(new XSSFColor(new Color(159,211,164))); // 总计数据表头的背景颜色
		   style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		   new DataEndUntil().setBoderStyle(style);
           new DataEndUntil().setFontChinese(font);
		   style.setFont(font);
			
	   }
	   /**
	    * 设置字体：为9号，宋体
	    */
	   public void setFontChinese(XSSFFont fontStyle){
		    fontStyle.setFontName("宋体");
			fontStyle.setFontHeightInPoints((short) 9);// 设置字体大小
	   }
	   /**
	    * 设置字体：9号，calibri
	    */
	   public void setFontNumber(XSSFFont fontStyle){
		   fontStyle.setFontName("calibri");
		   fontStyle.setFontHeightInPoints((short) 9); 
	   }
       /**
        * 设置汇总数据基础表汇总格式信息
        */
	   public void setStyleAshy(XSSFCellStyle style,XSSFFont font){
		   new DataEndUntil().setBoderStyle(style);
		   style.setFillForegroundColor(new XSSFColor(new Color(191, 191, 191))); // 汇总数据表头的背景颜色
		   style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		   new DataEndUntil().setBoderStyle(style);
		   new DataEndUntil().setFontNumber(font);
		   style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//数字靠右对齐
		   style.setFont(font);
			
	   }
	   
	   //累计报表中的汇总数据
	   public void setHuiStyle(XSSFCellStyle styleHuiZong,XSSFFont fontHui){
		    new DataEndUntil().setFontNumber(fontHui);
			fontHui.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			styleHuiZong.setFont(fontHui);// 选择需要用到的字体格式
			new DataExportController().setBoderStyle(styleHuiZong);
			styleHuiZong.setFillForegroundColor(new XSSFColor(new Color(219, 229, 241)));//汇总 的背景色
           styleHuiZong.setFillPattern(CellStyle.SOLID_FOREGROUND);
          
		   
	   }
	   
	   //累计报表中的汇总的中文字体
	   public void setHuiChStyle(XSSFCellStyle styleHuiZong,XSSFFont fontHui){
		    new DataEndUntil().setFontChinese(fontHui);
			fontHui.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			styleHuiZong.setFont(fontHui);// 选择需要用到的字体格式
			new DataExportController().setBoderStyle(styleHuiZong);
			styleHuiZong.setFillForegroundColor(new XSSFColor(new Color(219, 229, 241)));//汇总 的背景色
           styleHuiZong.setFillPattern(CellStyle.SOLID_FOREGROUND);
          
		   
	   }
	   
	   //累计报表中的汇总校验字体
	   public void setRedStyle(XSSFCellStyle styleRed,XSSFFont fontRed){
		    new DataEndUntil().setFontNumber(fontRed);//设置字体
			new DataEndUntil().setBoderStyle(styleRed);//设置边框
			styleRed.setFont(fontRed);// 选择需要用到的字体格式
			
          
		   
	   }
	   
		/**
		 * 设置顶部的标题信息
		 * @param workbook
		 */
		public static void setTitle(XSSFWorkbook workbook,SimpleDateFormat sd,Map<String, Object> map){
			
			//英文字体
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 9); // 9号字体
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			
			//日期字体
			XSSFFont font2 = workbook.createFont();
			font2.setFontName("calibri");
			font2.setFontHeightInPoints((short) 9); // 9号字体
			CellStyle cellStyle2 = workbook.createCellStyle();
			cellStyle2.setFont(font2);
			
			//汉字9号字体
			CellStyle ceStyle = workbook.createCellStyle();
			XSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			fontStyle.setFontHeightInPoints((short) 9);// 设置字体大小
			ceStyle.setFont(fontStyle);
			
			XSSFSheet sheet=null;
			 for(int i = 0; i < workbook.getNumberOfSheets(); i++){//获取每个Sheet表
				 sheet=(XSSFSheet) workbook.getSheetAt(i);
				 XSSFRow row1 = sheet.createRow(2);// 需要新建表格，来获取当前的信息
				 XSSFRow row2 = sheet.createRow(3);
				 XSSFRow row3 = sheet.createRow(4);
				 XSSFRow row4 = sheet.createRow(5);
				 XSSFCell cell1 = row1.createCell(1);
				 XSSFCell cell2 = row2.createCell(1);
				 XSSFCell cell3 = row3.createCell(1);
				 XSSFCell cell4 = row3.createCell(2);
				 XSSFCell cell5 = row2.createCell(2);
				 XSSFCell cell6 = row1.createCell(2);
				 XSSFCell cell7 = row4.createCell(1);
				 XSSFCell cell8 = row4.createCell(2);
				 
				 cell1.setCellValue("项目名称:");
				 cell1.setCellStyle(cellStyle);
				 cell2.setCellValue("报表周期:");
				 cell2.setCellStyle(cellStyle);
				 cell3.setCellValue("交付日期:");
				 cell3.setCellStyle(cellStyle);
				 Date de=new Date();
				//获得当前的系统日期
				 cell4.setCellValue(sd.format(de));
				 cell4.setCellStyle(cellStyle);
				 cell5.setCellValue( (String)map.get("smonth"));// 获得填写的日期信息
				 cell5.setCellStyle(cellStyle);
				 cell6.setCellValue((String) map.get("custName"));
				 cell6.setCellStyle(ceStyle);
				 cell7.setCellValue("交付人:");
				 cell7.setCellStyle(cellStyle);
				 cell8.setCellValue((String)map.get("user_name"));
				 cell8.setCellStyle(ceStyle);
				 
			 }
		}
		
		
	

	
}
