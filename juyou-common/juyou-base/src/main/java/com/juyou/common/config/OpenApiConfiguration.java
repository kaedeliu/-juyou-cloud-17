package com.juyou.common.config;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
public class OpenApiConfiguration {

    @Autowired
    private EnvUtils envUtils;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Bean
    public OpenAPI defaultOpenAPI() {
        String displayName = displayName(applicationName);
        String tokenHeader = CommonConstant.X_ACCESS_TOKEN;

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("巨友科技")
                        .description(displayName)
                        .version("3.0")
                        .contact(new Contact().name("").url("").email("")));

        // 统一安全头配置
        openAPI.addSecurityItem(new SecurityRequirement().addList(tokenHeader))
                .schemaRequirement(tokenHeader, new SecurityScheme()
                        .name(tokenHeader)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER));

        // 通过配置控制文档 server 列表（可配置多个，逗号分隔）
        Optional.ofNullable(envUtils)
                .map(utils -> utils.value(EnvKey.SpringDoc请求地址))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .ifPresent(servers -> {
                    Stream<String> stream = Arrays.stream(servers.split(","));
                    stream.map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .forEach(url -> openAPI.addServersItem(new Server().url(url)));
                });

        return openAPI;
    }

    /**
     * GlobalOpenApiCustomizer 用于给所有接口统一追加鉴权信息（Knife4j 推荐用法）
     */
    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        String tokenHeader = CommonConstant.X_ACCESS_TOKEN;
        return openApi -> {
            // 全局添加鉴权参数
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((s, pathItem) -> {
                    // 接口添加鉴权参数
                    pathItem.readOperations()
                            .forEach(operation ->
                                    operation.addSecurityItem(new SecurityRequirement().addList(tokenHeader))
                            );
                });
            }
        };
    }

    private static String displayName(String appName) {
        if (appName == null || appName.trim().isEmpty()) {
            return "服务";
        }
        switch (appName.trim()) {
            case "juyou-mg-admin":
                return "管理端";
            case "juyou-cl-app":
                return "客户端";
            case "juyou-order":
                return "订单管理";
            default:
                return appName.trim();
        }
    }
}
