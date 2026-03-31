package org.example.dto.response;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * Token类型
     */
    private String tokenType = "Bearer";
    
    /**
     * 用户信息
     */
    private UserResponse user;
}
