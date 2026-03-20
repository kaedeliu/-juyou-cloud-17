package com.juyou.opm.cms.dto.infotype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InfoTypeTreeSelectDto {


    @Schema(description = "标识" )
    private String typeId;

    @Schema(description = "名称" )
    private String name;

    @Schema(description = "父id" )
    private String parentId;

    @Schema(description = "子菜单" )
    List<InfoTypeTreeSelectDto> children;
}
