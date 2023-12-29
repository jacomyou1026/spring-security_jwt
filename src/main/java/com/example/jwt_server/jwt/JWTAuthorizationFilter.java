package com.example.jwt_server.jwt;

//시큐리티가 filter를 가지고 있는데 그 필터중에서 BasicAuthenticationFilter란느 것이 있음
// 권한이나 인증이 필요한 틀정 주소 요청시 위 필터를 탐
// 만약 권한, 인증이 필요한 주소가 아니라면 이 필터 안탐.

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jwt_server.auth.PrincipalDetails;
import com.example.jwt_server.model.Member;
import com.example.jwt_server.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;



    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        System.out.println("authenticationManager = " + authenticationManager);

    }

    //인증이나 권한이 필요한 주소 요청이 있을때 해당 필터 타게됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증아나 권한 필요");
        String header = request.getHeader("Authorization");
        System.out.println("header = " + header);


        //header가 있는지 확인
        if (header == null ||! header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        //JWT토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

        String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();

        //서명 정상적으로 됨
        if (username != null) {
            Member userEntity = userRepository.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            //JWt토큰 서명이 정상이면 객체 만듦
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            
            //강제로 시큐리티 세션 접근하여 authenticationToken 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            
        }



    }
}
