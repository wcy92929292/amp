package com.udbac.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.model.MonthReportModel;

/**
 * 实时数据相关DAO
 * @author LFQ
 *
 */
public interface RealDataDao {
	
	/**
	 * 查看历史7天的数据
	 * @param date
	 * @param mic
	 * @return
	 */
	List<MonthReportModel> listHistoryWeek(@Param("date") Date date,@Param("mic") String mic);
}
