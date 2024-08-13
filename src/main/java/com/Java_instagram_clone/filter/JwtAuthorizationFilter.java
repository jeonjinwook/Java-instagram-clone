package com.Java_instagram_clone.filter;

import com.Java_instagram_clone.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

// 인가
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String AUTHORIZATION_HEADER = "Authorization";

        String header = request.getHeader(AUTHORIZATION_HEADER);
        String accessToken = null;

        try {

            String BEARER_TYPE = "Bearer ";
            if ((!ObjectUtils.isEmpty(header)) && (header.startsWith(BEARER_TYPE))) {

                accessToken = header;

            }


        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }

        response.setHeader(AUTHORIZATION_HEADER, accessToken);

        chain.doFilter(request, response);
    }


}
