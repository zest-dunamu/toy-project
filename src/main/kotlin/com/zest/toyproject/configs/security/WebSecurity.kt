package com.zest.toyproject.configs.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurity(
    private val jwtTokenProvider: JwtTokenProvider
) : WebSecurityConfigurerAdapter() {
    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable()
            .csrf().disable() // csrf 보안 토큰 disable처리.
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션x
            .and()
            .authorizeRequests() // 요청에 대한 사용권한 체크
            .antMatchers(
                "/h2-console/**", "/v2/api-docs", "/v3/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.htm",
                "/webjars/**",
                "/swagger-ui/**"
            ).permitAll()
            .antMatchers("/auth/**").permitAll()//로그인
            .antMatchers("/api/health").hasAnyRole("USER")
            .antMatchers("/api/members").permitAll() //회원가입
            .anyRequest().authenticated() // 그외 나머지 요청은 인증 받아야함
            .and()
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
        http.headers().frameOptions().disable()
    }
}