package com.juyou.admin.sys.dto.dictdetail;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字典明细DTO
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "字典明细DTO", description = "字典明细DTO")
public class DictDetailDto implements Serializable {

	private static final long serialVersionUID = 6094779820901647477L;

	@Schema(description = "名称", required = true)
	private String name;

	@Schema(title = "值", required = true)
	private String value;

	@Schema(description = "备注")
	private String remark;
}
