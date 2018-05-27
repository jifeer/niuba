package com.etyero.dao;

import java.util.List;

import com.etyero.entity.DTDividend;

public interface DTDividendMapper {
	//添加DT日分紅
	void insertDTDividendInfo(DTDividend dtd);

	//根据日期查找DT日分红
	List<DTDividend> selectDTDividendInfoByDate(String startDate,String endDate);
	
	//判断是否已存在该日分红
	List<DTDividend> checkDTDividendInfoByDate(String startDate);

}
