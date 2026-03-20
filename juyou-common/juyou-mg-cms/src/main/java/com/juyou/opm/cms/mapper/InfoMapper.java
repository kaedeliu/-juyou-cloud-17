package com.juyou.opm.cms.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.opm.cms.dto.info.InfoListDto;
import com.juyou.opm.cms.entity.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 文章-属性表 Mapper 接口
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */
@Mapper
public interface InfoMapper extends BaseMapper<Info> {



    @Select("SELECT\n" +
            "\t ${ew.sqlSelect} \n" +
            "FROM\n" +
            "\tjuyou_cms_info AS a\n" +
            "\tJOIN juyou_cms_info_type as big on a.big_type =big.type_id\n" +
            "\tjoin juyou_cms_info_type as type on a.type_id=type.type_id ${ew.customSqlSegment}")
    IPage<InfoListDto> pageList(Page<Info> page,@Param(Constants.WRAPPER) QueryWrapper<Info> wrapper);


}
