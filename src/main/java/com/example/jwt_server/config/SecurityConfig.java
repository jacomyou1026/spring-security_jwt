package com.example.jwt_server.config;

import com.example.jwt_server.filter.MyFilter1;
import com.example.jwt_server.filter.MyFilter3;
import com.example.jwt_server.jwt.JWTAuthenticationFilter;
import com.example.jwt_server.jwt.JWTAuthorizationFilter;
import com.example.jwt_server.repository.UserRepository;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DialectOverride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter filter;

    @Autowired
    private  UserRepository userRepository;


    @Autowired
    private CorsConfig corsConfig;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilter(filter); //@CrossOrign(인증x), 시큐리티 필터에 등록 인증0
//        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class); // 시큐리티 필터가 먼저 실행
        http.csrf(cs-> cs.disable())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(f->f.disable())
                .httpBasic(h->h.disable())
                .apply(new MyCustomDs1());
        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("api/v1/user/**").authenticated()
                        .requestMatchers("api/v1/manager").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("api/v1/admin").hasRole("ADMIN")
                        .anyRequest().permitAll()
        );


        return http.build();
    }

    public class MyCustomDs1 extends AbstractHttpConfigurer<MyCustomDs1, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JWTAuthenticationFilter(authenticationManager))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager,userRepository));
        }
    }



}
