package com.etyero.service;

import java.util.List;

import com.etyero.entity.LCEXInfo;

public interface LCEXInfoService {
	// 插入LCEX当前分红信息
	public void addLCEXInfo(LCEXInfo lcexInfo);

	// 根据日期查找LCEX分红
	public List<LCEXInfo> getLCEXInfoByCreateTime(String createTime);

	// 根据时间区间查找LCEX分红
	public List<LCEXInfo> getLCEXInfoByCreateTimeRange(String startTime, String endTime);

	// 更新LCEX分红信息
	public void updateLCEXInfoByCreateTime(LCEXInfo lcexInfo);
}
