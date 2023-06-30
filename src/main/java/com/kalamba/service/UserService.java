package com.kalamba.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kalamba.entity.UserEntity;
import com.kalamba.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
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
    public Optional<UserEntity> regUser(UserEntity user) {

        return Optional.ofNullable(userRepository.save(user));
    }

    /**
     * 사용자 정보 업데이트
     * @return
     */
    @Transactional
    public Optional<UserEntity> updateUser(String puuid, UserEntity userEntity) {
        Optional<UserEntity> user = userRepository.findByPuuid(puuid);

        if (user.isPresent())
            user.get().updateAll(userEntity);
        
        return user;
    }
}
