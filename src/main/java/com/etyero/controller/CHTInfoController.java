package com.etyero.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etyero.action.ChtInfo;
import com.etyero.entity.Cht;

/**
 * 获取CHT分红信息
 * 
 * @author lijialong
 */
@Controller
@RequestMapping("/cht")
public class CHTInfoController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CHTInfoController.class);
	@Autowired
	private ChtInfo chtInfo;

	@RequestMapping("/getChtDayLine")
	public void getChtDayLine(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr;
		try {
			logger.info("获取CHT分红信息开始");
			JSONObject chtInfoJson = new JSONObject();
			Cht cht= chtInfo.getChtInfo();
			logger.info("获取CHT分红信息结束");
			chtInfoJson.put("yesterday_info", JSONObject.parse(cht.getYesterday_info()));
			chtInfoJson.put("dividend_histroy_BTC", JSONArray.parse(cht.getDividend_histroy_BTC()));
			chtInfoJson.put("dividend_histroy_ETH", JSONArray.parse(cht.getDividend_histroy_ETH()));
			chtInfoJson.put("dividend_histroy_count", JSONArray.parse(cht.getDividend_histroy_count()));
			chtInfoJson.put("dividend_histroy_date", JSONArray.parse(cht.getDividend_histroy_date()));
			jsonStr = resultSuccess("success", chtInfoJson);
		} catch (Exception e) {
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("CHT分红信息获取异常----{}", e);
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