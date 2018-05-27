package com.etyero.service;

import java.util.List;

import com.etyero.entity.DTInfo;

public interface  DTInfoService {
	// 插入DT当前分红信息
	public void addDTInfo(DTInfo dt);

	// 根据日期查找DT分红
	public List<DTInfo> getDTInfoByCreateTime(String createTime);

	// 更新DT分红信息
	public void updateDTInfoByCreateTime(DTInfo dt);
}
