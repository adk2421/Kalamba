package com.kalamba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kalamba.entity.TestEntity;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {
    
    // 데이터 수정
    @Modifying(clearAutomatically = true)
    @Query("update test_db set test_number = :number+100, test_text = :text where test_number = :number")
    int updateData(@Param("number") int number, @Param("text") String text);
}
