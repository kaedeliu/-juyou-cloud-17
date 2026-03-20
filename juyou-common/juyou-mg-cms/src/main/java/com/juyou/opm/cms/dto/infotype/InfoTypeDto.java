package com.juyou.opm.cms.dto.infotype;

import com.juyou.opm.cms.dto.typeattributes.TypeAttributesDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InfoTypeDto {

    @Schema(description = "标识" )
    private String typeId;

    @Schema(description = "名称" )
    private String name;

   List<TypeAttributesDto> attrs;

}
