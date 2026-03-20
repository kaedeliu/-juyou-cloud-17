package com.juyou.admin.sys.dto.user;

import com.juyou.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户分页查询对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title = "用户添加对象", description = "用户添加对象")
public class UserPageDto extends PageDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "编码")
	private String code;

	@Schema(description = "用户名")
	private String name;

	@Schema(description = "用户类型")
	private Integer userType;

	@Schema(description = "数据归属机构编码")
	private String macId;

	@Schema(description = "是否删除，0-否1-是")
	private Integer delFlag=0;

	@Schema(description = "状态，0-无效，1-有效")
	private Integer status;
}
