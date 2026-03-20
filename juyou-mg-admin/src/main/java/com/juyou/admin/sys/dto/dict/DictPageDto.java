package com.juyou.admin.sys.dto.dict;

import java.io.Serializable;

import com.juyou.common.dto.PageDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统字典分页查询对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title = "系统字典分页查询对象", description = "系统字典分页查询对象")
public class DictPageDto extends PageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "编码")
	private String code;

	@Schema(description = "名称")
	private String name;
}
