package com.juyou.opm.cms.dto.info;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class InfoDto {

    @Schema(description = "封面")
    @TableField("cover")
    private String cover;

    @Schema(description = "内容")
    private String content;


    @Schema(description = "标识")
    private String infoId;


    @Schema(description = "文章名称")
    private String name;


    @Schema(description = "大类")
    private String bigType;


    @Schema(description = "简介")
    private String briefIntroduction;

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

    List<TypeAttrDto> attrs;
}
