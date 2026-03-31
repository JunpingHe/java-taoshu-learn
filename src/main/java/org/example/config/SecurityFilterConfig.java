package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置
 * 
 * 注意: 这里我们禁用了Spring Security的默认登录页面
 * 因为我们使用自定义的JWT拦截器进行认证
 */
@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护(使用JWT不需要)
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用Session(使用JWT无状态认证)
                .sessionManagement(session -> session.disable())
                // 允许所有请求(由我们的JWT拦截器控制)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // 禁用默认登录页面
                .formLogin(form -> form.disable())
                // 禁用HTTP Basic认证
                .httpBasic(basic -> basic.disable());
        
        return http.build();
    }
}
