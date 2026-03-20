package com.juyou.shiro;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.juyou.common.base.service.BaseService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.LoginUserDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.error.ErrorCode;
import com.juyou.common.error.ErrorMsgUtil;
import com.juyou.common.jwt.JwtSecret;
import com.juyou.common.jwt.JwtUtil;
import com.juyou.common.log.Operation;
import com.juyou.common.utils.IPUtils;
import com.juyou.common.utils.LoginUtils;
import com.juyou.common.utils.SpringContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {


	@Autowired
	@Lazy
	BaseService baseService;

	@Autowired
	JwtSecret jwtSecret;

	@Autowired
	LoginUtils loginUtils;

	/**
	 * 必须重写此方法，不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	/**
	 * 处理超级管理员拥有全部权限
	 */
	@Override
	protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
		Collection<String> roles = info.getRoles();
		if (roles != null && roles.contains(CommonConstant.SYSTEM_ROLE_ADMIN))
			return true;
		//自己处理redis缓存
		if(roles==null || roles.isEmpty()) return false;
		return baseService.roleHasPermission(roles.iterator().next(),permission.toString());

		// TODO Auto-generated method stub
//		return super.isPermitted(permission, info);
	}



	/**
	 * 处理超级管理员拥有全部权限
	 */
	@Override
	protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
		// TODO Auto-generated method stub
		Collection<String> roles = info.getRoles();
		if (roles != null && roles.contains(CommonConstant.SYSTEM_ROLE_ADMIN))
			return true;
		return super.hasRole(roleIdentifier, info);
	}

	/**
	 * 设置用户权限，登录成功后在此设置
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		log.info("--------------设置权限-----------");
		LoginUserDto loginUser = null;
		if (principals != null) {
			loginUser = (LoginUserDto) principals.getPrimaryPrincipal();
			if (loginUser == null) {
//				log.info("-------------没有用户-----------");
				throwEx(ErrorCode.A9102);
			}
		}
//		log.info("权限用户:"+loginUser.getUserId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 客户端用户不设置权限
		if (loginUser.getUserType() != StatusEnum.USER_TYPE_客户端.getCode()) {
//			Integer userType = loginUser.getUserType();

			Set<String> roleSet = baseService.queryUserRole(loginUser.getUserId());
			info.setRoles(roleSet);
			//不在设置角色权限，自主处理缓存
//			Set<String> permissionsSet = baseService.queryUserPermission(loginUser.getUserId());
//			log.info("permissionsSet："+JSONObject.toJSONString(permissionsSet));
//			info.setStringPermissions(permissionsSet);
		}

		return info;
	}

	/**
	 * 开始认证身份
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		String token = (String) auth.getCredentials();
		if (token == null) {
			throwEx(ErrorCode.A9876);
		}
		Object loginUser = this.checkUserTokenIsEffect(token);
		return new SimpleAuthenticationInfo(loginUser, token, getName());
	}

	public LoginUserDto checkUserTokenIsEffect(String token) throws AuthenticationException {

		DecodedJWT jwt = JwtUtil.getJWT(token);
		String userId = JwtUtil.getUserId(jwt);
		Integer userType = JwtUtil.getUserType(jwt);
		Date ct = JwtUtil.getCt(jwt);
		Date et = JwtUtil.getExpiresAt(jwt);

		if (userId == null || userType == null || ct == null || et == null) {
			throwEx(ErrorCode.A9876);
		}
		long expireTime = et.getTime() - ct.getTime();

		String secret = jwtSecret.getSecrets(userId, userType, expireTime);
		if (StringUtils.isEmpty(secret)) {
			throwEx(ErrorCode.A9876);
		}
		// 校验token有效性
		if (!JwtUtil.verify(token, secret)) {
			throwEx(ErrorCode.A9876);
		}

		LoginUserDto loginUser = loginUtils.getLoginUser();
		//log.info("user:"+ JSONObject.toJSONString(loginUser));
		if (loginUser == null) {
			throw new AuthenticationException(ErrorCode.A9876);
		}
		// 判断是否在其它地方登录
		if (userType != StatusEnum.USER_TYPE_FEIGN.getCode()) {
			String userToken = loginUser.getToken();
			if (!token.equals(userToken)) {
				throwEx(ErrorCode.A9876, "用户在其它地方登录，登录IP:" + loginUser.getLoginIp());
			}
		}
		return loginUser;
	}

	private void throwEx(String code) {
		throwEx(code, null);
	}

	private void throwEx(String code, String msg) {
		HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
		try {
			operation(request);
		}catch (Exception e){}
		if (request != null) {
			request.setAttribute(ErrorMsgUtil.CODE, code);
			request.setAttribute(ErrorMsgUtil.MSG, msg);
		}
		throw new AuthenticationException(ErrorCode.A9876);
	}
	public void operation(HttpServletRequest request) throws IOException {
		Operation operation = new Operation();
		//获取request
		operation.setRequestParam(getReqestParams(request));
		//设置IP地址
		operation.setIp(IPUtils.getIpAddr(request));
		log.info("权限问题请求:"+JSONObject.toJSONString(operation));
	}

	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	private String getReqestParams(HttpServletRequest request) throws IOException {
		String httpMethod = request.getMethod();
		String params = "";
		if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
			BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				sb.append(inputStr);
			}
			params = sb.toString();
			//update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
		} else {
			params = request.getQueryString();

		}
		return params;
	}


}
