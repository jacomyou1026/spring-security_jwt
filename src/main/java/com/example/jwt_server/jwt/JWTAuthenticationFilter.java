package com.example.jwt_server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt_server.auth.PrincipalDetails;
import com.example.jwt_server.model.Member;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

// /login요청 후 username이나 pwd를 전송하면 (post)
//username
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    //login요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println(" 로그인 시도중");

        try {
            ObjectMapper orm = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream(); //HTTP message body는 InputStream을 사용해서 직접 읽을 수 있다
            Member member = orm.readValue(inputStream, Member.class);
            System.out.println("member = " + member);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                    (member.getUsername(), member.getPwd());


            //PricipalDailsService의 loadUserByUsername() 함수 실행
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();


            return authentication;


        }catch (JsonParseException e) {
            e.printStackTrace();
            // JSON 파싱 오류 처리
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //1. username, pwd 받아서
        //2 . 정상인지 로그인 시도해보기 authenticationManager로 로그인 시도하면 PrincSer -> loadUserUsername() 자동 실해
        //ㅔㄱ
        return null;
    }

    //attemptAuthentication 정상 실행되었으면 실행
    //JWT토큰을 만들어서 requset요청한 사용자에게 JWT토큰을 response해주면됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("인증 완료");
        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

        //RSA방식은 아니고 Hash 암호 방식
        String jwtToken = JWT.create()
                .withSubject("cos 토큰")
                .withExpiresAt(new Date  (System.currentTimeMillis()+JwtProperties.EXPLRATON_TIME)) // 유효시간
                .withClaim("id", principalDetailis.getMember().getId())
                .withClaim("username", principalDetailis.getMember().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));



        response.addHeader("Authorization", "Bearer "+jwtToken);
    }
}
