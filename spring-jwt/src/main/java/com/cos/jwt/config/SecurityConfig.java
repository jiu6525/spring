package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

// https://github.com/spring-projects/spring-security/issues/10822 참고

//        1.사용자가 http request.
//
//        2-1.로그인 요청이라면 (UsernamePasswordAuthenticationFilter를 상속한) AuthenticationFilter가
//        attemptAuthentication()를 실행되고 그 후 successfulAuthentication()을 실행된다.
//        해당 과정에서 토큰을 만들고 해당 토큰을 response 헤더에 담아준다.
//        2-2.이미 토큰을 발급받아 같이 요청한 상태라면, (UsernamePasswordAuthenticationFilter를상속한) AuthenticationFilter에서 아무런 동작없이 바로 인가처리로 이어진다.
//        (BasicAuthenticationFilter를 상속한 AuthorizationFilter)
//
//        3.BasicAuthenticationFilter를 상속한 AuthorizationFilter에서 받은 토큰을 parse하여 해당 Id가 db에
//        저장되어 있는지 확인한다.그후 존재한다면,해당 유저정보와 해당 유저의 권한이 담긴 토큰을
//        SecurityContextHolder를 사용하여 세션값에 저장한다.(권한 부여)
//        Ex) SecurityContextHolder.getContext().setAuthentication(authentication);

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl()) // 커스텀 필터 등록
                .and()
                .authorizeRequests(authroize -> authroize.antMatchers("/api/v1/user/**")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/api/v1/manager/**")
                        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/api/v1/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll())
                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }

}