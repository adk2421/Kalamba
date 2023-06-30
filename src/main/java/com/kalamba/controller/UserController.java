package com.kalamba.controller;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kalamba.entity.UserEntity;
import com.kalamba.repository.UserRepository;
import com.kalamba.service.SummonerService;
import com.kalamba.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;
    private final SummonerService summonerService;

    @PostMapping("reg-user")
    public Optional<UserEntity> findUser(@Param("name") String name) {
        String userName = name.trim();
        Optional<UserEntity> user = userService.findUser(userName);
        UserEntity userEntity = summonerService.summonerV4(userName);
        
        if (!user.isPresent())
            user = userService.regUser(userEntity);
        else
            user = userService.updateUser(user.get().getPuuid(), userEntity);

        return user;
    }
}
