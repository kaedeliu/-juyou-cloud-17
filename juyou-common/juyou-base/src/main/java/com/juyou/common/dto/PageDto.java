package com.juyou.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @功能 分页基类
 * @Author kaedeliu
 * @创建时间 2026/3/18 10:49
 * @修改人 kaedeliu
 * @修改时间 2026/3/18 10:49
 * @Param
 * @return
**/
@Data
public class PageDto {

    @Schema(description = "页码",defaultValue ="1",required = true)
    @Min(1)
    Integer pageNo=1;

    @Schema(description = "每页显示数",defaultValue = "10",required = true)
    @Max(10000)
    @Min(1)
    Integer pageSize=10;

    @Schema(description = "排序字段")
    String column;

    @Schema(description = "排序类型正序ASC,倒叙DESC")
    String order;
}
