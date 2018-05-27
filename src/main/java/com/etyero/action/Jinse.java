package com.etyero.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.etyero.tool.DoRequest;
import com.etyero.tool.TimeUtil;

@Component
public class Jinse {
	private static final Logger logger = LoggerFactory.getLogger(Jinse.class);
	@Value("${jinse_shortNews_url}")
	private String jinse_shortNews_url;

	/**
	 * 爬取金色财经简讯20条
	 * @author lijialong
	 */
	public  JSONObject getShortNews() throws Exception {
		StringBuilder url = new StringBuilder();
		url.append(jinse_shortNews_url);
		JSONObject jsonResponse = DoRequest.doRequest("get", url.toString(), "").getJSONArray("list").getJSONObject(0);
		logger.info("从金色财经获取简讯----请求：{}，----响应：{}", url, jsonResponse);
		JSONObject resultJson = new JSONObject();// 重新封装的简讯结果集
		JSONArray arrayResponse = jsonResponse.getJSONArray("lives");// 简讯明细
		JSONArray resultArray = new JSONArray();
		for (int i = 0; i < arrayResponse.size(); i++) {
			JSONObject json = new JSONObject();
			JSONObject jsonTemp = arrayResponse.getJSONObject(i);
			json.put("content", jsonTemp.getString("content"));// 简讯内容
			json.put("content_time", TimeUtil.longTodateStr(jsonTemp.getLong("created_at") *1000, "yyyy-MM-dd HH:mm"));// 简讯发布时间
			json.put("highlight", jsonTemp.getString("highlight_color").equals("")?0:1);// 是否高亮显示 0否1是
			json.put("content_link", jsonTemp.getString("link"));// 原文链接
			resultArray.add(i, json);
		}
		resultJson.put("content_list", resultArray);
		return resultJson;
	}
}
