package com.example.jwt_server.auth;

import com.example.jwt_server.model.Member;
import com.example.jwt_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http:/localhoust:8080/login  동작안함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService username = " + username);
        Member memberEntity = userRepository.findByUsername(username);
        System.out.println("memberEntity = " + memberEntity);
        return new PrincipalDetails(memberEntity);
    }
}
