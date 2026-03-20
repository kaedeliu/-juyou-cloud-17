package com.juyou.admin.sys.dto.mac;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MacTreeDto {

    @Schema(description = "文本")
    String macId;

    @Schema(description = "值")
    String name;

    @Schema(description = "父值Id")
    String parentId;

    @Schema(description = "子级")
    List<MacTreeDto> children;
}
