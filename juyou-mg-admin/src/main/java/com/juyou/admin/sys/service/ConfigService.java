package com.juyou.admin.sys.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.config.ConfigAddDto;
import com.juyou.admin.sys.dto.config.ConfigEditDto;
import com.juyou.admin.sys.dto.config.ConfigPageDto;
import com.juyou.admin.sys.entity.Config;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;

/**
 * 系统配置 服务接口
 * 
 * @author kaedeliu
 *
 */
public interface ConfigService {

	/**
	 * 分页查询系统参数
	 * 
	 * @return 系统参数集合
	 */
	public IPage<Config> pageList(ConfigPageDto configPage) throws Exception;

	/**
	 * 查询系统参数详细
	 * 
	 * @param dto 系统参数ID
	 * @return 系统参数
	 */
	public Config findById(IdDto dto);

	/**
	 * 添加系统参数
	 * 
	 * @param configAddDto 系统参数添加对象
	 */
	public void insert(ConfigAddDto configAddDto);

	/**
	 * 修改系统参数
	 * 
	 * @param ConfigEditDto 系统参数修改对象
	 */
	public void update(ConfigEditDto configEditDto);

	/**
	 * 删除系统参数
	 * 
	 * @param dto 系统参数ID
	 */
	public void delete(IdDto dto);

	/**
	 * 批量删除系统参数
	 * 
	 * @param dto 系统参数ID集合
	 */
	public void deletes(IdsDto dtos);

	public void update(Wrapper<Config> wrapper);
}