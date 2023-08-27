package com.cos.jwt.config.auth;

import com.cos.jwt.model.Users;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8082/login -> 여기서 동작을 안함 securityConfig 에서 막아놈
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService 의 loadUserByUsername 메서드 실행");
        Users userEntity = userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
