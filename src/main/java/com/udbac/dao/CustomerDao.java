package com.udbac.dao;

import java.util.List;

import com.udbac.entity.TbAmpBasicCustomerInfo;

/**
 * 客户管理Dao
 * @author LFQ
 *
 */
public interface CustomerDao {

	/**
	 * 查询所有的客户
	 * @return
	 */
	List<TbAmpBasicCustomerInfo> selectAll();
	
}
