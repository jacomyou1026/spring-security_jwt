package com.example.jwt_server.repository;

import com.example.jwt_server.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    public Member findByUsername(String username);
}
