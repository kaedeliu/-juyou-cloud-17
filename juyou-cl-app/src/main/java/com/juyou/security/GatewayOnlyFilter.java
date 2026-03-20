package com.juyou.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GatewayOnlyFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Internal-Gateway";
    private static final String EXPECTED_VALUE = "true";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String fromGateway = request.getHeader(HEADER);
        if (!EXPECTED_VALUE.equalsIgnoreCase(fromGateway)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"Please access via gateway\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }
}

