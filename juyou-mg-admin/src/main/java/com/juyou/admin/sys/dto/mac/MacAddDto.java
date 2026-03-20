package com.juyou.admin.sys.dto.mac;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 机构
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@TableName("juyou_sys_user")
@Schema(title = "机构对象", description = "机构对象")
public class MacAddDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "id,前端传父ID+输入编码", required = true)
	private String macId;

	@Schema(description = "名称", required = true)
	@NotBlank(message = "名称不能为空")
	private String name;

	@Schema(description = "父ID")
	private String parentId;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "排序号")
	private Integer sequence;

}
