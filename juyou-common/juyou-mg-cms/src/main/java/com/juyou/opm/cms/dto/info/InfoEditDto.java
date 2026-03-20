package com.juyou.opm.cms.dto.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文章-属性表
 * </p>
 *
 * @author yx
 * @since 2023-04-24 18:47:53
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title="InfoEditDto对象", description="InfoEdit对象")
public class InfoEditDto {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "请填写标识")
        @Schema(description = "标识" ,required = true)
    private String infoId;

    @NotNull(message = "请填写文章名称")
        @Schema(description = "文章名称" ,required = true)
    private String name;

        @Schema(description = "简介" )
    private String briefIntroduction;

    @NotNull(message = "请填写封面")
        @Schema(description = "封面" ,required = true)
    private String cover;

        @Schema(description = "内容" )
    private String content;

    @NotNull(message = "请填写文章类型")
        @Schema(description = "文章类型" ,required = true)
    private String typeId;

    @NotNull(message = "请填写是否热门")
        @Schema(description = "是否热门" ,required = true)
    private Integer hot;

    @NotNull(message = "请填写是否推荐")
        @Schema(description = "是否推荐" ,required = true)
    private Integer recommended;

    @NotNull(message = "请填写是否最新")
        @Schema(description = "是否最新" ,required = true)
    private Integer latest;

    @NotNull(message = "请填写是否精选")
        @Schema(description = "是否精选" ,required = true)
    private Integer curation;

    @Schema(description = "分类属性值")
    List<InfoAttrDto> attrs=new ArrayList<>();
}