package com.juyou.shiro;


import com.juyou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于修改用户或者角色后清除缓存权限
 * @author kaedeliu
 *
 */
@Component
public class ShiroCacheManage {

	@Autowired
	ShiroRealm realm;
	
	@Autowired
	RedisUtil redisUtils;
	
	
	/**
	 * 清除系统数据权限缓存
	 */
//	public void clearAllSysData() {
//		redisUtils.del(CommonConstant.SYSTEM_DATA_RULE);
//	}
	
	/**
	 * 清除用户权限(用户权限变更)
	 * @param user
	 */
	public void clearUserCacheAuthorizing(String userId) {
        realm.getAuthorizationCache().remove(userId);
//        /**
//         * 清除用户数据权限
//         */
//        redisUtils.del(CommonConstant.USER_DATA_RULE+userId);
	}
	
	
	/**
	 * 清除所有用户权限,非授权模块请勿调用（角色权限编辑）
	 * @param clearDataRow 数据条件数目
	 */
	public void clearAllCacheAuthorizing(int clearDataRow) {
		boolean clearDataRule=clearDataRow>0;
		clearAllCacheAuthorizing(clearDataRule);
	}
	
	/**
	 * 清除所有用户权限,非授权模块请勿调用（角色权限编辑）
	 * @param clearDataRule 是否清除数据权限
	 */
	public void clearAllCacheAuthorizing(boolean clearDataRule) {
		realm.getAuthorizationCache().clear();
		
//		/**
//		 * 清空用户数据权限
//		 */
//		if(clearDataRule)
//			clearUserAllDataRule();
	}
	
//	public void clearUserAllDataRule() {
//		redisUtils.delByPrex(CommonConstant.USER_DATA_RULE+"*");
//	}
}
