package com.etyero.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.LCEXInfoMapper;
import com.etyero.entity.LCEXInfo;
import com.etyero.service.LCEXInfoService;

@Service("LCEXInfoServiceImpl")
public class LCEXInfoServiceImpl implements LCEXInfoService {

	@Resource
	private LCEXInfoMapper lcexInfoMapper;

	@Override
	public void addLCEXInfo(LCEXInfo lcexInfo) {
		lcexInfoMapper.insertLCEXInfo(lcexInfo);
	}

	@Override
	public List<LCEXInfo> getLCEXInfoByCreateTime(String createTime) {
		return lcexInfoMapper.selectLCEXInfoByCreateTime(createTime);
	}

	@Override
	public void updateLCEXInfoByCreateTime(LCEXInfo lcexInfo) {
		lcexInfoMapper.updateLCEXInfoByCreateTime(lcexInfo);
	}

	@Override
	public List<LCEXInfo> getLCEXInfoByCreateTimeRange(String startTime, String endTime) {
		return lcexInfoMapper.selectLCEXInfoByCreateTimeRange(startTime, endTime);
	}

}
