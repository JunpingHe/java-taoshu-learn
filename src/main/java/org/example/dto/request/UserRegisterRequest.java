package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求DTO
 * 对比Django: Django Form或Serializer
 * 
 * 为什么需要DTO?
 * 1. 不直接使用Entity,保护敏感字段
 * 2. 前后端数据格式可以不同
 * 3. 添加验证规则
 */
@Data
public class UserRegisterRequest {
    
    /**
     * 手机号
     * @NotBlank: 不能为空或空字符串
     * @Pattern: 必须匹配正则表达式(1开头的11位数字)
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
    private String phone;
    
    /**
     * 密码
     * @Size: 长度限制
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;
    
    /**
     * 昵称(可选)
     */
    @Size(max = 50, message = "昵称最长50位")
    private String nickname;
}
