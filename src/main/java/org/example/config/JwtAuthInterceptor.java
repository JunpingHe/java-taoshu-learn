package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT认证拦截器
 * 对比Django: Django的中间件或@login_required装饰器
 * 
 * 拦截器作用:
 * 1. 验证请求是否携带有效Token
 * 2. 提取用户信息存入Request
 * 3. 保护需要登录的接口
 */
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    
    private final JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
                              HttpServletResponse response,
                              Object handler) throws Exception {
        // 1. 从请求头获取Token
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, 401, "未认证或Token格式错误");
            return false;
        }
        
        // 2. 提取Token
        String token = authHeader.substring(7); // 去掉"Bearer "
        
        // 3. 验证Token
        if (!jwtUtil.validateToken(token)) {
            sendErrorResponse(response, 401, "Token无效或已过期");
            return false;
        }
        
        // 4. 提取用户信息并存入请求属性
        Long userId = jwtUtil.getUserIdFromToken(token);
        String phone = jwtUtil.getPhoneFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("phone", phone);
        
        return true; // 放行,继续执行Controller
    }
    
    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response,
                                    int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format("{\"code\":%d,\"message\":\"%s\"}", status, message)
        );
    }
}
