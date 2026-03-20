package com.juyou.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Schema(title = "公共id参数")
@Accessors(chain = true)
public class IdDto {

    @Schema(description = "标识",required = true)
    @NotNull(message = "请填写标识信息")
    String id;
}
