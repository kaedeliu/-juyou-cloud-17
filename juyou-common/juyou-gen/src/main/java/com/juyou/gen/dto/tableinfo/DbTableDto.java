package com.juyou.gen.dto.tableinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class DbTableDto {

    @Schema(description = "表名称")
    String tableName;

    @Schema(description = "所属库")
    String tableSchema;

    @Schema(description = "类型表/视图")
    String tableType;

    @Schema(description = "创建时间")
    Date createTime;

    @Schema(description = "更新时间")
    Date updateTime;

}
