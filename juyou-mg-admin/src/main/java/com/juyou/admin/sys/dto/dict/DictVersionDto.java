package com.juyou.admin.sys.dto.dict;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "系统字典DTO", description = "系统字典DTO")
public class DictVersionDto implements Serializable {

	private static final long serialVersionUID = 8555814958900524480L;

	@Schema(description = "版本号")
	private String version;

	@Schema(description = "字典集合")
	private List<DictDto> dictDtos;
}
