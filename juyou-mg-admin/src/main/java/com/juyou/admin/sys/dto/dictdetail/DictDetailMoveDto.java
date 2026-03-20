package com.juyou.admin.sys.dto.dictdetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统字典详细添加
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "系统字典详细添加对象", description = "系统字典详细添加对象")
public class DictDetailMoveDto implements Serializable {

	private static final long serialVersionUID = -3242699745247417397L;

	@Schema(description = "系统字典明细主键")
	@NotBlank(message = "主键不能为空")
	private String dictDetailId;

	@Schema(description = "是否上移动",required = true,defaultValue = "false")
	private boolean up=false;
}
