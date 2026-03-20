package com.juyou.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReturnFileDto {

    @Schema(description = "返回数据，通常为文件名或者访问路径，或者标识",required = true)
    String key;

    @Schema(description = "后续处理类型，默认0，保留值,后续可能存在异步",required = true)
    Integer type=0;
}
