package com.etyero.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.etyero.action.ExchangeRank;
import com.etyero.entity.RankInfo;

/**
 * 获取HXC分红信息
 * 
 * @author lijialong
 */
@Controller
@RequestMapping("/rank")
public class RankInfoController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(RankInfoController.class);
	@Autowired
	private ExchangeRank exchangeRank;
    
	@RequestMapping("/getRankInfo")
	public void getRankInfo(String rankType,HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr;
		try {
			RankInfo rankInfo = new RankInfo();
			if(rankType.equals("0")){
				rankInfo = exchangeRank.getExchangeRankInfoForNow();
			}else{
				rankInfo = exchangeRank.getCurrencyRankInfoForNow();
			}
			jsonStr = JSONObject.parseObject(rankInfo.getRank_info());
			jsonStr.put("update_time", rankInfo.getUpdate_time());
			jsonStr = resultSuccess("success", jsonStr);
		}catch(Exception e){
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("获取交易所/币种排名异常----{}",e);
		}
		try {
			out = response.getWriter();
			out.write(jsonStr.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}