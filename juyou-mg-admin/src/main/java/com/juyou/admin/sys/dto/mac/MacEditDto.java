package com.juyou.admin.sys.dto.mac;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 机构修改
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@TableName("juyou_sys_user")
@Schema(title = "机构修改", description = "机构修改")
public class MacEditDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键", required = true)
	@NotBlank(message = "主键不能为空")
	private String macId;

	@Schema(description = "名称")
	private String name;

//	@Schema(description = "父ID")
//	private String parentId;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "排序号")
	private Integer sequence;

}
