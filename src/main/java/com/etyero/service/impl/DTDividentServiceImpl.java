package com.etyero.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.DTDividendMapper;
import com.etyero.entity.DTDividend;
import com.etyero.service.DTDividendService;

@Service("DTDividentServiceImpl")
public class DTDividentServiceImpl implements DTDividendService {
	@Resource
	private DTDividendMapper dtDividendMapper;

	@Override
	public void addDTDividendInfo(DTDividend dtd) {
		this.dtDividendMapper.insertDTDividendInfo(dtd);
	}

	@Override
	public List<DTDividend> getDTDividendInfoByDate(String startDate, String endDate) {
		return this.dtDividendMapper.selectDTDividendInfoByDate(startDate, endDate);
	}

	@Override
	public List<DTDividend> checkDTDividendInfoByDate(String startDate) {
		return this.dtDividendMapper.checkDTDividendInfoByDate(startDate);
	}

}
