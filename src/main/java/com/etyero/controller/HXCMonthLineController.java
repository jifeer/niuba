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
import com.etyero.action.HXCInfo;

/**
 * 获取HXC分红信息
 * 
 * @author lijialong
 */
@Controller
@RequestMapping("/hxc")
public class HXCMonthLineController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(HXCMonthLineController.class);
	@Autowired
	private HXCInfo hxcInfo;
	
	@RequestMapping("/getHxcDayLine")
	public void getHxcDayLine(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr;
		try {
			jsonStr = JSONObject.parseObject(hxcInfo.getHXCInfo());
			jsonStr = resultSuccess("success", jsonStr);
		}catch(Exception e){
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("获取HXC分红日线图异常----{}",e);
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