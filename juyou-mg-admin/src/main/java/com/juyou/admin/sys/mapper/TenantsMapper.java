package com.juyou.admin.sys.mapper;

import com.juyou.admin.sys.entity.Tenants;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yx
 * @since 2023-04-21 12:03:16
 */
// NOTE:
//  该 Mapper 与依赖包里的同名 TenantsMapper（com.juyou.basics.sys.mapper.TenantsMapper）
//  会发生 Spring Bean 命名冲突，导致应用启动失败。
//  因此这里不再启用 @Mapper 注册，让 Service 使用依赖里的 basics mapper。
public interface TenantsMapper extends BaseMapper<Tenants> {

}
