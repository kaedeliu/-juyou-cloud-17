package com.juyou.gen.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.gen.dto.tableinfo.DbTableDto;
import com.juyou.gen.dto.tableinfo.TableInfoColumnDto;
import com.juyou.gen.entity.TableInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yx
 * @since 2023-04-08 17:05:31
 */
@Mapper
public interface TableInfoMapper extends BaseMapper<TableInfo> {

    @Select("SELECT  ${ew.sqlSelect} FROM information_schema.tables ${ew.customSqlSegment} ")
    IPage<DbTableDto> pageList(Page<DbTableDto> page,@Param(Constants.WRAPPER) QueryWrapper<DbTableDto> queryWrapper);

    @Select("SELECT  ${ew.sqlSelect} FROM information_schema.COLUMNS ${ew.customSqlSegment} ")
    List<TableInfoColumnDto> dbTableInfo(@Param(Constants.WRAPPER) QueryWrapper<TableInfoColumnDto> queryWrapper);
}
