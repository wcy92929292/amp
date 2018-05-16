package com.udbac.entity;

import java.util.Date;

/**
 * 结案数据报表
 * @author Administrator
 *
 */
public class DataEndInfo {
   private String media_name;//媒体名称
   private String activity_name;//项目名称
   private String point_location;//投放位置
   private String put_function;//投放形式
   private Integer dirty_imp_uv;//曝光人数
   private Double dirty_imp_pv;//曝光次数
   private Integer dirty_clk_uv;//点击人数
   private Integer dirty_clk_pv;//点击次数
   private Integer exposure_avg;//预估曝光
   private Integer click_avg;//预估点击
   private String mic;//短代码
   private Integer visits;//访问次数
   private Integer visitor;//访问人数
   private Integer pv;//浏览量
   private Integer bounce_visit;//跳出次数
   private Double visits_time;//平均访问时间
   private Integer  num_fre;//频次
   private String province;//省份
   private String media;//点击分媒体的媒体名称
   private Integer sumuv;//点击分媒体的人数汇总
   private Date startDate;//活动的开始日期
   private Date endDate;//活动的结束日期
   private Date put_date;//投放日期
   private String customer_name;//投放单位
   private String unit; // 投放单位
   private Double put_value;// 投放量
   private String activity_code;//活动编号
   
  


public String getActivity_code() {
	return activity_code;
}


public void setActivity_code(String activity_code) {
	this.activity_code = activity_code;
}


public String getUnit() {
	return unit;
}


public void setUnit(String unit) {
	this.unit = unit;
}


public Double getPut_value() {
	return put_value;
}


public void setPut_value(Double put_value) {
	this.put_value = put_value;
}


public String getCustomer_name() {
	return customer_name;
}


public void setCustomer_name(String customer_name) {
	this.customer_name = customer_name;
}


public Date getPut_date() {
	return put_date;
}


public void setPut_date(Date put_date) {
	this.put_date = put_date;
}


public Date getStartDate() {
	return startDate;
}


public void setStartDate(Date startDate) {
	this.startDate = startDate;
}


public Date getEndDate() {
	return endDate;
}


public void setEndDate(Date endDate) {
	this.endDate = endDate;
}


public String getMedia() {
	return media;
}


public void setMedia(String media) {
	this.media = media;
}


public Integer getSumuv() {
	return sumuv;
}


public void setSumuv(Integer sumuv) {
	this.sumuv = sumuv;
}


  

	public String getProvince() {
	return province;
}


public void setProvince(String province) {
	this.province = province;
}


	public Integer getNum_fre() {
	return num_fre;
}


public void setNum_fre(Integer num_fre) {
	this.num_fre = num_fre;
}


	public String getMedia_name() {
		return media_name;
	}
	
	
	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}
	
	
	public String getActivity_name() {
		return activity_name;
	}
	
	
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	
	
	public String getPoint_location() {
		return point_location;
	}
	
	
	public void setPoint_location(String point_location) {
		this.point_location = point_location;
	}
	
	
	public String getPut_function() {
		return put_function;
	}
	
	
	public void setPut_function(String put_function) {
		this.put_function = put_function;
	}
	
	
	public Integer getDirty_imp_uv() {
		return dirty_imp_uv;
	}
	
	
	public void setDirty_imp_uv(Integer dirty_imp_uv) {
		this.dirty_imp_uv = dirty_imp_uv;
	}


	public Double getDirty_imp_pv() {
		return dirty_imp_pv;
	}


	public void setDirty_imp_pv(Double dirty_imp_pv) {
		this.dirty_imp_pv = dirty_imp_pv;
	}


	public Integer getDirty_clk_uv() {
		return dirty_clk_uv;
	}


	public void setDirty_clk_uv(Integer dirty_clk_uv) {
		this.dirty_clk_uv = dirty_clk_uv;
	}


	public Integer getDirty_clk_pv() {
		return dirty_clk_pv;
	}


	public void setDirty_clk_pv(Integer dirty_clk_pv) {
		this.dirty_clk_pv = dirty_clk_pv;
	}


	public Integer getExposure_avg() {
		return exposure_avg;
	}


	public void setExposure_avg(Integer exposure_avg) {
		this.exposure_avg = exposure_avg;
	}


	public Integer getClick_avg() {
		return click_avg;
	}


	public void setClick_avg(Integer click_avg) {
		this.click_avg = click_avg;
	}


	public String getMic() {
		return mic;
	}


	public void setMic(String mic) {
		this.mic = mic;
	}


	public Integer getVisits() {
		return visits;
	}


	public void setVisits(Integer visits) {
		this.visits = visits;
	}


	public Integer getVisitor() {
		return visitor;
	}


	public void setVisitor(Integer visitor) {
		this.visitor = visitor;
	}


	public Integer getPv() {
		return pv;
	}


	public void setPv(Integer pv) {
		this.pv = pv;
	}


	public Integer getBounce_visit() {
		return bounce_visit;
	}


	public void setBounce_visit(Integer bounce_visit) {
		this.bounce_visit = bounce_visit;
	}


	public Double getVisits_time() {
		return visits_time;
	}


	public void setVisits_time(Double visits_time) {
		this.visits_time = visits_time;
	}
	
	 
}
