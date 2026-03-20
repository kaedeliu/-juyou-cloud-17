package com.juyou.common.service;

import com.juyou.common.dto.LoginUserDto;
import com.juyou.common.dto.TenantsBasicDto;

import java.util.List;

public interface MgCommonService {
    /**
     * 获取登录用户信息
     * @param userId
     * @return
     */
    LoginUserDto findLoginUserDto(String userId);

//    /**
//     * 通过用户名获取用户ID
//     * @param userName
//     * @return
//     */
//    String findUserIdByName(String userName);

//    /**
//     * 查找用户的角色列表
//     * @param userId
//     * @return
//     */
//    List<String> findRoleByUser(String userId);

    /**
     * 获取用户角色拥有的权限
     * @param roleCodes 角色标识
     * @return
     */
    List<String> findRolesAuth(List<String> roleCodes, String tenantsId);

    /**
     * 获取租户信息
     * @param tenantsId 租户id
     * @return
     */
    TenantsBasicDto findTenants(String tenantsId);
}
