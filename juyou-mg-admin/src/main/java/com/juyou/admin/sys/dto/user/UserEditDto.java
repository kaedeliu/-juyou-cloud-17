package com.juyou.admin.sys.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 用户修改对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户修改对象", description = "用户修改对象")
public class UserEditDto implements Serializable {

	private static final long serialVersionUID = -7602502753032843605L;

	@Schema(description = "用户主键", required = true)
	@NotBlank(message = "用户主键不能为空")
	private String userId;

	@Schema(description = "编码")
	@NotBlank(message = "编码不能为空")
	private String code;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "用户类型")
	private Integer userTyp=0;

	@Schema(description = "密码")
	private String password;

	@Schema(description = "状态 0禁用,1启用")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "机构Id")
	@NotEmpty(message = "机构ID不能为空")
	private String macId;
}
