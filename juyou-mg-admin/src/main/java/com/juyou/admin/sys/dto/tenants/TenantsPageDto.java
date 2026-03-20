package com.juyou.admin.sys.dto.tenants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import com.juyou.common.dto.PageDto;
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
@Schema(title="TenantsPageDto对象", description="TenantsPage查询对象")
public class TenantsPageDto extends PageDto {

    private static final long serialVersionUID = 1L;


        @Schema(description = "租户ID")
    private String tenantsId;

        @Schema(description = "名称")
    private String name;

        @Schema(description = "状态")
    private Byte status;
}