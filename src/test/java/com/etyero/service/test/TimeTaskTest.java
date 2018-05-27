package com.etyero.service.test;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.etyero.service.CodeService;

public class TimeTaskTest extends QuartzJobBean {
	//解决spring+quartz无法自动注入bean问题
	//1.将其成员变量注解为@Autowired
	@Autowired
	private CodeService codeService;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		//2.添加以下代码自动注入成员变量实现类
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		System.out.println("I am FirstScheduledJob");
		System.out.println(codeService.getCodeById(1));
	}
}
