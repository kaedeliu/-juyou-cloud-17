package com.juyou.gen.dto.tableinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShowTableFilesDto {

    @Schema(description = "文件名称",required = true)
    String fileName;

    @Schema(description = "文件内容",required = true)
    String fileContent;
}
