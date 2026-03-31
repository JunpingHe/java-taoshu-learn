package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 * 对比Django: Django的make_password和check_password
 */
@Configuration
public class SecurityConfig {
    
    /**
     * 密码加密器
     * BCrypt加密算法:
     * 1. 自动加盐
     * 2. 计算慢(防暴力破解)
     * 3. 每次加密结果不同
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
