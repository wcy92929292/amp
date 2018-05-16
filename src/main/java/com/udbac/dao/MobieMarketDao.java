package com.udbac.dao;

import java.util.Date;
import java.util.List;

import com.udbac.model.DetailTerminalSales;

/****
 * 移动商城Dao
 * 
 * @author lily
 * @date 2015-03-10
 *
 */
public interface MobieMarketDao {
	
	/***
	 * 插入商品明细数据
	 * 
	 * @param poList
	 */
	public void insertTerminaldata(List<DetailTerminalSales> poList);

	/***
	 * 查询某日期下商品明细数据
	 * 
	 * @param parse
	 */
	public int findTerminalByDate(Date parse);

	/***
	 * 删除某日期下商品明细数据
	 * 
	 * @param parse
	 */
	public void deleteTerminalByDate(Date parse);
	
	/***
	 * 调用存储过程，517数据销售
	 * 
	 */
	public void callSaleFunction(String dateStr);


}
