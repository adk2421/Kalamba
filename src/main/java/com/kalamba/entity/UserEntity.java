package com.kalamba.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@DynamicUpdate // Dirty Checking(상태 변경 검사) 시, 전체 필드 업데이트가 아닌 변경 필드만 업데이트 되도록 함.
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @Column(length = 63, nullable = false)
    private String id;

    @Column(length = 56, nullable = false)
    private String accountId;

    @Column(length = 78, nullable = false)
    private String puuid;
    
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 5, nullable = false)
    private String profileIconId;

    @Column(length = 13, nullable = false)
    private String revisionDate;

    @Column(length = 4, nullable = false)
    private String summonerLevel;

    public UserEntity(String name) {
        this.name = name;
    }

    public void updateAll(UserEntity userEntity) {
        this.setName(userEntity.getName());
        this.setProfileIconId(userEntity.getProfileIconId());
        this.setRevisionDate(userEntity.getRevisionDate());
        this.setSummonerLevel(userEntity.getSummonerLevel());
    }
}
