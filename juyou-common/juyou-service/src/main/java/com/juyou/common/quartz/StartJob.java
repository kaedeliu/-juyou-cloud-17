package com.juyou.common.quartz;

import java.util.List;

import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.juyou.common.base.service.BaseService;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.exception.BaseException;
import com.juyou.common.task.CommonTask;
import com.juyou.common.utils.SpringContextUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 启动自定义结构的任务
 * @author 27174
 *
 */
@Component
@Slf4j
public class StartJob implements CommandLineRunner {

//
	@Autowired
	@Lazy
	BaseService baseService;

	@Autowired
	EnvUtils envUtils;

	@Autowired
	JobUtil jobUtil;

	static String TAG="Schedule";


	@Override
	public void run(String... args) throws Exception {
		if(!envUtils.value(EnvKey.任务调度状态,Boolean.class)) return;
		log.info(TAG+"我开始执行任务配置");
		try {
			jobUtil.clear();
		}catch (Exception e){
			log.error("job error",e);
		}

		init();
	}

	public void init() {
		List<CommonTask> commonTasks = baseService.selectCommonTask(envUtils.value(EnvKey.任务调度类型),1);
		if(!commonTasks.isEmpty()) {
			for (CommonTask commonTask : commonTasks) {
				try {
					add(commonTask);
				} catch (Exception e) {
					log.error(TAG,e);
				}
			}
		}
	}

	public void add(CommonTask commonTask) {
		if(StringUtils.isEmpty(commonTask.getCronExpression()))
			throw new BaseException("","cron错误");
		if(!CronSequenceGenerator.isValidExpression(commonTask.getCronExpression())){
			throw new BaseException("","cron错误");
		}
		Object obj=SpringContextUtils.getBean(commonTask.getBeanName());
		if(StringUtils.isEmpty(obj))
			throw new BaseException("","bean错误");
		if(!(obj instanceof Job))
			throw new BaseException("","实现类错误");
		try {
			jobUtil.addJob(commonTask.getBeanName(),commonTask.getTaskId(), commonTask.getCronExpression());
			log.info(TAG+"任务启动成功:"+commonTask.getTaskDesc()+" "+commonTask.getBeanName()+" " + commonTask.getCronExpression());
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.error(TAG+"启动失败:"+commonTask.getTaskDesc()+" "+commonTask.getBeanName()+" " + commonTask.getCronExpression(),e);

		}
	}

}
