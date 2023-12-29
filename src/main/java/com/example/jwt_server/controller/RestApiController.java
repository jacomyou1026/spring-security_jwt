package com.example.jwt_server.controller;

import com.example.jwt_server.auth.PrincipalDetails;
import com.example.jwt_server.model.Member;
import com.example.jwt_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

//@CrossOrigin // 인증이 필요한 요청은 해결안됨
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 유저 혹은 매니저 혹은 어드민이 접근 가능
    @GetMapping("user")
    public String user(Authentication authentication) {
        return "<h1>user</h1>";
    }
    
//manager, admin 권한만 접근 가능
    @GetMapping("manager")
        public String manager() {

            return "<h1>manager</h1>";
    }


    //admin만 가능
    @GetMapping("admin")
    public String admin() {

        return "<h1>user</h1>";
    }

    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody Member user) {
        user.setPwd(bCryptPasswordEncoder.encode(user.getPwd()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";

    }
}
