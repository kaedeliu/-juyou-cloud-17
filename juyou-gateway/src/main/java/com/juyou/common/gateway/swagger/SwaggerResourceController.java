package com.juyou.common.gateway.swagger;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Compatibility endpoints for old Knife4j/Swagger UI.
 * doc.html in some versions still reads /swagger-resources* instead of springdoc swagger-config.
 */
@RestController
public class SwaggerResourceController {

    private final Environment environment;

    public SwaggerResourceController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/swagger-resources")
    public List<Map<String, Object>> swaggerResources() {
        List<SwaggerUrl> configuredUrls = Binder.get(environment)
                .bind("springdoc.swagger-ui.urls", Bindable.listOf(SwaggerUrl.class))
                .orElse(Collections.emptyList());

        List<Map<String, Object>> resources = new ArrayList<>();
        for (SwaggerUrl item : configuredUrls) {
            String name = item.getName() == null ? "default" : item.getName();
            String url = item.getUrl() == null ? "/v3/api-docs" : item.getUrl();
            Map<String, Object> resource = new LinkedHashMap<>();
            resource.put("name", name);
            resource.put("url", url);
            resource.put("location", url);
            resource.put("swaggerVersion", "3.0");
            resources.add(resource);
        }

        if (resources.isEmpty()) {
            resources.add(defaultResource());
        }
        return resources;
    }

    @GetMapping("/swagger-resources/configuration/ui")
    public Map<String, Object> swaggerUiConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("deepLinking", true);
        config.put("displayOperationId", false);
        config.put("defaultModelsExpandDepth", 1);
        config.put("defaultModelExpandDepth", 1);
        config.put("defaultModelRendering", "example");
        config.put("displayRequestDuration", false);
        config.put("docExpansion", "none");
        config.put("filter", false);
        config.put("maxDisplayedTags", null);
        config.put("operationsSorter", "alpha");
        config.put("showExtensions", false);
        config.put("showCommonExtensions", false);
        config.put("tagsSorter", "alpha");
        config.put("supportedSubmitMethods", Arrays.asList("get", "put", "post", "delete", "options", "head", "patch", "trace"));
        config.put("validatorUrl", "");
        return config;
    }

    @GetMapping("/swagger-resources/configuration/security")
    public Map<String, Object> swaggerSecurityConfig() {
        return new LinkedHashMap<>();
    }

    private Map<String, Object> defaultResource() {
        Map<String, Object> resource = new LinkedHashMap<>();
        resource.put("name", "default");
        resource.put("url", "/v3/api-docs");
        resource.put("location", "/v3/api-docs");
        resource.put("swaggerVersion", "3.0");
        return resource;
    }

    public static class SwaggerUrl {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
