package com.etyero.service;

import java.util.List;

import com.etyero.entity.HXCDividend;

public interface  HXCDividendService {
	// 插入HXC分红信息
	public void addHXCInfo(HXCDividend hxc);

	// 根据日期查找HXC分红信息
	public List<HXCDividend> getHXCInfoByCreateTime(String createTime);

	// 更新HXC分红信息
	public void updateHXCInfoByCreateTime(HXCDividend hxc);
}
