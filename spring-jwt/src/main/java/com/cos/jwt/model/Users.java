package com.cos.jwt.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private String roles; // USER, ADMIN

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
