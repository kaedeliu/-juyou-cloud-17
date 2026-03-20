package com.juyou.common.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juyou.common.base.entity.BaseEntity;
import com.juyou.common.base.mapper.BaseMapper;
import com.juyou.common.base.service.BaseService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.log.Operation;
import com.juyou.common.permissiondata.PermissionDataRule;
import com.juyou.common.task.CommonTask;
import com.juyou.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
@Slf4j
public class BaseServiceImpl extends ServiceImpl<BaseMapper, BaseEntity> implements BaseService {

	@Autowired
	BaseMapper baseMapper;

	/**服务器与数据库服务器的时间差**/
	static Long dbtime= null;

	@Autowired
	RedisUtil redisUtil;


	/**
	 * 获取服务器时间
	 * @return
	 */
	public Date getDbTime(){
		if(dbtime==null){
			Date date = baseMapper.getDbDate();
			dbtime = System.currentTimeMillis() - date.getTime();
		}
		return new Date(System.currentTimeMillis()-dbtime);
	}

	/**
	 * 获取所有数据权限
	 * @return
	 */
	@Cacheable(cacheNames = CommonConstant.SYS_CACHE_PERMISSION,key="'ALL'", unless="#result == null")
	public Map<String,List<PermissionDataRule>> getAllPermissionData(){
		List<PermissionDataRule>  permissionDataRules = baseMapper.queryAllPermissionData();
		Map<String,List<PermissionDataRule>> map=new HashMap<String, List<PermissionDataRule>>();
		if(permissionDataRules!=null) {
			for (PermissionDataRule permissionDataRule : permissionDataRules) {
				if(!StringUtils.hasLength(permissionDataRule.getPerms())) continue;
				if(map.containsKey(permissionDataRule.getPerms())) {
					map.get(permissionDataRule.getPerms()).add(permissionDataRule);
				}else{
					List<PermissionDataRule> temps=new ArrayList<PermissionDataRule>();
					temps.add(permissionDataRule);
					map.put(permissionDataRule.getPerms(), temps);
				}
			}
		}
		return map;
	}
	
	@Override
	public Set<String> queryUserRole(String id) {
		// TODO Auto-generated method stub
		return baseMapper.queryUserRole(id);
	}

//	@Override
//	public Set<String> queryUserPermission(String id) {
//		// TODO Auto-generated method stub
//		log.info("---开始查用户权限:"+id);
//		List<Permission> ps= baseMapper.queryUserPermission(id);
//		log.info("---查询到："+ps.size());
//		Set<String> pss=new HashSet<String>();
//		for (Permission p : ps) {
//			if(p!=null && StringUtils.hasLength(p.getPerms()))
//				pss.add(p.getPerms());
//		}
//		return pss;
//	}

	@Override
	public boolean roleHasPermission(String roleCode, String permission) {
		String key=CommonConstant.SYS_CACHE_ROLE_PERMISSION+"::"+roleCode;
		if(redisUtil.hasKey(key)){
			log.info("ke:"+key+" "+" permission:"+permission);
			return redisUtil.getStringRedisTemplate().opsForSet().isMember(key,permission.toLowerCase());
		}else{
			Set<String> permissions=baseMapper.queryRolePermission(roleCode);
			if(permissions==null) permissions=new HashSet<>();
			//String[] ps=permissions.toArray(new String[permissions.size()]);
			Iterator<String> iterator = permissions.iterator();
			log.info("role1:"+roleCode+" "+" "+String.join(",",permissions));
			while (iterator.hasNext()) {
				String key1=iterator.next();
				if(StringUtils.hasLength(key1))
					redisUtil.getStringRedisTemplate().opsForSet().add(key, key1.toLowerCase());
			}
			return redisUtil.getStringRedisTemplate().opsForSet().isMember(key,permission.toLowerCase());
		}
	}

	public Set<String> getRolePermissions(String roleCode){
		String key=CommonConstant.SYS_CACHE_ROLE_PERMISSION+"::"+roleCode;
		Set<String> permissions=new HashSet<>();
		Set<Object> ps=null;
		if(redisUtil.hasKey(key)) {
			ps = redisUtil.getRedisTemplate().opsForSet().members(key);
		}else{
			ps= Collections.singleton(baseMapper.queryRolePermission(roleCode));
		}
		Iterator<Object> iterator = ps.iterator();
		while (iterator.hasNext()) {
			Object key1=iterator.next();
			if(key1!=null)
				permissions.add(key1+"");
		}
		return permissions;
	}

	@Override
	@Cacheable(cacheNames = CommonConstant.SYS_CACHE_PERMISSION,key="#userSeq", unless="#result == null")
	public Set<String> queryUserDataRule(String userSeq) {
		Set<String> set=baseMapper.queryUserDataRule(userSeq);
		Set<String> item=new HashSet<String>();
		if(set!=null && set.size()>0) {
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String string = (String) iterator.next();
				if(StringUtils.hasLength(string)) {
					String[] s=string.split(",");
					for (String string2 : s) {
						if(!item.contains(string2)) {
							item.add(string2);
						}
					}
				}
			}
		}
		return item;
	}

//	@Cacheable(cacheNames = CommonConstant.SYS_CACHE_LOGIN_USERS,key = "#userId", unless="#result == null")
//	@Override
//	public LoginUserDto queryLoginUserInfo(String userId) {
//		LoginUserDto loginUserDto = baseMapper.queryLoginUserInfo(userId);
//
//		return loginUserDto;
//	}
//	@Cacheable(cacheNames = CommonConstant.SYS_CACHE_LOGIN_USERS,key = "#userId", unless="#result == null")
//	@Override
//	public LoginUserDto queryCcLoginUserInfo(String userId) {
//		LoginUserDto loginUserDto = baseMapper.queryCcLoginUserInfo(userId);
//		return loginUserDto;
//	}

	@Override
	public void saveOperation(List<Operation> operations) {

		baseMapper.saveOperation(operations);
	}

	@Override
	public List<CommonTask> selectCommonTask(String type, Integer status) {
		// TODO Auto-generated method stub
		return baseMapper.selectCommonTask(type,status);
	}
	
//	@Override
//	public CommonTask selectCommonTaskById(String id) {
//		// TODO Auto-generated method stub
//		return baseMapper.selectCommonTaskById(id);
//	}
	


	@Transactional(propagation = Propagation.NEVER)
	public void updatePkNum(String pkType,int increasingVal) {
		long row=baseMapper.updatePkNum(pkType, increasingVal);
		if(row<1) {
			baseMapper.insertPk(pkType, increasingVal);
		}
	}

	@Override
	public Integer selectPkByType(String pkType) {
		// TODO Auto-generated method stub
		return baseMapper.selectPkByType(pkType);
	}

//	@Override
//	public void updateCommonTaskById(CommonTask commonTask) {
//		// TODO Auto-generated method stub
//		baseMapper.updateCommonTaskById(commonTask);
//	}


}
