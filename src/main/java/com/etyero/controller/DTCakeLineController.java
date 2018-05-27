package com.etyero.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etyero.action.DTAmountInfo;
import com.etyero.action.DTYerstodayAmountInfo;
import com.etyero.entity.DTDividend;
import com.etyero.entity.DTInfo;
import com.etyero.tool.TimeUtil;

/**
 * 获取DT分红信息
 * 
 * @author lijialong
 */
@Controller
@RequestMapping("/dt")
public class DTCakeLineController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(DTCakeLineController.class);
	@Autowired
	private DTAmountInfo dtAmountInfo;
	@Autowired
	private DTYerstodayAmountInfo yAmountInfo;

	@RequestMapping("/getDtInfo")
	public void getDtInfo(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr;
		try {
			logger.info("获取DT分红信息开始");
			DTInfo dtInfo = dtAmountInfo.getAmountInfoForNow();
			
			// 当日分红概况及时线图
			jsonStr = JSONObject.parseObject(dtInfo.getDt_nowTime());
			jsonStr.put("today", JSONArray.parse(dtInfo.getDt_today()));
			jsonStr.put("yesterday", JSONArray.parse(dtInfo.getDt_yerstoday()));
			jsonStr.put("yesterdayBefore", JSONArray.parse(dtInfo.getDt_beforeYerstoday()));

			// 昨日分红概况
			DTDividend dtDividend = dtAmountInfo.getAmountInforYerstoday();
			logger.info("获取DT分红信息结束");
			jsonStr.put("yerstodayCake", dtDividend.getDt_dividend());
			jsonStr.put("yerstodayOnePeopleCost", dtDividend.getDt_onePeopleCost());
			jsonStr.put("yerstodayDoublePeopleCost", dtDividend.getDt_doublePeopleCost());
			jsonStr = resultSuccess("success", jsonStr);
		} catch (Exception e) {
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("DT分红信息获取异常----{}", e);
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
    
	//DT分红日线
	@RequestMapping("/getDtDayLine")
	public void getDtDayLine(HttpServletResponse response) {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out;
		JSONObject jsonStr = new JSONObject();
		try {
		    String[] month_DT_date = new String[30];
		    String[] month_DT_dividend = new String[30];
		    String[] dt_onePeopleCost = new String[30];
		    String[] dt_doublePeopleCost = new String[30];
		    Date today = new Date();
	    	Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(today);
	        calendar.add(Calendar.DAY_OF_MONTH, -1);
	        String yesterdayStr;
			try {
				yesterdayStr = TimeUtil.longTodateStr(calendar.getTime().getTime(), "yyyy-MM-dd");
				
				calendar.add(Calendar.DAY_OF_MONTH, -29);
				List<DTDividend> dTList = yAmountInfo.getDTdividend(TimeUtil.longTodateStr(calendar.getTime().getTime(), "yyyy-MM-dd"), yesterdayStr);
				int i = 0;
				for(DTDividend dividend : dTList){
					month_DT_date[i] = dividend.getDt_dividend_date();
					month_DT_dividend[i] = dividend.getDt_dividend().toString();
					dt_onePeopleCost[i] = dividend.getDt_onePeopleCost().toString();
					dt_doublePeopleCost[i] = dividend.getDt_doublePeopleCost().toString();
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonStr.put("month_DT_date", month_DT_date);
			jsonStr.put("month_DT_dividend", month_DT_dividend);
			jsonStr.put("DT_onePeopleCost", dt_onePeopleCost);
			jsonStr.put("DT_doublePeopleCost", dt_doublePeopleCost);
		} catch (Exception e) {
			jsonStr = resultFailure("failed", "服务器未知异常");
			logger.info("DT分红信息获取异常----{}", e);
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