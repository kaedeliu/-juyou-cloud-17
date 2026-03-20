package com.juyou.common.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.juyou.common.exception.BaseException;
import com.juyou.common.utils.SpringContextUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobUtil {

    private static JobUtil jobUtil;

    @Autowired
    private Scheduler scheduler;

    public JobUtil(){
        log.info("init jobUtil");
        jobUtil = this;
    }

    public static JobUtil getInstance(){
    	log.info("retun  JobCreateUtil");
        return JobUtil.jobUtil;
    }

    public void start() throws Exception {
    	if(!scheduler.isShutdown()) {
    		scheduler.start();
    	}
    }

    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    /**
     * 创建job
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public  void addJob(String beanName,String groupName, String cronExpression) throws Exception {

    	Object bean=SpringContextUtils.getBean(beanName);
    	if(bean==null) {
    		throw new BaseException("未找到可执行的bean");
    	}

        // 启动调度器
        start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) bean.getClass()).withIdentity(beanName, groupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(beanName, groupName)
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 暂停任务
     * @param beanName
     * @throws SchedulerException
     */
    public void pause(String beanName,String groupName) throws SchedulerException {
    	scheduler.pauseTrigger(TriggerKey.triggerKey(beanName,groupName));
    }

    /**
     * 恢复任务
     * @param beanName
     * @throws SchedulerException
     */
    public void resume(String beanName,String groupName) throws SchedulerException {
    	scheduler.resumeTrigger(TriggerKey.triggerKey(beanName,groupName));
    }

    /**
     * 移除任务
     * @param beanName
     * @throws SchedulerException
     */
    public void remove(String beanName,String groupName) throws SchedulerException {
    	pause(beanName,groupName);
    	scheduler.unscheduleJob(TriggerKey.triggerKey(beanName,groupName));//移除触发器
    	scheduler.deleteJob(JobKey.jobKey(beanName,groupName));//删除Job
    }
}