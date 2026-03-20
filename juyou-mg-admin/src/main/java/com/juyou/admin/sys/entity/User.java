package com.juyou.admin.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.juyou.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户
 * 
 * @author kaedeliu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("juyou_sys_user")
@Schema(title = "用户对象", description = "用户对象")
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户主键")
	@TableId(value = "user_id", type = IdType.ASSIGN_ID)
	private String userId;

	@Schema(description = "编码")
	@TableField("code")
	private String code;

	@Schema(description = "用户名")
	@TableField("name")
	private String name;

	@Schema(description = "用户类型")
	@TableField("user_type")
	private Integer userType;

	@Schema(description = "密码")
	@TableField("password")
	private String password;

	@Schema(description = "加密盐")
	@TableField("secret")
	private String secret;

	@Schema(description = "状态 0禁用,1启用")
	@TableField("status")
	private Integer status;

	@Schema(description = "锁定状态 0-正常,1- 锁定")
	@TableField("lock_status")
	private Integer lockStatus;

	@Schema(description = "锁定时间")
	@TableField("lock_time")
	private Integer lockTime;

	@Schema(description = "是否删除0-正常,1-删除")
	@TableField("del_flag")
	private Integer delFlag;

	@Schema(description = "数据归属机构编码")
	@TableField("mac_id")
	private String macId;

	@Schema(description = "登录时间")
	@TableField("login_time")
	private Date loginTime;

	@Schema(description = "登录IP")
	@TableField("login_ip")
	private String loginIp;

	@Schema(description = "备注")
	@TableField("remark")
	private String remark;

	@Schema(description = "文章收藏数量")
	@TableField("collect_num")
	private String collectNum;


	@Schema(description = "类型0默认,1预留")
	@TableField("sys_type")
	private Integer sysType;
}
