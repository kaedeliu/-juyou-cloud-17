package com.juyou.common.log;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.juyou.common.base.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 异步日志处理类
 * @author 27174
 *
 */
@Component
@Slf4j
public class LogTask {
	
	@Autowired
    @Lazy
    BaseService baseService;
	
	static CopyOnWriteArraySet<Operation>  operations=new CopyOnWriteArraySet<Operation>();
	
	static Thread thread=null;
	
	static Object lock=new Object();
	
	/**
	 * 考虑到mysql sql语句1MB限制，日志数据大小，一次最多插入100条
	 */
	static final int max=100;
	
	
	public void save(Operation operation) {

		operations.add(operation);
		synchronized (lock) {
			init();
			lock.notifyAll();
		}
	}
	
	private void init() {
		if(thread==null) {
			thread=new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							if(!operations.isEmpty()) {
								int len=0;
								List<Operation> os=new ArrayList<Operation>();
								Iterator<Operation>  iterator= operations.iterator();
								while (iterator.hasNext() && len<max) {
									Operation operation = iterator.next();
									operation.setOperationId(IdWorker.getIdStr());
									os.add(operation);
									len++;
								}
								try {
									baseService.saveOperation(os);
								} catch (Exception e) {
									// TODO: 只是try起来，报错直接抛弃，不做处理
									e.printStackTrace();
								}
								
								operations.removeAll(os);
							}
							Thread.sleep(1000);
							if(operations.isEmpty()) {
								synchronized (lock) {
									lock.wait();
								}
							}
						} catch (Exception e) {
							log.error("save logs error",e);
						}
					}
				}
			});
			thread.setName("log-save-task");
			thread.start();
		}
	}
	
}
