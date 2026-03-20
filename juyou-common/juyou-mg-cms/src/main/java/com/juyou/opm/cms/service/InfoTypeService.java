package com.juyou.opm.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juyou.common.dto.IdDto;
import com.juyou.opm.cms.dto.infotype.*;
import com.juyou.opm.cms.entity.InfoType;

import java.util.List;

/**
 * <p>
 * 文章分类 服务类
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */
public interface InfoTypeService extends IService<InfoType>{


        List<InfoTypeTreeDto> treeList() throws Exception;

        InfoTypeDto findInfoTypeAttr(IdDto dto) throws Exception;

        void insertTypeAttr( InfoTypeAttrAddDto infoTypeAttrAddDto) throws Exception;


        void insert( InfoTypeAddDto infoTypeAddDto);

        List<InfoTypeTreeSelectDto> treeSelect() throws Exception;

        void update( InfoTypeEditDto infoTypeEditDto);
}