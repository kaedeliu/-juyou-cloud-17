package com.juyou.admin.sys.dto.config;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统参数查询对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统参数添加对象", description = "系统参数添加对象")
public class ConfigAddDto implements Serializable {

	private static final long serialVersionUID = 4020941936396427589L;

	@Schema(description = "编码", required = true)
	@NotBlank(message = "编码不能为空")
	private String code;

	@Schema(description = "名称", required = true)
	@NotBlank(message = "名称不能为空")
	private String name;

	@Schema(description = "值", required = true)
	@NotBlank(message = "值不能为空")
	private String value;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "排序号")
	private Integer sequence;

	@Schema(description = "备注")
	private String remark;
}
