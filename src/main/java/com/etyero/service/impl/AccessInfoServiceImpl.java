package com.etyero.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.etyero.dao.AccessInfoMapper;
import com.etyero.entity.AccessInfo;
import com.etyero.service.AccessInfoService;

@Service("AccessInfoServiceImpl")
public class AccessInfoServiceImpl implements AccessInfoService {
	@Resource
	private AccessInfoMapper accessInfoMapper;

	@Override
	public void addAccessInfo(AccessInfo accessInfo) {
		this.accessInfoMapper.insertAccessInfo(accessInfo);
	}
}
