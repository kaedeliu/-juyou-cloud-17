package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juyou.admin.sys.constant.MacType;
import com.juyou.admin.sys.constant.Status;
import com.juyou.admin.sys.dto.mac.MacAddDto;
import com.juyou.admin.sys.dto.mac.MacEditDto;
import com.juyou.admin.sys.dto.mac.MacTreeDto;
import com.juyou.admin.sys.entity.Mac;
import com.juyou.admin.sys.mapper.MacMapper;
import com.juyou.admin.sys.service.MacService;
import com.juyou.admin.sys.service.TenantsService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.exception.BaseException;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.TreeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("macService")
public class MacServiceImpl implements MacService {

	@Resource
	private MacMapper macMapper;

	@Resource
	LoginUtilsService loginUtilsService;

	@Resource
	TenantsService tenantsService;
	
	@Override
	public List<Mac> list() throws Exception {
		QueryWrapper<Mac> queryWrapper = QueryGenerator.initQueryWrapper(new Mac(),Mac.class);
		List<Mac> menus = this.macMapper.selectList(queryWrapper);
		menus = packagTree(menus, StringUtil.TREE_ROOT_ID);
		return menus;
	}

	@Override
	public Mac findById(IdDto dto) {
		return this.macMapper.selectById(dto.getId());
	}

	@Override
	public void insert(MacAddDto macAddDto) throws IllegalAccessException {

		Mac mac = new Mac();
		// 验证编码名称否重复
		LambdaQueryWrapper<Mac> queryWrapper = new LambdaQueryWrapper<Mac>();
//		queryWrapper.eq(Mac::getCode, macAddDto.getCode());
//		Long count = this.macMapper.selectCount(queryWrapper);
//		if (count > 0) {
//			throw BaseException.defaultCode("编码[" + macAddDto.getCode() + "]重复");
//		}
		queryWrapper.clear();
		queryWrapper.eq(Mac::getName, macAddDto.getName());
		Long count = this.macMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + macAddDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(macAddDto, mac);
		if (null == mac.getStatus()) {
			mac.setStatus(Status.禁用.getId()); // 默认禁用
		}
		if (null == mac.getType()) {
			mac.setType(MacType.机构.getId()); // 默认一级菜单
		}
//		if (StringUtil.isBlank(mac.getParentId())) {
//			throw BaseException.defaultCode("父ID不能为空");
//		}
		if (null == mac.getSequence()) {
			// 自动获取下一个排序号
			queryWrapper.clear();
			queryWrapper.eq(Mac::getParentId, mac.getParentId());
			queryWrapper.orderByDesc(Mac::getSequence);
			queryWrapper.last(StringUtil.LIMIT_1);
			Mac mnu = this.macMapper.selectOne(queryWrapper);
			if (null == mnu) {
				mac.setSequence(1);
			} else {
				mac.setSequence(null == mnu.getSequence() ? 1 : mnu.getSequence() + 1);
			}
		}
		this.macMapper.insert(mac);
	}

	@Override
	public void update(MacEditDto macEditDto) {
		Mac mac = new Mac();
		// 严重编码名称否重复
		LambdaQueryWrapper<Mac> queryWrapper = new LambdaQueryWrapper<Mac>();
//		queryWrapper.eq(Mac::getCode, macEditDto.getCode()).ne(Mac::getMacId, macEditDto.getMacId());
//		Long count = this.macMapper.selectCount(queryWrapper);
//		if (count > 0) {
//			throw BaseException.defaultCode("编码[" + macEditDto.getMacId() + "]重复");
//		}
		queryWrapper.clear();
		queryWrapper.eq(Mac::getName, macEditDto.getName()).ne(Mac::getMacId, macEditDto.getMacId());
		Long count = this.macMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + macEditDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(macEditDto, mac);
		this.macMapper.updateById(mac);
	}

	@Override
	public void delete(IdDto dto) {
		this.macMapper.deleteById(dto.getId());
	}

	@Override
	public void deletes(IdsDto dtos) {
		this.macMapper.deleteBatchIds(Arrays.asList(dtos.getIds()));
	}

	@Override
	public List<MacTreeDto> findTree() throws Exception {
		QueryWrapper<Mac> queryWrapper= QueryGenerator.initQueryWrapper(new Mac(),Mac.class);
		queryWrapper.orderByAsc("sequence").eq("status", StatusEnum.是.getCode());
		queryWrapper.select("mac_id","name","parent_id");
		List<Mac> macs=this.macMapper.selectList(queryWrapper);
		List<MacTreeDto> treeDtos=TreeUtils.formatTree(macs,"0","macId",MacTreeDto.class,Mac.class);
		return treeDtos;
	}

	/**
	 * 树形结构封装
	 * 
	 * @param data         查询结果集
	 * @param rootParentId 根节点父ID
	 * @return 树形结构结果集
	 */
	private List<Mac> packagTree(List<Mac> data, String rootParentId) {
		List<Mac> treeDtos = new ArrayList<Mac>();
		treeDtos = analyzeTree(data, rootParentId);
		return treeDtos;
	}

	/**
	 * 组装树
	 * 
	 * @param data     查询结果集
	 * @param parentId 根节点父ID
	 * @return 树形结构结果集
	 */
	private List<Mac> analyzeTree(List<Mac> data, String parentId) {
		List<Mac> treeDtos = new ArrayList<Mac>();
		List<Mac> temp = new ArrayList<Mac>(data);
		if (data != null && data.size() > 0) {
			for (Mac mac : data) {
				if (parentId.equals(mac.getParentId())) {
					Mac mnu = new Mac();
					BeanUtils.copyProperties(mac, mnu);
					temp.remove(mac);
					mnu.setChildren(analyzeTree(temp, mac.getMacId()));
					treeDtos.add(mnu);
				}
			}
		}
		return treeDtos;
	}
}
