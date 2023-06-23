package com.kalamba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kalamba.entity.TestEntity;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
    
}
