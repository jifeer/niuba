package com.etyero.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.RankInfoMapper;
import com.etyero.entity.RankInfo;
import com.etyero.service.RankInfoService;

@Service("RankInfoServiceImpl")
public class RankInfoServiceImpl implements RankInfoService {
	@Resource
	private RankInfoMapper rankInfoMapper;

	@Override
	public void addRankInfo(RankInfo rInfo) {
		this.rankInfoMapper.insertRankInfo(rInfo);
	}

	@Override
	public List<RankInfo> getRankInfoByDate(String date) {
		return this.rankInfoMapper.selectRankInfoByDate(date);
	}

	@Override
	public void updateRankInfoByDate(RankInfo rInfo) {
		this.rankInfoMapper.updateRankInfoByDate(rInfo);

	}

}
