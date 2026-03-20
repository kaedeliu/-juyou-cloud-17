package com.juyou.admin.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juyou.admin.sys.entity.RolePermission;

/**
 * 角色菜单Mapper接口
 * 
 * @author kaedeliu
 *
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

	/**
	 * 根据角色ID查询菜单
	 * 
	 * @param roleId 角色ID
	 * @return 菜单ID集合
	 */
	List<String> queryByRoleId(@Param("roleId") String roleId);

}
