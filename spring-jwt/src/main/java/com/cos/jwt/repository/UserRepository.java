package com.cos.jwt.repository;


import com.cos.jwt.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository 가 들고있음
// @Repository 라는 어노테이션이 없어도 IoC 됨 -> JpaRepository 를 상속했기 때문
public interface UserRepository extends JpaRepository<Users, Integer> {

//  findBy 규칙 -> Username 문법
//  select * from user were username = ?
    public Users findByUsername(String username);
}
