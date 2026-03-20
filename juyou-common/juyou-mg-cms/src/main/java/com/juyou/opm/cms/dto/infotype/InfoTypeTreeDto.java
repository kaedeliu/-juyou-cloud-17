package com.juyou.opm.cms.dto.infotype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InfoTypeTreeDto {

    @Schema(description = "标识" )
    private String typeId;

    @Schema(description = "名称" )
    private String name;

    @Schema(description = "父id" )
    private String parentId;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建人")
    private String create_name;

    @Schema(description = "最后修改时间")
    private Date lastUpdateTime;

    @Schema(description = "最后修改人")
    private String lastUpdateName;


    @Schema(description = "子分类")
    private List<InfoTypeTreeDto> children;

}
