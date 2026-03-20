package com.juyou.admin.sys.service;

import com.juyou.admin.sys.dto.mac.MacAddDto;
import com.juyou.admin.sys.dto.mac.MacEditDto;
import com.juyou.admin.sys.dto.mac.MacTreeDto;
import com.juyou.admin.sys.entity.Mac;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;

import java.util.List;

/**
 * 机构服务接口
 * 
 * @author kaedeliu
 *
 */
public interface MacService {

	/**
	 * 查询机构
	 * 
	 * @return 机构集合
	 */
	public List<Mac> list() throws Exception;

	/**
	 * 查询机构详细
	 * 
	 * @param dto 机构ID
	 * @return 机构
	 */
	public Mac findById(IdDto dto);

	/**
	 * 添加机构
	 * 
	 * @param macAddDto 机构添加对象
	 */
	public void insert(MacAddDto macAddDto) throws IllegalAccessException;

	/**
	 * 修改机构
	 * 
	 * @param macEditDto 机构修改对象
	 */
	public void update(MacEditDto macEditDto);

	/**
	 * 删除机构
	 * 
	 * @param dto 机构ID
	 */
	public void delete(IdDto dto);

	/**
	 * 批量删除机构
	 * 
	 * @param dto 机构ID集合
	 */
	public void deletes(IdsDto dtos);

	/**
	 * 查询机构树型菜单
	 * @return
	 */
	public List<MacTreeDto> findTree() throws Exception;


}
