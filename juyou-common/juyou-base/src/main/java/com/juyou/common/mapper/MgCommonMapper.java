package com.juyou.common.mapper;

import com.juyou.common.dto.LoginUserDto;
import com.juyou.common.dto.TenantsBasicDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MgCommonMapper {
    LoginUserDto findLoginUserDto(@Param("userId") String userId);

    String findUserIdByName(@Param("userName") String userName);

    /**
     * 查找用户的角色列表
     * @param userId
     * @return
     */
    List<String> findRoleByUser(@Param("userId") String userId);

    /**
     * 获取用户角色拥有的权限
     * @param roleCode
     * @return
     */
    List<String> findRoleAuth(@Param("roleCode") String roleCode);

//    /**
//     * 获取用户角色拥有的权限
//     * @param roleId
//     * @return
//     */
//    List<String> findRoleAuth(@Param("roleIds") List<String> roleId);

    /**
     * 获取租户信息
     * @param tenantsId 租户id
     * @return
     */
    TenantsBasicDto findTenants(@Param("tenantsId") String tenantsId);

    /**
     * 查找租户管理员的角色权限
     * @return
     */
    List<String> findTenantsAdminAuth();
}
