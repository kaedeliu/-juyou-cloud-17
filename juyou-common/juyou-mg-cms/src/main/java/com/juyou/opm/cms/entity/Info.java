package com.juyou.opm.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.juyou.common.entity.BaseEntity;
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
@TableName("juyou_cms_info")
@Schema(title="Info对象", description="文章-属性表")
public class Info extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "info_id",type= IdType.ASSIGN_ID)
    private String infoId;

    @TableField("name")
    private String name;

    @TableField("big_type")
    private String bigType;

    @Schema(description = "简介")
    @TableField("brief_introduction")
    private String briefIntroduction;

    @Schema(description = "封面")
    @TableField("cover")
    private String cover;

    @Schema(description = "内容")
    @TableField("content")
    private String content;

    @Schema(description = "文件类型")
    @TableField("type_id")
    private String typeId;

    @Schema(description = "是否热门")
    @TableField("hot")
    private Integer hot;

    @Schema(description = "推荐")
    @TableField("recommended")
    private Integer recommended;

    @Schema(description = "最新")
    @TableField("latest")
    private Integer latest;

    @Schema(description = "精选")
    @TableField("curation")
    private Integer curation;


}