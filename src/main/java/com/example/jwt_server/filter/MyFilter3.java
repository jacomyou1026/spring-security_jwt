package com.example.jwt_server.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

//시큐리티 요청 전에 걸러냄
public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");

        // 토큰 : 코스
        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청 됨");
            String authorization = req.getHeader(" ");
            System.out.println("authorization = " + authorization);

//            //토큰 : cos이걸만들어줘야 함. id,pwd정상적으로 들어와서 로그인
//            if (authorization.equals("cos")) {
//                filterChain.doFilter(request, response);
//
//            } else {
//                PrintWriter writer = res.getWriter();
//                writer.println("인증안됨");
//
//            }
        }

    }
}
