package com.juyou.security.utils;

import com.juyou.security.config.SecurityProConfig;
import com.juyou.security.constant.SecurityConstant;
import com.juyou.security.dto.Authority;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
@Component
public class SecurityUtils {

    @Autowired
    SecurityProConfig securityProConfig;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    /**
     *
     * @return
     */
    public boolean isWhilt(ServerHttpRequest request){
        //白名单
        String excludeUrls = securityProConfig.getExcludeUrls();
//        excludeUrls="/admin/login2";
        if(StringUtils.isNotEmpty(excludeUrls)){
            String[] excludes=excludeUrls.split(",");
            String path = request.getURI().getPath();
            for (String url:excludes){
                if(antPathMatcher.match(url,path)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取一个匿名账户配置
     */
    public UsernamePasswordAuthenticationToken getAnon(){
        List<GrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new Authority(SecurityConstant.ANON_ROLE));
       return new UsernamePasswordAuthenticationToken(SecurityConstant.ANON_NAME,SecurityConstant.ANON_NAME,authorities);
    }

    /**
     * 因客户端通常不需要做这么严格的权限验证
     * 通过用户角色标识来设置不验证的白名单
     */
    public List<String> getRoleWhiltPaths(Collection<GrantedAuthority> authorities){
        List<String> paths=new ArrayList<>();
        if(!authorities.isEmpty()){
            for (GrantedAuthority authoritie: authorities) {
                if(securityProConfig.getRoleWhilePath()!=null && securityProConfig.getRoleWhilePath().containsKey(authoritie.getAuthority())){
                    List<String> pathUrls=securityProConfig.getRoleWhilePath().get(authoritie.getAuthority());
                    if(pathUrls!=null && !pathUrls.isEmpty()) paths.addAll(pathUrls);
                }
            }
        }
        return paths;
    }

}
