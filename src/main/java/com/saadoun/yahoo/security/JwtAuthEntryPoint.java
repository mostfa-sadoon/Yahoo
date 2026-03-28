package com.saadoun.yahoo.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public  void commence(HttpServletRequest request , HttpServletResponse response , AuthenticationException exception) throws IOException, ServletException {

        Exception ex = (Exception) request.getAttribute("jwt_exception");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        if (ex instanceof ExpiredJwtException){
            response.getWriter().write("{\"message\": \"Token expired\"}");
        }
        else if (ex instanceof JwtException) {
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
        else {
            response.getWriter().write("{\"message\": \"Not authenticated or token invalid\"}");
        }
    }
}