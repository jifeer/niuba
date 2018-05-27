package com.etyero.action;

import java.math.BigDecimal;
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
import com.etyero.entity.LCEXInfo;
import com.etyero.service.LCEXInfoService;
import com.etyero.tool.Arith;
import com.etyero.tool.DoRequest;
import com.etyero.tool.TimeUtil;

/**
 * lcex分红
 * 
 * @author lijialong
 * */
@Component
public class LcexAmountInfo {
	private final static Logger logger = LoggerFactory.getLogger(LcexAmountInfo.class);
	private static double canDoProfitDT = 1549107.312;// 初始时间的可分红乐币数
	private static long initDate = 1525881600;// 2018/5/10 0:0:0初始时间
	private final static String timeFormat_hour = "yyyy-MM-dd HH";
	private final static String dateFormat = "yyyy-MM-dd";

	@Autowired
	private LCEXInfoService lcexInfoService;
	@Value("${lcex_amount_url}")
	private String lcex_amount_url;
	
	
	/** 
	 * 获取lcex当前分红信息
	 * 
	 *  */
	public LCEXInfo getLcexAmountInfoForNow() {
		return lcexInfoService.getLCEXInfoByCreateTime(TimeUtil.getDate(timeFormat_hour)).get(0);
	}

	
	/**
	 * 获取lcex近一个月的分红日线
	 * 
	 */
	public JSONObject getLcexAmountInfoForDays() throws Exception {
		JSONObject lcexMonthJson = new JSONObject();
		long todayEndTime = TimeUtil.getEndTime()/1000;//当前日期23点时间戳
		long daysAgoEndTime = todayEndTime - 31 * 24 * 60 * 60;// 30天前的23点时间戳
	    JSONArray lcexDividendDayArr = new JSONArray();
	    JSONArray lcexDividendInfoArr = new JSONArray();
		for(int i=0;i<31;i++){
			String endQueryTime = TimeUtil.longTodateStr(daysAgoEndTime * 1000, timeFormat_hour);
			try {
				logger.info("endQueryTime----{}",endQueryTime);
				LCEXInfo lcexInfo = lcexInfoService.getLCEXInfoByCreateTime(endQueryTime).get(0);
				JSONObject dividendInfoJson = JSONObject.parseObject(lcexInfo.getDividend_info());
				lcexDividendDayArr.add(TimeUtil.longTodateStr(daysAgoEndTime * 1000, dateFormat));
				lcexDividendInfoArr.add(dividendInfoJson.getString("lcex_current_profit_CNY"));
			} catch (Exception e) {
				logger.info("查询lcex的23点分红异常----{}",e);
			}
			daysAgoEndTime = daysAgoEndTime + 1 * 24 * 60 * 60;//加一天
		}
		lcexMonthJson.put("lcex_dividend_day", lcexDividendDayArr);
		lcexMonthJson.put("lcex_dividend_info", lcexDividendInfoArr);
		return lcexMonthJson;
	}

	
	/**
	 * 获取lcex所处小时的今日、昨日、前日分红信息
	 * 
	 * @throws Exception
	 */
	public JSONObject getLcexAmountInfoForHours() throws Exception {
		JSONObject lcexInfoJson = new JSONObject();
		long now_timeStamp = TimeUtil.strToDate(TimeUtil.getDate("yyyyMMdd"), "yyyyMMdd").getTime()/1000;//当前日期0点时间戳
        long start_timeStamp = now_timeStamp - 48 * 60 *60;//两天前的0点时间戳
        long yesTodayTimeStamp = now_timeStamp - 24 * 60 * 60;//昨天0点时间戳
		String endTime = TimeUtil.getDate(timeFormat_hour);//当前所处小时
		List<LCEXInfo> lcexInfos = lcexInfoService.getLCEXInfoByCreateTimeRange(String.valueOf(start_timeStamp), endTime);
		JSONArray beforeYesterdayLcexArr = new JSONArray();
		JSONArray yesterdayLcexArr = new JSONArray();
		JSONArray todayLcexArr = new JSONArray();
		for (LCEXInfo lcexInfo : lcexInfos) {
			long lcexInfo_timeStamp = TimeUtil.strToDate(lcexInfo.getCreate_time(), timeFormat_hour).getTime()/1000;
			JSONObject dividendInfoJson = JSONObject.parseObject(lcexInfo.getDividend_info());
			String lcex_current_profit_CNY = dividendInfoJson.getString("lcex_current_profit_CNY");
			if(lcexInfo_timeStamp >= start_timeStamp && lcexInfo_timeStamp < yesTodayTimeStamp){
				beforeYesterdayLcexArr.add(lcex_current_profit_CNY);
			}else if(lcexInfo_timeStamp >= yesTodayTimeStamp && lcexInfo_timeStamp < now_timeStamp){
				yesterdayLcexArr.add(lcex_current_profit_CNY);
			}else if(lcexInfo_timeStamp >= now_timeStamp){
				todayLcexArr.add(lcex_current_profit_CNY);
			}
		}
		lcexInfoJson.put("lcex_beforeYesterday_dividend", beforeYesterdayLcexArr);
		lcexInfoJson.put("lcex_yesterday_dividend", yesterdayLcexArr);
		lcexInfoJson.put("lcex_today_dividend", todayLcexArr);
		return lcexInfoJson;
	}
	
	
	/**
	 * lcex分红信息入库
	 * 
	 * */
	public void updateLcexAmountInfo() throws Exception{
		LCEXInfo lcexInfo = getLcexAmountInfo();
		List<LCEXInfo> lcexInfos = lcexInfoService.getLCEXInfoByCreateTime(TimeUtil.getDate(timeFormat_hour));
		if(lcexInfos.isEmpty()){
			lcexInfoService.addLCEXInfo(lcexInfo);
		}else {
			lcexInfoService.updateLCEXInfoByCreateTime(lcexInfo);
		}
	}
	
	
	/**
	 * 爬取lcex分红信息
	 * 
	 * */
	public LCEXInfo getLcexAmountInfo() throws Exception {
		logger.info("获取乐币当前交易额开始，请求----{}", lcex_amount_url);
		JSONObject tradeInfoJson = DoRequest.doRequest("get", lcex_amount_url, "");
		logger.info("获取乐币当前交易额结束，响应----{}", tradeInfoJson);
		JSONObject lastKLineJson = tradeInfoJson.getJSONObject("lastKLine");// 各币种交易额
		JSONArray productListArray = tradeInfoJson.getJSONArray("productList");// 币种列表
		double today_amount = 0;// 当前交易总额
		// 循环各币种交易额
		for (int i = 0; i < productListArray.size(); i++) {
			JSONObject lastKLineJsonTemp = lastKLineJson.getJSONArray(productListArray.getString(i)).getJSONObject(5);
			today_amount = Arith.add(today_amount,lastKLineJsonTemp.getJSONObject("payload").getDouble("dayTotalDealAmount"));
		}
		long nowDate = TimeUtil.strToDate(TimeUtil.getDate(timeFormat_hour), timeFormat_hour).getTime() / 1000;// 当前日期0点时间戳
		int days = (int) ((nowDate - initDate) / (24 * 60 * 60));// 初始日期距离当前日期天数
		double canDoProfitDT_today = canDoProfitDT + 51200 * days;// 当前可分红乐币数
		Map<String, Double> amount = getAmount(today_amount, canDoProfitDT_today);
		logger.info("当前分红结果----{}", amount);
		LCEXInfo lcexInfo = new LCEXInfo();
		JSONObject lcexInfoJson = new JSONObject();
		lcexInfoJson.put("current_platform_amount", amount.get("current_platform_amount"));
		lcexInfoJson.put("current_profit", amount.get("current_profit"));
		lcexInfoJson.put("lcex_current_profit", amount.get("lcex_current_profit"));
		lcexInfoJson.put("lcex_current_profit_CNY", new BigDecimal(amount.get("lcex_current_profit_CNY")).setScale(4, BigDecimal.ROUND_HALF_UP));
		lcexInfo.setDividend_info(lcexInfoJson.toString());
		return lcexInfo;
	}

	
	/**
	 * 计算分红
	 * 
	 * @param amount
	 *            总交易额
	 * @param canDoProfitDT 
	 *            可分红乐币数
	 * @return Map<String, Double>
	 */
	public static Map<String, Double> getAmount(double amount, double canDoProfitDT) {
		double current_platform_amount = Arith.mul(amount, 2);// 当前平台总交易额=当前交易总额*2
		double current_profit = Arith.mul(current_platform_amount, 0.002);// 当前分红=当前平台交易总额*0.002
		double singleDT_current_profit = Arith.div(current_profit, canDoProfitDT);// 单币分红=当前分红/可分红币数
		double singleDT_current_profit_CNY = Arith.mul(singleDT_current_profit, 6.5);// 转换为CNY，汇率6.5
		Map<String, Double> amountMap = new HashMap<String, Double>();
		amountMap.put("current_platform_amount", current_platform_amount);
		amountMap.put("current_profit", current_profit);
		amountMap.put("lcex_current_profit", singleDT_current_profit);
		amountMap.put("lcex_current_profit_CNY", singleDT_current_profit_CNY);
		return amountMap;
	}
}
