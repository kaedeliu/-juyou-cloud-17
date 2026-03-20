package com.juyou.admin.sys.dto.dictdetail;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统字典详细添加
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统字典详细添加对象", description = "系统字典详细添加对象")
public class DictDetailAddDto implements Serializable {

	private static final long serialVersionUID = -3242699745247417397L;

	@Schema(description = "系统参数主键", required = true)
	@NotBlank(message = "系统字典主键不能为空")
	private String dictId;

	@Schema(description = "名称", required = true)
	@NotBlank(message = "名称不能为空")
	private String name;

	@Schema(title = "值", required = true)
	@NotBlank(message = "值不能为空")
	private String value;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "排序号")
	private Integer sequence;
}
