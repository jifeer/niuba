package com.etyero.timeTask;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etyero.action.HXCInfo;

/**
 * 爬取HXC分红定时任务
 * 
 * @author lijialong
 * */
@Component("GetHXCInfoTask")
public class GetHXCInfoTask {
	private static final Logger logger = LoggerFactory.getLogger(GetHXCInfoTask.class);
	@Autowired
	private HXCInfo hxcInfo;

	public void execute() throws JobExecutionException {
		try {
			 logger.info("获取HXC分红日线定时任务开始执行");
			 hxcInfo.updateHXCInfo();
			 logger.info("获取HXC分红日线定时任务执行结束");
		} catch (Exception e) {
			logger.info("获取HXC分红日线执行异常----{}", e);
		}
	}
}