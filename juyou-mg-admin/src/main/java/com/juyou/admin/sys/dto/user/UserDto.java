package com.juyou.admin.sys.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统用户对象", description = "系统用户对象")
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户主键")
	private String userId;

	@Schema(description = "编码")
	private String code;

	@Schema(description = "用户名")
	private String name;

	@Schema(description = "用户类型")
	private Integer userType;

	@Schema(description = "状态 0禁用,1启用")
	private Integer status;

	@Schema(description = "锁定状态 0-正常,1- 锁定")
	private Integer lockStatus;

	@Schema(description = "是否删除0-正常,1-删除")
	private Integer delFlag;

	@Schema(description = "数据归属机构编码")
	private String macId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "登录时间")
	private Date loginTime;

	@Schema(description = "登录IP")
	private String loginIp;

	@Schema(description = "token")
	private String token;

	@Schema(description = "用户角色")
	private List<String> roleIds;

	@Schema(description = "商户名称")
	private String tenantsName;
}
