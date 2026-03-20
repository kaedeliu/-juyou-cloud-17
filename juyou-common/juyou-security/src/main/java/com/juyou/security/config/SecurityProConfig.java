package com.juyou.security.config;

import com.juyou.security.constant.SecurityConstant;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * security相关配置
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.security")
@Component
public class SecurityProConfig {

    /**
     * 白名单url
     */
    String excludeUrls;

    /**
     * token加签Secret
     */
    String jwtSecret= SecurityConstant.JWT_SECRET;

    /**
     * 管理端附加角色，配合roleWhilePath使用，处理下拉列表框等公共接口，要求登录
     */
    List<String> adminAddRoleCodes;

    /**
     * 根据角色key配置得白名单
     */
    Map<String,List<String>> roleWhilePath;
}
