package com.juyou.common.permissiondata;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 数据权限表
 * </p>
 *
 * @author kaedeliu
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description ="PermissionDataRule对象")
public class PermissionDataRule implements Serializable {

    private static final long serialVersionUID=1L;

    /*1~*/@Schema(description = "主键")
    private String dataRuleId;

    /*1~*/@Schema(description = "权限菜单主键")
    private String permissionId;

    /*1~*/@Schema(description = "字段")
    private String ruleColumn;

    /*1~*/@Schema(description = "条件")
    private String ruleConditions;

    /*1~*/@Schema(description = "规则值")
    private String ruleValue;
    
    /*1~*/@Schema(description = "权限标识")
    private String perms;
    
}
