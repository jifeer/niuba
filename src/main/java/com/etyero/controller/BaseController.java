package com.etyero.controller;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;


@Controller
public class BaseController {
	
	// 返回成功信息
	public JSONObject success(String msg) {
		JSONObject result = new JSONObject();
		result.put("status", 1);
		result.put("msg", msg);
		return result;
	}

	// 返回失败信息
	public JSONObject failure(String msg) {
		JSONObject result = new JSONObject();
		result.put("status", 0);
		result.put("msg", msg);
		return result;
	}

	// 返回供前端使用的result-成功
	public JSONObject resultSuccess(String msg, Object resultStr) {
		JSONObject result = new JSONObject();
		result.put("status", 1);
		result.put("msg", msg);
		result.put("result", resultStr);
		return result;
	}

	// 返回供前端使用的result-失败
	public JSONObject resultFailure(String msg, Object resultStr) {
		JSONObject result = new JSONObject();
		result.put("status", 0);
		result.put("msg", msg);
		result.put("result", resultStr);
		return result;
	}
}
