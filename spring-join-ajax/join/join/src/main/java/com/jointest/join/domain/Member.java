package com.jointest.join.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="members")
@Getter
@NoArgsConstructor
public class Member {

    @Id @NotEmpty
    private String id;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String id, String password, String name, MemberRole role){
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;


    }
}
