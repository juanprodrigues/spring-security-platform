package com.security.starter.handler;

import java.io.IOException;

import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAccessDeniedHandler
        implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException ex)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.setContentType("application/json");

        response.getWriter().write("""
            {
              "error":"Forbidden"
            }
            """);
    }
}