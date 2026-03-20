package com.juyou.admin.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juyou.admin.sys.entity.UserRole;

/**
 * 用户角色Mapper
 * 
 * @author kaedeliu
 *
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userId 用户ID
	 * @return 角色ID集合
	 */
	List<String> queryByUserId(@Param("userId") String userId);
}
