package com.juyou.security.sevice;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SecurityService {

    /**
     * 获取用户授权信息
     * @param token
     * @return
     */
    UsernamePasswordAuthenticationToken getUserAuthenticationToken(String token);

    /**
     * 获取用户拥有的权限
     * @param userToken
     * @return
     */
    List<String> getUserAuthority(Authentication userToken);
}
