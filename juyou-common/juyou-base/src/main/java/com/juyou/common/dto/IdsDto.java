package com.juyou.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(title = "公共ids参数")
public class IdsDto {

    @Schema(description = "标识列表",required = true)
    @NotNull(message = "请填写标识信息")
    String[] ids;
}
