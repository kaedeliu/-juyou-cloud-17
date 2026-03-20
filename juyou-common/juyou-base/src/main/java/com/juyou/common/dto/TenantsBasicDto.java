package com.juyou.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 租户基础数据类
 */
@Data
@Accessors(chain = true)
public class TenantsBasicDto {

    @Schema(description = "标识")
    String tenantsId;

    @Schema(description = "名称")
    String name;

    @Schema(description = "状态")
    Integer status;
}
