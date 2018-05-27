package com.etyero.timeTask;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etyero.action.ChtInfo;

/**
 * CHT定时任务
 * 
 * @author lijialong
 */
@Component("GetCHTInfoTask")
public class GetCHTInfoTask {
	private static final Logger logger = LoggerFactory.getLogger(GetCHTInfoTask.class);
	@Autowired
	private ChtInfo chtInfo;

	public void execute() throws JobExecutionException {
		try {
			logger.info("获取CHT分红日线定时任务开始执行");
			chtInfo.updateCHTInfo();
			logger.info("获取CHT分红日线定时任务执行结束");
		} catch (Exception e) {
			logger.info("更新CHT分红日线执行异常----{}", e);
		}
	}
}