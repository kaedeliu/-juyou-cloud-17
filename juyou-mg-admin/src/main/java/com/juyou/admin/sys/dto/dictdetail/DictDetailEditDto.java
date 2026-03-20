package com.juyou.admin.sys.dto.dictdetail;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统字典详细修改
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统字典详细修改对象", description = "系统字典详细修改对象")
public class DictDetailEditDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "系统字典明细主键")
	@NotBlank(message = "主键不能为空")
	private String dictDetailId;

	@Schema(description = "系统参数主键")
	private String dictId;

	@Schema(description = "名称")
	private String name;

	@Schema(title = "值", name = "值", description = "值")
	private String value;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "排序号")
	private Integer sequence;
}
