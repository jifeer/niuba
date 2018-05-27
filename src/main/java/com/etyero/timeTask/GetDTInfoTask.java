package com.etyero.timeTask;

import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etyero.action.DTAmountInfo;

/**
 * 爬取DT分红时线定时任务
 * 
 * @author lijialong
 */
@Component("GetDTInfoTask")
public class GetDTInfoTask {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GetDTInfoTask.class);
	@Autowired
	private DTAmountInfo dtAmountInfo;

	// 爬取DT分红时线数据
	public void execute() throws JobExecutionException {
		try {
			logger.info("【DT分红时线定时任务执行开始】");
			dtAmountInfo.updateAmountInfo();
			logger.info("【DT分红时线定时任务执行结束】");
		} catch (Exception e) {
			logger.info("DT分红时线定时任务执行异常----{}", e);
		}
	}
}
