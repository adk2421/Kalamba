package com.kalamba.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kalamba.entity.TestEntity;
import com.kalamba.repository.TestRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestRepository testRepository;
    
    /**
     * 모든 데이터 조회
     * @return
     */
    @GetMapping("select")
    public List<TestEntity> findAllData() {
        return testRepository.findAll();
    }

    /**
     * ID 데이터 조회
     * @return
     */
    @GetMapping("select-id")
    public Optional<TestEntity> findData() {
        return testRepository.findById(1L);
    }

    /**
     * 데이터 입력
     * @return
     * Error: The database returned no natively generated identity value - PK가 AUTO_INCREMENT 속성으로 되어있지 않아서 발생
     */
    @PostMapping("insert")
    public TestEntity insertData() {
        List<TestEntity> data = findAllData();
        final TestEntity testEntity = TestEntity.builder().testText("Insert Test " + data.get(data.size()-1).getTestNumber()).build();
        
        return testRepository.save(testEntity);
    }

    /**
     * 데이터 수정 - 사용자 정의 쿼리
     * @return
     * Error: For queries with named parameters you need to use provide names for method parameters. - Repository에 파라미터를 명시해주지 않음 ( @Param )
     */
    @Transactional
    @PutMapping("update")
    public int updateData() {
        List<TestEntity> data = findAllData();
        return testRepository.updateData((int)data.get(data.size()-1).getTestNumber(), "UPDATE TEST" + data.get(data.size()-1).getTestNumber());
    }

    /**
     * 조건 데이터 삭제
     * @return
     */
    @DeleteMapping("delete")
    public void deleteData() {
        final TestEntity testEntity = TestEntity.builder().testNumber(7).testText("Insert Test5").build();
        testRepository.delete(testEntity);
    } 
}
