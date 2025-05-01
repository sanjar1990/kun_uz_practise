//package com.example.config;
//
//import com.example.dto.JwtDTO;
//import com.example.utility.JwtUtil;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.GenericFilterBean;
//
//import java.io.IOException;
////@Component
//public class JwtFilter extends GenericFilterBean {
//    @Override
//    public void doFilter(ServletRequest servletRequest,
//                         ServletResponse servletResponse,
//                         FilterChain filterChain) throws IOException, ServletException {
//    final HttpServletRequest request = (HttpServletRequest) servletRequest;
//    final HttpServletResponse response = (HttpServletResponse) servletResponse;
//    final String authHeader = request.getHeader("Authorization");
//    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setHeader("Message","Token not found");
//        return;
//    }
//    final String token = authHeader.substring(7);
//        JwtDTO jwtDTO;
//    try {
//            jwtDTO= JwtUtil.decode(token);
//
//    }catch (JwtException e){
//        System.out.println("EXCEPTION TWO");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setHeader("Message","Token invalid");
//        return;
//    }
//    request.setAttribute("id",jwtDTO.getPhone());
//    request.setAttribute("role",jwtDTO.getRole());
//    filterChain.doFilter(request, response);
//    }
//}
