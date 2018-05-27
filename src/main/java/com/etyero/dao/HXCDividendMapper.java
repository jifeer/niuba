package com.etyero.dao;

import java.util.List;

import com.etyero.entity.HXCDividend;

/**
 * @author lijialong
 * */
public interface HXCDividendMapper {
	// 插入HXC分红信息
	public void insertHXCInfo(HXCDividend hxc);

	// 根据日期查找HXC分红信息
	public List<HXCDividend> selectHXCInfoByCreateTime(String createTime);

	// 更新HXC分红信息
	public void updateHXCInfoByCreateTime(HXCDividend hxc);
}
