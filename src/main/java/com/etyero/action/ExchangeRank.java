package com.etyero.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etyero.entity.RankInfo;
import com.etyero.service.RankInfoService;
import com.etyero.tool.DoRequest;
import com.etyero.tool.TimeUtil;

/**
 * 交易所及币种交易排行榜
 * 
 * @author lijialong
 * */
@Component
public class ExchangeRank {
	
	private final static Logger logger = LoggerFactory.getLogger(ExchangeRank.class);
	@Autowired
	private RankInfoService rankInfoService;
	@Value("${rank_url}")
	private String rank_url;
	
	/**
	 * 更新排名
	 * 
	 * */
	public void updateRankInfo(){
		String dateStr = TimeUtil.getDate("yyyy-MM-dd HH:mm:ss");
		logger.info("{}---获取排名信息开始....", logger.getName());
		String rankInfoJsonStr = DoRequest.getResponse(rank_url);
		logger.info("{}---获取排名信息结束....{}", logger.getName(),rankInfoJsonStr);
		RankInfo rankInfo = new RankInfo();
		rankInfo.setRank_info(rankInfoJsonStr);
		logger.info("{}---入库开始....", logger.getName());
		if(rankInfoJsonStr!=null){
			if (rankInfoService.getRankInfoByDate(dateStr).isEmpty()) {
				rankInfoService.addRankInfo(rankInfo);
			} else {
				rankInfoService.updateRankInfoByDate(rankInfo);
			}
		}else {
			logger.info("{}---获取排名信息为空....", logger.getName());
		}
		
		logger.info("{}---入库结束....", logger.getName());
	}
	
	
	/**
	 * 获取交易所排名信息
	 * 
	 * return RankInfo
	 * */
	public RankInfo getExchangeRankInfoForNow(){
		String dateStr = TimeUtil.getDate("yyyy-MM-dd HH:mm:ss");
		try {
			RankInfo rankInfo = rankInfoService.getRankInfoByDate(dateStr).get(0);
			JSONObject exchange_rank_json = JSONObject.parseObject(rankInfo.getRank_info());
			JSONArray exchange_rank_arr = exchange_rank_json.getJSONArray("exchange_rank");
			String[] exchange_name = new String[exchange_rank_arr.size()];
			String[] exchange_account = new String[exchange_rank_arr.size()];
			for(int i=0;i<exchange_rank_arr.size();i++){
				JSONObject rank_info = exchange_rank_arr.getJSONObject(i);
				exchange_name[i] = rank_info.getString("exchange_name");
				exchange_account[i] = rank_info.getString("exchange_account").replace("¥", "").replace("万", "").replace(",", "");
			}
			JSONObject exchange_rank_json_new = new JSONObject();
			exchange_rank_json_new.put("exchange_name", exchange_name);
			exchange_rank_json_new.put("exchange_account", exchange_account);
			rankInfo.setRank_info(exchange_rank_json_new.toString());
			return rankInfo;
		} catch (Exception e) {
			return null;
		}
	}

	
	/**
	 * 获取币种排名信息 return RankInfo
	 * 
	 * @return RankInfo
	 */
	public RankInfo getCurrencyRankInfoForNow() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		String dateStr = TimeUtil.getDate("yyyy-MM-dd HH:mm:ss");
		try {
			RankInfo rankInfo = rankInfoService.getRankInfoByDate(dateStr).get(0);
			JSONObject virtual_currency_rank_json = JSONObject.parseObject(rankInfo.getRank_info());
			JSONArray virtual_currency_rank_arr = virtual_currency_rank_json.getJSONArray("virtual_currency_rank");
			String[] virtual_currency_name = new String[virtual_currency_rank_arr.size()];
			String[] virtual_currency_account = new String[virtual_currency_rank_arr.size()];
			String[] virtual_currency_price = new String[virtual_currency_rank_arr.size()];
			for(int i=0;i<virtual_currency_rank_arr.size();i++){
				JSONObject virtual_currency_rank_info = virtual_currency_rank_arr.getJSONObject(i);
				virtual_currency_name[i] = virtual_currency_rank_info.getString("virtual_currency_name");
				virtual_currency_account[i] = virtual_currency_rank_info.getString("virtual_currency_account").replace("¥", "").replace("万", "").replace(",", "");
				virtual_currency_price[i] = virtual_currency_rank_info.getString("virtual_currency_price").replace("¥", "");

			}
			JSONObject virtual_currency_rank_json_new = new JSONObject();
			virtual_currency_rank_json_new.put("virtual_currency_name", virtual_currency_name);
			virtual_currency_rank_json_new.put("virtual_currency_account", virtual_currency_account);
			virtual_currency_rank_json_new.put("virtual_currency_price", virtual_currency_price);
			rankInfo.setRank_info(virtual_currency_rank_json_new.toString());
			return rankInfo;
		} catch (Exception e) {
			return null;
		}
	}
}
