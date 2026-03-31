package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体
 * 对比Django: 类似于Django的User模型
 */
@Data
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 手机号(唯一)
     * 对比Django: username字段,这里用phone作为唯一标识
     */
    @Column(unique = true, length = 11, nullable = false)
    private String phone;
    
    /**
     * 密码(加密存储)
     * 对比Django: Django自动加密,这里需要手动加密
     */
    @Column(nullable = false, length = 100)
    private String password;
    
    /**
     * 昵称
     */
    @Column(length = 50)
    private String nickname;
    
    /**
     * 头像URL
     */
    @Column(length = 255)
    private String avatar;
    
    /**
     * 用户状态: 1-正常 0-禁用
     * 对比Django: is_active字段
     */
    @Column(nullable = false)
    private Integer status = 1;
    
    /**
     * 创建时间
     * 对比Django: auto_now_add=True
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     * 对比Django: auto_now=True
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;
    
    /**
     * 保存前自动设置时间
     * 对比Django: override save()方法
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 更新前自动设置时间
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
