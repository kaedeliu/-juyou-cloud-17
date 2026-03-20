package com.juyou.admin.sys.service;

import com.juyou.admin.sys.dto.dictdetail.DictDetailAddDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailEditDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailMoveDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailQueryDto;
import com.juyou.admin.sys.entity.DictDetail;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;

import java.util.List;

/**
 * 字典详细服务接口
 * 
 * @author kaedeliu
 *
 */
public interface DictDetailService {

	/**
	 * 分页查询字典详细
	 * 
	 * @return 字典详细集合
	 */
	public List<DictDetail> list(DictDetailQueryDto dictDetailQueryDto) throws Exception;

	/**
	 * 查询字典详细详细
	 * 
	 * @param dto 字典详细ID
	 * @return 字典详细
	 */
	public DictDetail findById(IdDto dto);

	/**
	 * 添加字典详细
	 * 
	 * @param configAddDto 字典详细添加对象
	 */
	public void insert(DictDetailAddDto configAddDto);

	/**
	 * 修改字典详细
	 * 
	 * @param DictDetailEditDto 字典详细修改对象
	 */
	public void update(DictDetailEditDto configEditDto);

	/**
	 * 删除字典详细
	 * 
	 * @param dto 字典详细ID
	 */
	public void delete(IdDto dto);

	/**
	 * 批量删除字典详细
	 * 
	 * @param dto 字典详细ID集合
	 */
	public void deletes(IdsDto dtos);

	/**
	 * 字典项排序
	 * @param dictDetailMoveDto
	 */
	public void move(DictDetailMoveDto dictDetailMoveDto);
}
