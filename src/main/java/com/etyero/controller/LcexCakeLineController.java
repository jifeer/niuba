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
import com.etyero.action.LcexAmountInfo;
import com.etyero.entity.LCEXInfo;

/**
 * 获取LCEX分红信息
 * 
 * @author lijialong
 */
@Controller
@RequestMapping("/lcex")
public class LcexCakeLineController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(LcexCakeLineController.class);
	@Autowired
	private LcexAmountInfo lcexAmountInfo;

	@RequestMapping("/getLcexInfo")
	public void getLcexInfo(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr;
		try {
			logger.info("获取LCEX分红信息开始");
			LCEXInfo lcexInfo = lcexAmountInfo.getLcexAmountInfoForNow();
			JSONObject lcexInfoJson = new JSONObject();
			lcexInfoJson.put("lcex_current_info", JSONObject.parse(lcexInfo.getDividend_info()));// 当前分红
			jsonStr = resultSuccess("success", lcexInfoJson);
			logger.info("获取LCEX分红信息结束");
		} catch (Exception e) {
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("LCEX分红信息获取异常----{}", e);
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

	// LCEX分红时线
	@RequestMapping("/getLcexHourLine")
	public void getLcexHourLine(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr = new JSONObject();
		try {
			logger.info("获取LCEX分红时线信息开始");
			JSONObject lcexInfoJson = lcexAmountInfo.getLcexAmountInfoForHours();
			jsonStr = resultSuccess("success", lcexInfoJson);
			logger.info("获取LCEX分红时线信息结束");
		} catch (Exception e) {
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("LCEX分红时线信息获取异常----{}", e);
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

	// LCEX分红日线
	@RequestMapping("/getLcexDayLine")
	public void getLcexDayLine(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr = new JSONObject();
		try {
			logger.info("获取LCEX分红日线信息开始");
			JSONObject lcexInfoJson = lcexAmountInfo.getLcexAmountInfoForDays();
			jsonStr = resultSuccess("success", lcexInfoJson);
			logger.info("获取LCEX分红日线信息结束");
		} catch (Exception e) {
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("LCEX分红日线信息获取异常----{}", e);
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