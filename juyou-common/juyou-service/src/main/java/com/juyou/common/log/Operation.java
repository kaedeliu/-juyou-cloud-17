package com.juyou.common.log;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author kaedeliu
 * @since 2021-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title ="TkJcOperation对象", description="操作日志")
public class Operation implements Serializable {

    private static final long serialVersionUID=1L;

    /*1~*/@Schema(title = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private String operationId;

    /*1~*/@Schema(title = "日志类型（1登录日志，2操作日志）")
    private Integer logType;

    /*1~*/@Schema(title = "日志内容")
    private String logContent;

    /*1~*/@Schema(title = "操作类型")
    private Integer operateType;

    /*1~*/@Schema(title = "操作用户账号")
    private String userId;

    /*1~*/@Schema(title = "操作用户名称")
    private String username;

    private String ip;

    /*1~*/@Schema(title = "请求java方法")
    private String method;

    /*1~*/@Schema(title = "请求路径")
    private String requestUrl;

    /*1~*/@Schema(title = "请求参数")
    private String requestParam;

    /*1~*/@Schema(title = "请求类型")
    private String requestType;

    /*1~*/@Schema(title = "是否打印返回参数",name="是否打印返回参数",description="是否打印返回参数")
    private boolean response=false;

    /*1~*/@Schema(title = "返回参数",name="返回参数",description="返回参数")
    private String responseParam;

    /*1~*/@Schema(title = "耗时")
    private Long costTime;

    /*1~*/@Schema(title = "创建时间")
    private Date createTime;

    /*1~*/@Schema(title = "创建人")
    private String createName;

    boolean saveDb;

    @TableField(exist = false)
    /*1~*/@Schema(title = "异常")
    private String errorMsg;
    

}
