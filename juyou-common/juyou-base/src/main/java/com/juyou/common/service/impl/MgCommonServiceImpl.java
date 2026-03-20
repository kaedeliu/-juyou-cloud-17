package com.juyou.common.service.impl;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.LoginUserDto;
import com.juyou.common.dto.TenantsBasicDto;
import com.juyou.common.mapper.MgCommonMapper;
import com.juyou.common.service.MgCommonService;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MgCommonServiceImpl implements MgCommonService {

    @Autowired
    MgCommonMapper mgCommonMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public LoginUserDto findLoginUserDto(String userId) {
        String redisKey=CommonConstant.SYS_LOGIN_USER+CommonConstant.REDIS_BUFF+userId;
        LoginUserDto loginUserDto = (LoginUserDto) redisUtil.get(redisKey);
        if(loginUserDto!=null) return loginUserDto;
        loginUserDto=mgCommonMapper.findLoginUserDto(userId);
        if(loginUserDto!=null) {
            loginUserDto.setRoleCodes(mgCommonMapper.findRoleByUser(userId));
            redisUtil.set(redisKey,loginUserDto);
        }
        return loginUserDto;
    }

//    public String findUserIdByName(String userName){
//        return mgCommonMapper.findUserIdByName(userName);
//    }

//    @Override
//    public List<String> findRoleByUser(String userId) {
//        return mgCommonMapper.findRoleByUser(userId);
//    }

    @Override
    public List<String> findRolesAuth(List<String> roleCodes, String tenantsId) {
        if(roleCodes.contains(CommonConstant.TENANTS_ADMIN)) //租户管理员
            return findTenantsAdminAuth();

        List<String> auths=new ArrayList<>();
        for (String roleCode: roleCodes) {
            List<String> temps=findRolesAuth(roleCode,tenantsId);
            if(!temps.isEmpty()) auths.addAll(temps);
        }
        if(auths.isEmpty()) return auths;
        return auths.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public TenantsBasicDto findTenants(String tenantsId) {
        String redisKey=CommonConstant.SYS_TENANTS+CommonConstant.REDIS_BUFF+tenantsId;
        TenantsBasicDto tenantsBasicDto= (TenantsBasicDto) redisUtil.get(redisKey);
        if(tenantsBasicDto==null){
            tenantsBasicDto = mgCommonMapper.findTenants(tenantsId);
            if(tenantsBasicDto!=null){
                redisUtil.set(redisKey,tenantsBasicDto);
            }
        }
        return tenantsBasicDto;
    }

    /**
     * 获取角色的权限
     * @param roleCode
     * @param tenantsId
     * @return
     */
    private List<String> findRolesAuth(String roleCode,String tenantsId){
        String reidsKey=CommonConstant.SYS_ROLE_AUTH+ CommonConstant.REDIS_BUFF+tenantsId+CommonConstant.REDIS_BUFF+roleCode;
        List<String> auths= (List<String>) redisUtil.get(reidsKey);
        if(auths!=null){
            auths = mgCommonMapper.findRoleAuth(roleCode);
            redisUtil.set(reidsKey,auths);
        }
        return auths;
    }

    /**
     * 获取租户管理员角色权限
     * @return
     */
    private List<String> findTenantsAdminAuth(){
        String reidsKey=CommonConstant.SYS_TENANTS_ADMIN_AUTH;
        List<String> auths= (List<String>) redisUtil.get(reidsKey);
        if(auths!=null){
            auths = mgCommonMapper.findTenantsAdminAuth();
            redisUtil.set(reidsKey,auths);
        }
        return auths;
    }
}
