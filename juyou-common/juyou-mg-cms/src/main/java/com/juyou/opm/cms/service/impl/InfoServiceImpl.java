package com.juyou.opm.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.exception.BaseException;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.CommonUtils;
import com.juyou.opm.cms.constant.InfoConstant;
import com.juyou.opm.cms.dto.info.*;
import com.juyou.opm.cms.dto.typeattributes.TypeAttributesAddDto;
import com.juyou.opm.cms.entity.Info;
import com.juyou.opm.cms.entity.InfoType;
import com.juyou.opm.cms.entity.TypeAttrVal;
import com.juyou.opm.cms.entity.TypeAttributes;
import com.juyou.opm.cms.mapper.InfoMapper;
import com.juyou.opm.cms.service.InfoService;
import com.juyou.opm.cms.service.InfoTypeService;
import com.juyou.opm.cms.service.TypeAttrValService;
import com.juyou.opm.cms.service.TypeAttributesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {

        @Autowired
        TypeAttributesService typeAttributesService;

        @Autowired
        TypeAttrValService typeAttrValService;

        @Autowired
        InfoTypeService infoTypeService;

    @Autowired
    LoginUtilsService loginUtilsService;

       @Override
       @Transactional(rollbackFor = Exception.class)
       public IPage<InfoListDto> pageList(InfoPageDto infoPageDto) throws  Exception{
           QueryWrapper<Info> queryWrapper = QueryGenerator.initQueryWrapper(infoPageDto,Info.class);
            queryWrapper.select( "a.info_id", "a.name", "a.big_type", "a.brief_introduction", "a.cover",  "a.type_id", "a.hot", "a.recommended", "a.latest", "a.curation"
                    ,"big.name as bigTypeName","type.name as typeName");
            Page<Info> page = new Page<Info>(infoPageDto.getPageNo(), infoPageDto.getPageSize());
           return baseMapper.pageList(page, queryWrapper);
       }



       @Override
       @Transactional(rollbackFor = Exception.class)
       public void insert( InfoAddDto infoAddDto){
            Info info = new Info();
            BeanUtils.copyProperties(infoAddDto,info);

            info.setBigType(getBigType(info.getTypeId()));

            save(info);

            vfAttr(info,infoAddDto.getAttrs());

            saveInfoVal(info,infoAddDto.getAttrs());
       }



    @Override
    public InfoDto findById(IdDto idDto) throws Exception {
        Info info=getById(idDto.getId());
        if(info==null)
            throw BaseException.defaultCode("文章信息异常或者删除");

        LambdaQueryWrapper<TypeAttributes> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(TypeAttributes::getTypeId,info.getBigType(), InfoConstant.COMMON_ATTR_TYPE).orderByAsc(TypeAttributes::getOrderNum).eq(TypeAttributes::getTenantsId,info.getTenantsId());
        List<TypeAttributes> typeAttributess=typeAttributesService.list(queryWrapper);
        List<TypeAttrDto> attrDtos= CommonUtils.copyProperties(typeAttributess,TypeAttrDto.class);

        //组装文章值
        LambdaQueryWrapper<TypeAttrVal> wrapper=new LambdaQueryWrapper<>();
        wrapper.in(TypeAttrVal::getInfoId,info.getInfoId(),InfoConstant.COMMON_ATTR_TYPE).eq(TypeAttrVal::getTenantsId,info.getTenantsId())
                .select(TypeAttrVal::getTypeAttrId,TypeAttrVal::getVal);
        List<TypeAttrVal> typeAttrVals=typeAttrValService.list(wrapper);
        Map<String,String> map=typeAttrVals.stream().collect(Collectors.toMap(TypeAttrVal::getTypeAttrId,TypeAttrVal::getVal));

        attrDtos.forEach(item->{
            if(map.containsKey(item.getAttrId())){
                item.setVal(map.get(item.getAttrId()));
            }
        });
        InfoDto infoDto=new InfoDto();
        BeanUtils.copyProperties(info,infoDto);
        infoDto.setAttrs(attrDtos);
        return infoDto;
    }

    @Override
       @Transactional(rollbackFor = Exception.class)
       public void update(InfoEditDto infoEditDto){

            Info info = new Info();
            BeanUtils.copyProperties(infoEditDto,info);
            info.setBigType(getBigType(info.getTypeId()));
            vfAttr(info,infoEditDto.getAttrs());

            updateById(info);

            //删除原本的值
           LambdaUpdateWrapper<TypeAttrVal> wrapper=new LambdaUpdateWrapper<TypeAttrVal>();
           wrapper.eq(TypeAttrVal::getInfoId,info.getInfoId());
           typeAttrValService.remove(wrapper);

           //保存属性
            saveInfoVal(info,infoEditDto.getAttrs());
       }

       private String getBigType(String typeId){
           InfoType infoType=infoTypeService.getById(typeId);
           if(infoType==null) throw BaseException.defaultCode("获取分类信息错误");
           String parentId=infoType.getParentId();
           if(null == parentId || "".equals(parentId) || "0".equals(parentId)) return typeId;
           return getBigType(infoType.getParentId());
       }

       private void saveInfoVal(Info info,List<InfoAttrDto> attrs){
           List<TypeAttrVal> attrVals=new ArrayList<>();
           for (InfoAttrDto attr:attrs) {
               TypeAttrVal val=new TypeAttrVal();
               val.setInfoId(info.getInfoId())
                       .setTypeId(info.getBigType())
                       .setTypeAttrId(attr.getAttrId())
                       .setVal(attr.getVal());
               attrVals.add(val);
           }
           typeAttrValService.saveBatch(attrVals);
       }

    private void vfAttr( Info info,List<InfoAttrDto> attrs){
        LambdaQueryWrapper<TypeAttributes> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(TypeAttributes::getTypeId,info.getBigType(), InfoConstant.COMMON_ATTR_TYPE).eq(TypeAttributes::getTenantsId,info.getTenantsId());
        List<TypeAttributes> typeAttributess=typeAttributesService.list(queryWrapper);
//           if(typeAttributess.isEmpty() &&(attrs!=null && !attrs.isEmpty()))
//               throw BaseException.defaultCode("分类属性信息不符合");
//           if(typeAttributess.isEmpty()) return;
//           if(typeAttributess.size() != attrs.size()){
//               throw BaseException.defaultCode("分类属性信息不符合");
//           }
        Map<String,InfoAttrDto> map=attrs.stream().collect(Collectors.toMap(InfoAttrDto::getAttrId,InfoAttrDto->InfoAttrDto));
        for (TypeAttributes typeAttributes : typeAttributess){
//               if(!map.containsKey(typeAttributes.getAttrId()))
//                   throw  BaseException.defaultCode("分类属性:"+typeAttributes.getName()+"异常");
            if(typeAttributes.getIsRequired()==1 && !StringUtils.hasLength(map.get(typeAttributes.getAttrId()).getVal())){
                throw  BaseException.defaultCode("分类属性:"+typeAttributes.getName()+"必须填写值");
            }
        }
    }

       @Override
       @Transactional(rollbackFor = Exception.class)
       public void delete(IdDto dto){
            removeById(dto.getId());
           //删除原本的值
           LambdaUpdateWrapper<TypeAttrVal> wrapper=new LambdaUpdateWrapper<TypeAttrVal>();
           wrapper.eq(TypeAttrVal::getInfoId,dto.getId());
           typeAttrValService.remove(wrapper);
       }

       @Override
       @Transactional(rollbackFor = Exception.class)
       public void deletes( IdsDto dto){
            removeByIds(Arrays.asList(dto.getIds()));
           //删除原本的值
           LambdaUpdateWrapper<TypeAttrVal> wrapper=new LambdaUpdateWrapper<TypeAttrVal>();
           wrapper.in(TypeAttrVal::getInfoId,Arrays.asList(dto.getIds()));
           typeAttrValService.remove(wrapper);
       }

    @Override
    public List<TypeAttributesAddDto> findTypeAttr(IdDto dto) throws Exception {
        String bigType=getBigType(dto.getId());
        LambdaQueryWrapper<TypeAttributes> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(TypeAttributes::getTypeId,bigType,InfoConstant.COMMON_ATTR_TYPE).orderByAsc(TypeAttributes::getOrderNum)
                .eq(TypeAttributes::getTenantsId,loginUtilsService.getLoginUserInfo().getTenantsId());
        List<TypeAttributes> typeAttributess=typeAttributesService.list(queryWrapper);
        return CommonUtils.copyProperties(typeAttributess,TypeAttributesAddDto.class);
    }
}