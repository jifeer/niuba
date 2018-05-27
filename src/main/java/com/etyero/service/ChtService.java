package com.etyero.service;

import java.util.List;

import com.etyero.entity.Cht;

public interface  ChtService {
	public void addChtInfo(Cht cht);
    
	public List<Cht> getChtInfoByCreateTime(String createTime);
   
	public void updateChtInfoByCreateTime(Cht cht);
}
