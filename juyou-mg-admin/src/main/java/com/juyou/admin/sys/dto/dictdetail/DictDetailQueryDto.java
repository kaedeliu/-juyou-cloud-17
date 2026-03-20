package com.juyou.admin.sys.dto.dictdetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统字典详细修改
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统字典详细查询对象", description = "系统字典详细查询对象")
public class DictDetailQueryDto  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "系统字典主键", required = true)
	@NotBlank(message = "系统字典主建不能为空")
	private String dictId;

	@Schema(description = "排序字段")
	String column;

	@Schema(description = "排序类型正序ASC,倒叙DESC")
	String order;
}
