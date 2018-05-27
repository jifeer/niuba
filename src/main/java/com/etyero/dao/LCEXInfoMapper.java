package com.etyero.dao;

import java.util.List;

import com.etyero.entity.LCEXInfo;

public interface LCEXInfoMapper {
	// 插入LCEX当前分红信息
	public void insertLCEXInfo(LCEXInfo lcexInfo);

	// 根据日期查找LCEX分红
	public List<LCEXInfo> selectLCEXInfoByCreateTime(String createTime);
	
	//根据时间区间查找LCEX分红
	public List<LCEXInfo> selectLCEXInfoByCreateTimeRange(String startTime,String endTime);
	

	// 更新LCEX分红信息
	public void updateLCEXInfoByCreateTime(LCEXInfo lcexInfo);
}
