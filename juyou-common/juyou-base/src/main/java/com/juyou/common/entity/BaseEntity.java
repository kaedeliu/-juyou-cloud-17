package com.juyou.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "实体基础类")
public class BaseEntity implements java.io.Serializable{

    @Schema(description = "创建时间")
    @TableField(value = "create_time")
    private Date createTime;

    @Schema(description = "创建人")
    @TableField(value = "create_name")
    private String create_name;

    @Schema(description = "最后修改时间")
    @TableField(value = "last_update_time")
    private Date lastUpdateTime;

    @Schema(description = "最后修改人")
    @TableField(value = "last_update_name")
    private String lastUpdateName;

    @Schema(description = "商户Id")
    @TableField("tenants_id")
    private String tenantsId;

}
