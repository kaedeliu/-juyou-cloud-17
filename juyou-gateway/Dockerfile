FROM java:8
#jar包
ADD /target/gateway.jar gateway.jar
#挂载目录
#VOLUME /home/service/gateway
#VOLUME /home/logs
#VOLUME /yun

RUN echo "Asia/Shanghai" > /etc/timezone
#运行端口
EXPOSE 8184
#运行后执行得命名
ENTRYPOINT ["java","-jar","-Dlogging.config=/home/logs/logback-spring.xml","-Dspring.config.location=/home/service/gateway/bootstrap.yml","-Dfile.encoding=UTF8","-Duser.timezone=GMT+08","gateway.jar"]
