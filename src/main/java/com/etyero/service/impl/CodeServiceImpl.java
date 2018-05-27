package com.etyero.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.CodeMapper;
import com.etyero.entity.Code;
import com.etyero.service.CodeService;

@Service("CodeServiceImpl")
public class CodeServiceImpl implements CodeService{
	@Resource 
	private CodeMapper codeMapper;

	@Override
	public List<Code> getCodeById(int id) {
		return this.codeMapper.selectByPrimaryKey(id);
	}
}
