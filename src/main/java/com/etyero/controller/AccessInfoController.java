package com.etyero.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.etyero.entity.AccessInfo;
import com.etyero.service.AccessInfoService;

/**
 * ip采集
 * 
 * @author lijialong
 */
@Controller
@RequestMapping("/access")
public class AccessInfoController  extends BaseController {
	
	@Resource
	private AccessInfoService accessInfoService = null;
	private static final Logger logger = LoggerFactory.getLogger(AccessInfoController.class);

	@RequestMapping("/save")
	public void Save(@ModelAttribute AccessInfo accessInfo, HttpServletResponse response) {
		PrintWriter out;
		JSONObject jsonStr;
		try {
			logger.info("访问者信息入库----ip:{}----地址:{}",accessInfo.getIp(),accessInfo.getAddress());
			accessInfoService.addAccessInfo(accessInfo);
			jsonStr = success("success");
		} catch (Exception e) {
			jsonStr = failure("failed");
			logger.info("访问者信息入库异常----{}",e);
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