package com.juyou.common.utils;

import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.permissiondata.PermissionDataRuleUtils;
import com.juyou.common.service.LoginUtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认 LoginUtilsService 实现：
 * 从 PermissionDataRuleUtils 的上下文读取登录用户。
 * 具体项目可在业务侧通过 PermissionDataRuleUtils.setLoginUser(...) 写入。
 */
@Slf4j
@Component
public class LoginUtils implements LoginUtilsService {

    @Override
    public LoginUserBasicDto getLoginUserInfo() {
        Object user = PermissionDataRuleUtils.getLoginUser();
        if (user == null) {
            return null;
        }
        if (user instanceof LoginUserBasicDto) {
            return (LoginUserBasicDto) user;
        }
        // 兼容历史：上下文里若放入了 Map/JSON 等，避免直接抛错导致系统无法运行
        log.debug("Unsupported login user type in context: {}", user.getClass().getName());
        return null;
    }
}

