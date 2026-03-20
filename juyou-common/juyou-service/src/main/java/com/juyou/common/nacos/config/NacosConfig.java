package com.juyou.common.nacos.config;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NacosConfig implements ApplicationRunner{

	@Autowired(required = false)
    private NacosAutoServiceRegistration registration;

	@Value("${server.port}")
    Integer port;

    @SuppressWarnings("deprecation")
	@Override
    public void run(ApplicationArguments args) {
    	log.info("regis nacos ");

        if (registration != null) {
        	log.info("regis nacos2 " +registration.isAutoStartup() +"   " + registration.isRunning());
        	if(registration.isRunning()) return;
            Integer tomcatPort = port;
            if(port==null) {
            	 try {
                     tomcatPort = new Integer(getTomcatPort());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
            }

            log.info("tomcatPort:" + tomcatPort);
            registration.setPort(tomcatPort);
            registration.start();
        }
    }

	/**
	*	获取外部tomcat端口
	*/
    public String getTomcatPort() throws Exception {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"), Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        String port = objectNames.iterator().next().getKeyProperty("port");
        return port;
    }


}
