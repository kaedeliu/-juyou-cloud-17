package com.juyou.opm.cms.dto.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章分类属性
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title="TypeAttributesPageDto对象", description="TypeAttributesPage查询对象")
public class TypeAttrDto {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标识" )
    private String attrId;

    @Schema(description = "名称" )
    private String name;

    @Schema(description = "值类型" )
    private String valType;

    @Schema(description = "序号" )
    private Integer orderNum;

    @Schema(description = "是否必填（0不是，1是）")
    private Integer isRequired;

    @Schema(description = "是否是字典（0不是，1是）")
    private Integer isDict;

    @Schema(description = "字典key")
    private String dictKey;

    @Schema(description = "值")
    private String val;
}