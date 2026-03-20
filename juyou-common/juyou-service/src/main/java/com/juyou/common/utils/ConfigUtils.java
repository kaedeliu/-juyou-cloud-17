package com.juyou.common.utils;

import com.juyou.common.base.mapper.BaseMapper;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.exception.BaseException;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取配置属性
 */
@Component
public class ConfigUtils {

    @Autowired
    BaseMapper baseMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MyConversionService myConversionService;

    @Autowired
    LoginUtilsService loginUtilsService;

    /**
     * 获取系统配置
     * @param code 编码
     * @param defaultVal 默认值
     * @return
     */
    public String findVal(String code,String defaultVal){
        return findVal(code,defaultVal,String.class,loginUtilsService.getLoginUserInfo().getTenantsId());
    }

    /**
     * 获取系统配置
     * @param code 编码
     * @param defaultVal 默认值
     * @param tenantsId 租户ID
     * @return
     */
    public String findVal(String code,String defaultVal,String tenantsId){
        return findVal(code,defaultVal,String.class,tenantsId);
    }

    /**
     * 获取系统配置
     * @param code 字典code
     * @param defaultVal 默认值
     * @param clazz 值类型
     * @return
     * @param <T>
     */
    public <T> T findVal(String code,T defaultVal,Class<T> clazz){
        return findVal(code,defaultVal,clazz,loginUtilsService.getLoginUserInfo().getMacId());
    }

    /**
     * 获取系统配置
     * @param code 字典code
     * @param defaultVal 默认值
     * @param clazz 值类型
     * @param tenantsId 租户ID
     * @return
     * @param <T>
     */
    public <T> T findVal(String code,T defaultVal,Class<T> clazz,String tenantsId){
        if(!StringUtils.hasLength(tenantsId)){
            throw BaseException.defaultCode("租户信息异常");
        }
        String key=CommonConstant.SYS_CACHE_CONFIG+ CommonConstant.REDIS_BUFF +tenantsId + CommonConstant.REDIS_BUFF + code;
        String value = (String) redisUtil.get(key);
        if(!StringUtils.hasLength(value)){
             value = baseMapper.findConfig(code,tenantsId);
             if(StringUtils.hasLength(value)){
                 redisUtil.set(key,value);
             }
        }
        if(StringUtils.hasLength(value)) {
            return myConversionService.convert(value, clazz);
        }
        return defaultVal;
    }

    /**
     * 批量获取字典值
     * @param codes 字典codes
     * @param defaultVal 默认值
     * @return
     */
    public Map<String,String> findVals(Collection<String> codes,String defaultVal){
        return findVals(codes,defaultVal,loginUtilsService.getLoginUserInfo().getTenantsId());
    }
    /**
     * 批量获取字典值
     * @param codes 字典codes
     * @param defaultVal 默认值
     * @param tenantsId 租户ID
     * @return
     */
    public Map<String,String> findVals(Collection<String> codes,String defaultVal,String tenantsId){
        Map<String,String> maps=new HashMap<>();
        for(String code : codes) {
           String val=findVal(code,defaultVal,String.class,tenantsId);
           maps.put(code,val);
        }
        return maps;
    }
}
