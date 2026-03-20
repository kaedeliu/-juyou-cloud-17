package com.juyou.admin.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.juyou.admin.sys.entity.Permission;
import com.juyou.admin.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author kaedeliu
 *
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用拥有的权限
     * @param queryWrapper
     * @return
     */
    List<Permission> findUserPermission(@Param(Constants.WRAPPER) QueryWrapper<User> queryWrapper);
}
