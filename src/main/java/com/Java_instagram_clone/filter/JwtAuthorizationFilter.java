package com.Java_instagram_clone.filter;

import com.Java_instagram_clone.domain.auth.service.CustomAuthDetailService;
import com.Java_instagram_clone.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_TYPE = "Bearer ";

  private final JwtUtil jwtUtil;
  private final CustomAuthDetailService customAuthDetailService;

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
      CustomAuthDetailService customAuthDetailService) {
    super(authenticationManager);
    this.jwtUtil = jwtUtil;
    this.customAuthDetailService = customAuthDetailService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    String accessToken = resolveToken(request.getHeader(AUTHORIZATION_HEADER));

    if (accessToken != null) {
      if (jwtUtil.isTokenBlacklisted(accessToken)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }

      try {
        jwtUtil.validateAccessToken(accessToken);

        String username = jwtUtil.getUsernameFromToken(accessToken);

        setAuthentication(username);

      } catch (ExpiredJwtException e) {
        String username = e.getClaims().getSubject();
        String newAccessToken = jwtUtil.reIssueAccessToken(username);

        if (newAccessToken != null) {
          response.setHeader(AUTHORIZATION_HEADER, BEARER_TYPE + newAccessToken);
          setAuthentication(username);
        } else {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
        }
      } catch (JwtException | IllegalArgumentException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }

    chain.doFilter(request, response);
  }

  private String resolveToken(String header) {
    if (!ObjectUtils.isEmpty(header) && header.startsWith(BEARER_TYPE)) {
      return header.substring(BEARER_TYPE.length());
    }
    return null;
  }

  private void setAuthentication(String username) {
    UserDetails userDetails = customAuthDetailService.loadUserByUsername(username);
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails, null, userDetails.getAuthorities()
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}




