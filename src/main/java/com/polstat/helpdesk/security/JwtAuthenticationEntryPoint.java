package com.polstat.helpdesk.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Mengirimkan error 401 jika pengguna mencoba mengakses sumber daya yang tidak diizinkan
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}