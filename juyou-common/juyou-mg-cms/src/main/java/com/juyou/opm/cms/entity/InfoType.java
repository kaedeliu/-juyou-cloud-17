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
 * 文章分类
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("juyou_cms_info_type")
@Schema(title="InfoType对象", description="文章分类")
public class InfoType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "type_id",type= IdType.ASSIGN_ID)
    private String typeId;

    @TableField("name")
    private String name;

    @TableField("parent_id")
    private String parentId;

    @TableField("code")
    private String code;

    @TableField("sequence")
    private Integer sequence;


}