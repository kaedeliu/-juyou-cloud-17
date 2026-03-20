package com.juyou.common.permissiondata;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kaedeliu
 * @since 2021-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title ="Permission对象", description="")
public class Permission implements Serializable {

    private static final long serialVersionUID=1L;

    /*1~*/@Schema(description = "菜单权限编码")
    private String perms;

}
