package com.juyou.admin.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.juyou.admin.sys.entity.Tenants;
import com.juyou.common.dto.IdDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yx
 * @since 2023-04-21 12:03:16
 */
public interface TenantsService extends IService<Tenants>{


    /**
     * 初始化数据
     * 1、补用户
     * 2、补角色
     * 3、补系统配置
     * 4、补字段数据
     * 5、补机构数据(如果没有一级机构)
     * @param idDto
     */
    void updateInitData(IdDto idDto);

    /**
     * 判断对象是否传入了商户ID
     * @param object
     * @throws IllegalAccessException
     */
    void hasInsert(Object object) throws IllegalAccessException;

}