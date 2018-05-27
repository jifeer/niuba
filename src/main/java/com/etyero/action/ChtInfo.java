package com.etyero.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.etyero.entity.Cht;
import com.etyero.service.ChtService;
import com.etyero.tool.DoRequest;
import com.etyero.tool.TimeUtil;

/**
 * cht分红
 * 
 * @author lijialong
 */
@Component
public class ChtInfo {
	@Autowired
	private ChtService chtService;

	@Value("${cht_dayLine_url}")
	private String cht_dayLine_url;

	@Value("${cht_yesterday_url}")
	private String cht_yesterday_url;
	private final static Logger logger = LoggerFactory.getLogger(ChtInfo.class);

	/**
	 * 获取当前日期的cht分红
	 * 
	 * @return Cht
	 */
	public Cht getChtInfo() throws Exception {
		String dateStr = TimeUtil.getDate("yyyy-MM-dd");
		Cht cht = chtService.getChtInfoByCreateTime(dateStr).get(0);
		return cht;
	}

	/**
	 * 更新cht分红
	 * 
	 */
	public void updateCHTInfo() throws Exception {
		String dateStr = TimeUtil.getDate("yyyy-MM-dd");
		Cht cht = new Cht();
		// 历史分红
		logger.info("获取cht历史分红开始，请求----{}", cht_dayLine_url);
		JSONObject chtjson = DoRequest.doRequest("get", cht_dayLine_url, "");
		String dividend_histroy_BTC = chtjson.getString("dividend_histroy_BTC");
		String dividend_histroy_ETH = chtjson.getString("dividend_histroy_ETH");
		String dividend_histroy_count = chtjson.getString("dividend_histroy_count");
		String dividend_histroy_date = chtjson.getString("dividend_histroy_date");
		logger.info("获取cht历史分红结束，响应----{}", chtjson);

		// 昨日分红概况
		logger.info("获取cht昨日分红开始，请求----{}", cht_yesterday_url);
		JSONObject yesterdayJson = DoRequest.doRequest("get", cht_yesterday_url, "");
		logger.info("获取cht昨日分红结束，响应----{}", yesterdayJson);

		cht.setDividend_histroy_BTC(dividend_histroy_BTC);
		cht.setDividend_histroy_ETH(dividend_histroy_ETH);
		cht.setDividend_histroy_count(dividend_histroy_count);
		cht.setDividend_histroy_date(dividend_histroy_date);
		cht.setYesterday_info(yesterdayJson.toString());
		cht.setCreate_time(dateStr);
		if (chtService.getChtInfoByCreateTime(dateStr).isEmpty()) {
			chtService.addChtInfo(cht);
		} else {
			chtService.updateChtInfoByCreateTime(cht);
		}
	}
}
