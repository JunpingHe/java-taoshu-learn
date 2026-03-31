package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体(书籍)
 * 对比Django: Django ORM模型
 */
@Data
@Entity
@Table(name = "book")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 书名
     */
    @Column(nullable = false, length = 100)
    private String title;
    
    /**
     * 作者
     */
    @Column(nullable = false, length = 50)
    private String author;
    
    /**
     * 出版社
     */
    @Column(length = 100)
    private String publisher;
    
    /**
     * ISBN
     */
    @Column(unique = true, length = 20)
    private String isbn;
    
    /**
     * 价格
     * 对比Django: DecimalField
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    /**
     * 库存
     */
    @Column(nullable = false)
    private Integer stock = 0;
    
    /**
     * 已售数量
     */
    @Column(nullable = false)
    private Integer sales = 0;
    
    /**
     * 封面图片URL
     */
    @Column(length = 255)
    private String coverImage;
    
    /**
     * 商品详情
     * 对比Django: TextField
     */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /**
     * 商品状态: 1-上架 0-下架
     */
    @Column(nullable = false)
    private Integer status = 1;
    
    /**
     * 所属卖家ID
     */
    @Column(nullable = false)
    private Long sellerId;
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
