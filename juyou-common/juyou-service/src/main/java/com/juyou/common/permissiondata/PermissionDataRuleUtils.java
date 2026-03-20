package com.juyou.common.permissiondata;

import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.utils.SpringContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



public class PermissionDataRuleUtils {

	public static final String MENU_DATA_RULE="MENU_DATA_RULE";
	
	public static final String LOGIN_USER="LOGIN_USER";
	
	
	/**
	 * 临时存储数据权限
	 * @param request
	 * @param dataRules
	 */
	public static void setMenuDataRules(HttpServletRequest request, List<PermissionDataRule> dataRules) {
		request.setAttribute(MENU_DATA_RULE, dataRules);
	}
	
	/**
	 * 读取数据权限
	 * @return
	 */
	public static List<PermissionDataRule> getMenuDataRules() {
		@SuppressWarnings("unchecked")
		List<PermissionDataRule> attribute = (List<PermissionDataRule>) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_RULE);
		return attribute;
	}
	
	/**
	 * 设置登录用户信息
	 * @param request
	 * @param dataRules
	 */
	public static void setLoginUser(HttpServletRequest request, LoginUserBasicDto loginUser) {
		request.setAttribute(LOGIN_USER, loginUser);
	}

	/**
	 * 读取登录用户信息
	 * @return
	 */
	public static LoginUserBasicDto getLoginUser() {
		LoginUserBasicDto loginUser = (LoginUserBasicDto) SpringContextUtils.getHttpServletRequest().getAttribute(LOGIN_USER);
		return loginUser;
	}
}
