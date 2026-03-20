package com.juyou.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class SelectDto {

    /**
     * 文本
     */
    @Schema(description = "文本")
    String name;

    /**
     * 值
     */
    @Schema(description = "值")
    String value;

    /**
     * 附加属性
     */
    Map<String,Object> attrs;
}
