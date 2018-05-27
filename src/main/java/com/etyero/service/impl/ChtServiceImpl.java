package com.etyero.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.ChtMapper;
import com.etyero.entity.Cht;
import com.etyero.service.ChtService;

@Service("ChtServiceImpl")
public class ChtServiceImpl implements ChtService {
	@Resource
	private ChtMapper chtMapper;

	@Override
	public void addChtInfo(Cht cht) {
		this.chtMapper.insertChtInfo(cht);
	}

	@Override
	public List<Cht> getChtInfoByCreateTime(String createTime) {
		return this.chtMapper.selectChtInfoByCreateTime(createTime);
	}

	@Override
	public void updateChtInfoByCreateTime(Cht cht) {
		this.chtMapper.updateChtInfoByCreateTime(cht);
	}
}
