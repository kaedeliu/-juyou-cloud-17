package com.juyou.gen.dto.tableinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CreateDto {

    @NotNull
    @Schema(description = "生成moduleName",required = true)
    String moduleName;

    @NotNull
    @Schema(description = "待生成的table",required = true)
    String[] tableNames;
}
