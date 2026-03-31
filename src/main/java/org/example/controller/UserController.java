package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.request.UserLoginRequest;
import org.example.dto.request.UserRegisterRequest;
import org.example.dto.response.LoginResponse;
import org.example.dto.response.UserResponse;
import org.example.service.UserService;
import org.example.util.UserContext;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 * 对比Django: Django的View或APIView
 * 
 * Controller层职责:
 * 1. 接收HTTP请求
 * 2. 验证参数格式(@Valid)
 * 3. 调用Service处理业务
 * 4. 返回响应
 * 
 * 注意: Controller不应该包含业务逻辑!
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户注册
     * POST /api/user/register
     * 
     * @Valid: 自动验证参数
     * 验证失败会抛出MethodArgumentNotValidException
     * 被全局异常处理器捕获
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse user = userService.register(request);
        return Result.success("注册成功", user);
    }
    
    /**
     * 用户登录
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success("登录成功", response);
    }
    
    /**
     * 获取当前用户信息
     * GET /api/user/info
     * 
     * 需要JWT认证(拦截器会验证)
     */
    @GetMapping("/info")
    public Result<UserResponse> getUserInfo(HttpServletRequest request) {
        Long userId = UserContext.getUserId(request);
        UserResponse user = userService.getUserInfo(userId);
        return Result.success(user);
    }
}
