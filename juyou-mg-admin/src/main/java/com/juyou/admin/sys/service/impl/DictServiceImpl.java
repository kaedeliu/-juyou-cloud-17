package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.admin.sys.constant.Status;
import com.juyou.admin.sys.dto.dict.DictAddDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailDto;
import com.juyou.admin.sys.entity.Config;
import com.juyou.admin.sys.entity.Dict;
import com.juyou.admin.sys.entity.DictDetail;
import com.juyou.admin.sys.mapper.DictDetailMapper;
import com.juyou.admin.sys.mapper.DictMapper;
import com.juyou.admin.sys.service.ConfigService;
import com.juyou.admin.sys.service.DictService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.exception.BaseException;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.utils.ConfigUtils;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("dictService")
public class DictServiceImpl implements DictService {

	@Resource
	private DictMapper dictMapper;

	@Resource
	private DictDetailMapper dictDetailMapper;

	@Resource
	private LoginUtilsService loginUtilsService;

	@Resource
	private ConfigService configService;

	@Resource
	private ConfigUtils configUtils;

	@Resource
	private RedisUtil redisUtil;

	@Override
	public IPage<Dict> pageList(com.juyou.admin.sys.dto.dict.DictPageDto dictPage) throws Exception {
		Page<Dict> page = new Page<>(dictPage.getPageNo(), dictPage.getPageSize());
		QueryWrapper<Dict> queryWrapper= QueryGenerator.initQueryWrapper(dictPage,Dict.class);
//		LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<Dict>();
//		if (!StringUtil.isBlank(dictPage.getName())) {
//			queryWrapper.like(Dict::getName, dictPage.getName());
//		}
//		if (!StringUtil.isBlank(dictPage.getCode())) {
//			queryWrapper.like(Dict::getCode, dictPage.getCode());
//		}
//		queryWrapper.orderByDesc(Dict::getSequence);
		Page<Dict> dicts = this.dictMapper.selectPage(page, queryWrapper);
		return dicts;
	}

	@Override
	public Dict findById(IdDto dto) {
		return this.dictMapper.selectById(dto.getId());
	}

	@Override
	public void insert(DictAddDto dictAddDto) {
		Dict dict = new Dict();

		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
		// 验证编码名称否重复
		LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<Dict>();
//		queryWrapper.eq(Dict::getCode, dictAddDto.getCode()).eq(Dict::getTenantsId, loginUserDto.getTenantsId()); // 获取机构所属编码
//		Long count = this.dictMapper.selectCount(queryWrapper);
//		if (count > 0) {
//			throw BaseException.defaultCode("编码[" + dictAddDto.getCode() + "]重复");
//		}
		queryWrapper.clear();
		queryWrapper.eq(Dict::getName, dictAddDto.getName()).eq(Dict::getTenantsId, loginUserDto.getTenantsId());
		Long count = this.dictMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + dictAddDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(dictAddDto, dict);
		if (null == dict.getStatus()) {
			dict.setStatus(Status.禁用.getId()); // 默认禁用
		}
		if (null == dict.getSequence()) {
			// 自动获取下一个排序号
			queryWrapper.clear();
			queryWrapper.orderByDesc(Dict::getSequence);
			queryWrapper.last(StringUtil.LIMIT_1);
			Dict conf = this.dictMapper.selectOne(queryWrapper);
			if (null == conf) {
				dict.setSequence(1);
			} else {
				dict.setSequence(null == conf.getSequence() ? 1 : conf.getSequence() + 1);
			}
		}
		this.dictMapper.insert(dict);

		updateConfigAndCacheDict();
	}

	@Override
	public void update(com.juyou.admin.sys.dto.dict.DictEditDto dictEditDto) {
		Dict dict = new Dict();

		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
		// 验证编码名称否重复
		LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<Dict>();
//		queryWrapper.eq(Dict::getCode, dictEditDto.getCode()).ne(Dict::getDictId, dictEditDto.getDictId()).eq(Dict::getTenantsId, loginUserDto.getMacId());
//		Long count = this.dictMapper.selectCount(queryWrapper);
//		if (count > 0) {
//			throw BaseException.defaultCode("编码[" + dictEditDto.getCode() + "]重复");
//		}
		queryWrapper.clear();
		queryWrapper.eq(Dict::getName, dictEditDto.getName()).ne(Dict::getDictId, dictEditDto.getDictId()).eq(Dict::getTenantsId, loginUserDto.getMacId());
		Long count = this.dictMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + dictEditDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(dictEditDto, dict);
		this.dictMapper.updateById(dict);

		updateConfigAndCacheDict();
	}

	@Override
	@Transactional
	public void delete(IdDto dto) {
		// 删除字典明细
		LambdaQueryWrapper<DictDetail> queryWrapper = new LambdaQueryWrapper<DictDetail>();
		queryWrapper.eq(DictDetail::getDictId, dto.getId());
		this.dictDetailMapper.delete(queryWrapper);

		this.dictMapper.deleteById(dto.getId());

		updateConfigAndCacheDict();
	}

	@Override
	@Transactional
	public void deletes(IdsDto dtos) {
		// 关联删除明细
		LambdaQueryWrapper<DictDetail> queryWrapper = new LambdaQueryWrapper<DictDetail>();
		queryWrapper.in(DictDetail::getDictId, Arrays.asList(dtos.getIds()));
		this.dictDetailMapper.delete(queryWrapper);
		this.dictMapper.deleteBatchIds(Arrays.asList(dtos.getIds()));

		updateConfigAndCacheDict();

	}

	@Override
	public com.juyou.admin.sys.dto.dict.DictVersionDto list(com.juyou.admin.sys.dto.dict.DictListDto dictListDto) throws Exception {
		if (dictListDto.getVersion().equals(findDictVer(loginUtilsService.getLoginUserInfo().getTenantsId()))) {
			// 版本相同直接返回
			return new com.juyou.admin.sys.dto.dict.DictVersionDto().setVersion(dictListDto.getVersion());
		}
		return findDict();
	}

	/**
	 * 从缓存或者数据库查找字典配置
	 * 
	 * @return
	 * @throws Exception
	 */
	private com.juyou.admin.sys.dto.dict.DictVersionDto findDict() {
		String redisKey=CommonConstant.SYS_CACHE_DICT+CommonConstant.REDIS_BUFF+loginUtilsService.getLoginUserInfo().getTenantsId();
		com.juyou.admin.sys.dto.dict.DictVersionDto dictVersionDto = (com.juyou.admin.sys.dto.dict.DictVersionDto) redisUtil.get(redisKey);
		if (dictVersionDto != null)
			return dictVersionDto;
		else {
			dictVersionDto = new com.juyou.admin.sys.dto.dict.DictVersionDto();
			List<com.juyou.admin.sys.dto.dict.DictDto> dictDtos = new ArrayList<>();
			LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
			LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<Dict>();
			queryWrapper.eq(Dict::getTenantsId, loginUserDto.getTenantsId()).eq(Dict::getStatus, Status.启用.getId()).orderByDesc(Dict::getSequence);
			List<Dict> dicts = this.dictMapper.selectList(queryWrapper);

			if (null != dicts && dicts.size() > 0) {
				LambdaQueryWrapper<DictDetail> queryDictDetailWrapper = new LambdaQueryWrapper<DictDetail>();
				queryDictDetailWrapper.in(DictDetail::getDictId, dicts.stream().map(Dict::getDictId).collect(Collectors.toList()));
				queryDictDetailWrapper.eq(DictDetail::getStatus, Status.启用.getId()).orderByDesc(DictDetail::getSequence);
				List<DictDetail> dictDetails = this.dictDetailMapper.selectList(queryDictDetailWrapper);

				for (Dict dict : dicts) {
					com.juyou.admin.sys.dto.dict.DictDto dictDto = new com.juyou.admin.sys.dto.dict.DictDto();
					BeanUtils.copyProperties(dict, dictDto);
					List<DictDetailDto> dictDetailDtos = new ArrayList<>();
					for (DictDetail dictDetail : dictDetails) {
						if (dict.getDictId().equals(dictDetail.getDictId())) {
							DictDetailDto dictDetailDto = new DictDetailDto();
							BeanUtils.copyProperties(dictDetail, dictDetailDto);
							dictDetailDtos.add(dictDetailDto);
						}
					}
					dictDto.setDictDetailDtos(dictDetailDtos);
					dictDtos.add(dictDto);
				}
			}
			String ver = findDictVer(loginUtilsService.getLoginUserInfo().getTenantsId());
			dictVersionDto.setVersion(ver);
			dictVersionDto.setDictDtos(dictDtos);
			redisUtil.set(redisKey, dictVersionDto);
			return dictVersionDto;
		}
	}

	public  void updateConfigAndCacheDict(){
		updateConfigAndCacheDict(loginUtilsService.getLoginUserInfo().getTenantsId());
	}
	/**
	 * 更新数据库及缓存的config值
	 */
	public synchronized void updateConfigAndCacheDict(String tenantsId) {
		String version = findDictVer(tenantsId);
		LambdaUpdateWrapper<Config> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.set(Config::getValue, Integer.parseInt(version) + 1).eq(Config::getCode, CommonConstant.SYS_CACHE_DICT_CONFIG)
				.eq(Config::getTenantsId,tenantsId);

		configService.update(updateWrapper);
		deleteRedisCache(tenantsId);
	}

	/**
	 * 读取字典版本
	 */
	private String findDictVer(String tenantsId) {
		return configUtils.findVal(CommonConstant.SYS_CACHE_DICT_CONFIG, "0",tenantsId);
	}

	/**
	 * 清除redis的缓存
	 */
	private void deleteRedisCache(String tenantsId) {
		redisUtil.del(CommonConstant.SYS_CACHE_CONFIG + CommonConstant.REDIS_BUFF + tenantsId+CommonConstant.REDIS_BUFF+ CommonConstant.SYS_CACHE_DICT_CONFIG);
		redisUtil.del(CommonConstant.SYS_CACHE_DICT+CommonConstant.REDIS_BUFF+tenantsId);
	}
}