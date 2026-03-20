package com.juyou.admin.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.dict.DictAddDto;
import com.juyou.admin.sys.dto.dict.DictEditDto;
import com.juyou.admin.sys.dto.dict.DictListDto;
import com.juyou.admin.sys.dto.dict.DictPageDto;
import com.juyou.admin.sys.dto.dict.DictVersionDto;
import com.juyou.admin.sys.entity.Dict;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;

/**
 * 字典服务接口
 * 
 * @author kaedeliu
 *
 */
public interface DictService {

	/**
	 * 分页查询系统字典
	 * 
	 * @return 系统字典集合
	 */
	public IPage<Dict> pageList(DictPageDto dictPage) throws Exception;

	/**
	 * 查询系统字典详细
	 * 
	 * @param dto 系统字典ID
	 * @return 系统字典
	 */
	public Dict findById(IdDto dto);

	/**
	 * 添加系统字典
	 * 
	 * @param dictAddDto 系统字典添加对象
	 */
	public void insert(DictAddDto dictAddDto);

	/**
	 * 修改系统字典
	 * 
	 * @param DictEditDto 系统字典修改对象
	 */
	public void update(DictEditDto dictEditDto);

	/**
	 * 删除系统字典
	 * 
	 * @param dto 系统字典ID
	 */
	public void delete(IdDto dto);

	/**
	 * 批量删除系统字典
	 * 
	 * @param dto 系统字典ID集合
	 */
	public void deletes(IdsDto dtos);

	/**
	 * 查询所有系统字典
	 * 
	 * @return 系统字典集合
	 */
	public DictVersionDto list(DictListDto dictListDto) throws Exception;

	/**
	 * 更新缓存
	 */
	void updateConfigAndCacheDict();

	/**
	 * 更新缓存
	 * @param tenantsId
	 */
	void updateConfigAndCacheDict(String tenantsId);
}
