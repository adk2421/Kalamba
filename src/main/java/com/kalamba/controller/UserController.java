package com.kalamba.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kalamba.entity.UserEntity;
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
    public Optional<UserEntity> findUser(String name) {
        String testName = "옥철호";
        Optional<UserEntity> user = userService.findUser(testName);
        
        if (!user.isPresent()) {
            user = summonerService.summonerV4(testName);

            userService.regUser(user);
        }

        return user;
    }
}
