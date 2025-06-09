package com.sgs.tasktracker.repository;

import com.sgs.tasktracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;

import java.util.Optional;

@RedisHash
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}