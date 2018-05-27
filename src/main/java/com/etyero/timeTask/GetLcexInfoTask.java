package com.etyero.timeTask;

import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etyero.action.LcexAmountInfo;

/**
 * 爬取lcex分红定时任务
 * 
 * @author lijialong
 */
@Component("GetLcexInfoTask")
public class GetLcexInfoTask {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GetLcexInfoTask.class);
	@Autowired
	private LcexAmountInfo lcexAmountInfo;

	// 爬取LCEX分红时线数据
	public void execute() throws JobExecutionException {
		try {
			logger.info("lcex分红时线定时任务执行开始");
			lcexAmountInfo.updateLcexAmountInfo();
			logger.info("lcex分红时线定时任务执行结束");
		} catch (Exception e) {
			logger.info("DT分红时线定时任务执行异常----{}", e);
		}
	}
}
