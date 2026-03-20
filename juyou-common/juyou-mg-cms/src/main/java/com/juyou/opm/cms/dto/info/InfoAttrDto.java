package com.juyou.opm.cms.dto.info;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InfoAttrDto {

    @Schema(description = "属性值" )
    String val;

    @Schema(description = "文章分类属性ID",required = true)
    @NotNull(message = "文章分类属性ID")
    private String attrId;
}
