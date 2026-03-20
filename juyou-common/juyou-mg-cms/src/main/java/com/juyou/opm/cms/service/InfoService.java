package com.juyou.opm.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.opm.cms.dto.info.*;
import com.juyou.opm.cms.dto.typeattributes.TypeAttributesAddDto;
import com.juyou.opm.cms.entity.Info;

import java.util.List;

/**
 * <p>
 * 文章-属性表 服务类
 * </p>
 *
 * @author yx
 * @since 2023-04-24 18:57:06
 */
public interface InfoService extends IService<Info>{

    /**
    ** 获取分页对象
    **/
    IPage<InfoListDto> pageList(InfoPageDto infoPageDto) throws  Exception;


    /**
    ** 插入对象
    **/
    void insert( InfoAddDto infoAddDto);

    InfoDto findById(IdDto idDto) throws Exception;

    /**
    ** 更新对象
    **/
    void update(InfoEditDto infoEditDto);

    /**
    ** 删除对象
    **/
    void delete( IdDto dto);

    /**
    ** 批量删除对象
    **/
    void deletes( IdsDto dto);

    /**
     * 获取分类属性值
     * @param dto
     * @return
     */
    List<TypeAttributesAddDto> findTypeAttr(IdDto dto) throws Exception;
}