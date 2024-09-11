package com.Java_instagram_clone.filter;

import com.Java_instagram_clone.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

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
        String REFRESH_AUTHORIZATION_HEADER = "RefreshAuthorization";
        String BEARER_TYPE = "Bearer ";

        String header = request.getHeader(AUTHORIZATION_HEADER);
        String refreshHeader = request.getHeader(REFRESH_AUTHORIZATION_HEADER);
        String accessToken = null;
        String refreshToken = null;
        try {

            if ((!ObjectUtils.isEmpty(header)) && (header.startsWith(BEARER_TYPE))) {

                accessToken = header;

                if ((!ObjectUtils.isEmpty(refreshHeader)) && (refreshHeader.startsWith(
                        BEARER_TYPE))) {

                    refreshToken = refreshHeader;

                }

                try {

                    this.jwtUtil.validate(header.replaceAll(BEARER_TYPE, ""), request);

                } catch (ExpiredJwtException e) {

                    accessToken = this.jwtUtil.reNewAccessTokenFromRefreshToken(refreshToken,
                            request);

                }

                try {

                    this.jwtUtil.validate(refreshHeader.replaceAll(BEARER_TYPE, ""), request);

                } catch (ExpiredJwtException e) {

                    refreshToken = this.jwtUtil.reNewRefreshTokenFromAccessToken(accessToken,
                            request);

                }

            }


        } catch (ExpiredJwtException e) {
        }

        response.setHeader(AUTHORIZATION_HEADER, accessToken);
        response.setHeader(REFRESH_AUTHORIZATION_HEADER, refreshToken);

        chain.doFilter(request, response);
    }


}
