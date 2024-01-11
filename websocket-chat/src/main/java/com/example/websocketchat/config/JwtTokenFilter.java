package com.example.websocketchat.config;

import com.example.websocketchat.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    public UserDetailService userService;

    private final JwtDecoder jwtDecoder;

    public JwtTokenFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = extractTokenFromCookie(request);
        if (!token.equals("none")) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                UserDetails userDetails = userService.loadUserByUsername(jwt.getSubject());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                System.out.println("ERROR");
                System.out.println(e);
            }
        }
        filterChain.doFilter(request, response);
    }


    private String extractTokenFromCookie(HttpServletRequest request) {
        if (WebUtils.getCookie(request, "jwt") !=null){
            return WebUtils.getCookie(request, "jwt").getValue();
        } else {
            return "none";
        }
    }

}
