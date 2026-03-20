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
 * 文章分类属性值
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("juyou_cms_type_attr_val")
@Schema(title="TypeAttrVal对象", description="文章分类属性值")
public class TypeAttrVal extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "attr_id",type= IdType.ASSIGN_ID)
    private String attrId;

    @Schema(description = "文章ID")
    @TableField("info_id")
    private String infoId;

    @TableField("type_id")
    private String typeId;

    @TableField("val")
    private String val;

    @Schema(description = "文章分类ID")
    @TableField("type_attr_id")
    private String typeAttrId;


}