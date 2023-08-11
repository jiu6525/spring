package com.example.presentation;

import com.example.domain.UserEntity;
import com.example.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signUp")
    public String signUp() {
        UserEntity user = UserEntity.builder()
                .name("jiu6525")
                .password(passwordEncoder.encode("1234"))
                .role("user")
                .build();
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "loginPage";
    }
    @GetMapping("/")
    public String getIndex() {
        return "index";
    }
    @GetMapping("/main")
    public String getMain() {
        return "main";
    }
}