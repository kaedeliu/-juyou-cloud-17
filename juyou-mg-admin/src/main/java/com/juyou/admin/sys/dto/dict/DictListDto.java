package com.juyou.admin.sys.dto.dict;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字典列表查询
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "字典列表查询对象", description = "字典列表查询对象")
public class DictListDto implements Serializable {

	private static final long serialVersionUID = -8085638333214398378L;

	@Schema(description = "版本号", required = true)
	@NotBlank(message = "版本号不能为空")
	private String version;

}
