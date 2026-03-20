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
 * 文章分类属性
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("juyou_cms_type_attributes")
@Schema(title="TypeAttributes对象", description="文章分类属性")
public class TypeAttributes extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "attr_id",type= IdType.ASSIGN_ID)
    private String attrId;

    @TableField("name")
    private String name;

    @TableField("val_type")
    private String valType;

    @Schema(description = "序号")
    @TableField("order_num")
    private Integer orderNum;

    @Schema(description = "是否必填（0不是，1是）")
    @TableField("is_required")
    private Integer isRequired;

    @Schema(description = "是否是字典（0不是，1是）")
    @TableField("is_dict")
    private Integer isDict;

    @TableField("dict_key")
    private String dictKey;

    @TableField("type_id")
    private String typeId;

    @Schema(description = "标识code")
    @TableField("code")
    private String code;


}