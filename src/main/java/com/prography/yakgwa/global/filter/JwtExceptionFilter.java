package com.prography.yakgwa.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.yakgwa.global.format.exception.ApplicationRunException;
import com.prography.yakgwa.global.format.exception.auth.jwt.ExpiredJwtTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtTokenException e) {
            setErrorResponse(request,response,e);
        }

    }

    public void setErrorResponse(HttpServletRequest req, HttpServletResponse res, ApplicationRunException ex) throws IOException {

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("code", ex.getErrorEnumCode().getCode());
        body.put("time", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(res.getOutputStream(), body);
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
