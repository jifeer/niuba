package com.etyero.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etyero.tool.DoRequest;


@Component
public class Biniuniu {
	private static final Logger logger = LoggerFactory.getLogger(Biniuniu.class);
	@Value("${biniuniu_cashFlow_url}")
	private String biniuniu_cashFlow_url;

	/**
	 * 爬取币牛牛资金流数据
	 * @author lijialong
	 * @param period
	 *            (today 今日，3minutes 3分钟，10minutes 10分钟，1hour 小时，24hours 24小时)
	 */
	public JSONObject getCashFlow(String period) throws Exception {
		StringBuilder url = new StringBuilder();
		url.append(biniuniu_cashFlow_url);
		url.append(period);
		url.append("&sortColumn=net_flow");
		logger.info("从币牛牛获取资金流----请求：{}", url);
		JSONObject jsonResponse = DoRequest.doRequest("get", url.toString(), "").getJSONObject("data");
		logger.info("从币牛牛获取资金流----响应：{}", jsonResponse);
		JSONObject resultJson = new JSONObject();// 重新封装的资金流结果集
		resultJson.put("unit", jsonResponse.getString("unit"));
		JSONArray arrayResponse = jsonResponse.getJSONArray("items");// 币种资金流明细
		JSONArray resultArray = new JSONArray();
		for (int i = 0; i < arrayResponse.size(); i++) {
			JSONObject json = new JSONObject();
			JSONObject jsonTemp = arrayResponse.getJSONObject(i);
			json.put("net_inflow", jsonTemp.getString("netFlow"));// 净流入
			json.put("net_inflow_percent", jsonTemp.getString("netFlowPercent"));// 占成交额百分比
			json.put("trade_target", jsonTemp.getString("targetSymbol"));// 交易目标币种
			json.put("cny_price", jsonTemp.getString("rmbPrice"));// 人民币价格
			json.put("price_trend", jsonTemp.getString("priceColor"));// 价格趋势，
																		// 0下跌，1持平，2增涨
			json.put("trade_currency", jsonTemp.getString("baseSymbol"));// 交易币种
			json.put("turnover", jsonTemp.getString("volume"));// 交易额
			json.put("usd_price", jsonTemp.getString("price"));// 美元价格
			json.put("trade_currency_name", jsonTemp.getString("name"));// 币种中文名
			json.put("exchange", jsonTemp.getString("exchange"));// 交易所
			json.put("flow_out", jsonTemp.getString("flowOut"));// 流出
			json.put("flow_in", jsonTemp.getString("flowInto"));// 流入
			resultArray.add(i, json);
		}
		resultJson.put("cash_flow", resultArray);
		return resultJson;
	}
}
