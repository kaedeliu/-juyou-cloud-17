package com.juyou.common.log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.exception.BaseException;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.IPUtils;
import com.juyou.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 系统日志，切面处理类
 *
 */
@Aspect
@Component
@Slf4j
public class AutoLogAspect {

    @Autowired
    LogTask logTask;

    @Autowired
    LoginUtilsService loginUtilsService;

    @Pointcut("@annotation(com.juyou.common.log.AutoLog)")
    public void logPointCut() {

    }

    @Autowired
    EnvUtils envUtils;

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法

        Operation operation=null;
        try {
            operation=saveSysLog(point);
        }catch (Exception e){
            log.error("log error:",e.getMessage());
        }

        Exception exception=null;
        Object result=null;
        try {
            result = point.proceed();
        }catch (Exception e){
            exception=e;
        }
        try {
            long time = System.currentTimeMillis() - beginTime;
            if(exception!=null){
                if(exception instanceof BaseException){
                    operation.setErrorMsg(((BaseException) exception).getCode()+exception.getMessage());
                }else
                    operation.setErrorMsg(exception.getMessage());

            }
            //处理出参
            if(operation!=null && operation.isResponse()){
                if(result!=null){
                    operation.setResponseParam(JSONObject.toJSONString(result));
                }
            }
            //保存日志
            if(envUtils.value(EnvKey.日志是否保存数据库,Boolean.class)) {
                if (operation.saveDb) {
                    logTask.save(operation);
                }
            }
            if(time>1000){
                log.info("超长请求:"+time);
            }
            setRunOverInfo(operation,time);
        } catch (Exception e) {
            log.error("log error:",e);
        }

        log.info("\nreq:"+JSONObject.toJSONString(operation) );
        if(exception!=null){
            throw exception;
        }
        return result;
    }

    private Operation saveSysLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Operation operation = new Operation();
        AutoLog syslog = method.getAnnotation(AutoLog.class);
        if(syslog != null){
            //update-begin-author:taoyan date:
            String content = syslog.value();
            //注解上的描述,操作日志内容
            operation.setLogType(syslog.logType());
            operation.setLogContent(content);
            operation.setResponse(syslog.response());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        operation.setMethod(className + "." + methodName + "()");


        //设置操作类型
        if (operation.getLogType() == CommonConstant.LOG_操作) {
            operation.setOperateType(getOperateType(methodName, syslog.operateType()));
        }

        //获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        //请求的参数
        operation.setRequestParam(getReqestParams(request,joinPoint));
        //设置IP地址
        operation.setIp(IPUtils.getIpAddr(request));
        try {
//          //获取登录用户信息
            LoginUserBasicDto sysUser = loginUtilsService.getLoginUserInfo();
            if(sysUser!=null){
                operation.setUserId(sysUser.getUserId());
                operation.setUsername(sysUser.getName());
            }
        } catch (Exception e) {
        }

        operation.setCreateTime(new Date());
        if(syslog.db()) {
            operation.setSaveDb(true);

        }

        return operation;

    }

    /**
     * 设置执行完后的信息
     * @param operation
     * @param time
     */
    private  void setRunOverInfo(Operation operation,long time){
        operation.setCostTime(time);
    }



    /**
     * 获取操作类型
     */
    private int getOperateType(String methodName,int operateType) {
        if (operateType > 0) {
            return operateType;
        }
        if (methodName.startsWith("list")) {
            return CommonConstant.OPERATE_查询_1;
        }
        if (methodName.startsWith("add")) {
            return CommonConstant.OPERATE_添加_2;
        }
        if (methodName.startsWith("edit")) {
            return CommonConstant.OPERATE_更新_3;
        }
        if (methodName.startsWith("delete")) {
            return CommonConstant.OPERATE_删除_4;
        }
        if (methodName.startsWith("import")) {
            return CommonConstant.OPERATE_导入_5;
        }
        if (methodName.startsWith("export")) {
            return CommonConstant.OPERATE_导出_6;
        }
        return CommonConstant.OPERATE_查询_1;
    }

    /**
     * 获取请求参数
     * @param request
     * @param joinPoint
     * @return
     */
    private String getReqestParams(HttpServletRequest request, JoinPoint joinPoint) {
        String httpMethod = request.getMethod();
        String params = "";
        if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
            Object[] paramsArray = joinPoint.getArgs();
            // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
            //  https://my.oschina.net/mengzhang6/blog/2395893
            Object[] arguments  = new Object[paramsArray.length];
            for (int i = 0; i < paramsArray.length; i++) {
                if (paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = paramsArray[i];
            }
            //update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
            PropertyFilter profilter = new PropertyFilter() {
                @Override
                public boolean apply(Object o, String name, Object value) {
                    if(value!=null && value.toString().length()>500){
                        return false;
                    }
                    return true;
                }
            };
            params = JSONObject.toJSONString(arguments, profilter);
            //update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params += "  " + paramNames[i] + ": " + args[i];
                }
            }
        }
        return params;
    }

}
