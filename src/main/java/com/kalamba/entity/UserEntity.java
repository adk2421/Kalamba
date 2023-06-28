package com.kalamba.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @Column(length = 50, nullable = false)
    private String id;

    @Column(length = 50, nullable = false)
    private String accountId;

    @Column(length = 80, nullable = false)
    private String puuid;
    
    @Column(length = 15, nullable = false)
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
}
