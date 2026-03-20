package com.juyou.opm.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juyou.opm.cms.entity.TypeAttributes;
import com.juyou.opm.cms.mapper.TypeAttributesMapper;
import com.juyou.opm.cms.service.TypeAttributesService;

@Service
public class TypeAttributesServiceImpl extends ServiceImpl<TypeAttributesMapper, TypeAttributes> implements TypeAttributesService {

}