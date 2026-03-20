package com.juyou.opm.cms.dto.info;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Schema(title="InfoListDto对象", description="InfoList对象")
public class InfoListDto {

    private static final long serialVersionUID = 1L;


        @Schema(description = "标识")
    private String infoId;


        @Schema(description = "文章名称")
    private String name;


        @Schema(description = "大类")
    private String bigType;


        @Schema(description = "简介")
    private String briefIntroduction;


        @Schema(description = "封面")
    private String cover;


        @Schema(description = "文章类型")
    private String typeId;


        @Schema(description = "是否热门")
    private Integer hot;


        @Schema(description = "是否推荐")
    private Integer recommended;


        @Schema(description = "是否最新")
    private Integer latest;


        @Schema(description = "是否精选")
    private Integer curation;

    @Schema(description = "大类名称")
        String bigTypeName;

    @Schema(description = "小类名称")
        String typeName;


}