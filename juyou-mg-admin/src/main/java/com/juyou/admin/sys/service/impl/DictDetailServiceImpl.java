package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juyou.admin.sys.constant.Status;
import com.juyou.admin.sys.dto.dictdetail.DictDetailAddDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailEditDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailMoveDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailQueryDto;
import com.juyou.admin.sys.entity.DictDetail;
import com.juyou.admin.sys.mapper.DictDetailMapper;
import com.juyou.admin.sys.service.DictDetailService;
import com.juyou.admin.sys.service.DictService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.exception.BaseException;
import com.juyou.common.query.QueryGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service("dictDetailService")
public class DictDetailServiceImpl implements DictDetailService {

	@Resource
	private DictDetailMapper dictDetailMapper;

	@Resource
	@Lazy
	private DictService dictService;

	@Override
	public List<DictDetail> list(DictDetailQueryDto dictDetailQueryDto) throws Exception {
		QueryWrapper<DictDetail> queryWrapper= QueryGenerator.initQueryWrapper(dictDetailQueryDto,DictDetail.class);
		return this.dictDetailMapper.selectList(queryWrapper);
	}

	@Override
	public DictDetail findById(IdDto dto) {
		return this.dictDetailMapper.selectById(dto.getId());
	}

	@Override
	public void insert(DictDetailAddDto dictDetailAddDto) {
		DictDetail dictDetail = new DictDetail();
		BeanUtils.copyProperties(dictDetailAddDto, dictDetail);
		if (null == dictDetail.getStatus()) {
			dictDetail.setStatus(Status.禁用.getId()); // 默认禁用
		}
		if (null == dictDetail.getSequence()) {
			// 自动获取下一个排序号
			LambdaQueryWrapper<DictDetail> queryWrapper = new LambdaQueryWrapper<DictDetail>();
			queryWrapper.eq(DictDetail::getDictId, dictDetail.getDictId());
			queryWrapper.orderByDesc(DictDetail::getSequence);
			queryWrapper.last(StringUtil.LIMIT_1);
			DictDetail conf = this.dictDetailMapper.selectOne(queryWrapper);
			if (null == conf) {
				dictDetail.setSequence(1);
			} else {
				dictDetail.setSequence(null == conf.getSequence() ? 1 : conf.getSequence() + 1);
			}
		}
		this.dictDetailMapper.insert(dictDetail);
		dictService.updateConfigAndCacheDict();
	}

	@Override
	public void update(DictDetailEditDto dictDetailEditDto) {
		DictDetail dictDetail = new DictDetail();
		BeanUtils.copyProperties(dictDetailEditDto, dictDetail);
		this.dictDetailMapper.updateById(dictDetail);
		dictService.updateConfigAndCacheDict();
	}

	@Override
	public void delete(IdDto dto) {
		this.dictDetailMapper.deleteById(dto.getId());
		dictService.updateConfigAndCacheDict();
	}

	@Override
	public void deletes(IdsDto dtos) {
		this.dictDetailMapper.deleteBatchIds(Arrays.asList(dtos.getIds()));
		dictService.updateConfigAndCacheDict();
	}

	@Override
	public void move(DictDetailMoveDto dictDetailMoveDto) {
		DictDetail dictDetail=dictDetailMapper.selectById(dictDetailMoveDto.getDictDetailId());
		if(dictDetail==null)
			throw BaseException.defaultCode("数据不存在或者已删除");
		if(dictDetailMoveDto.isUp()){//上移
			//找到上一个选项，中间有删除，可能不是上一个序号
			LambdaQueryWrapper<DictDetail> queryWrapper=new LambdaQueryWrapper<>();
			queryWrapper.lt(DictDetail::getSequence,dictDetail.getSequence()).last("limit 1").orderByDesc(DictDetail::getSequence);
			DictDetail last=dictDetailMapper.selectOne(queryWrapper);
			if(last==null){
				throw BaseException.defaultCode("已经是最顶级了，不能上移");
			}
			last.setSequence(last.getSequence()+1);
			dictDetailMapper.updateById(last);
			dictDetail.setSequence(last.getSequence()-1);
			dictDetailMapper.updateById(dictDetail);
		}else{ //下移
			LambdaQueryWrapper<DictDetail> queryWrapper=new LambdaQueryWrapper<>();
			queryWrapper.gt(DictDetail::getSequence,dictDetail.getSequence()).last("limit 1").orderByAsc(DictDetail::getSequence);
			DictDetail next=dictDetailMapper.selectOne(queryWrapper);
			if(next==null){
				throw BaseException.defaultCode("已经是底部了，不能下移");
			}
			next.setSequence(next.getSequence()-1);
			dictDetailMapper.updateById(next);
			dictDetail.setSequence(next.getSequence()+1);
			dictDetailMapper.updateById(dictDetail);
		}
		dictService.updateConfigAndCacheDict();
	}
}