package com.juyou.common.service;

import com.juyou.common.dto.LoginUserBasicDto;

/**
 * 基类，具体有项目实现
 */
public interface LoginUtilsService {

	/**
	 * 返回登录基础数据，具体项目里可以强转子类
	 * 
	 * @return
	 * @throws Exception
	 */
	LoginUserBasicDto getLoginUserInfo();
}
