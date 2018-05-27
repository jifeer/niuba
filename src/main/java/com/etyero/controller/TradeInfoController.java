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
import com.etyero.action.Biniuniu;

/**
 * 
 * @author lijialong
 */
@Controller
@RequestMapping("/trade")
public class TradeInfoController  extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeInfoController.class);
	@Autowired
	private Biniuniu biniuniu;
	
	//获取资金流向信息
	@RequestMapping("/getCashFlow")
	public void getCashFlow(String cycleTime, HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr;
		try {
			jsonStr = biniuniu.getCashFlow(cycleTime);
			jsonStr = resultSuccess("success", jsonStr);
		} catch (Exception e) {
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("获取币牛牛资金流向异常----{}",e);
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