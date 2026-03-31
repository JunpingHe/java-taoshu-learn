package org.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.common.BusinessException;
import org.example.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 对比Django: Django REST Framework的exception_handler
 * 
 * 为什么需要全局异常处理?
 * 1. 统一错误响应格式
 * 2. 避免在每个方法中写try-catch
 * 3. 隐藏技术细节,返回友好错误信息
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理参数验证异常
     * 对比Django: ValidationError
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数验证失败: {}", message);
        return Result.badRequest(message);
    }
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }
    
    /**
     * 处理静态资源未找到异常
     * 如 favicon.ico 等，忽略即可
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> handleNoResourceFoundException(org.springframework.web.servlet.resource.NoResourceFoundException e) {
        // 静态资源未找到，不需要记录错误日志
        return Result.error("资源未找到");
    }
    
    /**
     * 处理所有异常(兜底)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error("系统繁忙,请稍后重试");
    }
}
