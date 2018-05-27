package com.etyero.service;

import java.util.List;

import com.etyero.entity.DTDividend;

public interface  DTDividendService {
	//添加DT日分紅
	public void addDTDividendInfo(DTDividend dtd);

	//根据日期查找DT日分红
	public List<DTDividend> getDTDividendInfoByDate(String startDate,String endDate);
	
	//判断是否已存在该日分红
	public List<DTDividend> checkDTDividendInfoByDate(String startDate);
}
