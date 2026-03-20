package com.juyou.shiro;

import com.alibaba.cloud.nacos.NacosConfigManager;

import com.alibaba.nacos.api.config.listener.Listener;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * nacos,监听shiro变化
 * @author kaedeliu
 *
 */
@Configuration
@Slf4j
public class NacosListener implements InitializingBean{

//    @Autowired
//    private NacosConfigProperties configProperties;
    
    @Autowired
	EnvUtils envUtils;
    
    static final  String NAME="shiro-";
    
    @Autowired
    ShiroConfigUpdate shiroConfigUpdate;

	@Autowired
	NacosConfigManager nacosConfigManager;

    
	@Override
	public void afterPropertiesSet() throws Exception {
//		Properties properties = new Properties();
//		properties.put("serverAddr", configProperties.getServerAddr());
//		ConfigService configService = NacosFactory.createConfigService(properties);
		String active=envUtils.value(EnvKey.开发环境);
		nacosConfigManager.getConfigService().addListener(NAME+active+".yml", active, new Listener() {
			
			@Override
			public void receiveConfigInfo(String configInfo) {
				Thread run =new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						log.info("----shiro配置更新--------");
						shiroConfigUpdate.updatePermission();
					}
				});
				run.start();
			}
			
			@Override
			public Executor getExecutor() {
				return null;
			}
		});
	}

}
