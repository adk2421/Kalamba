package com.kalamba.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
     * 데이터 입력
     * @return
     * Error: The database returned no natively generated identity value - PK가 AUTO_INCREMENT 속성으로 되어있지 않아서 발생
     */
    @PostMapping("insert")
    public TestEntity insertData() {
        List<TestEntity> data = findAllData();
        final TestEntity testEntity = TestEntity.builder().testNumber(403).testText("Insert Test " + data.get(data.size()-1).getTestNumber()).build();
        
        return testRepository.save(testEntity);
    }
}
