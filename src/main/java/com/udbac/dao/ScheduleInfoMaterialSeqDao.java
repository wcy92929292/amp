package com.udbac.dao;

import org.apache.ibatis.annotations.Select;

/**
 * 活动排期点位表的物料序列
 * @author LFQ
 *@date 2016-04-25
 */
public interface ScheduleInfoMaterialSeqDao {
	
	//查找物料序号
	@Select("select nextVal('tb_amp_basic_schedule_info_material_seq')")
	long nextval();
}
