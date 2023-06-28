package com.kalamba.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kalamba.entity.UserEntity;
import com.kalamba.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    
    /**
     * DB에 등록되어 있는 사용자인지 조회
     * @return
     */
    public Optional<UserEntity> findUser(String name) {
        
        return userRepository.findByName(name);
    }

    /**
     * 사용자 등록
     * @return
     */
    public void regUser(Optional<UserEntity> user) {
        // 2023-06-29 할 일 : Optional로 감싸져 있는 UserEntity 꺼내기
        // userRepository.save(userEntity);
    }
}
