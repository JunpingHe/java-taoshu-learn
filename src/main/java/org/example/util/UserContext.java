package org.example.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.common.BusinessException;

/**
 * 用户上下文工具类
 * 对比Django: request.user
 * 
 * 从Request中获取当前登录用户信息
 */
public class UserContext {
    
    /**
     * 获取当前登录用户ID
     * 对比Django: request.user.id
     */
    public static Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        return Long.valueOf(userId.toString());
    }
    
    /**
     * 获取当前登录用户手机号
     * 对比Django: request.user.username
     */
    public static String getPhone(HttpServletRequest request) {
        Object phone = request.getAttribute("phone");
        if (phone == null) {
            throw new BusinessException("未登录");
        }
        return phone.toString();
    }
}
