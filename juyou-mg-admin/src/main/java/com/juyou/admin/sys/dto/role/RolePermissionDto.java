package com.juyou.admin.sys.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "角色权限")
public class RolePermissionDto {

    @Schema(description = "权限IDs",required = true)
    List<String> permissionIds=new ArrayList<>();
}
