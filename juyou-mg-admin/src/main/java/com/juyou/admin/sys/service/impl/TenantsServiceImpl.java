package com.juyou.admin.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juyou.admin.sys.entity.*;
import com.juyou.admin.sys.mapper.ConfigMapper;
import com.juyou.admin.sys.mapper.TenantsMapper;
import com.juyou.admin.sys.service.DictService;
import com.juyou.admin.sys.service.TenantsService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.exception.BaseException;
import com.juyou.common.utils.CommonUtils;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.PasswordUtil;
import com.juyou.common.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TenantsServiceImpl extends ServiceImpl<TenantsMapper, Tenants> implements TenantsService {

    @Autowired
    com.juyou.admin.sys.mapper.UserMapper userMapper;

    @Autowired
    com.juyou.admin.sys.mapper.MacMapper macMapper;

    @Autowired
    com.juyou.admin.sys.mapper.RoleMapper roleMapper;

    @Autowired
    com.juyou.admin.sys.mapper.UserRoleMapper userRoleMapper;

    @Autowired
    ConfigMapper configMapper;

    @Autowired
    com.juyou.admin.sys.mapper.DictMapper dictMapper;

    @Autowired
    com.juyou.admin.sys.mapper.DictDetailMapper dictDetailMapper;

    @Autowired
    LoginUtilsService loginUtilsService;

    static final Object lock=new Object();


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInitData(IdDto idDto) {
        synchronized (lock){
            Tenants tenants=getById(idDto.getId());

            Mac mac=initMac(tenants);

            Role role=initRole(tenants);

            User user=initUser(tenants,mac);

            initUserRole(tenants,user,role);

            initConfig(tenants);

            initDict(tenants);
        }

    }

    private Mac initMac(Tenants tenants){
        //处理机构
        LambdaQueryWrapper<Mac> macWrapper=new LambdaQueryWrapper<>();
        macWrapper.eq(Mac::getTenantsId,tenants.getTenantsId()).eq(Mac::getParentId,"0").last("limit 1");
        Mac mac=macMapper.selectOne(macWrapper);
        if(mac==null){
            mac=new Mac()
                    .setParentId("0")
                    .setStatus(StatusEnum.是.getCode())
                    .setSequence(0)
                    .setMacId(tenants.getTenantsId())
                    .setCode(tenants.getTenantsId())
                    .setName(tenants.getName());

            mac.setTenantsId(tenants.getTenantsId());

            macMapper.insert(mac);
        }
        return mac;
    }

    private Role initRole(Tenants tenants){
        //处理角色
        LambdaQueryWrapper<Role> roleWrapper=new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getTenantsId,tenants.getTenantsId()).eq(Role::getCode,CommonConstant.TENANTS_ADMIN)
                .eq(Role::getSysType, StatusEnum.系统类型_预留.getCode())
                .last("limit 1");
        Role role=roleMapper.selectOne(roleWrapper);
        if(role==null){
            role=new Role()
                    .setCode(CommonConstant.TENANTS_ADMIN)
                    .setName("管理员")
                    .setStatus(StatusEnum.是.getCode())
                    .setSysType( StatusEnum.系统类型_预留.getCode());
            role.setTenantsId(tenants.getTenantsId());
            roleMapper.insert(role);
        }
        return role;
    }

    private User initUser(Tenants tenants, Mac mac){
        //处理用户
        LambdaQueryWrapper<User> userWrapper=new LambdaQueryWrapper<>();
        userWrapper.eq(User::getTenantsId,tenants.getTenantsId()).eq(User::getSysType, StatusEnum.系统类型_预留.getCode())
                .last("limit 1");
        User user=userMapper.selectOne(userWrapper);
        if(user==null){
            String secret = StringUtil.randomGen(8);
            String password = PasswordUtil.encrypt("admin_#$%", "admin_#$%", secret);
            user=new User()
                    .setUserType(StatusEnum.USER_TYPE_默认.getCode())
                    .setSysType(StatusEnum.系统类型_预留.getCode())
                    .setName("管理员")
                    .setCode(tenants.getTenantsId()+"_admin")
                    .setMacId(mac.getMacId())
                    .setSecret(secret)
                    .setPassword(password);
            user.setTenantsId(tenants.getTenantsId());
            userMapper.insert(user);
        }
        return user;
    }

    private UserRole initUserRole(Tenants tenants, User user, Role role){
        //处理用户角色
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper=new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId,user.getUserId()).eq(UserRole::getRoleId,role.getRoleId())
                .eq(UserRole::getTenantsId,tenants.getTenantsId())
                .last("limit 1");
        UserRole userRole=userRoleMapper.selectOne(userRoleLambdaQueryWrapper);
        if(userRole==null){
            userRole=new UserRole()
                    .setRoleId(role.getRoleId())
                    .setUserId(user.getUserId());
            userRole.setTenantsId(tenants.getTenantsId());
            userRoleMapper.insert(userRole);
        }
        return userRole;
    }

    /**
     * 处理系统配置
     * @param tenants
     */
    private void initConfig(Tenants tenants){
        LambdaQueryWrapper<Config> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Config::getTenantsId,"100");
        List<Config> sysConfigs=configMapper.selectList(queryWrapper);

        queryWrapper.clear();
        queryWrapper.eq(Config::getTenantsId,tenants.getTenantsId());
        List<Config> configs=configMapper.selectList(queryWrapper);
        List<String> codes=configs.stream().map(Config::getCode).collect(Collectors.toList());

        List<Config> newConfigs=sysConfigs.stream().filter(config -> !codes.contains(config.getCode())).collect(Collectors.toList());
        if(!newConfigs.isEmpty()){
            for (Config config: newConfigs) {
                config.setConfigId(null).setTenantsId(tenants.getTenantsId());
                configMapper.insert(config);
            }
        }
    }

    /**
     * 处理字典
     * @param tenants
     */
    private void initDict(Tenants tenants){

        Map<String,JSONObject> cache=new HashMap<>();
        LambdaQueryWrapper<Dict> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getTenantsId,"100");
        List<Dict> sysDicts=dictMapper.selectList(queryWrapper);

        LambdaQueryWrapper<DictDetail> queryDetailWrapper=new LambdaQueryWrapper<>();
        queryDetailWrapper.eq(DictDetail::getTenantsId,"100");
        List<DictDetail> sysDictDetails=dictDetailMapper.selectList(queryDetailWrapper);
        Map<String,List<DictDetail>> sysDictDetailsMap=sysDictDetails.stream().collect(Collectors.groupingBy(DictDetail::getDictId));
        sysDicts.forEach(item->{
            JSONObject json=new JSONObject();
            json.put("dict",item);
            json.put("dictDetails",sysDictDetailsMap.get(item.getDictId()));
            cache.put(item.getDictId(),json);
        });

        queryWrapper.clear();
        queryWrapper.eq(Dict::getTenantsId,tenants.getTenantsId());
        List<Dict> dicts=dictMapper.selectList(queryWrapper);
        Map<String, Dict> codes=dicts.stream().collect(Collectors.toMap(Dict::getCode, Dict->Dict));

        queryDetailWrapper.clear();
        queryDetailWrapper.eq(DictDetail::getTenantsId,tenants.getTenantsId());
        List<DictDetail> dictsDetails=dictDetailMapper.selectList(queryDetailWrapper);
        Map<String,List<DictDetail>> dictsDetailsMap=dictsDetails.stream().collect(Collectors.groupingBy(DictDetail::getDictId));

        //先处理需要新增的
        //看执行效率，多了需要做异步，和写xml批量插入
        List<Dict> newConfigs=sysDicts.stream().filter(dict -> !codes.containsKey(dict.getCode())).collect(Collectors.toList());
        if(!newConfigs.isEmpty()){
            for (Dict dict: newConfigs) {
                String id=dict.getDictId();
                List<DictDetail> tempDictDetails= (List<DictDetail>) cache.get(id).get("dictDetails");
                dict.setDictId(null).setTenantsId(tenants.getTenantsId());
                dictMapper.insert(dict);
                for(DictDetail item:tempDictDetails) {
                    item.setDictId(dict.getDictId()).setTenantsId(tenants.getTenantsId());
                    dictDetailMapper.insert(item);
                }
                cache.remove(id);
            }
        }

        List<Dict> upDicts=new ArrayList<>();
        List<DictDetail> newDictDetails=new ArrayList<>();
        List<DictDetail> upDictDetails=new ArrayList<>();
        //将系统数据和租户数据对照起来
        cache.forEach((key,value)->{
            Dict dict= (Dict) value.get("dict");
            String code=dict.getCode();
            Dict tdict=codes.get(code);
            List<DictDetail> dictDetails = (List<DictDetail>) value.get("dictDetails");
            if(tdict!=null){
                List<DictDetail> tdictDetails=dictsDetailsMap.get(tdict.getDictId());
                Map<String, DictDetail> tmap=new HashMap<>();
                if(tdictDetails!=null){
                    Map<String, DictDetail> tmaps=tdictDetails.stream().collect(Collectors.toMap(DictDetail::getName, DictDetail->DictDetail));
                    tmap.putAll(tmaps);
                }
                if(!Objects.equals(tdict.getStatus(), dict.getStatus())){
                    tdict.setStatus(dict.getStatus());
                    upDicts.add(tdict);
                }
                if(dictDetails!=null && !dictDetails.isEmpty()){
                    dictDetails.forEach(item->{
                        if(!tmap.containsKey(item.getName())){
                            item.setDictId(tdict.getDictId()).setDictDetailId(null);
                            item.setTenantsId(tenants.getTenantsId());
                            newDictDetails.add(item);
                        }else{
                            DictDetail dictDetail=tmap.get(item.getName());
                            if(!dictDetail.getSequence().equals(item.getSequence()) || !dictDetail.getStatus().equals(item.getStatus())){
                                dictDetail.setSequence(item.getSequence()).setStatus(item.getStatus());
                                upDictDetails.add(dictDetail);
                            }
                        }
                    });
                }
            }
        });

        upDicts.forEach(item->{
            dictMapper.updateById(item);
        });
        newDictDetails.forEach(item->{
            dictDetailMapper.insert(item);
        });
        upDictDetails.forEach(item->{
            dictDetailMapper.updateById(item);
        });
        //更新缓存
        SpringContextUtils.getBean(DictService.class).updateConfigAndCacheDict(tenants.getTenantsId());
    }

    public void hasInsert(Object object) throws IllegalAccessException {
        LoginUserBasicDto loginUserDto=loginUtilsService.getLoginUserInfo();
        if(loginUserDto.getUserType()==StatusEnum.USER_TYPE_系统超管.getCode()){ //系统超管在添加非功能数据时必须传入商户ID
            Field field =  CommonUtils.getField(object.getClass(), "tenantsId", false);
            String value= (String) field.get(object);
            if(!StringUtils.hasLength(value)){
                throw BaseException.defaultCode("系统账户添加数据时请确认商户");
            }
        }
    }
}