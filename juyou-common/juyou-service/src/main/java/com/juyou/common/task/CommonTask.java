package com.juyou.common.task;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 任务调度
 * </p>
 *
 * @author kaedeliu
 * @since 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title ="CommonTask对象", description="任务调度")
public class CommonTask implements Serializable {

    private static final long serialVersionUID=1L;

    /*1~*/@Schema(title = "主键")
    private String taskId;

    /*1~*/@Schema(title = "bean_name")
    private String beanName;

    /*1~*/@Schema(title = "cron表达式")
    private String cronExpression;

    /*1~*/@Schema(title = "任务状态1:正常;0:停用")
    private Integer status;

    /*1~*/@Schema(title = "任务描述")
    private String taskDesc;
    
    /*1~*/@Schema(title = "任务类型")
    public Integer type;
    
    /*1~*/@Schema(title = "错误信息")
    public String errorMsg;
    
    /*1~*/@Schema(title = "上次成功运行时间")
    public Date lastRunTime;

    /*1~*/@Schema(title = "数据库当前时间")
    public Date nowDate;
}
