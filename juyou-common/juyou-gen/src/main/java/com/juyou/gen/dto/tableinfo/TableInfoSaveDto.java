package com.juyou.gen.dto.tableinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author yx
 * @since 2023-04-08 17:05:31
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title="TableInfoSaveDto对象", description="TableInfoSaveDto对象")
public class TableInfoSaveDto {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表配置列信息",required = true)
    @NotNull
    List<TableInfoColumnInfoDto> tableInfoColumnInfoDto;

    @Schema(description = "表名称",required = true)
    @NotNull
    String tableName;

}