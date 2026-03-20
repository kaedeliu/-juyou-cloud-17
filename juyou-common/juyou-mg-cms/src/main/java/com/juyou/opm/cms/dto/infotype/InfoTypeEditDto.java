package com.juyou.opm.cms.dto.infotype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 文章分类
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title="InfoTypeEditDto对象", description="InfoTypeEdit对象")
public class InfoTypeEditDto {

    private static final long serialVersionUID = 1L;

        @Schema(description = "标识" )
    private String typeId;

    @NotNull(message = "请填写名称")
    @Schema(description = "名称" ,required = true)
    private String name;

    @Schema(description = "父id" )
    private String parentId;

    @NotNull(message = "请填写编号")
        @Schema(description = "编号" ,required = true)
    private String code;


    @NotNull(message = "请填写序号")
    @Schema(description = "请填写序号",required = true)
    private Integer sequence;
}