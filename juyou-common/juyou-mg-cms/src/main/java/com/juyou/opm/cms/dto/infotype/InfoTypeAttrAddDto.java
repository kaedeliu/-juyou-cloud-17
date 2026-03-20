package com.juyou.opm.cms.dto.infotype;

import com.juyou.opm.cms.dto.typeattributes.TypeAttributesAddDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InfoTypeAttrAddDto {

    @Schema(description = "标识",required = true )
    @NotNull
    private String typeId;

    @Schema(description = "分类属性集合")
    List<TypeAttributesAddDto> attrs;

}
