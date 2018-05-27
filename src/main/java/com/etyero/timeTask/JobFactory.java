package com.etyero.timeTask;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * quartz继承QuartzJobBean或实现job接口，必须要重写SchedulerFactoryBean的jobFactory，
 * 否则无法引用springmvc注入的bean 但是这种方式引用springmvc注入的bean里面涉及到@value的注解还是无法引用
 * 
 */
public class JobFactory extends org.springframework.scheduling.quartz.SpringBeanJobFactory

{

	@Autowired

	private AutowireCapableBeanFactory beanFactory;

	/**
	 * 
	 * 这里覆盖了super的createJobInstance方法，对其创建出来的类再进行autowire。
	 * 
	 */

	@Override

	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

		Object jobInstance = super.createJobInstance(bundle);

		beanFactory.autowireBean(jobInstance);

		return jobInstance;

	}

}