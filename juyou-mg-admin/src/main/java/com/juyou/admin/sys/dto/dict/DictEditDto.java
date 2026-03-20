package com.juyou.admin.sys.dto.dict;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统字典修改对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统字典修改对象", description = "系统字典修改对象")
public class DictEditDto implements Serializable {

	private static final long serialVersionUID = 4720770205015541179L;

	@Schema(description = "系统字典主键", required = true)
	@NotBlank(message = "系统字典主键不能为空")
	private String dictId;

	@Schema(description = "编码")
	private String code;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "排序号")
	private Integer sequence;
}
