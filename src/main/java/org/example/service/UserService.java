package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.dto.request.UserLoginRequest;
import org.example.dto.request.UserRegisterRequest;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.UserResponse;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户Service
 * 对比Django: Django的Service层或Manager
 * 
 * Service层职责:
 * 1. 业务逻辑处理
 * 2. 事务管理
 * 3. 调用Repository
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    /**
     * 用户注册
     * 对比Django: User.objects.create_user()
     * 
     * @Transactional: 开启事务,出现异常自动回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public UserResponse register(UserRegisterRequest request) {
        // 1. 检查手机号是否已注册
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("手机号已注册");
        }
        
        // 2. 创建用户
        User user = new User();
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 密码加密
        user.setNickname(request.getNickname() != null ? request.getNickname() : "用户" + request.getPhone().substring(7));
        user.setStatus(1);
        
        // 3. 保存到数据库
        user = userRepository.save(user);
        
        // 4. 返回DTO(隐藏敏感信息)
        return UserResponse.fromEntity(user);
    }
    
    /**
     * 用户登录
     * 对比Django: authenticate()
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(UserLoginRequest request) {
        // 1. 查询用户
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 2. 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        
        // 3. 检查状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 4. 更新最后登录时间
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        // 5. 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getPhone());
        
        // 6. 构造响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setUser(UserResponse.fromEntity(user));
        
        return response;
    }
    
    /**
     * 获取用户信息
     */
    public UserResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return UserResponse.fromEntity(user);
    }
}
