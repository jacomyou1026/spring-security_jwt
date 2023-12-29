package com.example.jwt_server.jwt;

public interface JwtProperties {
    String SECRET = "cos";
    int EXPLRATON_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEAD_STRING = "Authorization";

}
