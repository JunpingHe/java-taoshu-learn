package org.example.common;

/**
 * 业务异常
 * 对比Django: raise ValidationError("错误信息")
 * 
 * 为什么需要自定义异常?
 * 1. 区分业务错误和系统错误
 * 2. 全局异常处理器统一处理
 * 3. 返回友好的错误信息
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
