package com.kalamba.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kalamba.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByName(String name);

    Optional<UserEntity> findByPuuid(String puuid);
}
