package com.juyou.admin.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yx
 * @since 2023-04-21 12:03:16
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("juyou_sys_tenants")
@Schema(title="Tenants对象", description="")
public class Tenants implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户ID")
    @TableId(value = "tenants_id",type = IdType.INPUT)
    private String tenantsId;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "状态")
    @TableField("status")
    private Byte status;


}