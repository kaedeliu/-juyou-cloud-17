package com.juyou.admin.sys.dto.tenants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;

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
@Schema(title="TenantsAddDto对象", description="TenantsAddDto对象")
public class TenantsAddDto {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "请填写编号")
    @Schema(description = "租户ID")
    private String tenantsId;

    @NotNull(message = "请填写姓名")
    @Schema(description = "名称")
    private String name;

    @NotNull
    @Schema(description = "状态")
    private Byte status;

}