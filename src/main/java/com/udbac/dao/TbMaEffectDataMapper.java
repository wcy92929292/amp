package com.udbac.dao;

import java.util.List;

import com.udbac.model.TbMaEffectData;

public interface TbMaEffectDataMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(TbMaEffectData record);

    int insertSelective(TbMaEffectData record);

    TbMaEffectData selectByPrimaryKey(Integer id);
    
    List<TbMaEffectData>  selectByCreateDate(String cdate);
    
    List<TbMaEffectData>  selectCheckInClickByCreateDate(String cdate);

    List<TbMaEffectData>  selectCheckOutClickByCreateDate(String cdate);
    
    List<TbMaEffectData>  selectCheckInExposureByCreateDate(String cdate);

    List<TbMaEffectData>  selectCheckOutExposureByCreateDate(String cdate);

    int updateByPrimaryKeySelective(TbMaEffectData record);

    int updateByPrimaryKey(TbMaEffectData record);
}