package com.etyero.dao;

import java.util.List;

import com.etyero.entity.DTInfo;

public interface DTInfoMapper {
	// 插入DT当前分红信息
	public void insertDTInfo(DTInfo dt);

	// 根据日期查找DT分红
	public List<DTInfo> selectDTInfoByCreateTime(String createTime);

	// 更新DT分红信息
	public void updateDTInfoByCreateTime(DTInfo dt);
}
