package com.juyou.gen.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juyou.gen.dto.tableinfo.*;
import com.juyou.gen.entity.TableInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yx
 * @since 2023-04-08 17:05:31
 */
public interface TableInfoService extends IService<TableInfo>{

    /**
     * 分页获取表信息
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<DbTableDto> pageList(Page<DbTableDto> page, QueryWrapper<DbTableDto> queryWrapper);

    /**
     * 获取表信息
     * @param tableName
     * @return
     */
    List<TableInfoColumnDto> getByIdInfo(String tableName);

    /**
     * 创建模板并压缩为zip
     * @param createDto
     * @return
     */
    String createAndZip(CreateDto createDto) throws Exception;

    /**
     * 保存配置信息
     * @param tableInfoSaveDto
     * @return
     */
    void update(TableInfoSaveDto tableInfoSaveDto);

    /**
     * 查看生成文件
     * @param tableName
     * @return
     */
    List<ShowTableFilesDto> showTableFiles(String tableName) throws Exception;

    void down(String filePath, HttpServletResponse response);
}