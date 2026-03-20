package com.juyou.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Schema(description = "登录对象基类")
@Data
@Accessors(chain = true)
public class LoginUserBasicDto {

    @Schema(description = "用户主键")
    private String userId;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "锁定状态 0-正常,1- 锁定")
    Integer lockStatus=0;

    @Schema(description = "是否删除0-正常,1-删除")
    Integer delFlag=0;

    @Schema(description = "用户类型")
    Integer userType=0;

    @Schema(description = "状态 0禁用,1启用")
    Integer status=1;

    @Schema(description = "登录渠道")
    Integer deviceType;

    @Schema(description = "角色标识")
    List<String> roleCodes;

    @Schema(description = "机构ID")
    private String macId;

    @Schema(description = "租户ID")
    private String tenantsId;

    @Schema(description = "系统类型")
    private Integer sysType;


}
