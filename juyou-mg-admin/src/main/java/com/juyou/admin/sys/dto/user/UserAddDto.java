package com.juyou.admin.sys.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 用户添加对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户添加对象", description = "用户添加对象")
public class UserAddDto implements Serializable {

	private static final long serialVersionUID = -6620524541757813619L;

	@Schema(description = "编码", required = true)
	@NotBlank(message = "编码不能为空")
	private String code;

	@Schema(description = "用户名", required = true)
	@NotBlank(message = "用户名不能为空")
	private String name;

	@Schema(description = "用户类型", required = true)
	private Integer userType=0;

	@Schema(description = "密码")
	@NotBlank(message = "密码不能为空")
	private String password;

	@Schema(description = "状态 0禁用,1启用")
	private Integer status;

	@Schema(description = "角色ID集合", required = true)
	@NotEmpty(message = "角色ID集合不能为空")
	private List<String> roleIds;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "机构Id")
	@NotEmpty(message = "机构ID不能为空")
	private String macId;
}
