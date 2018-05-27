package com.etyero.timeTask;

import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etyero.action.DTYerstodayAmountInfo;

/**
 * 爬取DT昨日分红定时任务
 * 
 * @author lijialong
 */
@Component("GetDTYesterdayInfoTask")
public class GetDTYesterdayInfoTask {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GetDTYesterdayInfoTask.class);
	@Autowired
	private DTYerstodayAmountInfo dtYerstodayAmountInfo;

	public void execute() throws JobExecutionException {
        try {
        	logger.info("获取DT昨日分红信息并入库定时任务执行开始");
        	dtYerstodayAmountInfo.updateDTdividend();
     		logger.info("获取DT昨日分红信息并入库定时任务执行结束");
		} catch (Exception e) {
			logger.info("获取DT昨日分红信息并入库定时任务执行异常----{}",e);
		}
       
	}
}
