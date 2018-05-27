package com.etyero.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.etyero.entity.HXCDividend;
import com.etyero.service.HXCDividendService;
import com.etyero.tool.DoRequest;
import com.etyero.tool.TimeUtil;

/**
 * HXC分红
 * 
 * @author lijialong
 * */
@Component
public class HXCInfo {
	@Autowired
	private HXCDividendService hxcDividendService;
	private static final Logger logger = LoggerFactory.getLogger(HXCInfo.class);
	@Value("${hxc_dayLine_url}")
	private String hxc_dayLine_url;
    
	/**
	 * 获取HXC分红信息
	 * 
	 * */
	public String getHXCInfo() {
		String dateStr = TimeUtil.getDate("yyyy-MM-dd");
		HXCDividend hxc = hxcDividendService.getHXCInfoByCreateTime(dateStr).get(0);
		String hxcInfo = hxc.getHxc_dividend();
		return hxcInfo;
	}

	/**
	 * 更新HXC分红信息
	 * 
	 * */
	public void updateHXCInfo() throws Exception{
		String dateStr = TimeUtil.getDate("yyyy-MM-dd");
		HXCDividend hxc = new HXCDividend();
		JSONObject hxcJson = DoRequest.doRequest("get", hxc_dayLine_url, "");
		logger.info("获取HXC分红信息----响应：{}", hxcJson);
		hxc.setHxc_dividend(hxcJson.toString());
		hxc.setCreate_time(dateStr);
		if(hxcDividendService.getHXCInfoByCreateTime(dateStr).isEmpty()){
			hxcDividendService.addHXCInfo(hxc);
		}else{
			hxcDividendService.updateHXCInfoByCreateTime(hxc);
		}
	}
}
