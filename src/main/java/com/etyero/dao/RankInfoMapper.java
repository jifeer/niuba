package com.etyero.dao;

import java.util.List;

import com.etyero.entity.RankInfo;

public interface RankInfoMapper {
	// 插入排名信息
	public void insertRankInfo(RankInfo rInfo);

	// 根据日期查找排名信息
	public List<RankInfo> selectRankInfoByDate(String date);

	// 更新排名信息
	public void updateRankInfoByDate(RankInfo rInfo);
}
