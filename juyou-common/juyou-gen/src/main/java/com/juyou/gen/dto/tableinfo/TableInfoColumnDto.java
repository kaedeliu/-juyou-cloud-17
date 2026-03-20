package com.juyou.gen.dto.tableinfo;

import com.juyou.gen.entity.TableInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TableInfoColumnDto extends TableInfo {

    @Schema(description = "列说明")
    String columnComment;

    @Schema(description = "列类型")
    String columnType;
}
