package com.etyero.timeTask;

import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.etyero.action.ExchangeRank;

/**
 * 获取交易所及币种排名定时任务
 * 
 * @author lijialong
 */
@Component("GetExchangeRankTask")
public class GetExchangeRankTask {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GetExchangeRankTask.class);
	@Autowired
	private  ExchangeRank exchangeRank;

	public void execute() throws JobExecutionException {
		logger.info("获取交易所及币种排名定时任务执行开始");
		exchangeRank.updateRankInfo();
		logger.info("获取交易所及币种排名定时任务执行结束");
	}
}
