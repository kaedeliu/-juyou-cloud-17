package com.juyou.opm.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.exception.BaseException;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.CommonUtils;
import com.juyou.common.utils.TreeUtils;

import com.juyou.opm.cms.constant.InfoConstant;
import com.juyou.opm.cms.dto.infotype.*;
import com.juyou.opm.cms.dto.typeattributes.TypeAttributesAddDto;
import com.juyou.opm.cms.dto.typeattributes.TypeAttributesDto;
import com.juyou.opm.cms.entity.InfoType;
import com.juyou.opm.cms.entity.TypeAttributes;
import com.juyou.opm.cms.mapper.InfoTypeMapper;
import com.juyou.opm.cms.service.InfoTypeService;
import com.juyou.opm.cms.service.TypeAttributesService;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InfoTypeServiceImpl extends ServiceImpl<InfoTypeMapper, InfoType> implements InfoTypeService {

    @Autowired
    TypeAttributesService typeAttributesService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginUtilsService loginUtilsService;

    @Override
    public List<InfoTypeTreeDto> treeList() throws Exception {
        QueryWrapper<InfoType> queryWrapper = QueryGenerator.initQueryWrapper(new InfoType(),InfoType.class);
        List<InfoType> list = list(queryWrapper);
        return TreeUtils.formatTree(list,"0","typeId",InfoTypeTreeDto.class,InfoType.class);
    }

    @Override
    public InfoTypeDto findInfoTypeAttr(IdDto dto) throws Exception {
        String typeId=dto.getId();
        if(!InfoConstant.COMMON_ATTR_TYPE.equals(typeId)){ //公共属性
            InfoType infoType=getById(dto.getId());
            if(infoType==null){
                throw BaseException.defaultCode("分类信息异常");
            }
        }
        LambdaQueryWrapper<TypeAttributes> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(TypeAttributes::getTypeId,typeId).orderByAsc(TypeAttributes::getOrderNum).eq(TypeAttributes::getTenantsId,
                loginUtilsService.getLoginUserInfo().getTenantsId());
        List<TypeAttributes> typeAttributess=typeAttributesService.list(queryWrapper);
        List<TypeAttributesDto> attributesDtos= CommonUtils.copyProperties(typeAttributess,TypeAttributesDto.class);
        InfoTypeDto infoTypeDto=new InfoTypeDto();
        return infoTypeDto.setTypeId(typeId).setAttrs(attributesDtos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTypeAttr(InfoTypeAttrAddDto infoTypeAttrAddDto) throws Exception {
        String typeId=infoTypeAttrAddDto.getTypeId();
        if(!InfoConstant.COMMON_ATTR_TYPE.equals(typeId)) { //公共属性
            InfoType infoType = getById(infoTypeAttrAddDto.getTypeId());
            if (infoType == null) {
                throw BaseException.defaultCode("分类信息异常");
            }
        }
        //先处理顺序
        if(infoTypeAttrAddDto.getAttrs()==null) infoTypeAttrAddDto.setAttrs(new ArrayList<>());
        for(int i=0;i<infoTypeAttrAddDto.getAttrs().size();i++){
            infoTypeAttrAddDto.getAttrs().get(i).setOrderNum(i);
            infoTypeAttrAddDto.getAttrs().get(i).setTypeId(typeId);
        }
        Map<String, TypeAttributesAddDto> typeAttributesMap = infoTypeAttrAddDto.getAttrs().stream().filter(o-> StringUtils.hasLength(o.getAttrId()))
                .collect(Collectors.toMap(TypeAttributesAddDto::getAttrId, TypeAttributesAddDto->TypeAttributesAddDto));

        LambdaQueryWrapper<TypeAttributes> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(TypeAttributes::getTypeId,typeId).eq(TypeAttributes::getTenantsId,loginUtilsService.getLoginUserInfo().getTenantsId());
        List<TypeAttributes> typeAttributess=typeAttributesService.list(queryWrapper);
        //处理删除
        typeAttributess.forEach(item->{
            if(!typeAttributesMap.containsKey(item.getAttrId())){
                typeAttributesService.removeById(item);
            }
        });
        infoTypeAttrAddDto.getAttrs().forEach(item->{
            TypeAttributes typeAttributes=new TypeAttributes();
            BeanUtils.copyProperties(item,typeAttributes );
            if(StringUtils.hasLength(item.getAttrId())){
                typeAttributesService.updateById(typeAttributes);
            }else{
                typeAttributesService.save(typeAttributes);
            }
        });
        deleteInfoTypeAttrRedisCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(InfoTypeAddDto infoTypeAddDto) {
        InfoType infoType = new InfoType();
        BeanUtils.copyProperties(infoTypeAddDto,infoType);
        if(!StringUtils.hasLength(infoType.getParentId())){
            infoType.setParentId("0");
        }
        save(infoType);
        deleteInfoTypeRedisCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update( InfoTypeEditDto infoTypeEditDto){
        InfoType infoType = new InfoType();
        BeanUtils.copyProperties(infoTypeEditDto,infoType);
        updateById(infoType);
        deleteInfoTypeRedisCache();
    }

    @Override
    public List<InfoTypeTreeSelectDto> treeSelect() throws Exception {
        QueryWrapper<InfoType> queryWrapper = QueryGenerator.initQueryWrapper(new InfoType(),InfoType.class);
        queryWrapper.select("type_id","name","parent_id");
        List<InfoType> list = list(queryWrapper);
        return TreeUtils.formatTree(list,"0","typeId",InfoTypeTreeSelectDto.class,InfoType.class);
    }

    private void deleteInfoTypeRedisCache(){
        String redisKey= CommonConstant.SYS_INFO_TYPE+CommonConstant.REDIS_BUFF+loginUtilsService.getLoginUserInfo().getTenantsId();
        redisUtil.del(redisKey);
    }

    private void deleteInfoTypeAttrRedisCache(){
        String redisKey= CommonConstant.SYS_INFO_TYPE_ATTR+CommonConstant.REDIS_BUFF+loginUtilsService.getLoginUserInfo().getTenantsId();
        redisUtil.del(redisKey);
    }

}