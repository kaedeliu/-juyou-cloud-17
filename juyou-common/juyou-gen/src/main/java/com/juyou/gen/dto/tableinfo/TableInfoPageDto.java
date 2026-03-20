package com.juyou.gen.dto.tableinfo;

import com.juyou.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Schema(title="TableInfoPageDto对象", description="TableInfoPage查询对象")
public class TableInfoPageDto extends PageDto {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表名")
    private String tableName;

}