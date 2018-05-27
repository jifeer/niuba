package com.etyero.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.HXCDividendMapper;
import com.etyero.entity.HXCDividend;
import com.etyero.service.HXCDividendService;

@Service("HXCDividendServiceImpl")
public class HXCDividendServiceImpl implements HXCDividendService {
	@Resource
	private HXCDividendMapper hxcDividendMapper;

	@Override
	public void addHXCInfo(HXCDividend hxc) {
		this.hxcDividendMapper.insertHXCInfo(hxc);
	}

	@Override
	public List<HXCDividend> getHXCInfoByCreateTime(String createTime) {
		return this.hxcDividendMapper.selectHXCInfoByCreateTime(createTime);

	}

	@Override
	public void updateHXCInfoByCreateTime(HXCDividend hxc) {
		this.hxcDividendMapper.updateHXCInfoByCreateTime(hxc);

	}

}
