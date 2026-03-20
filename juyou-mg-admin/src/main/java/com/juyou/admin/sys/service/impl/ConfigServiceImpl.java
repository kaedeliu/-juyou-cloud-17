package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.admin.sys.constant.Status;
import com.juyou.admin.sys.dto.config.ConfigAddDto;
import com.juyou.admin.sys.dto.config.ConfigEditDto;
import com.juyou.admin.sys.dto.config.ConfigPageDto;
import com.juyou.admin.sys.entity.Config;
import com.juyou.admin.sys.mapper.ConfigMapper;
import com.juyou.admin.sys.service.ConfigService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.exception.BaseException;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

	@Resource
	private ConfigMapper configMapper;

	@Resource
	private LoginUtilsService loginUtilsService;

	@Resource
	RedisUtil redisUtil;

	@Override
	public IPage<Config> pageList(ConfigPageDto configPage) throws Exception {
		Page<Config> page = new Page<>(configPage.getPageNo(), configPage.getPageSize());
		QueryWrapper<Config> queryWrapper= QueryGenerator.initQueryWrapper(configPage,Config.class);
//		LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<Config>();
//		if (!StringUtil.isBlank(configPage.getName())) {
//			queryWrapper.like(Config::getName, configPage.getName());
//		}
//		if (!StringUtil.isBlank(configPage.getCode())) {
//			queryWrapper.like(Config::getCode, configPage.getCode());
//		}
//		queryWrapper.orderByDesc(Config::getSequence);
		if(loginUtilsService.getLoginUserInfo().getUserType()!= StatusEnum.USER_TYPE_系统超管.getCode()){
			queryWrapper.eq("sys_type",StatusEnum.系统类型_默认.getCode());
		}

		Page<Config> configs = this.configMapper.selectPage(page, queryWrapper);
		return configs;
	}

	@Override
	public Config findById(IdDto dto) {
		return this.configMapper.selectById(dto.getId());
	}

	@Override
	public void insert(ConfigAddDto configAddDto) {
		Config config = new Config();

		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
		// 验证编码名称否重复
		LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<Config>();
		queryWrapper.eq(Config::getCode, configAddDto.getCode()).eq(Config::getTenantsId, loginUserDto.getTenantsId()); // 获取机构所属编码
		Long count = this.configMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("编码[" + configAddDto.getCode() + "]重复");
		}
		queryWrapper.clear();
		queryWrapper.eq(Config::getName, configAddDto.getName()).eq(Config::getTenantsId, loginUserDto.getTenantsId());
		count = this.configMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + configAddDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(configAddDto, config);
		if (null == config.getStatus()) {
			config.setStatus(Status.禁用.getId()); // 默认禁用
		}
		if (null == config.getSequence()) {
			// 自动获取下一个排序号
			queryWrapper.clear();
			queryWrapper.orderByDesc(Config::getSequence);
			queryWrapper.last(StringUtil.LIMIT_1);
			Config conf = this.configMapper.selectOne(queryWrapper);
			if (null == conf) {
				config.setSequence(1);
			} else {
				config.setSequence(null == conf.getSequence() ? 1 : conf.getSequence() + 1);
			}
		}
		this.configMapper.insert(config);
	}

	@Override
	public void update(ConfigEditDto configEditDto) {
		Config config = new Config();

		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
		// 验证编码名称否重复
		LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<Config>();
		queryWrapper.eq(Config::getCode, configEditDto.getCode()).ne(Config::getConfigId, configEditDto.getConfigId()).eq(Config::getTenantsId, loginUserDto.getTenantsId());
		Long count = this.configMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("编码[" + configEditDto.getCode() + "]重复");
		}
		queryWrapper.clear();
		queryWrapper.eq(Config::getName, configEditDto.getName()).ne(Config::getConfigId, configEditDto.getConfigId()).eq(Config::getTenantsId, loginUserDto.getTenantsId());
		count = this.configMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + configEditDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(configEditDto, config);
		this.configMapper.updateById(config);
		// 清理缓存，走redis工具，不走配置
		redisUtil.del(CommonConstant.SYS_CACHE_CONFIG +CommonConstant.REDIS_BUFF +  loginUtilsService.getLoginUserInfo().getTenantsId() +CommonConstant.REDIS_BUFF + configEditDto.getCode());
	}

	@Override
	public void delete(IdDto dto) {
		Config config = this.findById(dto);
		this.configMapper.deleteById(dto.getId());
		// 清理缓存，走redis工具，不走配置
		if (null != config) {
			redisUtil.del(CommonConstant.SYS_CACHE_CONFIG  +CommonConstant.REDIS_BUFF +  loginUtilsService.getLoginUserInfo().getTenantsId() +CommonConstant.REDIS_BUFF  + config.getCode());
		}
	}

	@Override
	public void deletes(IdsDto dtos) {
		String[] ids = dtos.getIds();
		this.configMapper.deleteBatchIds(Arrays.asList(dtos.getIds()));
		for (String id : ids) {
			Config config = this.configMapper.selectById(id);
			// 清理缓存，走redis工具，不走配置
			if (null != config) {
				redisUtil.del(CommonConstant.SYS_CACHE_CONFIG +CommonConstant.REDIS_BUFF +  loginUtilsService.getLoginUserInfo().getTenantsId() +CommonConstant.REDIS_BUFF  + config.getCode());
			}
		}
	}

	@Override
	public void update(Wrapper<Config> wrapper) {
		this.configMapper.update(null, wrapper);
	}
}