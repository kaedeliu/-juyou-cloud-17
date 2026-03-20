package com.juyou.gen.dto.tableinfo;

import com.juyou.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DbTablePageDto extends PageDto {

    @Schema(description = "表名称")
    String tableName;

    @Schema(description = "所属库")
    String tableSchema;

    @Schema(description = "类型表/视图")
    String tableType;


}
