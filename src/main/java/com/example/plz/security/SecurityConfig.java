package com.example.plz.security;

import com.example.plz.domain.member.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

//        // 접근 권한 설정
//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/oauth-login/admin").hasRole(MemberRole.ADMIN.name())
//                        .requestMatchers("/oauth-login/info").authenticated()
//                        .anyRequest().permitAll()
//                );
//
//        // 폼 로그인 방식 설정
//        http
//                .formLogin((auth) -> auth.loginPage("/oauth-login/login")
//                        .loginProcessingUrl("/oauth-login/loginProc")
//                        .usernameParameter("loginId")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/oauth-login")
//                        .failureUrl("/oauth-login")
//                        .permitAll());
//
//        // OAuth 2.0 로그인 방식 설정
//        http
//                .oauth2Login((auth) -> auth.loginPage("/member/login")
//                        .defaultSuccessUrl("/login")
//                        .failureUrl("/member/login")
//                        .permitAll());
//
//        http
//                .logout((auth) -> auth
//                        .logoutUrl("/oauth-login/logout"));
//
//        http
//                .csrf((auth) -> auth.disable());
//
//        return http.build();
//    }

        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                                .defaultSuccessUrl("/")
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                )
        ;
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){


        return new BCryptPasswordEncoder();
    }
}