package com.etyero.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.DTInfoMapper;
import com.etyero.entity.DTInfo;
import com.etyero.service.DTInfoService;

@Service("DTInfoServiceImpl")
public class DTInfoServiceImpl implements DTInfoService {

	@Resource
	private DTInfoMapper dtInfoMapper;

	@Override
	public void addDTInfo(DTInfo dt) {
		this.dtInfoMapper.insertDTInfo(dt);
	}

	@Override
	public List<DTInfo> getDTInfoByCreateTime(String createTime) {
		return this.dtInfoMapper.selectDTInfoByCreateTime(createTime);
	}

	@Override
	public void updateDTInfoByCreateTime(DTInfo dt) {
		this.dtInfoMapper.updateDTInfoByCreateTime(dt);

	}

}
