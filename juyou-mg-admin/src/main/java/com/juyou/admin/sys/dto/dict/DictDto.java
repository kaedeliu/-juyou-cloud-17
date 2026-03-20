package com.juyou.admin.sys.dto.dict;

import java.io.Serializable;
import java.util.List;

import com.juyou.admin.sys.dto.dictdetail.DictDetailDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统字典DTO
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统字典DTO", description = "系统字典DTO")
public class DictDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "编码")
	private String code;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "字典明细")
	List<DictDetailDto> dictDetailDtos;
}
