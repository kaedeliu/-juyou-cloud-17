package com.juyou.common.task;//package com.juyou.common.task;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronSequenceGenerator;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.util.StringUtils;
//
//import com.juyou.common.base.service.BaseService;
//import com.juyou.common.env.EnvKey;
//import com.juyou.common.env.EnvUtils;
//import com.juyou.common.exception.BaseException;
//import com.juyou.common.utils.SpringContextUtils;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 任务管理器
// * @author kaedeliu
// *
// */
//@Configuration
//@EnableScheduling
//@Slf4j
//public class CompleteScheduleConfig implements SchedulingConfigurer{
//
//	static String TAG="Schedule";
//	
////	@Autowired
////	ThreadPoolTaskScheduler threadPoolTaskScheduler;
//	
//	 ScheduledExecutorService executorService;
//	
//	static Map<String,ScheduledFuture<?>> tasks=new HashMap<String,ScheduledFuture<?>>();
//	
//	@Autowired
//	@Lazy
//	BaseService baseService;
//	
//	@Autowired
//	EnvUtils envUtils;
//	
////	@Scheduled(cron = "*/1 * * * * ?")
////	public void test() {
////		log.info(TAG,"我在执行");
////	}
//	
////	@Bean
////	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
////		log.info(TAG+"配置任务线程池:poolSize:"+poolSize);
////		//创建一个线程池调度器
////		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
////		//设置线程池容量
////		threadPoolTaskScheduler.setPoolSize(poolSize);
////		//线程名前缀
////		threadPoolTaskScheduler.setThreadNamePrefix("task-");
////		//等待时常
////		threadPoolTaskScheduler.setAwaitTerminationSeconds(awaitTerminationSeconds);
////		//当调度器shutdown被调用时等待当前被调度的任务完成
////		threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
////		//设置当任务被取消的同时从当前调度器移除的策略
////		threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
////		//设置任务注册器的调度器
////		return threadPoolTaskScheduler;
////	}
////	
////	@Override
////	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
////		if(!envUtils.value(EnvKey.任务调度状态,Boolean.class)) return;
////		log.info(TAG+"我开始执行任务配置");
////		//创建一个线程池调度器
////		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
////		//设置线程池容量
////		threadPoolTaskScheduler.setPoolSize(envUtils.value(EnvKey.任务调度线程数,Integer.class));
////		//线程名前缀
////		threadPoolTaskScheduler.setThreadNamePrefix("task-");
////		//等待时常
////		threadPoolTaskScheduler.setAwaitTerminationSeconds(envUtils.value(EnvKey.线程空闲时间,Integer.class));
////		//当调度器shutdown被调用时等待当前被调度的任务完成
////		threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
////		//设置当任务被取消的同时从当前调度器移除的策略
////		threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
////		threadPoolTaskScheduler.initialize();
////		//设置任务注册器的调度器
////		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
////		init();
////
////	}
//	
//	@Override
//	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//		if(!envUtils.value(EnvKey.任务调度状态,Boolean.class)) return;
//		log.info(TAG+"我开始执行任务配置");
//		//创建一个线程池调度器
//		int poolSize=envUtils.value(EnvKey.任务调度线程数,Integer.class);
//		executorService =  Executors.newScheduledThreadPool(poolSize);
//		//设置线程池容量
//		threadPoolTaskScheduler.setPoolSize();
//		//线程名前缀
//		threadPoolTaskScheduler.setThreadNamePrefix("task-");
//		//等待时常
//		threadPoolTaskScheduler.setAwaitTerminationSeconds(envUtils.value(EnvKey.线程空闲时间,Integer.class));
//		//当调度器shutdown被调用时等待当前被调度的任务完成
//		threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
//		//设置当任务被取消的同时从当前调度器移除的策略
//		threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
//		threadPoolTaskScheduler.initialize();
//		//设置任务注册器的调度器
//		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
//		init();
//
//	}
//	
//	public void init() {
//		List<CommonTask> commonTasks = baseService.selectCommonTask(envUtils.value(EnvKey.任务调度类型));
//		if(!commonTasks.isEmpty()) {
//			for (CommonTask commonTask : commonTasks) {
//				try {
//					add(commonTask);
//				} catch (Exception e) {
//					log.error(TAG,e);
//				}
//			}
//		}
//	}
//	
//	public void add(CommonTask commonTask) {
//		if(StringUtils.isEmpty(commonTask.getCronExpression()))
//			throw new BaseException("","cron错误");
//		if(!CronSequenceGenerator.isValidExpression(commonTask.getCronExpression())){
//			throw new BaseException("","cron错误");
//		}
//		Object obj=SpringContextUtils.getBean(commonTask.getBeanName());
//		if(StringUtils.isEmpty(obj))
//			throw new BaseException("","bean错误");
//		if(!(obj instanceof TaskRunInterface))
//			throw new BaseException("","实现类错误");
//		stop(commonTask.getTaskSeq());
//		
//		ScheduledFuture<?> task = threadPoolTaskScheduler.schedule(new MyRunnable(commonTask), new CronTrigger(commonTask.getCronExpression()));
//		tasks.put(commonTask.getTaskSeq(), task);
//		
//		log.info(TAG+"任务启动成功:"+commonTask.getTaskDesc()+" "+commonTask.getBeanName()+" " + commonTask.getCronExpression());
//	}
//	
//	
//	public void stop(String id) {
//		ScheduledFuture<?> task=tasks.get(id);
//		if(task!=null) {
//			task.cancel(false);
//			tasks.remove(id);
//			log.info(TAG+"任务停止成功:"+id);
//		}
//	}
//	
//
//
//	class MyRunnable implements Runnable{
//		CommonTask commonTask;
//		public MyRunnable(CommonTask commonTask) {
//			this.commonTask=commonTask;
//		}
//		
//		@Override
//		public void run() {
//			try {
//				TaskRunInterface taskRunInterface = (TaskRunInterface) SpringContextUtils.getBean(commonTask.getBeanName());
//				taskRunInterface.run(commonTask);
//			} catch (Exception e) {
//				log.error(TAG, e);
//			}
//			
//		}
//	}
//}
