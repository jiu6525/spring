package com.example.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue
    private long userId;
    private String name;
    private String password;
    private String role;
    @Builder
    public UserEntity(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }
}