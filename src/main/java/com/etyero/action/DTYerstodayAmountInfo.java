package com.etyero.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etyero.entity.DTDividend;
import com.etyero.service.DTDividendService;
import com.etyero.tool.Arith;
import com.etyero.tool.DoRequest;
import com.etyero.tool.TimeUtil;

/**
 * DT历史分红
 * 
 * @author lijialong
 */
@Component
public class DTYerstodayAmountInfo {
	private static double canDoProfitDT = 7454057.326;// 初始时间的可分红龙币数
	private static long initDate = 1522080000;// 2018/3/27 0:0:0初始时间
	private final static Logger logger = LoggerFactory.getLogger(DTYerstodayAmountInfo.class);
	private static final String time_format = "yyyy-MM-dd";
	private static final String singleDT_current_profit_CNY = "singleDT_current_profit_CNY";
	private static final String singleDT_current_profit = "singleDT_current_profit";
	private static final String singleCost = "singleCost";
	private static final String doubleCost = "doubleCost";
	private static final String current_platform_amount = "current_platform_amount";
	private static final String current_profit = "current_profit";

	@Autowired
	private DTDividendService dtDividendService;

	@Value("${a.dragonex.im}")
	private String baseUrl;

	/**
	 * 获取指定时间区间内DT日分红信息
	 * 
	 */
	public List<DTDividend> getDTdividend(String startDate, String endDate) {
		return dtDividendService.getDTDividendInfoByDate(startDate, endDate);
	}

	/**
	 * 获取DT昨日分红信息入库
	 * 
	 */
	public void updateDTdividend() {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		try {
			String yesterdayStr = TimeUtil.longTodateStr(calendar.getTime().getTime(), time_format);
			DTDividend dtd = new DTDividend();
			Map<String, Double> map = getAmountInfo(1);
			dtd.setDt_dividend(
					new BigDecimal(map.get(singleDT_current_profit_CNY)).setScale(4, BigDecimal.ROUND_HALF_UP));
			dtd.setDt_onePeopleCost(new BigDecimal(map.get(singleCost)).setScale(4, BigDecimal.ROUND_HALF_UP));
			dtd.setDt_doublePeopleCost(new BigDecimal(map.get(doubleCost)).setScale(4, BigDecimal.ROUND_HALF_UP));
			dtd.setDt_dividend_date(yesterdayStr);
			dtDividendService.addDTDividendInfo(dtd);
		} catch (Exception e) {
			logger.info("DT昨日分红信息入库异常----{}", e);
		}

	}

	/**
	 * 从龙网爬取交易信息并计算分红
	 * 
	 * @param day
	 *            向前推算的天数，如1表示昨天
	 * @return Map<String,Double>
	 */
	public Map<String, Double> getAmountInfo(int day) throws Exception {
		JSONObject json = new JSONObject();
		// USDT
		String getCoinIdUrl_USDT = baseUrl + "/cct/list/?cr=usdt";
		logger.debug("getCoinIdUrl_USDT----{}", getCoinIdUrl_USDT);
		JSONObject coinIdJson_USDT = JSONObject.parseObject(DoRequest.getResponse(getCoinIdUrl_USDT));
		JSONArray coinIdArr_USDT = coinIdJson_USDT.getJSONArray("data");

		// ETH
		String getCoinIdUrl_ETH = baseUrl + "/cct/list/?cr=eth";
		JSONObject coinIdJson_ETH = JSONObject.parseObject(DoRequest.getResponse(getCoinIdUrl_ETH));
		JSONArray coinIdArr_ETH = coinIdJson_ETH.getJSONArray("data");

		// DT
		String getCoinIdUrl_DT = baseUrl + "/cct/list/?cr=dt";
		JSONObject coinIdJson_DT = JSONObject.parseObject(DoRequest.getResponse(getCoinIdUrl_DT));
		JSONArray coinIdArr_DT = coinIdJson_DT.getJSONArray("data");
		logger.info("获取DT coin id ==== 请求===={}，响应===={}", getCoinIdUrl_DT, coinIdJson_DT);

		String[] coin_id = new String[coinIdArr_USDT.size() + coinIdArr_ETH.size() + coinIdArr_DT.size()];
		for (int i = 0; i < coinIdArr_USDT.size(); i++) {
			coin_id[i] = coinIdArr_USDT.getJSONObject(i).getString("symbol_id");
		}

		int count = coinIdArr_USDT.size();
		for (int i = 0; i < coinIdArr_ETH.size(); i++) {
			coin_id[count] = coinIdArr_ETH.getJSONObject(i).getString("symbol_id");
			count++;
		}

		int count2 = coinIdArr_USDT.size() + coinIdArr_ETH.size();
		for (int i = 0; i < coinIdArr_DT.size(); i++) {
			coin_id[count2] = coinIdArr_DT.getJSONObject(i).getString("symbol_id");
			count2++;
		}

		String BaseUrl = baseUrl + "/market/kline/?";
		double amount = 0.0;
		for (int i = 0; i < coin_id.length; i++) {// 循环各币种交易
			String url = BaseUrl + "coin_id=" + coin_id[i] + "&direction=2&cycle=1day&count=31";
			json = JSONObject.parseObject(DoRequest.getResponse(url));
			JSONArray jsonArray = json.getJSONArray("data");
			int length = jsonArray.size();
			try {
				if (jsonArray.getJSONObject(length - (day + 1)).getDouble("usdt_amount") < 0.00001) {
					amount = Arith.add(jsonArray.getJSONObject(length - (day + 1)).getDouble("amount"), amount);
				} else {
					amount = Arith.add(jsonArray.getJSONObject(length - (day + 1)).getDouble("usdt_amount"), amount);
				}
			} catch (Exception e) {
				continue;
			}
		}

		long nowDate = TimeUtil.strToDate(TimeUtil.getDate("yyyyMMdd"), "yyyyMMdd").getTime() / 1000;// 当前日期0点时间戳
		int days = (int) ((nowDate - initDate) / (24 * 60 * 60)) - day;// 初始日期距离昨天天数

		double canDoProfitDT02 = canDoProfitDT + 51200 * days;
		return getAmount(amount, canDoProfitDT02);
	}

	/**
	 * 计算分红及刷蛋成本
	 * 
	 * @param amount
	 *            总交易额
	 * @param canDoProfitDT
	 *            可分红龙币数
	 * @return Map<String, Double>
	 */
	public static Map<String, Double> getAmount(double amount, double canDoProfitDT) {
		double current_platform_amount_value = Arith.mul(amount, 2);// 当前平台总交易额=当前交易总额*2
		double current_profit_value = Arith.mul(current_platform_amount_value, 0.002);// 当前分红=当前平台交易总额*0.002
		double singleDT_current_profit_value = Arith.div(current_profit_value, canDoProfitDT);// 每个龙币分红=当前分红/可分红龙币
		double singleDT_current_profit_CNY_value = Arith.mul(singleDT_current_profit_value, 6.5);// 转换为CNY，汇率6.5
		double singleCost_value = Arith.mul(Arith.div(Arith.div(current_profit_value, 51200), 0.3), 6.5);// 单人刷蛋成本=分红/51200/0.3;
		double doubleCost_value = Arith.mul(Arith.div(Arith.div(current_profit_value, 51200), 0.5), 6.5);// 双人刷蛋成本=分红/51200/0.5;
		Map<String, Double> amountMap = new HashMap<String, Double>();
		amountMap.put(current_platform_amount, current_platform_amount_value);
		amountMap.put(current_profit, current_profit_value);
		amountMap.put(singleDT_current_profit, singleDT_current_profit_value);
		amountMap.put(singleDT_current_profit_CNY, singleDT_current_profit_CNY_value);
		amountMap.put(singleCost, singleCost_value);
		amountMap.put(doubleCost, doubleCost_value);
		return amountMap;
	}
}
