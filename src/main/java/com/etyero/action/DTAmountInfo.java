package com.etyero.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etyero.entity.DTDividend;
import com.etyero.entity.DTInfo;
import com.etyero.service.DTDividendService;
import com.etyero.service.DTInfoService;
import com.etyero.tool.Arith;
import com.etyero.tool.DoRequest;
import com.etyero.tool.TimeUtil;

/**
 * DT当前分红
 * @author lijialong
 * 
 * */
@Component
public class DTAmountInfo {
	private static double canDoProfitDT = 7454057.326;// 初始时间的可分红龙币数
	private static long initDate = 1522080000;// 2018/3/27 0:0:0初始时间
	private final static Logger logger = LoggerFactory.getLogger(DTAmountInfo.class);
	private final static String timeFormat = "yyyy-MM-dd";

	@Autowired
	private DTInfoService dtInfoService;
	@Autowired
	private DTDividendService dtDividendService;
	@Value("${a.dragonex.im}")
	private String baseUrl;

	/**
	 * 获取DT当前分红及分红日线
	 * 
	 * @return DTInfo
	 */
	public DTInfo getAmountInfoForNow() {
		List<DTInfo> dtInfos = dtInfoService.getDTInfoByCreateTime(TimeUtil.getDate(timeFormat));
		return dtInfos.get(0);
	}

	/**
	 * 获取DT昨日分红概况
	 * 
	 * @return DTDividend
	 */
	public DTDividend getAmountInforYerstoday() throws Exception {
		String startDate = TimeUtil.longTodateStr(new Date().getTime() - 24 * 60 * 60 * 1000, timeFormat);
		List<DTDividend> dividends = dtDividendService.getDTDividendInfoByDate(startDate, startDate);
		return dividends.get(0);
	}

	/**
	 * 更新DT分红时线信息
	 * 
	 * */ 
	public void updateAmountInfo() throws Exception {
		DTInfo dtInfo;
		dtInfo = getAmountInfo();
		if (dtInfoService.getDTInfoByCreateTime(TimeUtil.getDate("yyyy-MM-dd")).isEmpty()) {
			dtInfoService.addDTInfo(dtInfo);
		} else {
			dtInfoService.updateDTInfoByCreateTime(dtInfo);
		}
	}

	/**
	 * 从龙网爬取并遍历各币种交易信息，计算分红信息
	 * 
	 * @return DTInfo
	 */
	public DTInfo getAmountInfo() throws NumberFormatException, ParseException {
		JSONObject json = new JSONObject();
		
		//USDT
		String getCoinIdUrl_USDT = baseUrl + "/cct/list/?cr=usdt";
		JSONObject coinIdJson_USDT = JSONObject.parseObject(DoRequest.getResponse(getCoinIdUrl_USDT));
		JSONArray  coinIdArr_USDT = coinIdJson_USDT.getJSONArray("data");
		logger.info("获取USDT coin id ==== 请求===={}，响应===={}",getCoinIdUrl_USDT,coinIdJson_USDT);
		
		// ETH
		String getCoinIdUrl_ETH = baseUrl + "/cct/list/?cr=eth";
		JSONObject coinIdJson_ETH = JSONObject.parseObject(DoRequest.getResponse(getCoinIdUrl_ETH));
		JSONArray coinIdArr_ETH = coinIdJson_ETH.getJSONArray("data");
		logger.info("获取ETH coin id ==== 请求===={}，响应===={}",getCoinIdUrl_ETH,coinIdJson_ETH);
		
		// DT
		String getCoinIdUrl_DT = baseUrl + "/cct/list/?cr=dt";
		JSONObject coinIdJson_DT = JSONObject.parseObject(DoRequest.getResponse(getCoinIdUrl_DT));
		JSONArray coinIdArr_DT = coinIdJson_ETH.getJSONArray("data");
		logger.info("获取DT coin id ==== 请求===={}，响应===={}", getCoinIdUrl_DT, coinIdJson_DT);

		String[] coin_id = new String[coinIdArr_USDT.size() + coinIdArr_ETH.size() + coinIdArr_DT.size()];
		for(int i = 0;i<coinIdArr_USDT.size();i++){
			coin_id[i] = coinIdArr_USDT.getJSONObject(i).getString("symbol_id");
		}
		
		int count = coinIdArr_USDT.size();
		for(int i = 0;i<coinIdArr_ETH.size();i++){
			coin_id[count] = coinIdArr_ETH.getJSONObject(i).getString("symbol_id");
			count++;
		}
		
		int count2 = coinIdArr_USDT.size() + coinIdArr_ETH.size();
		for(int i = 0;i<coinIdArr_DT.size();i++){
			coin_id[count2] = coinIdArr_DT.getJSONObject(i).getString("symbol_id");
			count2++;
		}
		
 		String BaseUrl = baseUrl + "/market/kline/?";

 		long nowDate = TimeUtil.strToDate(TimeUtil.getDate("yyyyMMdd"), "yyyyMMdd").getTime()/1000;//当前日期0点时间戳
 		
 		long yerstoday = nowDate - 24*60*60;//昨天0点
 		long beforeYerstoday = nowDate - 24*60*60*2;//前天0点
 		
		Map<Long, Double> timestamp_amount = new TreeMap<>();// 所有币种某时间点的交易额
		for (int i = 0; i < coin_id.length; i++) {// 循环各币种交易
			String url = BaseUrl + "coin_id=" + coin_id[i] + "&direction=2&cycle=60min&count=72";
			json = JSONObject.parseObject(DoRequest.getResponse(url));
			JSONArray jsonArray = json.getJSONArray("data");// 每个时间点的成交情况
			int length = jsonArray.size();

			long timestamp = 0;
			for (int j = 0; j < length; j++) {// 循环单个币种交易
				JSONObject amountJson = jsonArray.getJSONObject(j);
				timestamp = amountJson.getLong("timestamp");// 龙网返回的时间点
				// 获取前天0点开始的各时间点的交易额
				if (timestamp >= beforeYerstoday) {
					double aomunt = amountJson.getDouble("usdt_amount");// 龙网返回的交易额
					if (timestamp_amount.get(timestamp) != null) {
						aomunt = Arith.add(timestamp_amount.get(timestamp), aomunt);
					}
					timestamp_amount.put(timestamp, aomunt);
				}
			}
		}
		
		// =========设置前天，昨天，今天的各个时间点的分红=========
		Map<Long, Map<String, Double>> beforeYerstodayResult = new TreeMap<>();
		Map<Long, Map<String, Double>> yerstodayResult = new TreeMap<>();
		Map<Long, Map<String, Double>> todayResult = new TreeMap<>();
		int days = (int) ((nowDate - initDate) / (24 * 60 * 60));// 初始日期距离当前日期天数
		double beforeYerstodayAmount = 0.0;
		double yerstodayAmount = 0.0;
		double todayAmount = 0.0;
		for (Map.Entry<Long, Double> timestamp_amount_temp : timestamp_amount.entrySet()) {
			long timestamp = timestamp_amount_temp.getKey();
			if (timestamp >= beforeYerstoday && timestamp < yerstoday) {// 前天各时间点分红
				double amount = timestamp_amount_temp.getValue();
				double canDoProfitDT01 = canDoProfitDT + 51200 * (days - 2);
				beforeYerstodayAmount = beforeYerstodayAmount + amount;
				beforeYerstodayResult.put(timestamp, getAmount(beforeYerstodayAmount, canDoProfitDT01));
			} else if (timestamp >= yerstoday && timestamp < nowDate) {// 昨天各时间点分红
				double amount = timestamp_amount_temp.getValue();
				double canDoProfitDT02 = canDoProfitDT + 51200 * (days - 1);
				yerstodayAmount = yerstodayAmount + amount;
				yerstodayResult.put(timestamp, getAmount(yerstodayAmount, canDoProfitDT02));
			} else {// 今天各时间点分红
				double amount = timestamp_amount_temp.getValue();
				double canDoProfitDT02 = canDoProfitDT + 51200 * days;
				todayAmount = todayAmount + amount;
				todayResult.put(timestamp, getAmount(todayAmount, canDoProfitDT02));
			}
		}

		//===================分红日线对比===================
		JSONArray today = new JSONArray();//今日分红
		JSONArray yesterday = new JSONArray();//昨日分红
		JSONArray beforeYesterday = new JSONArray();//前日分红
		long nowTime = TimeUtil.strToDate(TimeUtil.getDate("yyyyMMddHH"), "yyyyMMddHH").getTime()/1000;//当前所处小时

	    int i = 1;
	    today.add(0, 0);
		for(Map<String, Double> todayResultTemp : todayResult.values()){
	    	today.add(i, new BigDecimal(todayResultTemp.get("singleDT_current_profit_CNY")).setScale(4, BigDecimal.ROUND_HALF_UP));;
	    	i++;
	    }
		int j = 1;
		yesterday.add(0,0);
		for(Map.Entry<Long, Map<String, Double>> yerstodayResultTemp:yerstodayResult.entrySet()){
			long timestamp = yerstodayResultTemp.getKey();
			if(timestamp<=(nowTime - 24 * 60 * 60)){
				Map<String, Double> mapTemp = yerstodayResultTemp.getValue();
				yesterday.add(j,new BigDecimal(mapTemp.get("singleDT_current_profit_CNY")).setScale(4, BigDecimal.ROUND_HALF_UP));
				j++;
			}
		}
		int n = 1;
		beforeYesterday.add(0,0);
		for(Map.Entry<Long, Map<String, Double>> beforeYerstodayResultTemp:beforeYerstodayResult.entrySet()){
			long timestamp = beforeYerstodayResultTemp.getKey();
			if(timestamp<=(nowTime - 48 * 60 * 60)){
				Map<String, Double> mapTemp = beforeYerstodayResultTemp.getValue();
				beforeYesterday.add(n, new BigDecimal(mapTemp.get("singleDT_current_profit_CNY")).setScale(4, BigDecimal.ROUND_HALF_UP));;
				n++;
			}
		}
		// ===================当前分红概况===================
		Map<String, Double> nowMap = todayResult.get(nowTime);
		JSONObject nowCakeJson = new JSONObject();
		nowCakeJson.put("nowCake", new BigDecimal(nowMap.get("singleDT_current_profit_CNY")).setScale(4, BigDecimal.ROUND_HALF_UP));
		nowCakeJson.put("onePeopleCost", new BigDecimal(nowMap.get("singleCost")).setScale(4, BigDecimal.ROUND_HALF_UP));
		nowCakeJson.put("doublePeopleCost", new BigDecimal(nowMap.get("doubleCost")).setScale(4, BigDecimal.ROUND_HALF_UP));
        
		DTInfo dtInfo = new DTInfo();
		dtInfo.setDt_today(today.toJSONString());
		dtInfo.setDt_yerstoday(yesterday.toJSONString());
		dtInfo.setDt_beforeYerstoday(beforeYesterday.toJSONString());
		dtInfo.setDt_nowTime(nowCakeJson.toJSONString());
        return dtInfo;
	}

	/**
	 * 计算分红及刷蛋成本
	 * 
	 * @param amount
	 *            总交易额
	 * @param canDoProfitDT
	 *            可分红龙币数
	 * @return Map<String, Double> DT分红键值对
	 */
	public static Map<String, Double> getAmount(double amount, double canDoProfitDT) {
		double current_platform_amount = Arith.mul(amount, 2);// 当前平台总交易额=当前交易总额*2
		double current_profit = Arith.mul(current_platform_amount, 0.002);// 当前分红=当前平台交易总额*0.002
		double singleDT_current_profit = Arith.div(current_profit, canDoProfitDT);// 每个龙币分红=当前分红/可分红龙币
		double singleDT_current_profit_CNY = Arith.mul(singleDT_current_profit, 6.5);// 转换为CNY，汇率6.5
		double singleCost = Arith.mul(Arith.div(Arith.div(current_profit, 51200), 0.3), 6.5);// 单人刷蛋成本=分红/51200/0.3;
		double doubleCost = Arith.mul(Arith.div(Arith.div(current_profit, 51200), 0.5), 6.5);// 双人刷蛋成本=分红/51200/0.5;
		Map<String, Double> amountMap = new HashMap<String, Double>();
		amountMap.put("current_platform_amount", current_platform_amount);
		amountMap.put("current_profit", current_profit);
		amountMap.put("singleDT_current_profit", singleDT_current_profit);
		amountMap.put("singleDT_current_profit_CNY", singleDT_current_profit_CNY);
		amountMap.put("singleCost", singleCost);
		amountMap.put("doubleCost", doubleCost);
		return amountMap;
	}
}
