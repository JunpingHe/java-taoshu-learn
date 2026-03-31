package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性
 * 从application.yml读取配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    /**
     * 密钥
     */
    private String secret = "taoshu-secret-key-change-in-production-must-be-at-least-256-bits";
    
    /**
     * 过期时间(毫秒)
     * 默认24小时
     */
    private Long expiration = 86400000L;
}
