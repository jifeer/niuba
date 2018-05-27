package com.etyero.dao;

import java.util.List;

import com.etyero.entity.Cht;

public interface ChtMapper {
	//插入cht分紅信息
	public void insertChtInfo(Cht cht);
    
	//根据日期查找cht分红信息
	public List<Cht> selectChtInfoByCreateTime(String createTime);
   
	//更新cht分红信息
	public void updateChtInfoByCreateTime(Cht cht);
}
