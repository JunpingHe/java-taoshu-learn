package org.example.dto.response;

import lombok.Data;
import org.example.entity.User;
import java.time.LocalDateTime;

/**
 * 用户响应DTO
 * 为什么需要Response DTO?
 * 1. 隐藏敏感信息(如密码)
 * 2. 控制返回的数据结构
 * 3. 添加计算字段
 */
@Data
public class UserResponse {
    
    private Long id;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    
    /**
     * Entity转DTO
     * 对比Django: Serializer的to_representation方法
     */
    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setPhone(user.getPhone());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastLoginAt(user.getLastLoginAt());
        // 注意: 不返回password字段!
        return response;
    }
}
