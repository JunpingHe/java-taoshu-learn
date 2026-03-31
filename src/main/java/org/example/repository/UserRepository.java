package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户Repository
 * 对比Django: User.objects
 * Spring Data JPA会自动实现这些方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据手机号查询用户
     * 方法名即查询: findBy + 字段名
     * 对比Django: User.objects.get(phone=phone)
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * 检查手机号是否存在
     * 方法名即查询: existsBy + 字段名
     * 对比Django: User.objects.filter(phone=phone).exists()
     */
    boolean existsByPhone(String phone);
}
